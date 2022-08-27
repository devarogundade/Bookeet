package team.pacify.bookeet.ui.qrcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentQrCodeBinding
import team.pacify.bookeet.ui.qrcode.found.ItemFoundFragment

class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            scan.setOnClickListener {
                scanItemCode()
            }
        }

    }

    private fun scanItemCode() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt(getString(R.string.scan_item))
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
        }
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            foundItem()
        }
    }

    private fun foundItem() {
        ItemFoundFragment().show(childFragmentManager, "childFragmentManager")
    }
}