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
import com.wildantechnoart.frontendcodingtes.databinding.FragmentAddBinding
import com.wildantechnoart.frontendcodingtes.model.AddChecklistBody
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.ChecklistViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding
    private var mItemName: String? = null
    private val viewModel: ChecklistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        this?.btnAddItem?.let { bindProgressButton(it) }
        this?.btnAddItem?.attachTextChangeAnimator()

        this?.btnAddItem?.setOnClickListener {
            mItemName = this.inputItem.text.toString()

            val itemEmpty = Constant.isTextEmpty(mItemName)

            if (!itemEmpty) {
                    val body = AddChecklistBody()
                    body.name = mItemName
                    viewModel.addChecklist(requireContext(), body)
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
            successAddData.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }

            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }

            loading.observe(viewLifecycleOwner) {
                if (it) {
                    this@with?.btnAddItem?.isClickable = false
                    this@with?.btnAddItem?.showProgress {
                        progressColor = Color.parseColor("#FFFFFF")
                    }
                } else {
                    this@with?.btnAddItem?.isClickable = true
                    this@with?.btnAddItem?.hideProgress(R.string.action_add_checklist)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}