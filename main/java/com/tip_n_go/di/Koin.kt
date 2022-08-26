package com.tip_n_go.di

import com.tip_n_go.repository.Repository
import com.tip_n_go.repository.api.Api
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.repository.api.MainService
import com.tip_n_go.tools.ApiTools
import com.tip_n_go.viewmodel.EditProfileViewModel
import com.tip_n_go.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// koin module, basically for viewModel injection
val appModule = module {
    single<Api> { ApiTools.getRetrofit() }
    single { MainService() }
    single<DataSource> { Repository(get()) }
    viewModel { LandingViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { TipsViewModel(get()) }
    viewModel { ReviewsViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { WithdrawHistoryViewModel(get()) }
    viewModel { OrganizationsViewModel(get()) }
    viewModel { EditOrganizationViewModel(get()) }
    viewModel { EditStaffViewModel(get()) }
    viewModel { StaffListViewModel(get()) }
    viewModel { StaffViewModel(get()) }
    viewModel { QrCodeViewModel(get()) }
    viewModel { DistributedTipsViewModel(get()) }
    viewModel { TipDistributionViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
}
