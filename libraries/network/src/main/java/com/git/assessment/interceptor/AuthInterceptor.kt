package com.git.assessment.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor() : Interceptor {

    // For GitHub API, we can use a personal access token for authentication
    // In a real app, this would be stored securely and not hardcoded
    // For this assessment, we'll leave it as an empty string to use unauthenticated access
    // which has rate limits but is sufficient for demo purposes
    private val githubToken = ""
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val modifiedRequest = if (githubToken.isNotBlank()) {
            // Add Authorization header if token is provided
            originalRequest.newBuilder()
                .header("Authorization", "token $githubToken")
                .build()
        } else {
            // Use unauthenticated access
            originalRequest
        }

        return chain.proceed(modifiedRequest)
    }
}
