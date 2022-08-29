package team.pacify.bookeet.ui.money.send

import android.app.ProgressDialog
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.databinding.FragmentSendMoneyBinding
import team.pacify.bookeet.utils.Extensions.toNaira
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.Resource
import java.util.*
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

@AndroidEntryPoint
class SendMoneyFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val viewModel: SendMoneyViewModel by viewModels()
    private lateinit var binding: FragmentSendMoneyBinding

    private var isAmount = false
    private var isBank = false
    private var isAccountNumber = false

    private lateinit var progressDialog: ProgressDialog

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
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            send.setOnClickListener {
                promptConfirmation()
            }
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Sending money")
            setCancelable(false)
        }

        viewModel.sending.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressDialog.show()
                }
                is Resource.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("Money sent")
                        setMessage("You have successfully sent ${resource.data?.amount?.toNaira()} to ${resource?.data?.accountName}")
                        setNegativeButton("Close") { _, _ ->
                            findNavController().popBackStack()
                        }
                    }.show()
                }
            }
        }

        viewModel.banks.observe(viewLifecycleOwner) { banks ->
            val banksAdapter = ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                banks.map { it.name }
            )
            binding.banks.setAdapter(banksAdapter)
        }

    }

    private fun promptConfirmation() {
        if (allInputsValidated()) {

            val user = "Demo User ${Random.nextInt(1..100)}"
            val amount = binding.amount.text.toString().trim().toDouble()

            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Confirm transaction")
                setMessage("You are about to send an irreversible transaction to $user - ${amount.toNaira()}")
                setNegativeButton("Cancel", null)
                setPositiveButton("Confirm") { _, _ ->
                    viewModel.sendMoney(
                        Transaction(
                            timeStamp = Calendar.getInstance().time,
                            userId = firebaseAuth.currentUser?.uid ?: return@setPositiveButton,
                            accountNumber = binding.accountNumber.text.toString().trim(),
                            amount = amount,
                            status = "pending",
                            type = "TRANSFER",
                            accountName = user
                        )
                    )
                }
            }.show()
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
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