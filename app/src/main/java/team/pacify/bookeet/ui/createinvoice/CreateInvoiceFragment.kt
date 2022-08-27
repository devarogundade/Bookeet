package team.pacify.bookeet.ui.createinvoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentCreateInvoiceBinding
import team.pacify.bookeet.pager.InvoiceInteractivePagerAdapter
import team.pacify.bookeet.ui.createinvoice.steps.CreateInvoiceStepOneFragment
import team.pacify.bookeet.ui.createinvoice.steps.CreateInvoiceStepTwoFragment

class CreateInvoiceFragment : Fragment() {

    private lateinit var binding: FragmentCreateInvoiceBinding

    private val stepOneFragment = CreateInvoiceStepOneFragment()
    private val stepTwoFragment = CreateInvoiceStepTwoFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateInvoiceBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InvoiceInteractivePagerAdapter(
            requireActivity(),
            listOf(
                stepOneFragment,
                stepTwoFragment
            )
        )

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                if (viewPager.currentItem > 0) viewPager.currentItem--
                else findNavController().popBackStack()
            }

            viewPager.apply {
                this.adapter = adapter
                isUserInputEnabled = false
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position == 0) create.text = getString(R.string.next)
                        else create.text = getString(R.string.create_invoice)
                    }
                })
            }

            create.setOnClickListener {
                val result = adapter.getInvoice(viewPager.currentItem)

                if (result != null) {
                    if (viewPager.currentItem == 0) {
                        viewPager.currentItem++
                    } else createInvoice()
                } else {
                    Toast.makeText(requireContext(), "Fill the required fields", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun createInvoice() {

    }

}