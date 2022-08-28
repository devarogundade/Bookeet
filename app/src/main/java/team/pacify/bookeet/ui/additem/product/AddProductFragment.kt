package team.pacify.bookeet.ui.additem.product

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.databinding.FragmentAddProductBinding
import team.pacify.bookeet.pager.PagerFragment
import team.pacify.bookeet.ui.additem.AddItemViewModel
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.Resource
import team.pacify.bookeet.utils.UIConstants
import team.pacify.bookeet.utils.UIConstants.ItemUnits
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddProductFragment : PagerFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentAddProductBinding
    private val viewModel: AddItemViewModel by viewModels()

    private var isProductName = false
    private var isCostPrice = false
    private var isSellingPrice = false
    private var isQuantity = false
    private var isUnit = false
    private var selectedImageUri: Uri? = null

    private lateinit var counterHandler: CounterHandler
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpValidators()
        counterHandler = CounterHandler.Builder()
            .listener(counterListener)
            .incrementalView(binding.increment)
            .decrementalView(binding.decrement)
            .minRange(UIConstants.MIN_ITEM_IN_STORE)
            .maxRange(UIConstants.MAX_ITEM_IN_STORE)
            .startNumber(0)
            .build()

        val unitsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            ItemUnits
        )

        binding.apply {
            quantity.apply {
                setText("0")
                addTextChangedListener(quantityWatcher)
            }

            binding.units.setAdapter(unitsAdapter)

            addImage.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startForResultFromGallery.launch(intent)
            }
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Adding product")
            setCancelable(false)
        }

        viewModel.addProduct.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> progressDialog.show()
                is Resource.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("Product added!")
                        setMessage("You have successfully added ${resource.data?.name} to your inventory")
                        setPositiveButton("Add new") { _, _ ->
                            clearFields()
                        }
                        setNegativeButton("Close") { _, _ ->
                            findNavController().popBackStack()
                        }
                    }.show()
                }
            }
        }

    }

    private fun clearFields() {
        binding.apply {
            productName.setText("")
            costPrice.setText("")
            sellingPrice.setText("")
            quantity.setText("")
            units.setText("")
        }
    }

    private fun setImage(uri: Uri?) {
        selectedImageUri = uri

        if (uri == null) {
            binding.image.visibility = View.GONE
            return
        }

        val bitmap =
            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
        binding.image.apply {
            setImageBitmap(bitmap)
            visibility = View.VISIBLE
        }
    }

    private val startForResultFromGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                if (result.data != null && result.data!!.data != null) {
                    val selectedImageUri = result.data!!.data
                    setImage(selectedImageUri)
                }
            } catch (exception: Exception) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val quantityWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val quantity = binding.quantity.text.toString().trim()

            if (quantity.isNotEmpty()) {
                counterHandler.updateCounter(quantity.toInt())
                return
            }

            counterHandler.updateCounter(0)
        }

        override fun afterTextChanged(p0: Editable?) = Unit
    }

    private val counterListener = object : CounterHandler.CounterListener {
        override fun onIncrement(view: View?, number: Int) {
            binding.quantity.setText(number.toString())
        }

        override fun onDecrement(view: View?, number: Int) {
            binding.quantity.setText(number.toString())
        }
    }

    override fun onClick() {
        if (allInputsValidated()) {
            val uploadImage = ""

            viewModel.addProduct(
                Product(
                    image = uploadImage,
                    barcodeString = "",
                    inStock = binding.quantity.text.toString().trim().toInt(),
                    id = "",
                    userId = firebaseAuth.currentUser?.uid ?: return,
                    name = binding.productName.text.toString().trim(),
                    costPrice = binding.costPrice.text.toString().trim().toDouble(),
                    sellingPrice = binding.sellingPrice.text.toString().trim().toDouble(),
                    qty = binding.quantity.text.toString().trim().toDouble(),
                    unit = binding.units.text.toString().trim(),
                    timeStamp = Calendar.getInstance().time
                )
            )
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpValidators() {
        binding.productName.addTextChangedListener(TextFieldValidation(binding.productName) { result ->
            isProductName = result
        })
        binding.costPrice.addTextChangedListener(TextFieldValidation(binding.costPrice) { result ->
            isCostPrice = result
        })
        binding.sellingPrice.addTextChangedListener(TextFieldValidation(binding.sellingPrice) { result ->
            isSellingPrice = result
        })
        binding.quantity.addTextChangedListener(TextFieldValidation(binding.quantity) { result ->
            isQuantity = result
        })
        binding.units.addTextChangedListener(TextFieldValidation(binding.units) { result ->
            isUnit = result
        })
    }

    private fun allInputsValidated(): Boolean =
        isProductName && isCostPrice && isSellingPrice && isQuantity && isUnit

    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.productName -> result(
                    this@AddProductFragment.requireContext().validateInput(
                        binding.productName,
                        binding.productNameLayout
                    )
                )
                R.id.costPrice -> result(
                    this@AddProductFragment.requireContext().validateInput(
                        binding.costPrice,
                        binding.costPriceLayout
                    )
                )
                R.id.sellingPrice -> result(
                    this@AddProductFragment.requireContext().validateInput(
                        binding.sellingPrice,
                        binding.sellingPriceLayput,
                    )
                )
                R.id.quantity -> result(
                    this@AddProductFragment.requireContext().validateInput(
                        binding.quantity,
                        binding.quantityLayout,
                        minLength = 1,
                    )
                )
                R.id.units -> result(
                    this@AddProductFragment.requireContext().validateInput(
                        binding.units,
                        binding.unitLayout
                    )
                )
            }
        }
    }

}