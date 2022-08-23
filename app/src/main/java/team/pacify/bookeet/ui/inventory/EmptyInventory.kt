package team.pacify.bookeet.ui.inventory

import android.view.View
import team.pacify.bookeet.databinding.EmptyInventoryBinding

class EmptyInventory(
    private val binding: EmptyInventoryBinding,
    private val onAddNewProduct: () -> Unit
) {

    init {
        binding.addNewProduct.setOnClickListener {
            onAddNewProduct()
        }
    }

    fun show() {
        binding.root.visibility = View.VISIBLE
    }

    fun hide() {
        binding.root.visibility = View.GONE
    }

}