package team.pacify.bookeet.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.databinding.VerificationCodeDialogBinding
import team.pacify.bookeet.utils.Extensions.expand

class VerificationCodeDialog(
    private val onEnterCode: (String) -> Unit,
    private val resendCode: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: VerificationCodeDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VerificationCodeDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog != null) {
            expand(view, dialog!!, requireActivity(), true)
            dialog!!.window?.setDimAmount(0f)
        }

        binding.apply {
            verifyCode.setOnClickListener {
                val code = this.code.text.toString().trim()

                if (code.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Enter a valid verification code",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                onEnterCode(code)
                dismiss()
            }
            resendCode.setOnClickListener {
                resendCode()
                dismiss()
            }
        }

    }


}