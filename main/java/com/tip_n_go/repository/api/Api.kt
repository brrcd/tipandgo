package com.tip_n_go.repository.api

import com.tip_n_go.data.PeriodType
import com.tip_n_go.data.incoming.*
import com.tip_n_go.data.outgoing.*
import com.tip_n_go.tools.PERIOD
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    // Auth
    @POST
    suspend fun getJwtToken(@Url url: String, @Body body: TokenRequest): Response<Token>

    @POST
    suspend fun checkPhone(@Url url: String, @Body body: CheckPhone): Response<Unit>

    @POST
    suspend fun refreshJwtToken(@Url url: String, @Body body: RefreshToken): Response<Token>

    @POST
    suspend fun recoverPasswordByEmail(@Url url: String, @Body body: EmailPasswordRecovery): Response<Unit>

    // Registration
    @POST
    suspend fun checkPhoneRegistration(@Url url: String, @Body body: CheckPhone): Response<Unit>

    @POST
    suspend fun checkEmailRegistration(@Url url: String, @Body body: CheckEmail): Response<Unit>

    @POST
    suspend fun registerUser(@Url url: String, @Body body: RegisterUser): Response<Token>

    @POST
    suspend fun registerUserToOrganization(
        @Url url: String,
        @Body body: RegisterOrganizationUser
    ): Response<Unit>

    // Users
    @GET
    suspend fun getUser(@Url url: String): Response<User>

    @PUT
    suspend fun updateUser(@Url url: String, @Body body: UpdateUser): Response<User>

    @GET
    suspend fun getUserInOrganization(@Url url: String): Response<User>

    @POST
    suspend fun uploadAvatar(@Url url: String, @Body part: MultipartBody): Response<UrlResponse>

    // Reports
    @POST
    suspend fun getReportsData(@Url url: String, @Body body: ReportDataRequest): Response<List<ReportData>>

    // Tips
    @GET
    suspend fun getUserTips(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Tip>>

    @GET
    suspend fun getOrganizationUserTips(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Tip>>

    @GET
    suspend fun getOrganizationTips(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Tip>>

    // Reviews
    @GET
    suspend fun getUserReviews(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Review>>

    @GET
    suspend fun getOrganizationReviews(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Review>>

    @GET
    suspend fun getOrganizationUserReviews(
        @Url url: String,
        @Query(PERIOD) period: PeriodType
    ): Response<List<Review>>

    // Finance
    @GET
    suspend fun getUserBalance(@Url url: String): Response<Balance>

    @GET
    suspend fun getBankCards(@Url url: String): Response<List<BankCard>>

    @POST
    suspend fun payoutUser(@Url url: String, @Body payout: PayoutUser): Response<Balance>

    @GET
    suspend fun getUserPayoutHistory(@Url url: String): Response<List<Payout>>

    @GET
    suspend fun getOrganizationDistributedTips(@Url url: String): Response<List<DistributedTip>>

    @GET
    suspend fun getOrganizationBalance(@Url url: String): Response<Balance>

    @GET
    suspend fun getOrganizationUndistributedTips(@Url url: String): Response<List<Tip>>

    @POST
    suspend fun distributeTips(@Url url: String, @Body body: RequestBody): Response<Unit>

    // Fcm Token
    @POST
    suspend fun registerFcmToken(@Url url: String, @Body body: RegisterFcmToken): Response<Unit>

    @HTTP(method = "DELETE", hasBody = true)
    suspend fun removeFcmToken(@Url url: String, @Body body: RemoveFcmToken): Response<Unit>

    // Notifications
    @GET
    suspend fun getUserNotifications(@Url url: String): Response<List<Notification>>

    @PATCH
    suspend fun markNotificationAsRead(@Url url: String): Response<Unit>

    // Organizations
    @GET
    suspend fun getUserOrganizations(@Url url: String): Response<List<Organization>>

    @POST
    suspend fun createNewOrganization(@Url url: String, @Body body: RequestBody): Response<Organization>

    @PUT
    suspend fun updateOrganization(@Url url: String, @Body body: UpdateOrganization): Response<Organization>

    @GET
    suspend fun getAvailableRoles(@Url url: String): Response<List<Role>>

    @GET
    suspend fun getOrganizationByHash(@Url url: String): Response<Organization>

    @GET
    suspend fun getOrganizationSettings(@Url url: String): Response<List<Settings>>

    @PUT
    suspend fun updateOrganizationSettings(
        @Url url: String,
        @Body body: UpdateOrganizationSettings
    ): Response<List<Settings>>

    // OrganizationUsers
    @GET
    suspend fun getOrganizationUsersByHash(@Url url: String): Response<List<OrganizationUser>>

    @PATCH
    suspend fun updateOrganizationUser(@Url url: String, @Body body: UpdateOrganizationUser): Response<User>

    // Ecommpay
    @GET
    suspend fun getEcommpayConfig(@Url url: String): Response<EcommpayConfig>

    // QR
    @POST
    suspend fun getQrCode(@Url url: String, @Body body: QrCodeRequest): Response<QrCodeData>

    // Brands
    @GET
    suspend fun getUserBrand(@Url url: String): Response<List<Brand>>

    @GET
    suspend fun getBrandByHash(@Url url: String): Response<Brand>

    @PUT
    suspend fun updateBrand(@Url url: String, @Body body: Brand): Response<Brand>

    @POST
    suspend fun createBrand(@Url url: String, @Body body: CreateBrand): Response<Brand>

    @POST
    suspend fun uploadBrandLogo(@Url url: String, @Body part: MultipartBody): Response<Brand>

    // Settings
    @GET
    suspend fun getCountries(@Url url: String): Response<List<Country>>

    @GET
    suspend fun getTipsDistributionMethods(
        @Url url: String
    ): Response<List<TipDistributionMethod>>
}
