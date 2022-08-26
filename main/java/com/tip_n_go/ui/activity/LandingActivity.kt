package com.tip_n_go.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tip_n_go.R
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get
import com.tip_n_go.tools.SharedTools.set
import com.tip_n_go.tools.TOKEN
import com.tip_n_go.viewmodel.LandingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// "splash screen" activity that checks for token inspiration
class LandingActivity : AppCompatActivity() {

    private val viewModel: LandingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_landing)

        val sharedPrefs = SharedTools.sharedPrefs()
        val token: Token? = sharedPrefs[TOKEN, Token()]
        if (token != null) {
            viewModel.refreshToken(token)
        } else {
            openLoginActivity()
        }

        viewModel.refreshJwtTokenLiveData.observe(this) {
            // on token successful token proceed to application
            if (it is UiState.Success<Token>) {
                sharedPrefs[TOKEN] = it.data
                val intent = Intent(this@LandingActivity, MainActivity::class.java)
                startActivity(intent)
                this@LandingActivity.finish()
            }
        }
        viewModel.errorLiveData.observe(this) {
            if (it is UiState.Error) {
                openLoginActivity()
            }
        }
    }

    private fun openLoginActivity() {
        val intent = Intent(this@LandingActivity, LoginActivity::class.java)
        startActivity(intent)
        this@LandingActivity.finish()
    }
}
