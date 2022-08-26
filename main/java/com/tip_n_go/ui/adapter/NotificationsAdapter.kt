package com.tip_n_go.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.data.incoming.Notification
import com.tip_n_go.databinding.ItemNotificationBinding
import com.tip_n_go.ui.listener.NotificationListener
import com.tip_n_go.ui.viewholder.NotificationsViewHolder

class NotificationsAdapter : RecyclerView.Adapter<NotificationsViewHolder>() {

    private var list = listOf<Notification>()
    private var listener: NotificationListener? = null

    fun updateList(list: List<Notification>) {
        this.list = list
        this.notifyItemRangeInserted(0, list.size)
    }

    fun setListener(notificationListener: NotificationListener) {
        this.listener = notificationListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

    override fun getItemCount() = list.size
}
