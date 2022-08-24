package team.pacify.bookeet.ui.bookkeeping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentBookKeepingBinding
import team.pacify.bookeet.pager.PagerAdapter
import team.pacify.bookeet.ui.bookkeeping.cashbook.CashbookFragment
import team.pacify.bookeet.ui.bookkeeping.debts.DebtsFragment

class BookKeepingFragment : Fragment() {

    private lateinit var binding: FragmentBookKeepingBinding
    private val cashbookFragment = CashbookFragment()
    private val debtsFragment = DebtsFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookKeepingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagerAdapter(
            requireActivity(),
            listOf(
                cashbookFragment,
                debtsFragment
            )
        )

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            viewPager.apply {
                this.adapter = adapter
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.cashbook)
                    else -> getString(R.string.debts)
                }
            }.attach()
        }

    }


}