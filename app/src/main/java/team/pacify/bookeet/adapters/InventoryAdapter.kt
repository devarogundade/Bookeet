package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.inventory.Product
import team.pacify.bookeet.databinding.InventoryItemBinding
import team.pacify.bookeet.utils.Extensions.toNaira

class InventoryAdapter(
    private val onProductQrCode: (Product) -> Unit,
    private val onProductEdit: (Product) -> Unit,
) :
    RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.inventory_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        holder.bind(diffResult.currentList[position])
    }

    override fun getItemCount(): Int = diffResult.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setProducts(product: List<Product>) {
        diffResult.submitList(product)
    }

    inner class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = InventoryItemBinding.bind(itemView)

        fun bind(product: Product) {
            binding.apply {
                name.text = product.name
                inStock.text = product.inStock.toString()
                price.text = product.sellingPrice.toNaira()

                editItem.setOnClickListener { onProductEdit(product) }
                qrcode.setOnClickListener { onProductQrCode(product) }
            }
        }
    }

}