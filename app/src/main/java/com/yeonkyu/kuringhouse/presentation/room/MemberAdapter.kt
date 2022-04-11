package com.yeonkyu.kuringhouse.presentation.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ItemMemberBinding
import com.yeonkyu.kuringhouse.domain.model.Member

class MemberAdapter : ListAdapter<Member, MemberAdapter.MemberViewHolder>(
    MemberDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        val binding = ItemMemberBinding.bind(view)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MemberViewHolder constructor(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member) {
            binding.member = member
        }
    }

    object MemberDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.nickname == oldItem.nickname
        }
    }
}