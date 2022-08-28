package team.pacify.bookeet.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.databinding.FragmentProfileBinding
import team.pacify.bookeet.ui.main.MainFragmentDirections
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var user: User? = null

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var unsetProfile: UnsetProfile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser(firebaseAuth.currentUser?.uid ?: return)

        unsetProfile = UnsetProfile(binding.unsetProfile) {
            findNavController().navigate(R.id.action_mainFragment_to_profileSettingsFragment)
        }

        binding.apply {
            profileSettings.setOnClickListener {
                val action =
                    MainFragmentDirections.actionMainFragmentToProfileSettingsFragment(user)
                findNavController().navigate(action)
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { resource ->
            user = resource.data

            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE

                    if (resource.data == null) {
                        binding.setProfile.visibility = View.GONE
                        unsetProfile.show()
                        return@observe
                    }

                    binding.setProfile.visibility = View.VISIBLE
                    unsetProfile.hide()

                    val user = resource.data
                    binding.apply {
                        name.text = user.name
                        accountNumber.text = user.accountNumber.toString()
                    }
                }
            }
        }

    }


}