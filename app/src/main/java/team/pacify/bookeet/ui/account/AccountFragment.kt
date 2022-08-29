package team.pacify.bookeet.ui.account

import android.app.ProgressDialog
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
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.databinding.FragmentAccountBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()

    private var user: User? = null

    private lateinit var progressDialog: ProgressDialog

    private lateinit var emptyAccount: EmptyAccount

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser(firebaseAuth.currentUser?.uid ?: return)
        viewModel.getAccount(firebaseAuth.currentUser?.uid ?: return)

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        emptyAccount = EmptyAccount(binding.emptyAccount) {
            if (user == null) {
                Toast.makeText(requireContext(), "Set up your profile first", Toast.LENGTH_SHORT)
                    .show()
                return@EmptyAccount
            }
            viewModel.createAccount(firebaseAuth.currentUser?.uid ?: return@EmptyAccount)
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Creating your bank")
            setCancelable(false)
        }

        viewModel.creating.observe(viewLifecycleOwner) { creating ->
            if (creating) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { resource ->
            user = resource.data
        }

        viewModel.account.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE

                    if (resource.data == null) {
                        binding.setAccount.visibility = View.GONE
                        emptyAccount.show()
                        return@observe
                    }

                    binding.setAccount.visibility = View.VISIBLE
                    emptyAccount.hide()

                    val account = resource.data
                    binding.apply {
                        bankName.text = account.bankName
                        accountNumber.text = account.accNo
                        accountName.text = user?.name
                    }
                }
            }
        }

    }

}