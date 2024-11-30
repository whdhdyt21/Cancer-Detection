package com.dicoding.asclepius.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.entity.ClassificationEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding

class ListHistoryAdapter(
    private val onDeleteClick: (ClassificationEntity) -> Unit
) : ListAdapter<ClassificationEntity, ListHistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ClassificationEntity) {
            with(binding) {
                tvHistory.text = buildString {
        append(item.category)
        append(" - ")
        append(item.confidence)
    }
                ivHistory.setImageURI(Uri.parse(item.imageUri))
                btnDelete.setOnClickListener { onDeleteClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClassificationEntity>() {
            override fun areItemsTheSame(oldItem: ClassificationEntity, newItem: ClassificationEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ClassificationEntity, newItem: ClassificationEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
