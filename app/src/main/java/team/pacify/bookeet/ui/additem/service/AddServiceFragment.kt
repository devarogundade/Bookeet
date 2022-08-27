package team.pacify.bookeet.ui.additem.service

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentAddServiceBinding
import team.pacify.bookeet.pager.PagerFragment
import team.pacify.bookeet.ui.additem.AddItemViewModel
import team.pacify.bookeet.utils.Extensions.validateInput

class AddServiceFragment : PagerFragment() {

    private lateinit var binding: FragmentAddServiceBinding
    private val viewModel: AddItemViewModel by viewModels()

    private var isServiceName = false
    private var isServiceCharge = false
    private var isSellingPrice = false
    private var isUnit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddServiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpValidators()

    }

    override fun onClick() {
        if (allInputsValidated()) {
            viewModel.addService()
        } else {
            Toast.makeText(requireContext(), "Fill all required fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun allInputsValidated(): Boolean =
        isServiceName && isServiceCharge && isUnit


    private fun setUpValidators() {
        binding.serviceName.addTextChangedListener(TextFieldValidation(binding.serviceName) { result ->
            isServiceName = result
        })
        binding.serviceCharge.addTextChangedListener(TextFieldValidation(binding.serviceChargeLayout) { result ->
            isServiceCharge = result
        })
        binding.units.addTextChangedListener(TextFieldValidation(binding.units) { result ->
            isUnit = result
        })
    }


    inner class TextFieldValidation(
        private val view: View,
        private val result: (Boolean) -> Unit
    ) :
        TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.serviceName -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.serviceName,
                        binding.serviceNameLayout
                    )
                )
                R.id.serviceCharge -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.serviceCharge,
                        binding.serviceChargeLayout,
                        minLength = 2,
                    )
                )
                R.id.units -> result(
                    this@AddServiceFragment.requireContext().validateInput(
                        binding.units,
                        binding.unitLayout
                    )
                )
            }
        }
    }

}