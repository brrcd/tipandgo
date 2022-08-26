package com.tip_n_go.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Notification
import com.tip_n_go.databinding.ItemNotificationBinding
import com.tip_n_go.ui.listener.NotificationListener

class NotificationsViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(notification: Notification, listener: NotificationListener?) {
        val date = notification.date
        val title = notification.text
        val isRead = notification.isRead

        binding.notificationDate.text = date
        binding.notificationTitle.text = title
        if (!isRead) {
            binding.root.setOnClickListener {
                listener?.onNotificationRead(adapterPosition)
                val colorStateList = binding.root.context.getColorStateList(R.color.white)
                binding.root.backgroundTintList = colorStateList
            }
        } else {
            val colorStateList = binding.root.context.getColorStateList(R.color.white)
            binding.root.backgroundTintList = colorStateList
        }
    }
}
