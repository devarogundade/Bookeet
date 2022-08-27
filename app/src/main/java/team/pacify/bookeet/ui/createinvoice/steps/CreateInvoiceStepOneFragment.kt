package team.pacify.bookeet.ui.createinvoice.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.databinding.FragmentCreateInvoiceStepOneBinding
import team.pacify.bookeet.pager.ResultPagerFragment


class CreateInvoiceStepOneFragment : ResultPagerFragment() {

    private lateinit var binding: FragmentCreateInvoiceStepOneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateInvoiceStepOneBinding.inflate(inflater)
        return binding.root
    }

    override fun onClick() = Unit

    override fun invoice(): Sale? {
        return null
    }

}