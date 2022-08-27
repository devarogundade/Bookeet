package team.pacify.bookeet.ui.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.InvoiceAdapter
import team.pacify.bookeet.databinding.FragmentInvoiceBinding
import team.pacify.bookeet.utils.Resource

class InvoiceFragment : Fragment() {

    private val viewModel: InvoiceViewModel by viewModels()
    private lateinit var binding: FragmentInvoiceBinding
    private val invoiceAdapter = InvoiceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInvoiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getInvoices()

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            createInvoice.setOnClickListener {
                findNavController().navigate(R.id.action_invoiceFragment_to_createInvoiceFragment)
            }

            invoices.apply {
                adapter = invoiceAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.invoices.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.empty.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.empty.visibility = View.GONE

                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                        return@observe
                    }
                    invoiceAdapter.setSales(resource.data)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }


    }

}