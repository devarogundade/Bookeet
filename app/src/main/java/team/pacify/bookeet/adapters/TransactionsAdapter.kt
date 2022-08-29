package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.ocpsoft.prettytime.PrettyTime
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Transaction
import team.pacify.bookeet.databinding.TransactionItemBinding
import team.pacify.bookeet.utils.Extensions.toNaira

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

    private val diffUtil = object : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Transaction,
            newItem: Transaction
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setTransactions(transactions: List<Transaction>) {
        diffResult.submitList(transactions)
    }

    inner class TransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prettyTime = PrettyTime()
        val binding = TransactionItemBinding.bind(itemView)

        fun bind(transaction: Transaction) {
            binding.apply {
                productName.text = transaction.accountName
                price.text = transaction.amount.toNaira()
                date.text = prettyTime.format(transaction.timeStamp)
            }
        }
    }

}