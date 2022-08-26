package team.pacify.bookeet.ui.qrcode.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import team.pacify.bookeet.databinding.FragmentGenerateQrCodeBinding
import team.pacify.bookeet.utils.Extensions

class GenerateQrCodeFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGenerateQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateQrCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog != null) {
            Extensions.expand(view, dialog!!, requireActivity(), true)
            dialog!!.window?.setDimAmount(0f)
        }

        binding.apply {
            materialToolbar.setNavigationOnClickListener {
                dismiss()
            }
        }

    }

}