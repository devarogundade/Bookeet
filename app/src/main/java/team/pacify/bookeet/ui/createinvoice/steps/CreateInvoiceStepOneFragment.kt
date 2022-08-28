package team.pacify.bookeet.ui.createinvoice.steps

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.databinding.FragmentCreateInvoiceStepOneBinding
import team.pacify.bookeet.pager.InvoicePagerFragment
import team.pacify.bookeet.ui.createinvoice.CreateInvoiceViewModel
import team.pacify.bookeet.utils.CounterHandler
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.UIConstants
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateInvoiceStepOneFragment : InvoicePagerFragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: CreateInvoiceViewModel by viewModels()

    private var isInvoiceId = false
    private var isInvoiceDate = false
    private var isCustomerName = false
    private var isProductName = false
    private var isAmount = false
    private var qty = 1

    private lateinit var counterHandler: CounterHandler

    private lateinit var binding: FragmentCreateInvoiceStepOneBinding

    private var date = Calendar.getInstance().time

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateInvoiceStepOneBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            getProducts(firebaseAuth.currentUser?.uid ?: return)
            getCustomers(firebaseAuth.currentUser?.uid ?: return)
        }

        setUpValidators()
        counterHandler = CounterHandler.Builder()
            .listener(counterListener)
            .incrementalView(binding.increment)
            .decrementalView(binding.decrement)
            .minRange(1)
            .maxRange(UIConstants.MAX_ITEM_IN_STORE)
            .startNumber(qty)
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
        datePicker.addOnPositiveButtonClickListener { time ->
            this.date = Date(time)
            binding.invoiceDate.setText(SimpleDateFormat("dd/MM/yyyy").format(date))
        }

        binding.apply {
            invoiceId.setText(UUID.randomUUID().toString().substring(0, 10))
            invoiceDate.setText(SimpleDateFormat("dd/MM/yyyy").format(date))

            this.datePicker.setOnClickListener {
                try {
                    datePicker.show(childFragmentManager, "childFragmentManager")
                } catch (e: Exception) {
                }
            }
        }

        viewModel.products.observe(viewLifecycleOwner) { products ->
            val productsAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                products.map { it.name }
            )
            binding.productName.setAdapter(productsAdapter)
        }

        viewModel.customers.observe(viewLifecycleOwner) { customers ->
            val customersAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                customers.map { it.name }
            )
            binding.customerName.setAdapter(customersAdapter)
        }

    }

    override fun onClick() = Unit

    override fun invoice(): Invoice? {
        if (!allInputValidated()) return null

        return Invoice(
            invoiceId = binding.invoiceId.text.toString().trim(),
            customerName = binding.customerName.text.toString().trim(),
            timeStamp = Calendar.getInstance().time,
            date = date,
            soldProductAmount = 0.0,
            qty = qty,
            soldProductName = binding.productName.text.toString().trim()
        )
    }

    private fun setUpValidators() {
        binding.invoiceId.addTextChangedListener(TextFieldValidation(binding.invoiceId) { result ->
            isInvoiceId = result
        })
        binding.invoiceDate.addTextChangedListener(TextFieldValidation(binding.invoiceDate) { result ->
            isInvoiceDate = result
        })
        binding.customerName.addTextChangedListener(TextFieldValidation(binding.customerName) { result ->
            isCustomerName = result
        })
        binding.productName.addTextChangedListener(TextFieldValidation(binding.productName) { result ->
            isProductName = result
        })
        binding.amount.addTextChangedListener(TextFieldValidation(binding.amount) { result ->
            isAmount = result
        })
    }

    private fun allInputValidated() =
        isInvoiceId && isInvoiceDate && isCustomerName && isProductName && isAmount

    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.invoiceId -> result(
                    this@CreateInvoiceStepOneFragment.requireContext().validateInput(
                        binding.invoiceId,
                        binding.invoiceIdLayout
                    )
                )
                R.id.invoiceDate -> result(
                    this@CreateInvoiceStepOneFragment.requireContext().validateInput(
                        binding.invoiceDate,
                        binding.invoiceDateLayout
                    )
                )
                R.id.customerName -> result(
                    this@CreateInvoiceStepOneFragment.requireContext().validateInput(
                        binding.customerName,
                        binding.customerNameLayout
                    )
                )
                R.id.productName -> result(
                    this@CreateInvoiceStepOneFragment.requireContext().validateInput(
                        binding.productName,
                        binding.productNameLayout
                    )
                )
                R.id.amount -> result(
                    this@CreateInvoiceStepOneFragment.requireContext().validateInput(
                        binding.amount,
                        binding.amountLayout
                    )
                )
            }
        }
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