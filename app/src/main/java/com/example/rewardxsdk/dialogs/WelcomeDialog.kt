package com.example.rewardxsdk.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.rewardxsdk.databinding.DialogWelcomeBinding

class WelcomeDialog(
    private val onCreateUser: (username: String, email: String) -> Unit,
    private val onLoginUser: (email: String) -> Unit
) : DialogFragment() {

    private var _binding: DialogWelcomeBinding? = null
    private val binding get() = _binding!!
    private var isLoginMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
        setupClickListeners()
        updateUI()
    }

    private fun setupClickListeners() {
        binding.root.findViewById<TextView>(com.example.rewardxsdk.R.id.tvToggleMode)?.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
            clearErrors()
        }

        binding.btnCreate.setOnClickListener {
            if (isLoginMode) {
                handleLogin()
            } else {
                handleCreate()
            }
        }
    }

    private fun updateUI() {
        if (isLoginMode) {
            binding.tilUsername.visibility = View.GONE
            binding.tilEmail.hint = "Email"
            binding.btnCreate.text = "Login"
            binding.root.findViewById<TextView>(com.example.rewardxsdk.R.id.tvToggleMode)?.text = "Need to create account?"
        } else {
            binding.tilUsername.visibility = View.VISIBLE
            binding.tilEmail.hint = "Email"
            binding.btnCreate.text = "Create Account"
            binding.root.findViewById<TextView>(com.example.rewardxsdk.R.id.tvToggleMode)?.text = "Already have an account?"
        }
    }

    private fun clearErrors() {
        binding.tilUsername.error = null
        binding.tilEmail.error = null
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()

        when {
            email.isEmpty() -> {
                binding.tilEmail.error = "Email is required"
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Invalid email format"
            }
            else -> {
                onLoginUser(email)
                dismiss()
            }
        }
    }

    private fun handleCreate() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        when {
            username.isEmpty() -> {
                binding.tilUsername.error = "Username is required"
            }
            email.isEmpty() -> {
                binding.tilEmail.error = "Email is required"
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Invalid email format"
            }
            else -> {
                onCreateUser(username, email)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}