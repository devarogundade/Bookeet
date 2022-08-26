package team.pacify.bookeet.ui.edititem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.databinding.FragmentEditItemBinding
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.UIConstants.MAX_ITEM_IN_STORE
import team.pacify.bookeet.utils.UIConstants.MIN_ITEM_IN_STORE

class EditItemFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditItemBinding
    private lateinit var counterHandler: CounterHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemQuantity = 1

        binding.apply {
            close.setOnClickListener { findNavController().popBackStack() }
            quantity.text = itemQuantity.toString()
        }

        counterHandler = CounterHandler.Builder()
            .listener(counterListener)
            .incrementalView(binding.increment)
            .decrementalView(binding.decrement)
            .minRange(MIN_ITEM_IN_STORE)
            .maxRange(MAX_ITEM_IN_STORE)
            .startNumber(itemQuantity)
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