package team.pacify.bookeet.ui.qrcode.generate

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.databinding.FragmentGenerateQrCodeBinding
import team.pacify.bookeet.utils.Extensions
import java.io.File
import java.io.FileOutputStream


class GenerateQrCodeFragment(private val product: Product) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentGenerateQrCodeBinding
    private var bitmap: Bitmap? = null

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
            materialToolbar.apply {
                title = product.name
            }.setNavigationOnClickListener {
                dismiss()
            }
            name.text = product.name
            saveQrCode.setOnClickListener {
                if (bitmap != null) saveImage(bitmap!!)
            }

            try {
                val barcodeEncoder = BarcodeEncoder()
                bitmap = barcodeEncoder.encodeBitmap(
                    product.barcodeString,
                    BarcodeFormat.QR_CODE,
                    400,
                    400
                )
                binding.image.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveImage(bitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fileName = "Image-${product.name}.jpg"
        val file = File(myDir, fileName)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.apply {
                flush()
                close()
            }
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to save", Toast.LENGTH_SHORT).show()
        }
    }

}