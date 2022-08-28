package team.pacify.bookeet.ui.createinvoice

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.databinding.FragmentCreateInvoiceBinding
import team.pacify.bookeet.pager.InvoiceInteractivePagerAdapter
import team.pacify.bookeet.ui.createinvoice.steps.CreateInvoiceStepOneFragment
import team.pacify.bookeet.ui.createinvoice.steps.CreateInvoiceStepTwoFragment
import team.pacify.bookeet.utils.Resource

@AndroidEntryPoint
class CreateInvoiceFragment : Fragment() {

    private val viewModel: CreateInvoiceViewModel by viewModels()

    private lateinit var binding: FragmentCreateInvoiceBinding
    private lateinit var progressDialog: ProgressDialog

    private val stepOneFragment = CreateInvoiceStepOneFragment()
    private val stepTwoFragment = CreateInvoiceStepTwoFragment()

    private var invoice = Invoice()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateInvoiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InvoiceInteractivePagerAdapter(
            requireActivity(),
            listOf(
                stepOneFragment,
                stepTwoFragment
            )
        )

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                if (viewPager.currentItem > 0) viewPager.currentItem--
                else findNavController().popBackStack()
            }

            viewPager.apply {
                this.adapter = adapter
                isUserInputEnabled = false
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position == 0) create.text = getString(R.string.next)
                        else create.text = getString(R.string.create_invoice)
                    }
                })
            }

            create.setOnClickListener {
                val result = adapter.getInvoice(viewPager.currentItem)

                if (result != null) {
                    if (viewPager.currentItem == 0) {
                        invoice = invoice.copy(
                            id = result.id,
                            date = result.date,
                            timeStamp = result.timeStamp,
                            customerName = result.customerName,
                            soldProductName = result.soldProductName,
                            soldProductAmount = result.soldProductAmount,
                            qty = result.qty
                        )
                        viewPager.currentItem++
                    } else {
                        invoice = invoice.copy(
                            amountReceived = result.amountReceived,
                            paymentMethod = result.paymentMethod
                        )
                        viewModel.addInvoice(invoice)
                    }
                } else {
                    Toast.makeText(requireContext(), "Fill the required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Creating invoice")
            setCancelable(false)
        }


        viewModel.invoice.observe(viewLifecycleOwner) { resource ->
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
                        setNegativeButton("Close") { _, _ ->
                            findNavController().popBackStack()
                        }
                        setOnCancelListener {
                            findNavController().popBackStack()
                        }
                    }.show()
                }
            }
        }

    }
}