package com.wildantechnoart.frontendcodingtes.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.FragmentRegisterBinding
import com.wildantechnoart.frontendcodingtes.model.RegisterBody
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private var mEmail: String? = null
    private var mUsername: String? = null
    private var mPassword: String? = null
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        this?.btnRegister?.let { bindProgressButton(it) }
        this?.btnRegister?.attachTextChangeAnimator()

        this?.btnRegister?.setOnClickListener {
            mEmail = this.inputEmail.text.toString()
            mUsername = this.inputUsername.text.toString()
            mPassword = this.inputPassword.text.toString()

            val isValidEmail = Constant.isValidEmail(mEmail)
            val usernameEmpty = Constant.isTextEmpty(mUsername)
            val passEmpty = Constant.isTextEmpty(mPassword)

            if (!isValidEmail) this.layoutEmail.error =
                getString(R.string.message_if_email_invalid) else this.layoutEmail.error = null

            if (!usernameEmpty && !passEmpty && isValidEmail) {
                if ((mPassword?.length ?: 0) < 8) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.message_if_wrong_pass),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val body = RegisterBody()
                    body.email = mEmail
                    body.password = mPassword
                    body.username = mUsername
                    viewModel.registerUser(requireContext(), body)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_if_data_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            registerSuccess.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                findNavController().navigate(action)
            }

            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    this@with?.btnRegister?.isClickable = false
                    this@with?.btnRegister?.showProgress {
                        progressColor = Color.parseColor("#FFFFFF")
                    }
                } else {
                    this@with?.btnRegister?.isClickable = true
                    this@with?.btnRegister?.hideProgress(R.string.action_register)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}