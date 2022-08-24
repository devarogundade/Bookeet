package team.pacify.bookeet.pager

import androidx.fragment.app.FragmentActivity
import team.pacify.bookeet.ui.additem.AddItemFragment

class AddItemPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<AddItemFragment>
) : PagerAdapter(fragmentActivity, fragments) {

    fun save(position: Int) {
        fragments[position].onSaveClick()
    }

}
