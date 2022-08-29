package team.pacify.bookeet.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.adapters.TransactionsAdapter
import team.pacify.bookeet.databinding.FragmentTransactionsBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.syncTransactions(firebaseAuth.currentUser?.uid ?: return)
        viewModel.getTransactions(firebaseAuth.currentUser?.uid ?: return)

        binding.apply {
            transactions.apply {
                adapter = transactionsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.transactions.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                    } else {
                        binding.empty.visibility = View.GONE
                    }

                    transactionsAdapter.setTransactions(resource.data ?: emptyList())
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }

}