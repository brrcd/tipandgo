package com.tip_n_go.tools

import android.content.Intent
import com.tip_n_go.App
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.data.outgoing.RefreshToken
import com.tip_n_go.repository.api.MainService
import com.tip_n_go.state.ResponseState
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.tools.SharedTools.set
import com.tip_n_go.ui.activity.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// class that tries to refresh token when request returns 401 code
class AuthHelper : Authenticator, KoinComponent {

    private val service: MainService by inject()
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount >= 3) {
            return null
        }
        val token = sharedPrefs[TOKEN, Token()] ?: return null
        val refreshToken = token.refreshToken
        val result: ResponseState<Token>
        runBlocking(Dispatchers.Default) {
            result = service.refreshJwtToken(RefreshToken(refreshToken))
        }

        return if (result is ResponseState.Success) {
            val data = result.data
            sharedPrefs[TOKEN] = data
            response.request
                .newBuilder()
                .header("Authorization", data.accessToken)
                .build()
        } else {
            // if refresh token is expired return to login activity
            if (result is ResponseState.Error) {
                val intent = Intent(App.application, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                App.application.startActivity(intent)
            }
            return null
        }
    }
}
