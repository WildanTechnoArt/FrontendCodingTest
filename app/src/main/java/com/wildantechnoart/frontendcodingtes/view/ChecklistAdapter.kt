package com.wildantechnoart.frontendcodingtes.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.databinding.ItemChecklistBinding
import com.wildantechnoart.frontendcodingtes.model.ChecklistData

class ChecklistAdapter(private val view: View) :
    ListAdapter<ChecklistData, ChecklistAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            val isComplete = data.checklistCompletionStatus ?: false

            textName.text = data.name ?: "-"
            checkItem.isChecked = isComplete
            cardItem.setOnClickListener {
                val action = ChecklistFragmentDirections.actionChecklistFragmentToItemsFragment(
                    checklistId = data.id.toString()
                )
                Navigation.findNavController(view).navigate(action)
            }
            btnMenus.setOnClickListener {
                val popupMenu = PopupMenu(it.context, it)
                popupMenu.setOnMenuItemClickListener(object :
                    android.widget.PopupMenu.OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        when (p0?.itemId) {
                            R.id.menu_delete -> {
                                onClick?.invoke(data)
                            }
                        }
                        return true
                    }
                })

                popupMenu.inflate(R.menu.menu_checklist)
                popupMenu.show()
            }
        }
    }

    var onClick: ((ChecklistData) -> Unit)? = null

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<ChecklistData>() {
        override fun areItemsTheSame(oldItem: ChecklistData, newItem: ChecklistData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChecklistData, newItem: ChecklistData): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemChecklistBinding) : RecyclerView.ViewHolder(binding.root)
}