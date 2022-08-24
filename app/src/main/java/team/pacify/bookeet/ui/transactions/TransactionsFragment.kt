package team.pacify.bookeet.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import team.pacify.bookeet.adapters.TransactionsAdapter
import team.pacify.bookeet.databinding.FragmentTransactionsBinding
import team.pacify.bookeet.utils.Resource

class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    private val transactionsAdapter = TransactionsAdapter()

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTransactions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            transactions.apply {
                adapter = transactionsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.transactions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar3.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBar3.visibility = View.GONE
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {

                        return@observe
                    }

                    transactionsAdapter.setTransactions(resource.data)
                    binding.progressBar3.visibility = View.GONE
                }
            }
        }

    }

}