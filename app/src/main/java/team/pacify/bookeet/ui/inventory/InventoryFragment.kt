package team.pacify.bookeet.ui.inventory

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
import team.pacify.bookeet.adapters.ItemsAdapter
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.databinding.FragmentInventoryBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentInventoryBinding
    private lateinit var emptyInventory: EmptyInventory
    private val viewModel: InventoryViewModel by viewModels()
    private val itemsAdapter = ItemsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        viewModel.getProducts(user?.uid)

        emptyInventory = EmptyInventory(binding.emptyInventory) {
            findNavController().navigate(R.id.action_mainFragment_to_baseAddFragment)
        }

        binding.apply {
            items.apply {
                adapter = itemsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_baseAddFragment)
            }
        }

        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.setInventory.visibility = View.GONE
                    emptyInventory.hide()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.setInventory.visibility = View.GONE
                    emptyInventory.show()
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {
                        emptyInventory.show()
                        return@observe
                    }

                    binding.progressBar.visibility = View.GONE
                    binding.setInventory.visibility = View.VISIBLE
                    emptyInventory.hide()

                    itemsAdapter.setSales(
                        listOf(
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                            Sale(),
                        )
                    )
                }
            }
        }

    }

}