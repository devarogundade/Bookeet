package team.pacify.bookeet.ui.createinvoice.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.pager.ResultPagerFragment

class CreateInvoiceStepTwoFragment : ResultPagerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_invoice_step_two, container, false)
    }

    override fun onClick() = Unit


    override fun invoice(): Sale? {
        return null
    }

}