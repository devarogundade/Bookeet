package team.pacify.bookeet.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.SalesAdapter
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.databinding.FragmentHomeBinding
import team.pacify.bookeet.ui.main.MainFragmentDirections
import team.pacify.bookeet.utils.Extensions.toNaira
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var user: User? = null

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
        viewModel.getUser(firebaseAuth.currentUser?.uid ?: return)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSales(firebaseAuth.currentUser?.uid ?: return)

        binding.apply {
            refresh.setOnRefreshListener {
                viewModel.getSales(firebaseAuth.currentUser?.uid ?: return@setOnRefreshListener)
            }
            viewAll.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_salesFragment)
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
                    R.id.settings -> {
                        val action =
                            MainFragmentDirections.actionMainFragmentToProfileSettingsFragment(user)
                        findNavController().navigate(action)
                    }
                    R.id.signOut -> signOut()
                }
                root.closeDrawer(GravityCompat.START)
                true
            }
            transactions.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_transactionsFragment)
            }
            allSales.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_salesFragment)
            }
            requestMoney.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_requestMoneyFragment)
            }
            sendMoney.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_sendMoneyFragment)
            }
            accountDetails.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_accountFragment)
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            this.user = user
            if (user != null) {
                binding.apply {
                    welcome.text = if (user.name.contains(" ")) {
                        "Welcome back, ${user.name.split(" ").first()}"
                    } else {
                        "Welcome back, ${user.name}"
                    }
                    name.text = user.name
                    balance.text = 0.0.toNaira()
                }
            } else {
                binding.welcome.text = "Hi, welcome"
                binding.balance.text = 0.0.toNaira()
            }
        }

        viewModel.sales.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.refresh.isRefreshing = true
                }
                is Resource.Error -> {
                    binding.refresh.isRefreshing = true
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.refresh.isRefreshing = false

                    if (resource.data == null || resource.data.isEmpty()) {
                    }

                    salesAdapter.setSales(resource.data ?: emptyList())
                }
            }
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        findNavController().navigate(R.id.action_mainFragment_to_phoneFragment)
    }

}