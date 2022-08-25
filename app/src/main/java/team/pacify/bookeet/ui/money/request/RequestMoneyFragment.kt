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
import team.pacify.bookeet.R
import team.pacify.bookeet.adapters.RequestAdapter
import team.pacify.bookeet.databinding.FragmentRequestMoneyBinding
import team.pacify.bookeet.utils.RecyclerViewDivider
import team.pacify.bookeet.utils.Resource

class RequestMoneyFragment : Fragment() {

    private lateinit var binding: FragmentRequestMoneyBinding
    private val requestAdapter = RequestAdapter()
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

        viewModel.getRequests()

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
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
                    requestAdapter.setSales(resource.data)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }


    }

}