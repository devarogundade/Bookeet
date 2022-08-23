package team.pacify.bookeet.ui.profile

import android.view.View
import team.pacify.bookeet.databinding.UnsetProfileBinding

class UnsetProfile(
    private val binding: UnsetProfileBinding,
    private val onProfileSettings: () -> Unit
) {

    init {
        binding.profileSettings.setOnClickListener {
            onProfileSettings()
        }
    }

    fun show() {
        binding.root.visibility = View.VISIBLE
    }

    fun hide() {
        binding.root.visibility = View.GONE
    }

}