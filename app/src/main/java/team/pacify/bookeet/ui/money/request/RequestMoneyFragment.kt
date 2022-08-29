package team.pacify.bookeet.ui.money.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.RequestAdapter
import team.pacify.bookeet.databinding.FragmentRequestMoneyBinding
import team.pacify.bookeet.utils.RecyclerViewDivider
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class RequestMoneyFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentRequestMoneyBinding
    private val requestAdapter = RequestAdapter { _ ->
        Toast.makeText(requireContext(), "Reminder sent", Toast.LENGTH_SHORT).show()
    }
    private val viewModel: RequestMoneyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestMoneyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRequests(firebaseAuth.currentUser?.uid ?: return)

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            requestMoney.setOnClickListener {
                findNavController().navigate(R.id.action_requestMoneyFragment_to_createRequestFragment)
            }

            requests.apply {
                adapter = requestAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    RecyclerViewDivider(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_gray
                        ),
                        height = 1
                    )
                )
            }
        }

        viewModel.requests.observe(viewLifecycleOwner) { resource ->
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

                    binding.requestsCount.text = resource.data?.size?.toString() ?: "0"
                    requestAdapter.setSales(resource.data ?: emptyList())
                }
            }
        }


    }

}