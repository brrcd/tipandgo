package com.tip_n_go.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.exception.ResponseError
import com.tip_n_go.data.incoming.Review
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.state.UiState
import com.tip_n_go.tools.CURRENT_ORGANIZATION_HASH
import com.tip_n_go.tools.SharedTools
import com.tip_n_go.tools.SharedTools.get

class ReviewsViewModel(private val repository: DataSource) : BaseViewModel() {

    private val _userReviewsLiveData = MutableLiveData<UiState<List<Review>>>()
    val userReviewLiveData: LiveData<UiState<List<Review>>> get() = _userReviewsLiveData
    private val _organizationReviewsLiveData = MutableLiveData<UiState<List<Review>>>()
    val organizationReviewsLiveData: LiveData<UiState<List<Review>>> get() = _organizationReviewsLiveData
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }
    override val errorLiveData: LiveData<UiState<ResponseError>> get() = _errorLiveData

    fun update() {
        val orgHash = sharedPrefs[CURRENT_ORGANIZATION_HASH] ?: ""
        getOrganizationReviews(organizationHash = orgHash)
    }

    private fun getUserReviews(period: PeriodType = PeriodType.DAY) {
        launchOnIo {
            val data = repository.getUserReviews(period)
            _userReviewsLiveData.postValue(UiState.Success(data))
        }
    }

    private fun getOrganizationReviews(
        period: PeriodType = PeriodType.DAY,
        organizationHash: String
    ) {
        launchOnIo {
            val data = repository.getOrganizationReviews(period, organizationHash)
            _organizationReviewsLiveData.postValue(UiState.Success(data))
        }
    }
}
