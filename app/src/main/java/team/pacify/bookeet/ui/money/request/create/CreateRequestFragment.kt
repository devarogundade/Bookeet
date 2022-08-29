package team.pacify.bookeet.ui.money.request.create

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.databinding.FragmentCreateRequestBinding
import team.pacify.bookeet.ui.money.request.RequestMoneyViewModel
import team.pacify.bookeet.utils.Extensions.validateInput
import team.pacify.bookeet.utils.Resource
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CreateRequestFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentCreateRequestBinding
    private val viewModel: RequestMoneyViewModel by viewModels()

    private var isAmount = false
    private var isCustomerName = false
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateRequestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRequests(firebaseAuth.currentUser?.uid ?: return)
        setUpValidators()

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            createRequest.setOnClickListener {
                if (allInputsValidated()) {
                    viewModel.createRequest(
                        Request(
                            userId = firebaseAuth.currentUser?.uid ?: return@setOnClickListener,
                            customerName = binding.customerName.text.toString().trim(),
                            amount = binding.amount.text.toString().toDouble(),
                            timestamp = Calendar.getInstance().time
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Creating request")
            setCancelable(false)
        }

        viewModel.createRequest.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> progressDialog.show()
                is Resource.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressDialog.dismiss()
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setTitle("Request sent!")
                        setMessage("You have successfully request money from ${resource.data?.customerName}")
                        setNegativeButton("Close") { _, _ -> findNavController().popBackStack() }
                    }.show()
                }
            }
        }

    }

    private fun setUpValidators() {
        binding.amount.addTextChangedListener(TextFieldValidation(binding.amount) { result ->
            isAmount = result
        })
        binding.customerName.addTextChangedListener(TextFieldValidation(binding.customerName) { result ->
            isCustomerName = result
        })
    }

    private fun allInputsValidated(): Boolean = isAmount && isCustomerName

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
                    this@CreateRequestFragment.requireContext().validateInput(
                        binding.amount,
                        binding.amountLayout
                    )
                )
                R.id.customerName -> result(
                    this@CreateRequestFragment.requireContext().validateInput(
                        binding.customerName,
                        binding.customerNameLayout
                    )
                )
            }
        }
    }

}