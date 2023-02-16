package g.takeru.homework.cathaybank.api

import g.takeru.homework.cathaybank.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    var apiService: ApiService? = null

    fun getInstance(): ApiService {
        if (apiService == null) {

            // log interceptor
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            // okHttpClient Builder for http logger
            val clientBuilder = OkHttpClient.Builder()
            clientBuilder.interceptors().add(httpLoggingInterceptor)

            // init retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()
            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
}