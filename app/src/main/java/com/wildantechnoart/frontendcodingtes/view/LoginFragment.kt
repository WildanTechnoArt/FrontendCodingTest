package com.wildantechnoart.frontendcodingtes.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.FragmentLoginBinding
import com.wildantechnoart.frontendcodingtes.model.LoginBody
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private var mUsername: String? = null
    private var mPassword: String? = null
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        this?.btnLogin?.let { bindProgressButton(it) }
        this?.btnLogin?.attachTextChangeAnimator()

        this?.btnLogin?.setOnClickListener {
            mUsername = this.inputUsername.text.toString()
            mPassword = this.inputPassword.text.toString()

            val usernameEmpty = Constant.isTextEmpty(mUsername)
            val passEmpty = Constant.isTextEmpty(mPassword)

            if (!usernameEmpty && !passEmpty) {
                if ((mPassword?.length ?: 0) < 8) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.message_if_wrong_pass),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val body = LoginBody()
                    body.username = mUsername
                    body.password = mPassword
                    viewModel.loginUser(requireContext(), body)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.message_if_data_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        this?.btnRegister?.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            loginSuccess.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }

            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    this@with?.btnLogin?.isClickable = false
                    this@with?.btnLogin?.showProgress {
                        progressColor = Color.parseColor("#FFFFFF")
                    }
                } else {
                    this@with?.btnLogin?.isClickable = true
                    this@with?.btnLogin?.hideProgress(R.string.action_login)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}