package team.pacify.bookeet.utils

import android.app.Dialog
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object Extensions {

    fun Double.toNaira(): String {
        return "₦ ${prettyCount()}"
    }

    private fun Double.prettyCount(): String {
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val value = floor(log10(this)).toInt()
        val base = value / 3
        return if (value >= 6 && base < suffix.size) {
            (this / 10.0.pow((base * 3).toDouble())).format(null) + suffix[base]
        } else {
            format(null)
        }
    }

    private fun Double.format(digits: Int?): String {
        val count: Int = digits
            ?: when (this) {
                in Double.MIN_VALUE..0.000000001 -> 9
                in Double.MIN_VALUE..0.000001 -> 8
                in Double.MIN_VALUE..0.0001 -> 7
                in Double.MIN_VALUE..0.001 -> 6
                in Double.MIN_VALUE..0.01 -> 5
                in Double.MIN_VALUE..0.1 -> 4
                in Double.MIN_VALUE..2.0 -> 3
                else -> 2
            }

        val decimalFormat = DecimalFormat().apply {
            minimumFractionDigits = count
            maximumFractionDigits = count
        }

        return decimalFormat.format(this)
    }

    private val VALID_EMAIL_ADDRESS_REGEX: Pattern =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private const val PHONE_SAMPLE = "+2348111755759"

    fun String.isAValidEmail(): Boolean {
        val matcher: Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(this)
        return matcher.find()
    }

    fun String.isAValidPhoneNumber(): Boolean {
        return (length == PHONE_SAMPLE.length) && (startsWith(PHONE_SAMPLE.substring(0, 3)))
    }

    // for expanding bottom sheet fragments to peek
    fun expand(view: View, dialog: Dialog, activity: FragmentActivity, lock: Boolean = false) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // cast to bottom sheet type
                dialog as BottomSheetDialog
                val bottomSheet =
                    dialog.findViewById<View>(
                        com.google.android.material.R.id.design_bottom_sheet
                    ) as FrameLayout

                // full height
                BottomSheetBehavior.from(bottomSheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    isDraggable = !lock
                }

                val viewGroupLayoutParams = bottomSheet.layoutParams

                // screen height
                viewGroupLayoutParams.height = activity.window?.decorView?.measuredHeight ?: 0
                bottomSheet.layoutParams = viewGroupLayoutParams
            }
        })
    }


}