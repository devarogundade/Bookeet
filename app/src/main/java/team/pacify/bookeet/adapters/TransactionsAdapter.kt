package team.pacify.bookeet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.TransactionItemBinding
import team.pacify.bookeet.models.Sale
import java.text.SimpleDateFormat

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        return TransactionsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.bind(diffResult.currentList[position])
    }

    override fun getItemCount(): Int = diffResult.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<List<Sale>>() {
        override fun areItemsTheSame(
            oldItem: List<Sale>,
            newItem: List<Sale>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: List<Sale>,
            newItem: List<Sale>
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setTransactions(transactions: List<List<Sale>>) {
        diffResult.submitList(transactions)
    }

    inner class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = TransactionItemBinding.bind(itemView)
        private val salesAdapter = SalesAdapter()

        @SuppressLint("SimpleDateFormat")
        fun bind(sales: List<Sale>) {
            binding.apply {
                date.text = SimpleDateFormat("dd MM yyyy").format(sales[0].soldOn)
                this.sales.apply {
                    adapter = salesAdapter
                    layoutManager = LinearLayoutManager(itemView.context)
                }
            }

            salesAdapter.setSales(sales)
        }
    }

}