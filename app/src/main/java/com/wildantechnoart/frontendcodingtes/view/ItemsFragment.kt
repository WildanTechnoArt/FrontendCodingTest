package com.wildantechnoart.frontendcodingtes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wildantechnoart.frontendcodingtes.databinding.FragmentItemsBinding
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.ItemViewModel
import kotlin.properties.Delegates

class ItemsFragment : Fragment() {

    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<ItemsAdapter>()
    private val viewModel: ItemViewModel by viewModels()
    private var checklistId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
    }

    private fun initView() = with(binding) {
        val bundle = Bundle(arguments)
        checklistId = ItemsFragmentArgs.fromBundle(bundle).checklistId

        mAdapter = ItemsAdapter(checklistId, requireView())
        this?.rvItems?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        viewModel.getItems(requireContext(), checklistId)
        this?.swipeRefresh?.setOnRefreshListener {
            viewModel.getItems(requireContext(), checklistId)
        }

        mAdapter.onClick = {
            val builder = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Konfirmasi")
                setMessage("Kamu yakin ingin menghapusnya?")
                setPositiveButton("Iya") { dialog, _ ->
                    viewModel.deleteItem(requireContext(), checklistId, it.id.toString())
                    dialog.dismiss()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            builder.show()
        }

        mAdapter.onChangeStatus = {
            viewModel.updateItems(requireContext(), checklistId, it.id.toString())
        }

        this?.fabAddItems?.setOnClickListener {
            val action = ItemsFragmentDirections.actionItemsFragmentToAddItemFragment(
                checklistId = checklistId.toString(),
                itemId = "",
                isEdit = false,
                itemName = ""
            )
            findNavController().navigate(action)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            successDelete.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                viewModel.getItems(requireContext(), checklistId)
            }
            successUpdate.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                viewModel.getItems(requireContext(), checklistId)
            }
            getItemList.observe(viewLifecycleOwner) { data ->
                Constant.handleData(
                    null, false, data, mAdapter,
                    this@with?.rvItems, this@with?.textMessageNoData
                )
            }
            error.observe(viewLifecycleOwner) {
                Constant.handleErrorApi(requireActivity(), it)
            }
            loading.observe(viewLifecycleOwner) {
                this@with?.swipeRefresh?.isRefreshing = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}