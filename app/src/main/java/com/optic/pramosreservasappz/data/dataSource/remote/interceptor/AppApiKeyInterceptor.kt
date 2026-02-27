package com.optic.pramosreservasappz.data.dataSource.remote.interceptor

// INTERCEPTOR QUE VERIFICA QUE EL HEADER TENGA EL API KEY A NIVEL APLICACION KOLTIN
import com.optic.pramosreservasappz.core.Config
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppApiKeyInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val request = originalRequest.newBuilder()
            .addHeader("X-APP-KEY", Config.APP_API_KEY)
            .build()

        return chain.proceed(request)
    }
}