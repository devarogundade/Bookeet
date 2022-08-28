package team.pacify.bookeet.ui.messaging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.adapters.ChatsAdapter
import team.pacify.bookeet.data.models.messaging.Chat
import team.pacify.bookeet.databinding.FragmentChatBinding
import team.pacify.bookeet.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentChatBinding
    private lateinit var chatsAdapter: ChatsAdapter

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChats(firebaseAuth.currentUser?.uid ?: return)

        chatsAdapter = ChatsAdapter(firebaseAuth.currentUser?.uid ?: return) { chat ->
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Delete message")
                setMessage("Message cannot be reversed!")
                setNegativeButton("Cancel", null)
                setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteChat(chat)
                }
            }.show()
        }

        binding.apply {
            chats.apply {
                adapter = chatsAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            }
            send.setOnClickListener {
                val text = message.text.toString().trim()

                if (text.isEmpty()) {
                    Toast.makeText(requireContext(), "Type something", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                binding.message.setText("")

                viewModel.addChat(
                    Chat(
                        userId = firebaseAuth.currentUser?.uid ?: return@setOnClickListener,
                        text = text
                    )
                )
            }
        }

        viewModel.chats.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.root.isRefreshing = true
                }
                is Resource.Error -> {
                    binding.root.isRefreshing = false
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.root.isRefreshing = false

                    if (resource.data == null || resource.data.isEmpty()) {
                        binding.empty.visibility = View.VISIBLE
                    } else {
                        binding.empty.visibility = View.GONE
                    }

                    chatsAdapter.setChats(resource.data ?: emptyList())

                }
            }
        }

    }

}