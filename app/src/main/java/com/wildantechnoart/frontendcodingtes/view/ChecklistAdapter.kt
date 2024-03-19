package com.wildantechnoart.frontendcodingtes.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.frontendcodingtes.databinding.ItemChecklistBinding
import com.wildantechnoart.frontendcodingtes.model.ItemData

class ChecklistAdapter : ListAdapter<ItemData, ChecklistAdapter.Holder>(MyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemChecklistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            textName.text = data.name ?: "-"
            textItem.text = data.items ?: "Nothing Message"
            textCheckStatus.text = if(data.checklistCompletionStatus == true) "Finish" else "Not finished yet"
            btnDelete.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }

    var onClick: ((ItemData) -> Unit)? = null

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<ItemData>() {
        override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemChecklistBinding) : RecyclerView.ViewHolder(binding.root)
}