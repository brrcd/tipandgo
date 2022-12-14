package com.tip_n_go.tools

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


@GlideModule
class GlideAppModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(10000, TimeUnit.SECONDS)
        builder.writeTimeout(10000, TimeUnit.SECONDS)
        builder.connectTimeout(10000, TimeUnit.SECONDS)
        registry.append(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(builder.build())
        )
    }
}
