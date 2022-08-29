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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.InvoiceAdapter
import team.pacify.bookeet.databinding.FragmentInvoiceBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

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

        viewModel.getInvoices(firebaseAuth.currentUser?.uid ?: return)

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
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
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE

                    if (resource.data == null || resource.data.isEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                    } else {
                        binding.empty.visibility = View.GONE
                    }

                    invoiceAdapter.setSales(resource.data ?: emptyList())
                }
            }
        }

    }

}