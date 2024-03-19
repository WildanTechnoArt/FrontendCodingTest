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
import com.wildantechnoart.frontendcodingtes.databinding.FragmentAddItemBinding
import com.wildantechnoart.frontendcodingtes.model.ItemBody
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.ItemViewModel

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding
    private var mChecklistId: String? = null
    private var mItemId: String? = null
    private var mItemName: String? = null
    private var mIsEdit = false
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        val bundle = Bundle(arguments)
        mChecklistId = AddItemFragmentArgs.fromBundle(bundle).checklistId
        mItemId = AddItemFragmentArgs.fromBundle(bundle).itemId
        mIsEdit = AddItemFragmentArgs.fromBundle(bundle).isEdit

        if (mIsEdit) {
            mItemName = AddItemFragmentArgs.fromBundle(bundle).itemName
            this?.inputItem?.setText(mItemName.toString())
            this?.btnAddItem?.text = getString(R.string.action_edit_item)
        }


        this?.btnAddItem?.let { bindProgressButton(it) }
        this?.btnAddItem?.attachTextChangeAnimator()

        this?.btnAddItem?.setOnClickListener {
            mItemName = this.inputItem.text.toString()

            val itemEmpty = Constant.isTextEmpty(mItemName)

            if (!itemEmpty) {
                val body = ItemBody()
                body.itemName = mItemName
                if (mIsEdit) {
                    viewModel.renameItems(requireContext(), mChecklistId, mItemId, body)
                } else {
                    viewModel.postItems(requireContext(), mChecklistId, body)
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
            successAddData.observe(viewLifecycleOwner) {
                backToMain(it.message)
            }
            successUpdate.observe(viewLifecycleOwner) {
                backToMain(it.message)
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
                    this@with?.btnAddItem?.hideProgress(
                        if (mIsEdit)
                            R.string.action_edit_item else R.string.action_add_item
                    )
                }
            }
        }
    }

    private fun backToMain(message: String?) {
        Toast.makeText(requireContext(), message ?: "-", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}