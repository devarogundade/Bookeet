package team.pacify.bookeet.ui.inventory.edititem

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.databinding.FragmentEditItemBinding
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.toNaira
import team.pacify.bookeet.utils.UIConstants.MAX_ITEM_IN_STORE
import team.pacify.bookeet.utils.UIConstants.MIN_ITEM_IN_STORE

class EditItemFragment(
    private val product: Product,
    private val update: (Product) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditItemBinding
    private lateinit var counterHandler: CounterHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditItemBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            close.setOnClickListener { findNavController().popBackStack() }
            name.text = product.name
            price.text = product.sellingPrice.toNaira()
            remaining.text = "Remaining - ${product.inStock}"
            quantity.text = product.inStock.toString()

            editItem.setOnClickListener {
                val qty = quantity.text.toString()

                if (qty.isEmpty()) {
                    Toast.makeText(requireContext(), "Invalid quantity", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                update(
                    product.copy(
                        inStock = qty.toInt()
                    )
                )
                dismiss()
            }
        }

        counterHandler = CounterHandler.Builder()
            .listener(counterListener)
            .incrementalView(binding.increment)
            .decrementalView(binding.decrement)
            .minRange(MIN_ITEM_IN_STORE)
            .maxRange(MAX_ITEM_IN_STORE)
            .startNumber(product.inStock)
            .build()
    }

    private val counterListener = object : CounterHandler.CounterListener {
        override fun onIncrement(view: View?, number: Int) {
            binding.quantity.text = number.toString()
        }

        override fun onDecrement(view: View?, number: Int) {
            binding.quantity.text = number.toString()
        }
    }

}