package team.pacify.bookeet.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentMainBinding
import team.pacify.bookeet.pager.PagerAdapter
import team.pacify.bookeet.ui.home.HomeFragment
import team.pacify.bookeet.ui.inventory.InventoryFragment
import team.pacify.bookeet.ui.profile.ProfileFragment
import team.pacify.bookeet.ui.qrcode.QrCodeFragment

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val homeFragment = HomeFragment()
    private val inventoryFragment = InventoryFragment()
    private val qrCodeFragment = QrCodeFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = PagerAdapter(
            requireActivity(), listOf(
                homeFragment,
                inventoryFragment,
                qrCodeFragment,
                profileFragment
            )
        )

        binding.apply {
            viewPager.apply {
                this.adapter = pagerAdapter
                isUserInputEnabled = false
            }

            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.profileFragment -> viewPager.currentItem = 3
                    R.id.qrCodeFragment -> viewPager.currentItem = 2
                    R.id.inventoryFragment -> viewPager.currentItem = 1
                    else -> viewPager.currentItem = 0
                }
                true
            }
        }

    }
}