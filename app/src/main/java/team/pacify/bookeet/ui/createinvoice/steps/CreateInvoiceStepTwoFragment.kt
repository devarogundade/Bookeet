package team.pacify.bookeet.ui.createinvoice.steps

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.databinding.FragmentCreateInvoiceStepTwoBinding
import team.pacify.bookeet.pager.InvoicePagerFragment
import team.pacify.bookeet.utils.Extensions.validateInput

class CreateInvoiceStepTwoFragment : InvoicePagerFragment() {

    private var isAmountReceived = false

    private lateinit var binding: FragmentCreateInvoiceStepTwoBinding
    private var paymentMethod = "Cash"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateInvoiceStepTwoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpValidators()

        binding.apply {
            paymentMethod.setOnCheckedChangeListener { _, id ->
                this@CreateInvoiceStepTwoFragment.paymentMethod = when (id) {
                    R.id.cash -> "Cash"
                    R.id.cheque -> "Cheque"
                    else -> "Internet transfer"
                }
            }
        }

    }

    override fun onClick() = Unit

    override fun invoice(): Invoice? {
        if (!allInputValidated()) return null

        return Invoice(
            amountReceived = binding.amountReceived.text.toString().toDouble(),
            paymentMethod = paymentMethod
        )
    }

    private fun allInputValidated() = isAmountReceived

    private fun setUpValidators() {
        binding.amountReceived.addTextChangedListener(TextFieldValidation(binding.amountReceived) { result ->
            isAmountReceived = result
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
                R.id.amountReceived -> result(
                    this@CreateInvoiceStepTwoFragment.requireContext().validateInput(
                        binding.amountReceived,
                        binding.amountReceivedLayout
                    )
                )
            }
        }
    }

}