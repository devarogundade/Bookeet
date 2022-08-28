package team.pacify.bookeet.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import team.pacify.bookeet.data.models.finance.Invoice

open class PagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}

open class InteractivePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<PagerFragment>
) : PagerAdapter(fragmentActivity, fragments) {

    fun click(position: Int) {
        fragments[position].onClick()
    }

}

class InvoiceInteractivePagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<InvoicePagerFragment>
) : InteractivePagerAdapter(fragmentActivity, fragments) {

    fun getInvoice(position: Int): Invoice? {
        return fragments[position].invoice()
    }

}