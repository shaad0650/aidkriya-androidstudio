package com.example.walker.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object ApiProvider {

    // THIS IS THE OBJECT YOU NEED TO ADD
    object TokenHolder {

        var accessToken: String? = null
    }

    private const val BASE_URL = "YOUR_BASE_URL_HERE" // <-- IMPORTANT: Replace with your actual API base URL

    // Create an interceptor to add the auth token to headers
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        // Add the token if it exists
        TokenHolder.accessToken?.let { token ->
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    // Create a logging interceptor for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create the OkHttpClient with both interceptors
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    // Create Moshi for JSON parsing
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Create the Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // Lazily create the AuthApi service
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    /**
    WalkApi Property to fix unresolved
    */
    val walkApi: WalkApi by lazy {
        retrofit.create(WalkApi::class.java)
    }
}
