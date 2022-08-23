package team.pacify.bookeet.ui.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import team.pacify.bookeet.R
import team.pacify.bookeet.databinding.FragmentPhoneBinding
import team.pacify.bookeet.utils.Extensions.isAValidPhoneNumber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneFragment : Fragment() {

    val TAG = "AuthFragment"

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: FragmentPhoneBinding
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var sendingCode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            sendCode.setOnClickListener {
                if (sendingCode) return@setOnClickListener

                var phoneNumber = this.phoneNumber.text.toString().trim()

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter your phone number", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                phoneNumber = "+234$phoneNumber"
                if (!phoneNumber.isAValidPhoneNumber()) {
                    Toast.makeText(
                        requireContext(),
                        "Enter a valid phone number",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                startAuthentication(phoneNumber)
            }
        }

    }

    private fun startAuthentication(phoneNumber: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Confirm your number")
            setMessage("Is $phoneNumber correct?")
                .setNegativeButton("Change number", null)
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    authenticate(phoneNumber)
                }
        }.show()
    }

    private fun authenticate(phoneNumber: String) {
        sendingCode = true
        binding.progressBar.visibility = View.VISIBLE

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token

            launchVerificationDialog()
        }
    }

    private fun launchVerificationDialog() {
        sendingCode = false
        binding.progressBar.visibility = View.GONE

        if (storedVerificationId == null) {
            Toast.makeText(requireContext(), "Code has not been sent", Toast.LENGTH_SHORT).show()
            return
        }

        VerificationCodeDialog(
            onEnterCode = { code ->
                val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, code)
                signInWithPhoneAuthCredential(credential)
            },
            resendCode = {

            }
        ).show(childFragmentManager, TAG)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        // its deprecated but its a hackathon :)
        val dialog = ProgressDialog(requireContext()).apply {
            setMessage("Verifying code")
            setCancelable(false)
        }

        dialog.show()

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                dialog.dismiss()
                findNavController().navigate(R.id.action_phoneFragment_to_mainFragment)
            } else {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(
                    requireContext(),
                    task.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}