package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.ocpsoft.prettytime.PrettyTime
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.ItemItemBinding
import team.pacify.bookeet.models.Sale

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(diffResult.currentList[position])
    }

    override fun getItemCount(): Int = diffResult.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(
            oldItem: Sale,
            newItem: Sale
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Sale,
            newItem: Sale
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setSales(sales: List<Sale>) {
        diffResult.submitList(sales)
    }

    inner class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prettyTime = PrettyTime()
        val binding = ItemItemBinding.bind(itemView)

        fun bind(sale: Sale) {
            binding.apply {

            }
        }
    }

}