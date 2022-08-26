package team.pacify.bookeet.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.SalesAdapter
import team.pacify.bookeet.databinding.FragmentHomeBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentHomeBinding
    private val salesAdapter = SalesAdapter()

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSales()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewAll.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_transactionsFragment)
            }
            sales.apply {
                adapter = salesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            materialToolbar.setNavigationOnClickListener {
                binding.root.openDrawer(GravityCompat.START)
            }
            drawer.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.invoice -> findNavController().navigate(R.id.action_mainFragment_to_invoiceFragment)
                    R.id.bookKeeping -> findNavController().navigate(R.id.action_mainFragment_to_bookKeepingFragment)
                    R.id.reminder -> findNavController().navigate(R.id.action_mainFragment_to_reminderFragment)
                    R.id.settings -> findNavController().navigate(R.id.action_mainFragment_to_profileSettingsFragment)
                    R.id.signOut -> signOut()
                }
                root.closeDrawer(GravityCompat.START)
                true
            }
            requestMoney.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_requestMoneyFragment)
            }
            sendMoney.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_sendMoneyFragment)
            }
        }

        viewModel.sales.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                else -> {
                    if (resource.data == null || resource.data.isEmpty()) {

                        return@observe
                    }

                    salesAdapter.setSales(resource.data)
                }
            }
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        findNavController().navigate(R.id.action_mainFragment_to_phoneFragment)
    }

}