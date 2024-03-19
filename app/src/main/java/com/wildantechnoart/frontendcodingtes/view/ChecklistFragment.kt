package com.wildantechnoart.frontendcodingtes.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.FragmentChecklistBinding
import com.wildantechnoart.frontendcodingtes.utils.Constant
import com.wildantechnoart.frontendcodingtes.viewmodel.ChecklistViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ChecklistFragment : Fragment() {

    private var _binding: FragmentChecklistBinding? = null
    private val binding get() = _binding
    private var mAdapter by Delegates.notNull<ChecklistAdapter>()
    private val viewModel: ChecklistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLiveData()
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
                            setTitle("Konfirmasi")
                            setMessage("Kamu yakin ingin keluar?")
                            setPositiveButton("Iya") { dialog, _ ->
                                lifecycleScope.launch {
                                    MyApp.getInstance().clearDataStore(requireContext())
                                    requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
                                    requireActivity().finish()
                                }
                                dialog.dismiss()
                            }
                            setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()
                            }
                        }

                        builder.show()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    private fun initView() = with(binding) {
        mAdapter = ChecklistAdapter()
        this?.rvChecklist?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        viewModel.getChecklist(requireContext())
        this?.swipeRefresh?.setOnRefreshListener {
            viewModel.getChecklist(requireContext())
        }

        mAdapter.onClick = {
            val builder = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle("Konfirmasi")
                setMessage("Kamu yakin ingin menghapusnya?")
                setPositiveButton("Iya") { dialog, _ ->
                    viewModel.deleteChecklist(requireContext(), it.id.toString())
                    dialog.dismiss()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
            }

            builder.show()
        }

        this?.fabAddChecklist?.setOnClickListener {
            val action = ChecklistFragmentDirections.actionChecklistFragmentToAddChecklistFragment()
            findNavController().navigate(action)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            successDelete.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                viewModel.getChecklist(requireContext())
            }

            getChecklist.observe(viewLifecycleOwner) { data ->
                Constant.handleData(
                    null, false, data, mAdapter,
                    this@with?.rvChecklist, this@with?.textMessageNoData
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