package team.pacify.bookeet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import team.pacify.bookeet.R
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.databinding.ReceiverBinding
import team.pacify.bookeet.databinding.SenderBinding
import team.pacify.bookeet.utils.UIConstants.RECEIVER_VIEW_TYPE
import team.pacify.bookeet.utils.UIConstants.SENDER_VIEW_TYPE

class ChatsAdapter(
    private val userId: String,
    private val onHold: (Chat) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENDER_VIEW_TYPE -> {
                SenderMessagesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.sender, parent, false)
                )
            }
            else -> {
                ReceiverMessagesViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.receiver, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = diffResult.currentList[position]
        return if (chat.userId == userId) SENDER_VIEW_TYPE
        else RECEIVER_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SenderMessagesViewHolder -> {
                holder.bind(diffResult.currentList[position])
            }
            is ReceiverMessagesViewHolder -> {
                holder.bind(diffResult.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = diffResult.currentList.size

    private val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.text == newItem.text
        }
    }

    fun setChats(chats: List<Chat>) {
        diffResult.submitList(chats)
    }

    private val diffResult = AsyncListDiffer(this, diffUtil)

    inner class SenderMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SenderBinding.bind(itemView)

        fun bind(chat: Chat) {
            binding.apply {
                text.text = chat.text

                if (chat.userId == userId) {
                    root.setOnLongClickListener {
                        onHold(chat)
                        false
                    }
                }
            }
        }
    }

    inner class ReceiverMessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ReceiverBinding.bind(itemView)

        fun bind(chat: Chat) {
            binding.apply {
                text.text = chat.text

                if (chat.userId == userId) {
                    root.setOnLongClickListener {
                        onHold(chat)
                        false
                    }
                }
            }
        }
    }


}