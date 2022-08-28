package team.pacify.bookeet.ui.settings

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.person.User
import team.pacify.bookeet.databinding.FragmentProfileSettingsBinding
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSettingsFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentProfileSettingsBinding
    private val viewModel: ProfileSettingsViewModel by viewModels()

    private var user: User? = null

    private var isAccountName = false
    private var isEmailAddress = false
    private var isPin = false
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = requireArguments().getSerializable("user") as User?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpValidators()
        fillFields(user)

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            updateProfile.setOnClickListener {
                if (allInputsValidated()) {
                    viewModel.updateUser(
                        User(
                            id = firebaseAuth.currentUser?.uid ?: return@setOnClickListener,
                            name = binding.accountName.text.toString().trim(),
                            accountNumber = 1234567890,
                            emailAddress = binding.email.text.toString().trim(),
                            phoneNumber = firebaseAuth.currentUser?.phoneNumber?.replace("+234", "")
                                ?: return@setOnClickListener,
                            bvn = binding.bvn.text.toString().trim(),
                            photo = null,
                            userId = firebaseAuth.currentUser?.uid ?: return@setOnClickListener,
                            pin = binding.pin.text.toString().trim().toInt()
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Updating profile")
            setCancelable(false)
        }

        viewModel.user.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> progressDialog.show()
                is Resource.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("Profile updated!")
                        setMessage("You have successfully updated your profile")
                        setNegativeButton("Okay", null)
                    }.show()

                    fillFields(resource.data)
                }
            }
        }

    }

    private fun fillFields(user: User?) {
        Log.d("TAG", "fillFields: $user")
        binding.phoneNumber.setText(firebaseAuth.currentUser?.phoneNumber?.replace("+234", ""))

        if (user != null) {
            binding.apply {
                accountName.setText(user.name)
                accountNumber.setText(user.accountNumber.toString())
                email.setText(user.emailAddress)
                if (user.pin > 999) {
                    pin.setText(user.pin.toString())
                }
                bvn.setText(user.bvn)
            }
        }
    }

    private fun setUpValidators() {
        binding.accountName.addTextChangedListener(TextFieldValidation(binding.accountName) { result ->
            isAccountName = result
        })
        binding.email.addTextChangedListener(TextFieldValidation(binding.email) { result ->
            isEmailAddress = result
        })
        binding.pin.addTextChangedListener(TextFieldValidation(binding.pin) { result ->
            isPin = result
        })
    }

    private fun allInputsValidated(): Boolean =
        isAccountName && isEmailAddress && isPin

    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.accountName -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.accountName,
                        binding.accountNameLayout,
                        minLength = 3,
                        message = getString(R.string.min_name)
                    )
                )
                R.id.email -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.email,
                        binding.emailLayout,
                        isEmail = true
                    )
                )
                R.id.pin -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.pin,
                        binding.pinLayout,
                        requiredLength = 4
                    )
                )
                R.id.bvn -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.bvn,
                        binding.bvnLayout,
                        requiredLength = 11,
                        optional = true
                    )
                )
            }
        }
    }


}