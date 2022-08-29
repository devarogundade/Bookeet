package team.pacify.bookeet.ui.account

import android.view.View
import team.pacify.bookeet.databinding.EmptyAccountBinding

class EmptyAccount(
    private val binding: EmptyAccountBinding,
    private val onCreateAccount: () -> Unit
) {

    init {
        binding.createAccount.setOnClickListener {
            onCreateAccount()
        }
    }

    fun show() {
        binding.root.visibility = View.VISIBLE
    }

    fun hide() {
        binding.root.visibility = View.GONE
    }

}