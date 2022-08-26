package team.pacify.bookeet.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentProfileSettingsBinding
import team.pacify.bookeet.utils.Extensions.validateInput

class ProfileSettingsFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingsBinding

    private var isAccountName = false
    private var isAccountNumber = false
    private var isEmailAddress = false
    private var isPin = false
    private var isBvn = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            updateProfile.setOnClickListener {
                if (allInputsValidated()) {

                } else {
                    Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun setUpValidators() {
        binding.accountName.addTextChangedListener(TextFieldValidation(binding.accountName) { result ->
            isAccountName = result
        })
        binding.accountNumber.addTextChangedListener(TextFieldValidation(binding.accountNumber) { result ->
            isAccountNumber = result
        })
        binding.email.addTextChangedListener(TextFieldValidation(binding.email) { result ->
            isEmailAddress = result
        })
        binding.pin.addTextChangedListener(TextFieldValidation(binding.pin) { result ->
            isPin = result
        })
        binding.bvn.addTextChangedListener(TextFieldValidation(binding.bvn) { result ->
            isBvn = result
        })
    }

    private fun allInputsValidated(): Boolean =
        isAccountName && isAccountNumber && isEmailAddress && isPin && isBvn

    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.amount -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.accountName,
                        binding.accountNameLayout,
                        minLength = 3,
                        message = getString(R.string.min_name)
                    )
                )
                R.id.accountNumber -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.accountNumber,
                        binding.accountNumberLayout
                    )
                )
                R.id.email -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.email,
                        binding.emailLayout,
                        requiredLength = 10
                    )
                )
                R.id.pin -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.accountNumber,
                        binding.accountNumberLayout,
                        requiredLength = 4
                    )
                )
                R.id.bvn -> result(
                    this@ProfileSettingsFragment.requireContext().validateInput(
                        binding.email,
                        binding.emailLayout,
                        requiredLength = 11
                    )
                )
            }
        }
    }


}