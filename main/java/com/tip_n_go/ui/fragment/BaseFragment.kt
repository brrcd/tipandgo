package com.tip_n_go.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.gone
import com.tip_n_go.tools.visible
import com.tip_n_go.ui.activity.MainActivity
import com.tip_n_go.ui.listener.TopBarChangerInterface

// base class for fragment: handles binding, main activity top bar logic
abstract class BaseFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!
    protected val mainActivity get() = requireActivity() as MainActivity
    // todo subscribe to baseViewModel error live data

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateLayout(inflater, container)
        if (this is TopBarChangerInterface) {
            setupTopBar()
            setupTopBarTitle()
        }
        if (requireActivity() is MainActivity) {
            if (this is ProfileFragment ||
                this is DashboardFragment ||
                this is TipsFragment ||
                this is ReviewsFragment
            ) {
                mainActivity.binding.navView.visible()
            } else {
                mainActivity.binding.navView.gone()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this is TopBarChangerInterface) {
            this.restoreDefaultTopBar()
        }
    }

    protected fun navigateTo(destinationId: Int) {
        this.findNavController().navigate(destinationId)
    }

    protected fun navigateTo(destinationId: Int, bundle: Bundle) {
        this.findNavController().navigate(destinationId, bundle)
    }

    private fun navigateUp() {
        this.findNavController().navigateUp()
    }

    private fun setupTopBar() {
        val activityBinding = mainActivity.binding
        activityBinding.topBarTitle.apply {
            visible()
        }
        activityBinding.arrowBack.apply {
            setOnClickListener {
                navigateUp()
            }
            visible()
        }
        activityBinding.viewPager.gone()
        activityBinding.tabLayout.gone()
    }

    protected abstract fun renderData(uiState: UiState<Any>)
    protected abstract fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): T
}
