package com.example.bill_genrating_app.Api.Util
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class CurlInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val curlCmd = StringBuilder("curl")

        curlCmd.append(" -X ${request.method}")

        // Add Headers
        for (header in request.headers) {
            curlCmd.append(" -H \"${header.first}: ${header.second}\"")
        }

        // Add URL
        curlCmd.append(" \"${request.url}\"")

        // Add Body if exists
        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            curlCmd.append(" --data '$bodyString'")
        }

        Log.d("CURL_REQUEST", curlCmd.toString())

        return chain.proceed(request)
    }
}
