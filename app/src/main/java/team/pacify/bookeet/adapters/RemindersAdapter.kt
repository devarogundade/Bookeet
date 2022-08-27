package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.ocpsoft.prettytime.PrettyTime
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Sale
import team.pacify.bookeet.databinding.ReminderItemBinding

class RemindersAdapter(
    private val onReminderDelete: (Sale) -> Unit,
    private val onReminderEdit: (Sale) -> Unit,
) :
    RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        return RemindersViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.reminder_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
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

    inner class RemindersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prettyTime = PrettyTime()
        val binding = ReminderItemBinding.bind(itemView)

        fun bind(sale: Sale) {
            binding.apply {
                delete.setOnClickListener { onReminderDelete(sale) }
                edit.setOnClickListener { onReminderEdit(sale) }
            }
        }
    }

}