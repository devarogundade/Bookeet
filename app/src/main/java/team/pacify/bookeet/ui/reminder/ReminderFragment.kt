package team.pacify.bookeet.ui.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import team.pacify.bookeet.adapters.RemindersAdapter
import team.pacify.bookeet.databinding.FragmentReminderBinding
import team.pacify.bookeet.utils.Resource

class ReminderFragment : Fragment() {

    private val viewModel: ReminderViewModel by viewModels()
    private lateinit var binding: FragmentReminderBinding
    private val remindersAdapter = RemindersAdapter({}, {})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getReminders()

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            createReminder.setOnClickListener {
            }

            reminders.apply {
                adapter = remindersAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.reminders.observe(viewLifecycleOwner) { resource ->
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
                    remindersAdapter.setSales(resource.data)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }
}