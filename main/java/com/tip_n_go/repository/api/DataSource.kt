package com.tip_n_go.repository.api

import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.*
import java.io.File

interface DataSource {

    // Account
    suspend fun getJwtToken(tokenRequest: TokenRequest): Token
    suspend fun checkPhone(checkPhone: CheckPhone)
    suspend fun refreshJwtToken(refreshToken: RefreshToken): Token
    suspend fun recoverPasswordByEmail(email: EmailPasswordRecovery)

    // Registration
    suspend fun checkPhoneRegistration(checkPhone: CheckPhone)
    suspend fun checkEmailRegistration(checkEmail: CheckEmail)
    suspend fun registerUser(user: RegisterUser): Token
    suspend fun registerUserToOrganization(user: RegisterOrganizationUser)

    // User
    suspend fun getUser(): User
    suspend fun updateUser(user: UpdateUser): User
    suspend fun getUserInOrganization(user: UserInOrganizationRequest): User
    suspend fun uploadAvatar(avatar: File): UrlResponse

    // Reports
    suspend fun getReportsData(request: ReportDataRequest): List<ReportData>

    // Tips
    suspend fun getUserTips(period: PeriodType): List<Tip>
    suspend fun getOrganizationUserTips(period: PeriodType, userHash: String, organizationHash: String): List<Tip>
    suspend fun getOrganizationTips(period: PeriodType, organizationHash: String): List<Tip>

    // Reviews
    suspend fun getUserReviews(period: PeriodType): List<Review>
    suspend fun getOrganizationReviews(period: PeriodType, organizationHash: String): List<Review>
    suspend fun getOrganizationUserReviews(period: PeriodType, userHash: String, organizationHash: String): List<Review>

    // Finance
    suspend fun getUserBalance(): Balance
    suspend fun getUserBankCards(): List<BankCard>
    suspend fun payoutUser(payout: PayoutUser): Balance
    suspend fun getUserPayoutHistory(): List<Payout>
    suspend fun getOrganizationDistributedTips(organizationHash: String): List<DistributedTip>
    suspend fun getOrganizationBalance(organizationHash: String): Balance
    suspend fun getOrganizationUndistributedTips(organizationHash: String): List<Tip>
    suspend fun distributeTips(tipsToDistribute: DistributeTipsRequest)

    // Fcm Token
    suspend fun registerFcmToken(registerFcmToken: RegisterFcmToken)
    suspend fun removeFcmToken(removeFcmToken: RemoveFcmToken)

    // Notifications
    suspend fun getUserNotifications(): List<Notification>
    suspend fun markNotificationAsRead(notificationId: Int)

    // Organizations
    suspend fun getUserOrganizations(): List<Organization>
    suspend fun createNewOrganization(organization: CreateOrganization): Organization
    suspend fun updateOrganization(organization: UpdateOrganization): Organization
    suspend fun getAvailableRoles(organizationHash: String): List<Role>
    suspend fun getOrganizationByHash(organizationHash: String): Organization
    suspend fun getOrganizationSettings(organizationHash: String): List<Settings>
    suspend fun updateOrganizationSettings(organizationHash: String, settings: UpdateOrganizationSettings): List<Settings>

    // OrganizationUsers
    suspend fun getOrganizationUsersByHash(organizationHash: String): List<OrganizationUser>
    suspend fun updateOrganizationUser(user: UpdateOrganizationUser): User

    // Ecommpay
    suspend fun getEcommpayConfig(language: String): EcommpayConfig

    // QR
    suspend fun getQrCode(qrCodeRequest: QrCodeRequest): QrCodeData

    // Brands
    suspend fun getUserBrands(): List<Brand>
    suspend fun getBrandByHash(brandHash: String): Brand
    suspend fun updateBrand(brand: Brand): Brand
    suspend fun createBrand(createBrand: CreateBrand): Brand
    suspend fun uploadBrandLogo(brandHash: String, logo: File): Brand

    // Settings
    suspend fun getCountries(): List<Country>
    suspend fun getTipsDistributionMethods(): List<TipDistributionMethod>
}
