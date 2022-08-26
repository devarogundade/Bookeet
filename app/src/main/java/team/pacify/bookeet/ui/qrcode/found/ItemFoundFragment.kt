package team.pacify.bookeet.ui.qrcode.found

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.databinding.FragmentItemFoundBinding
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.expand
import team.pacify.bookeet.utils.UIConstants

class ItemFoundFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentItemFoundBinding
    private lateinit var counterHandler: CounterHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemFoundBinding.inflate(inflater)
        return binding.root
    }

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
            materialToolbar.setNavigationOnClickListener {
                dismiss()
            }

            quantity.apply {
                setText("0")
                addTextChangedListener(quantityWatcher)
            }

            sold.setOnClickListener {
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