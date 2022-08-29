package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.ocpsoft.prettytime.PrettyTime
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Request
import team.pacify.bookeet.databinding.RequestItemBinding
import team.pacify.bookeet.utils.Extensions.toNaira

class RequestAdapter(
    private val remind: (Request) -> Unit
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

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

    private val diffUtil = object : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(
            oldItem: Request,
            newItem: Request
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Request,
            newItem: Request
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setSales(requests: List<Request>) {
        diffResult.submitList(requests)
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prettyTime = PrettyTime()
        val binding = RequestItemBinding.bind(itemView)

        fun bind(request: Request) {
            binding.apply {
                icon.text = request.customerName
                customerName.text = request.customerName
                date.text = prettyTime.format(request.timeStamp)
                amount.text = request.amount.toNaira()
                remind.setOnClickListener { remind(request) }
            }
        }
    }

}