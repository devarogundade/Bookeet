package team.pacify.bookeet.ui.additem.service

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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.inventory.Service
import team.pacify.bookeet.databinding.FragmentAddServiceBinding
import team.pacify.bookeet.pager.PagerFragment
import team.pacify.bookeet.ui.additem.AddItemViewModel
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.Resource

class AddServiceFragment : PagerFragment() {

    private lateinit var binding: FragmentAddServiceBinding
    private val viewModel: AddItemViewModel by viewModels()

    private var isServiceName = false
    private var isServiceCharge = false
    private var isSellingPrice = false
    private var isUnit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpValidators()

        binding.apply {
            addImage.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startForResultFromGallery.launch(intent)
            }
        }

    }

    private fun setImage(uri: Uri?) {
        if (uri == null) {
            binding.image.visibility = View.GONE
            return
        }

        val bitmap =
            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
        binding.image.apply {
            setImageBitmap(bitmap)
            visibility = View.GONE
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

    override fun onClick() {
        if (allInputsValidated()) {
            val progress = ProgressDialog(requireContext()).apply {
                setTitle("Adding service")
                setCancelable(false)
            }

            progress.show()

            val uploadImage = ""

            val resource = viewModel.addService(
                Service(
                    image = uploadImage,
                    id = "",
                    userId = "",
                    name = binding.serviceName.text.toString().trim(),
                    unit = binding.units.text.toString().trim(),
                    costPrice = binding.serviceCharge.text.toString().trim().toDouble(),
                    sellingPrice = 0.0,
                    qty = 0.0
                )
            )

            progress.dismiss()

            if (resource is Resource.Error) {
                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allInputsValidated(): Boolean =
        isServiceName && isServiceCharge && isUnit


    private fun setUpValidators() {
        binding.serviceName.addTextChangedListener(TextFieldValidation(binding.serviceName) { result ->
            isServiceName = result
        })
        binding.serviceCharge.addTextChangedListener(TextFieldValidation(binding.serviceChargeLayout) { result ->
            isServiceCharge = result
        })
        binding.units.addTextChangedListener(TextFieldValidation(binding.units) { result ->
            isUnit = result
        })
    }


    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.serviceName -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.serviceName,
                        binding.serviceNameLayout
                    )
                )
                R.id.serviceCharge -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.serviceCharge,
                        binding.serviceChargeLayout,
                        minLength = 2,
                    )
                )
                R.id.units -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.units,
                        binding.unitLayout
                    )
                )
            }
        }
    }

}