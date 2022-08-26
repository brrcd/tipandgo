package com.tip_n_go.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tip_n_go.R
import com.tip_n_go.data.incoming.User
import com.tip_n_go.databinding.ActivityMainBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.visible
import com.tip_n_go.ui.adapter.TopBarAdapter
import com.tip_n_go.ui.listener.CustomTabSelectedListener
import com.tip_n_go.ui.listener.TopBarListener
import com.tip_n_go.ui.listener.TopBarSwipeListener
import com.tip_n_go.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

// applications main activity
class MainActivity : AppCompatActivity(), TopBarListener {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private var navHostFragment by Delegates.notNull<NavHostFragment>()

    // todo remove shared from vm to view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        viewModel.userLiveData.observe(this) { renderData(it) }
        viewModel.getUser()
    }

    private fun renderData(uiState: UiState<Any>) {
        when (uiState) {
            is UiState.Success -> {
                val data = uiState.data
                if (data is User) {
                    setupTopBar()
                    if (viewModel.organizationsTitles.size > 1) {
                        setupHeaderAdapter(viewModel.organizationsTitles)
                    }
                }
            }
            is UiState.Error -> {
            }
            is UiState.Loading -> {
            }
        }
    }

    private fun setupTopBar() = with(binding) {
        if (viewModel.organizationsTitles.isEmpty()) {
            topBarTitle.text = viewModel.userName
            topBarTitle.visible()
            viewPager.gone()
            tabLayout.gone()
        } else if (viewModel.organizationsTitles.size == 1) {
            val organizationName = viewModel.organizationsTitles.first()
            topBarTitle.text = organizationName
            topBarTitle.visible()
            viewPager.gone()
            tabLayout.gone()
        } else {
            topBarTitle.gone()
            viewPager.visible()
            tabLayout.visible()
        }
        arrowBack.gone()
        openNotifications.gone()
    }

    private fun setupNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        setupNavBar(navController)
        binding.openQrCode.setOnClickListener {
            navController.navigate(R.id.action_global_qrCodeFragment)
        }
    }

    private fun setupNavBar(navController: NavController) {
        binding.navView.setupWithNavController(navController)
        binding.navView.visibility = View.VISIBLE
    }

    private fun setupHeaderAdapter(listOfTitles: List<String>) = with(binding) {
        val adapter = TopBarAdapter(listOfTitles)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
        }.attach()
        tabLayout.addOnTabSelectedListener(object : CustomTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) viewModel.selectCurrentOrganizationHash(tab.position)

                val fragment = navHostFragment.childFragmentManager.primaryNavigationFragment
                if (fragment is TopBarSwipeListener) {
                    fragment.onTopBarSwipe()
                }
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onReturnFromFragment() {
        setupTopBar()
    }
}
