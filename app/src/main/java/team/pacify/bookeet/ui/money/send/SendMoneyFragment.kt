package team.pacify.bookeet.ui.money.send

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentSendMoneyBinding
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.UIConstants.ItemUnits

class SendMoneyFragment : Fragment() {

    private val viewModel: SendMoneyViewModel by viewModels()
    private lateinit var binding: FragmentSendMoneyBinding

    private var isAmount = false
    private var isBank = false
    private var isAccountNumber = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendMoneyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBanks()
        setUpValidators()

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            send.setOnClickListener {
                promptConfirmation()
            }
        }

        viewModel.banks.observe(viewLifecycleOwner) { banks ->
            val banksAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                ItemUnits
            )
            binding.banks.setAdapter(banksAdapter)
        }

    }

    private fun promptConfirmation() {
        if (allInputsValidated()) {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Confirm transaction")
                setMessage("You are about to send an irreversible transaction to ROSE MIKES - ₦ 4,000.00")
                setNegativeButton("Cancel", null)
                setPositiveButton("Confirm") { _, _ ->
                    sendMoney()
                }
            }.show()
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendMoney() {

    }

    private fun setUpValidators() {
        binding.amount.addTextChangedListener(TextFieldValidation(binding.amount) { result ->
            isAmount = result
        })
        binding.banks.addTextChangedListener(TextFieldValidation(binding.banks) { result ->
            isBank = result
        })
        binding.accountNumber.addTextChangedListener(TextFieldValidation(binding.accountNumber) { result ->
            isAccountNumber = result
        })
    }

    private fun allInputsValidated(): Boolean = isAmount && isBank && isAccountNumber

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
                    this@SendMoneyFragment.requireContext().validateInput(
                        binding.amount,
                        binding.amountLayout,
                        minLength = 3,
                        message = getString(R.string.min_amount_send)
                    )
                )
                R.id.banks -> result(
                    this@SendMoneyFragment.requireContext().validateInput(
                        binding.banks,
                        binding.bankLayout
                    )
                )
                R.id.accountNumber -> result(
                    this@SendMoneyFragment.requireContext().validateInput(
                        binding.accountNumber,
                        binding.accountNumberLayout,
                        requiredLength = 10
                    )
                )
            }
        }
    }


}