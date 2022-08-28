package team.pacify.bookeet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.finance.Invoice
import team.pacify.bookeet.databinding.InvoiceItemBinding
import team.pacify.bookeet.utils.Extensions.toNaira

class InvoiceAdapter : RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        return InvoiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.invoice_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        holder.bind(diffResult.currentList[position])
    }

    override fun getItemCount(): Int = diffResult.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<Invoice>() {
        override fun areItemsTheSame(
            oldItem: Invoice,
            newItem: Invoice
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Invoice,
            newItem: Invoice
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    fun setSales(invoices: List<Invoice>) {
        diffResult.submitList(invoices)
    }

    inner class InvoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = InvoiceItemBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(invoice: Invoice) {
            binding.apply {
                customerName.text = invoice.customerName
                invoiceId.text = "INV: ${invoice.invoiceId}"
                price.text = invoice.amountReceived.toNaira()
            }
        }
    }

}