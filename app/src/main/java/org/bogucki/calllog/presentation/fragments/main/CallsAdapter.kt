package org.bogucki.calllog.presentation.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.bogucki.calllog.R
import org.bogucki.calllog.domain.models.CallLogEntry
import org.bogucki.calllog.extensions.secondstoHHmmssString

class CallsAdapter : ListAdapter<CallLogEntry, CallsAdapter.ViewHolder>(getDiffCallback()) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_call, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvName.text = item.name ?: item.number
        holder.tvDuration.text = item.duration.secondstoHHmmssString()
    }
}

private fun getDiffCallback() = object : DiffUtil.ItemCallback<CallLogEntry>() {
    override fun areItemsTheSame(oldItem: CallLogEntry, newItem: CallLogEntry) =
        oldItem.beginning == newItem.beginning

    override fun areContentsTheSame(oldItem: CallLogEntry, newItem: CallLogEntry) =
        oldItem == newItem

}