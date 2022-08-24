package team.pacify.bookeet.ui.additem.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import team.pacify.bookeet.R
import team.pacify.bookeet.ui.additem.AddItemFragment

class AddProductFragment : AddItemFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onSaveClick() {

    }

}