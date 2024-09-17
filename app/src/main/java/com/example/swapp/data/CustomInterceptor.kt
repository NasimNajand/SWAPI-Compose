package com.example.swapp.data

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class CustomInterceptor: Interceptor {
    companion object {
        private const val TAG = "CustomInterceptor"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept: called")

        val req = chain.request().newBuilder().build()
        Log.d(TAG, "intercept: request URL: ${req.url}")

        val response = chain.proceed(req)

        Log.d(TAG, "intercept: response code: ${response.code}")
        Log.d(TAG, "intercept: response body: ${response.peekBody(Long.MAX_VALUE).string()}")

        return response
    }
}