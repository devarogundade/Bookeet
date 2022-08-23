package team.pacify.bookeet.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.databinding.FragmentProfileBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

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

    override fun onResume() {
        super.onResume()
        val user = firebaseAuth.currentUser ?: return
        viewModel.getUser(user.uid)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unsetProfile = UnsetProfile(binding.unsetProfile) {

        }

        viewModel.user.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    unsetProfile.show()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    if (resource.data == null) {
                        binding.setProfile.visibility = View.GONE
                        unsetProfile.show()
                        return@observe
                    }

                    binding.setProfile.visibility = View.VISIBLE
                    unsetProfile.hide()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

    }


}