package team.pacify.bookeet.ui.inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val user = firebaseAuth.currentUser ?: return
        viewModel.getProducts(user.uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyInventory = EmptyInventory(binding.emptyInventory) {

        }

        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {
                        emptyInventory.show()
                        return@observe
                    }

                    emptyInventory.hide()
                }
            }
        }

    }

}