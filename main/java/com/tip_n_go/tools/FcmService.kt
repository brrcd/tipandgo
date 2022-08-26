package com.tip_n_go.tools

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tip_n_go.tools.SharedTools.set

class FcmService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SharedTools.sharedPrefs()[FCM_TOKEN] = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        FcmTools.showNotification(message)
    }
}
