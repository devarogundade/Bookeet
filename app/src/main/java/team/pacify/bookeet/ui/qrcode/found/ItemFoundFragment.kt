package team.pacify.bookeet.ui.qrcode.found

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.data.models.person.Customer
import team.pacify.bookeet.databinding.FragmentItemFoundBinding
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.expand
import team.pacify.bookeet.utils.UIConstants

class ItemFoundFragment(
    private val product: Product,
    private val sold: (Int, Product, Customer?) -> Unit
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentItemFoundBinding
    private lateinit var counterHandler: CounterHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemFoundBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog != null) {
            expand(view, dialog!!, requireActivity(), true)
            dialog!!.window?.setDimAmount(0f)
        }

        counterHandler = CounterHandler.Builder()
            .listener(counterListener)
            .incrementalView(binding.increment)
            .decrementalView(binding.decrement)
            .minRange(UIConstants.MIN_ITEM_IN_STORE)
            .maxRange(UIConstants.MAX_ITEM_IN_STORE)
            .startNumber(0)
            .build()

        binding.apply {
            materialToolbar.apply {
                title = product.name
            }.setNavigationOnClickListener {
                dismiss()
            }

            question.text = "How many ${product.unit} did you sold?"

            quantity.apply {
                setText("0")
                addTextChangedListener(quantityWatcher)
            }

            sold.setOnClickListener {
                val qty = binding.quantity.text.toString().trim()

                if (qty.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Enter the amount you sold",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (product.inStock < qty.toInt()) {
                    Toast.makeText(
                        requireContext(),
                        "You have only ${product.inStock} in stock",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                sold(qty.toInt(), product, null)
                dismiss()
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

}