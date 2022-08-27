package team.pacify.bookeet.ui.additem.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentAddProductBinding
import team.pacify.bookeet.pager.PagerFragment
import team.pacify.bookeet.ui.additem.AddItemViewModel
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.UIConstants
import team.pacify.bookeet.utils.UIConstants.ItemUnits

class AddProductFragment : PagerFragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val viewModel: AddItemViewModel by viewModels()

    private var isProductName = false
    private var isCostPrice = false
    private var isSellingPrice = false
    private var isQuantity = false
    private var isUnit = false

    private lateinit var counterHandler: CounterHandler

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
            viewModel.addProduct()
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpValidators() {
        binding.productName.addTextChangedListener(TextFieldValidation(binding.productName) { result ->
            isQuantity = result
        })
        binding.costPrice.addTextChangedListener(TextFieldValidation(binding.costPrice) { result ->
            isCostPrice = result
        })
        binding.sellingPrice.addTextChangedListener(TextFieldValidation(binding.sellingPrice) { result ->
            isSellingPrice = result
        })
        binding.quantity.addTextChangedListener(TextFieldValidation(binding.quantity) { result ->
            isCostPrice = result
        })
        binding.units.addTextChangedListener(TextFieldValidation(binding.units) { result ->
            isSellingPrice = result
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