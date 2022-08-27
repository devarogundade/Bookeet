package team.pacify.bookeet.ui.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentAddItemBinding
import team.pacify.bookeet.pager.InteractivePagerAdapter
import team.pacify.bookeet.ui.additem.product.AddProductFragment
import team.pacify.bookeet.ui.additem.service.AddServiceFragment

class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val addProductFragment = AddProductFragment()
    private val addServiceFragment = AddServiceFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InteractivePagerAdapter(
            requireActivity(),
            listOf(
                addProductFragment,
                addServiceFragment
            )
        )

        binding.apply {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            viewPager.apply {
                this.adapter = adapter
            }

            save.setOnClickListener {
                adapter.click(viewPager.currentItem)
            }

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.product)
                    else -> getString(R.string.service)
                }
            }.attach()
        }

    }

}