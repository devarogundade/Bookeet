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
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.InventoryAdapter
import team.pacify.bookeet.databinding.FragmentInventoryBinding
import team.pacify.bookeet.ui.qrcode.generate.GenerateQrCodeFragment
import team.pacify.bookeet.utils.Resource
import team.pacify.bookeet.utils.ScrollAdapter
import team.pacify.bookeet.utils.UIConstants.FIREBASE_LOAD_SIZE
import javax.inject.Inject

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentInventoryBinding
    private lateinit var emptyInventory: EmptyInventory
    private val viewModel: InventoryViewModel by viewModels()
    private var scrollAdapter: ScrollAdapter? = null


    private val inventoryAdapter = InventoryAdapter({ _ ->
        GenerateQrCodeFragment().show(childFragmentManager, "childFragmentManager")
    }, { _ ->
        findNavController().navigate(R.id.action_mainFragment_to_editItemFragment)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initial call for data
        viewModel.getProducts(firebaseAuth.currentUser?.uid ?: return)

        emptyInventory = EmptyInventory(binding.emptyInventory) {
            findNavController().navigate(R.id.action_mainFragment_to_baseAddFragment)
        }

        binding.apply {
            root.setOnRefreshListener {
                viewModel.getProducts(firebaseAuth.currentUser?.uid ?: return@setOnRefreshListener)
            }

            items.apply {
                adapter = inventoryAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (scrollAdapter == null) {
                        // initialize scroll helper class
                        scrollAdapter = ScrollAdapter(recyclerView)
                    }

                    scrollAdapter!!.onScroll { hasReachedBottom ->
                        if (hasReachedBottom && (inventoryAdapter.itemCount % FIREBASE_LOAD_SIZE.toInt()) == 0) {
                            // calls for more data user user scroll to last result
                            viewModel.getProducts(firebaseAuth.currentUser?.uid ?: return@onScroll)
                        }
                    }
                }
            })

            fab.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_baseAddFragment)
            }
        }

        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.root.isRefreshing = true
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    binding.root.isRefreshing = false

                    if (inventoryAdapter.itemCount == 0) {
                        emptyInventory.show()
                    }
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {
                        emptyInventory.show()
                        return@observe
                    }

                    binding.root.isRefreshing = false
                    emptyInventory.hide()

                    inventoryAdapter.setProducts(resource.data)
                }
            }
        }

    }

}