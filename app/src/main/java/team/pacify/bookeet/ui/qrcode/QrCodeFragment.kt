package team.pacify.bookeet.ui.qrcode

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentQrCodeBinding
import team.pacify.bookeet.ui.qrcode.found.ItemFoundFragment
import team.pacify.bookeet.utils.Resource

@AndroidEntryPoint
class QrCodeFragment : Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private val viewModel: QrCodeViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

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

        progressDialog = ProgressDialog(requireContext()).apply {
            setTitle("Finding product")
            setCancelable(false)
        }

        viewModel.product.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> progressDialog.show()
                is Resource.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    progressDialog.dismiss()

                    if (resource.data == null) {
                        Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT)
                            .show()
                        return@observe
                    }

                    ItemFoundFragment(resource.data) { qty, product, customer ->
                        viewModel.recordSale(qty, product, customer)
                    }.show(
                        childFragmentManager,
                        "childFragmentManager"
                    )
                }
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
            viewModel.findProduct(result.contents)
        } else {
            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
        }
    }

}