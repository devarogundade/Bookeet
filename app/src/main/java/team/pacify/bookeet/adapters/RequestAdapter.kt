package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.databinding.RequestItemBinding

class RequestAdapter : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.request_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
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

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RequestItemBinding.bind(itemView)

        fun bind(sale: Sale) {
            binding.apply {
            }
        }
    }

}