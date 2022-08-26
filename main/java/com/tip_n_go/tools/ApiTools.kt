package com.tip_n_go.tools

import android.util.Base64
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.tip_n_go.BuildConfig
import com.tip_n_go.data.incoming.Token
import com.tip_n_go.repository.api.Api
import com.tip_n_go.tools.SharedTools.get
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object ApiTools {

    private const val URL = BuildConfig.API_URL
    private const val CLIENT_ID = BuildConfig.CLIENT_ID
    private const val API_KEY = BuildConfig.API_KEY
    private const val HMAC_SHA256 = "HmacSHA256"
    private var requestContentBase64String = ""
    private val sharedPrefs by lazy { SharedTools.sharedPrefs() }

    // headers
    private const val AUTHORIZATION = "Authorization"
    private const val AUTHENTICATION_TYPE = "AuthenticationType"

    private fun getKey(methodName: String, url: String): String {
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val encodedURL = URLEncoder.encode(url, "UTF-8").lowercase()
        val requestTimeStamp = (System.currentTimeMillis() / 1000).toString()
        val signatureRawData = CLIENT_ID +
            methodName +
            encodedURL +
            uuid +
            requestTimeStamp +
            requestContentBase64String
        val requestSignatureBase64String = encodeSignatureWithKey(signatureRawData)
        return "amx $CLIENT_ID:$requestSignatureBase64String:$uuid:$requestTimeStamp"
    }

    private fun encodeSignatureWithKey(signature: String): String {
        val sha256: Mac = Mac.getInstance(HMAC_SHA256)
        val secretKey = SecretKeySpec(API_KEY.toByteArray(), HMAC_SHA256)
        sha256.init(secretKey)
        return Base64.encodeToString(sha256.doFinal(signature.toByteArray()), Base64.NO_WRAP)
    }

    private fun provideOkHttpClient(isHmac: Boolean) =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request
                if (isHmac) {
                    val bodyAsString = bodyToString(chain.request().body)
                    requestContentBase64String = ""
                    if (bodyAsString != "") {
                        requestContentBase64String = encodeSignatureWithKey(bodyAsString)
                    }
                    val hmacKey = getKey(
                        chain.call().request().method,
                        chain.call().request().url.toString()
                    )
                    val origin = chain.request()
                    val requestBuilder = origin.newBuilder()
                        .header(AUTHORIZATION, hmacKey)
                        .header(AUTHENTICATION_TYPE, "amx")
                    request = requestBuilder.build()
                } else {
                    val token = sharedPrefs[TOKEN, Token()]
                    val bearer = "Bearer ${token?.accessToken}"
                    val origin = chain.request()
                    val requestBuilder = origin.newBuilder()
                        .header(AUTHORIZATION, bearer)
                    request = requestBuilder.build()
                }
                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(
                OkHttpProfilerInterceptor()
            )
            .authenticator(AuthHelper())
            .build()

    private fun bodyToString(request: RequestBody?): String {
        if (request == null) return ""
        val buffer = Buffer()
        request.writeTo(buffer)
        return buffer.readUtf8()
    }

    fun getRetrofit(isHmac: Boolean = false): Api =
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(isHmac))
            .build()
            .create(Api::class.java)
}
