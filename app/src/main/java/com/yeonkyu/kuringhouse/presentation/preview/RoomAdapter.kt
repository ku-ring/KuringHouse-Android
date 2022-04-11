package com.yeonkyu.kuringhouse.presentation.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.databinding.ItemRoomBinding
import com.yeonkyu.kuringhouse.domain.model.Room

class RoomAdapter constructor(
    private val itemClick: (Room) -> Unit
) : ListAdapter<Room, RoomAdapter.RoomViewHolder>(
    RoomDiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        val binding = ItemRoomBinding.bind(view)
        return RoomViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class RoomViewHolder constructor(
        private val binding: ItemRoomBinding,
        private val itemClick: (Room) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(adapterPosition)?.let {
                    itemClick(it)
                }
            }
        }

        fun bind(room: Room) {
            binding.roomInfo = room
        }
    }

    object RoomDiffCallback : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.title == newItem.title
                    && oldItem.participants == newItem.participants
        }
    }
}