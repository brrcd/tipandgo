package com.tip_n_go.repository.api

import com.google.gson.Gson
import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.*
import com.tip_n_go.state.ResponseState
import com.tip_n_go.tools.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MainService : ApiService() {

    private val hmacApi = ApiTools.getRetrofit(true)
    private val jwtApi = ApiTools.getRetrofit(false)

    // Auth
    suspend fun getJwtToken(
        tokenRequest: TokenRequest
    ): ResponseState<Token> {
        return apiCall { hmacApi.getJwtToken(ACCOUNT_GET_JWT_TOKEN, tokenRequest) }
    }

    suspend fun checkPhone(checkPhone: CheckPhone): ResponseState<Unit> {
        return apiCall { hmacApi.checkPhone(ACCOUNT_CHECK_PHONE, checkPhone) }
    }

    suspend fun refreshJwtToken(refreshToken: RefreshToken): ResponseState<Token> {
        return apiCall { hmacApi.refreshJwtToken(ACCOUNT_REFRESH_JWT_TOKEN, refreshToken) }
    }

    suspend fun recoverPasswordByEmail(recovery: EmailPasswordRecovery): ResponseState<Unit> {
        return apiCall { hmacApi.recoverPasswordByEmail(ACCOUNT_RECOVER_PASSWORD_BY_EMAIL, recovery) }
    }

    // Registration
    suspend fun checkPhoneRegistration(checkPhone: CheckPhone): ResponseState<Unit> {
        return apiCall { hmacApi.checkPhoneRegistration(REGISTRATION_CHECK_PHONE, checkPhone) }
    }

    suspend fun checkEmailRegistration(checkEmail: CheckEmail): ResponseState<Unit> {
        return apiCall { hmacApi.checkEmailRegistration(REGISTRATION_CHECK_EMAIL, checkEmail) }
    }

    suspend fun registerUser(user: RegisterUser): ResponseState<Token> {
        return apiCall { hmacApi.registerUser(REGISTRATION_REGISTER_USER, user) }
    }

    suspend fun registerUserToOrganization(user: RegisterOrganizationUser): ResponseState<Unit> {
        return apiCall {
            jwtApi.registerUserToOrganization(
                REGISTRATION_REGISTER_USER_TO_ORGANIZATION, user
            )
        }
    }

    // Users
    suspend fun getUser(): ResponseState<User> {
        return apiCall { jwtApi.getUser(USERS) }
    }

    suspend fun updateUser(user: UpdateUser): ResponseState<User> {
        return apiCall { jwtApi.updateUser(USERS_UPDATE_USER, user) }
    }

    suspend fun getUserInOrganization(
        request: UserInOrganizationRequest
    ): ResponseState<User> {
        val url = "$USERS/${request.organizationUserHash}/${request.organizationHash}"
        return apiCall { jwtApi.getUserInOrganization(url) }
    }

    suspend fun uploadAvatar(avatar: File): ResponseState<UrlResponse> {
        val logoRequestBody = avatar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val logoPart = MultipartBody.Part.createFormData("photo", TEMP_FILE_NAME, logoRequestBody)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(logoPart)
            .build()
        return apiCall { jwtApi.uploadAvatar(USERS_UPLOAD_AVATAR, multipartBody) }
    }

    // Reports
    suspend fun getReportsData(request: ReportDataRequest): ResponseState<List<ReportData>> {
        return apiCall { jwtApi.getReportsData(REPORTS, request) }
    }

    // Tips
    suspend fun getUserTips(period: PeriodType): ResponseState<List<Tip>> {
        return apiCall { jwtApi.getUserTips(TIPS, period) }
    }

    suspend fun getOrganizationUserTips(
        period: PeriodType,
        userHash: String,
        organizationHash: String
    ): ResponseState<List<Tip>> {
        val url = "$TIPS/$userHash/$organizationHash"
        return apiCall { jwtApi.getOrganizationUserTips(url, period) }
    }

    suspend fun getOrganizationTips(
        period: PeriodType,
        organizationHash: String
    ): ResponseState<List<Tip>> {
        val url = "$TIPS/$organizationHash"
        return apiCall { jwtApi.getOrganizationTips(url, period) }
    }

    // Reviews
    suspend fun getUserReviews(period: PeriodType): ResponseState<List<Review>> {
        return apiCall { jwtApi.getUserReviews(REVIEWS, period) }
    }

    suspend fun getOrganizationReviews(
        period: PeriodType,
        organizationHash: String
    ): ResponseState<List<Review>> {
        val url = "$REVIEWS/$organizationHash"
        return apiCall { jwtApi.getOrganizationReviews(url, period) }
    }

    suspend fun getOrganizationUserReviews(
        period: PeriodType,
        userHash: String,
        organizationHash: String
    ): ResponseState<List<Review>> {
        val url = "$REVIEWS/$userHash/$organizationHash"
        return apiCall { jwtApi.getOrganizationUserReviews(url, period) }
    }

    // Finance
    suspend fun getUserBalance(): ResponseState<Balance> {
        return apiCall { jwtApi.getUserBalance(FINANCE_BALANCE) }
    }

    suspend fun getUserBankCards(): ResponseState<List<BankCard>> {
        return apiCall { jwtApi.getBankCards(FINANCE_BANK_CARDS) }
    }

    suspend fun payoutUser(payout: PayoutUser): ResponseState<Balance> {
        return apiCall { jwtApi.payoutUser(FINANCE_PAYOUT, payout) }
    }

    suspend fun getUserPayoutHistory(): ResponseState<List<Payout>> {
        return apiCall { jwtApi.getUserPayoutHistory(FINANCE_PAYOUTS) }
    }

    suspend fun getOrganizationDistributedTips(organizationHash: String): ResponseState<List<DistributedTip>> {
        val url = "$FINANCE_TIP_DISTRIBUTIONS/$organizationHash"
        return apiCall { jwtApi.getOrganizationDistributedTips(url) }
    }

    suspend fun getOrganizationBalance(organizationHash: String): ResponseState<Balance> {
        val url = "$FINANCE_ORGANIZATION_BALANCE/$organizationHash"
        return apiCall { jwtApi.getOrganizationBalance(url) }
    }

    suspend fun getOrganizationUndistributedTips(organizationHash: String): ResponseState<List<Tip>> {
        val url = "$FINANCE_ORGANIZATION_UNDISTRIBUTED_TIPS/$organizationHash"
        return apiCall { jwtApi.getOrganizationUndistributedTips(url) }
    }

    suspend fun distributeTips(
        tipsToDistribute: DistributeTipsRequest
    ): ResponseState<Unit> {
        val json = tipsToDistribute.toJson()
        val requestBody = json.toGenericRequestBody()
        return apiCall { jwtApi.distributeTips(FINANCE_DISTRIBUTE_TIPS, requestBody) }
    }

    // Fcm Token
    suspend fun registerFcmToken(registerFcmToken: RegisterFcmToken): ResponseState<Unit> {
        return apiCall { jwtApi.registerFcmToken(MOBILE_DEVICES, registerFcmToken) }
    }

    suspend fun removeFcmToken(removeFcmToken: RemoveFcmToken): ResponseState<Unit> {
        return apiCall { hmacApi.removeFcmToken(MOBILE_DEVICES, removeFcmToken) }
    }

    // Notifications
    suspend fun getUserNotifications(): ResponseState<List<Notification>> {
        return apiCall { jwtApi.getUserNotifications(NOTIFICATIONS) }
    }

    suspend fun markNotificationAsRead(notificationId: Int): ResponseState<Unit> {
        val url = "$NOTIFICATIONS/$notificationId/MarkAsRead"
        return apiCall { jwtApi.markNotificationAsRead(url) }
    }

    // Organizations
    suspend fun getUserOrganizations(): ResponseState<List<Organization>> {
        return apiCall { jwtApi.getUserOrganizations(ORGANIZATIONS) }
    }

    suspend fun createNewOrganization(
        organization: CreateOrganization
    ): ResponseState<Organization> {
        val json = organization.toJson()
        val requestBody = json.toGenericRequestBody()
        return apiCall { jwtApi.createNewOrganization(ORGANIZATIONS, requestBody) }
    }

    suspend fun updateOrganization(
        organization: UpdateOrganization
    ): ResponseState<Organization> {
        return apiCall { jwtApi.updateOrganization(ORGANIZATIONS, organization) }
    }

    suspend fun getAvailableRoles(organizationHash: String): ResponseState<List<Role>> {
        val url = "$ORGANIZATIONS_AVAILABLE_ROLES/$organizationHash"
        return apiCall { jwtApi.getAvailableRoles(url) }
    }

    suspend fun getOrganizationByHash(organizationHash: String): ResponseState<Organization> {
        val url = "$ORGANIZATIONS/$organizationHash"
        return apiCall { jwtApi.getOrganizationByHash(url) }
    }

    suspend fun getOrganizationSettings(organizationHash: String): ResponseState<List<Settings>> {
        val url = "$ORGANIZATION_SETTINGS/$organizationHash"
        return apiCall { jwtApi.getOrganizationSettings(url) }
    }

    suspend fun updateOrganizationSettings(
        organizationHash: String,
        settings: UpdateOrganizationSettings
    ): ResponseState<List<Settings>> {
        val url = "$ORGANIZATION_SETTINGS/$organizationHash"
        return apiCall { jwtApi.updateOrganizationSettings(url, settings) }
    }

    // OrganizationUsers
    suspend fun getOrganizationUsersByHash(organizationHash: String): ResponseState<List<OrganizationUser>> {
        val url = "$ORGANIZATION_USERS_BY_HASH/$organizationHash"
        return apiCall { jwtApi.getOrganizationUsersByHash(url) }
    }

    suspend fun updateOrganizationUser(
        user: UpdateOrganizationUser
    ): ResponseState<User> {
        return apiCall { jwtApi.updateOrganizationUser(ORGANIZATION_USERS, user) }
    }

    // Ecommpay
    suspend fun getEcommpayConfig(language: String): ResponseState<EcommpayConfig> {
        val url = "$ECOMMPAY/$language/0/true"
        return apiCall { jwtApi.getEcommpayConfig(url) }
    }

    // QR
    suspend fun getQrCode(
        qrCodeRequest: QrCodeRequest
    ): ResponseState<QrCodeData> {
        return apiCall { jwtApi.getQrCode(QR_GET_QR_CODE, qrCodeRequest) }
    }

    // Brands
    suspend fun getUserBrands(): ResponseState<List<Brand>> {
        return apiCall { jwtApi.getUserBrand(BRANDS) }
    }

    suspend fun getBrandByHash(brandHash: String): ResponseState<Brand> {
        val url = "$BRANDS/$brandHash"
        return apiCall { jwtApi.getBrandByHash(url) }
    }

    suspend fun updateBrand(brand: Brand): ResponseState<Brand> {
        return apiCall { jwtApi.updateBrand(BRANDS, brand) }
    }

    suspend fun createBrand(createBrand: CreateBrand): ResponseState<Brand> {
        return apiCall { jwtApi.createBrand(BRANDS, createBrand) }
    }

    suspend fun uploadBrandLogo(brandHash: String, logo: File): ResponseState<Brand> {
        val logoRequestBody = logo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val logoPart = MultipartBody.Part.createFormData("logo", TEMP_FILE_NAME, logoRequestBody)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("brandHash", brandHash)
            .addPart(logoPart)
            .build()
        return apiCall { jwtApi.uploadBrandLogo(BRANDS_UPLOAD_LOGO, multipartBody) }
    }

    // Settings
    suspend fun getCountries(): ResponseState<List<Country>> {
        return apiCall { hmacApi.getCountries(SETTINGS_GET_COUNTRIES) }
    }

    suspend fun getTipsDistributionMethods(): ResponseState<List<TipDistributionMethod>> {
        return apiCall { hmacApi.getTipsDistributionMethods(SETTINGS_GET_TIPS_DISTRIBUTION_METHODS) }
    }

    private fun String.toGenericRequestBody(): RequestBody {
        return this.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    private fun Any.toJson(): String = Gson().toJson(this)
}
