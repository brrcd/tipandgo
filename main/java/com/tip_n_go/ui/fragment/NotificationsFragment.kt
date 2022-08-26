package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Notification
import com.tip_n_go.databinding.FragmentNotificationsBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.listCastCheck
import com.tip_n_go.ui.adapter.NotificationsAdapter
import com.tip_n_go.ui.listener.NotificationListener
import com.tip_n_go.ui.listener.TopBarChangerInterface
import com.tip_n_go.viewmodel.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment :
    BaseFragment<FragmentNotificationsBinding>(),
    TopBarChangerInterface,
    NotificationListener {

    private val viewModel: NotificationViewModel by viewModel()
    private var notifications = listOf<Notification>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notificationLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.markAsReadLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getUserNotifications()
    }

    override fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                data.listCastCheck<Notification> { setupNotificationsRecycler(it) }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupNotificationsRecycler(list: List<Notification>) {
        notifications = list
        val adapter = NotificationsAdapter()
        binding.notificationsRecycler.adapter = adapter
        adapter.updateList(list)
        adapter.setListener(this)
    }

    override fun onNotificationRead(position: Int) {
        viewModel.markNotificationAsRead(notifications[position].id)
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarTitle() {
        mainActivity.binding.topBarTitle.text = getString(R.string.notifications)
        mainActivity.binding.openNotifications.gone()
    }

    override fun restoreDefaultTopBar() {}
}
