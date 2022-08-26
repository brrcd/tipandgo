package com.tip_n_go.repository

import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.*
import com.tip_n_go.repository.api.DataSource
import com.tip_n_go.repository.api.MainService
import com.tip_n_go.state.ResponseState
import java.io.File

class Repository(private val service: MainService) : DataSource {

    // Auth
    override suspend fun getJwtToken(
        tokenRequest: TokenRequest
    ): Token {
        return when (val result = service.getJwtToken(tokenRequest)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun checkPhone(checkPhone: CheckPhone) {
        return when (val result = service.checkPhone(checkPhone)) {
            is ResponseState.Success -> Unit
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun refreshJwtToken(refreshToken: RefreshToken): Token {
        return when (val result = service.refreshJwtToken(refreshToken)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun recoverPasswordByEmail(email: EmailPasswordRecovery) {
        return when (val result = service.recoverPasswordByEmail(email)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Registration
    override suspend fun checkPhoneRegistration(checkPhone: CheckPhone) {
        return when (val result = service.checkPhoneRegistration(checkPhone)) {
            is ResponseState.Success -> Unit
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun checkEmailRegistration(checkEmail: CheckEmail) {
        return when (val result = service.checkEmailRegistration(checkEmail)) {
            is ResponseState.Success -> Unit
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun registerUser(user: RegisterUser): Token {
        return when (val result = service.registerUser(user)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun registerUserToOrganization(
        user: RegisterOrganizationUser
    ) {
        return when (val result = service.registerUserToOrganization(user)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Users
    override suspend fun getUser(): User {
        return when (val result = service.getUser()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun updateUser(user: UpdateUser): User {
        return when (val result = service.updateUser(user)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getUserInOrganization(
        user: UserInOrganizationRequest
    ): User {
        return when (val result = service.getUserInOrganization(user)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun uploadAvatar(avatar: File): UrlResponse {
        return when (val result = service.uploadAvatar(avatar)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Reports
    override suspend fun getReportsData(request: ReportDataRequest): List<ReportData> {
        return when (val result = service.getReportsData(request)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Tips
    override suspend fun getUserTips(period: PeriodType): List<Tip> {
        return when (val result = service.getUserTips(period)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationUserTips(
        period: PeriodType,
        userHash: String,
        organizationHash: String
    ): List<Tip> {
        return when (
            val result =
                service.getOrganizationUserTips(period, userHash, organizationHash)
        ) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationTips(
        period: PeriodType,
        organizationHash: String
    ): List<Tip> {
        return when (val result = service.getOrganizationTips(period, organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Reviews
    override suspend fun getUserReviews(period: PeriodType): List<Review> {
        return when (val result = service.getUserReviews(period)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationReviews(
        period: PeriodType,
        organizationHash: String
    ): List<Review> {
        return when (val result = service.getOrganizationReviews(period, organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationUserReviews(
        period: PeriodType,
        userHash: String,
        organizationHash: String
    ): List<Review> {
        return when (
            val result =
                service.getOrganizationUserReviews(period, userHash, organizationHash)
        ) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Finance
    override suspend fun getUserBalance(): Balance {
        return when (val result = service.getUserBalance()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getUserBankCards(): List<BankCard> {
        return when (val result = service.getUserBankCards()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun payoutUser(payout: PayoutUser): Balance {
        return when (val result = service.payoutUser(payout)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getUserPayoutHistory(): List<Payout> {
        return when (val result = service.getUserPayoutHistory()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationDistributedTips(organizationHash: String): List<DistributedTip> {
        return when (val result = service.getOrganizationDistributedTips(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationBalance(organizationHash: String): Balance {
        return when (val result = service.getOrganizationBalance(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationUndistributedTips(organizationHash: String): List<Tip> {
        return when (val result = service.getOrganizationUndistributedTips(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun distributeTips(
        tipsToDistribute: DistributeTipsRequest
    ) {
        return when (val result = service.distributeTips(tipsToDistribute)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // FcmToken
    override suspend fun registerFcmToken(registerFcmToken: RegisterFcmToken) {
        return when (val result = service.registerFcmToken(registerFcmToken)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun removeFcmToken(removeFcmToken: RemoveFcmToken) {
        return when (val result = service.removeFcmToken(removeFcmToken)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Notifications
    override suspend fun getUserNotifications(): List<Notification> {
        return when (val result = service.getUserNotifications()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun markNotificationAsRead(notificationId: Int) {
        return when (val result = service.markNotificationAsRead(notificationId)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Organizations
    override suspend fun getUserOrganizations(): List<Organization> {
        return when (val result = service.getUserOrganizations()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun createNewOrganization(
        organization: CreateOrganization
    ): Organization {
        return when (val result = service.createNewOrganization(organization)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun updateOrganization(
        organization: UpdateOrganization
    ): Organization {
        return when (val result = service.updateOrganization(organization)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getAvailableRoles(organizationHash: String): List<Role> {
        return when (val result = service.getAvailableRoles(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationByHash(organizationHash: String): Organization {
        return when (val result = service.getOrganizationByHash(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getOrganizationSettings(organizationHash: String): List<Settings> {
        return when (val result = service.getOrganizationSettings(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun updateOrganizationSettings(
        organizationHash: String,
        settings: UpdateOrganizationSettings
    ): List<Settings> {
        return when (val result = service.updateOrganizationSettings(organizationHash, settings)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // OrganizationUsers
    override suspend fun getOrganizationUsersByHash(organizationHash: String): List<OrganizationUser> {
        return when (val result = service.getOrganizationUsersByHash(organizationHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun updateOrganizationUser(
        user: UpdateOrganizationUser
    ): User {
        return when (val result = service.updateOrganizationUser(user)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Ecommpay
    override suspend fun getEcommpayConfig(language: String): EcommpayConfig {
        return when (val result = service.getEcommpayConfig(language)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // QR
    override suspend fun getQrCode(
        qrCodeRequest: QrCodeRequest
    ): QrCodeData {
        return when (val result = service.getQrCode(qrCodeRequest)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Brands
    override suspend fun getUserBrands(): List<Brand> {
        return when (val result = service.getUserBrands()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getBrandByHash(brandHash: String): Brand {
        return when (val result = service.getBrandByHash(brandHash)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun updateBrand(brand: Brand): Brand {
        return when (val result = service.updateBrand(brand)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun createBrand(createBrand: CreateBrand): Brand {
        return when (val result = service.createBrand(createBrand)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun uploadBrandLogo(brandHash: String, logo: File): Brand {
        return when (val result = service.uploadBrandLogo(brandHash, logo)) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    // Settings
    override suspend fun getCountries(): List<Country> {
        return when (val result = service.getCountries()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }

    override suspend fun getTipsDistributionMethods(): List<TipDistributionMethod> {
        return when (val result = service.getTipsDistributionMethods()) {
            is ResponseState.Success -> result.data
            is ResponseState.Error -> throw result.exception
        }
    }
}
