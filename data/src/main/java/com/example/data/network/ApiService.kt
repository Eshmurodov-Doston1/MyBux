package com.example.data.network

import com.example.domain.constant.AppConstant.APIKEY
import com.example.domain.models.auth.resAuth.Login
import com.example.domain.models.getProductList.getProducts.Products
import com.example.domain.models.logOut.successLogOut.LogOut
import com.example.domain.models.settingsAuth.res.ResponseSettings
import com.example.domain.models.sig.Sig
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json", "Authorization:$APIKEY")
    @POST("api/method/users.get_sig")
    suspend fun getSig(): Response<Sig>

    @Headers("Content-Type: application/json", "Authorization:$APIKEY")
    @POST("api/method/auth.login")
    suspend fun authLogin(
        @Query("phone") phone: String?,
        @Query("password") password_text: String?,
        @Query("sig") sig: String?,
        @Query("remember") remember: String?,
        @Query("terminal_id") terminal_id: String?,
        @Query("device_id") device_id: String?
    ): Response<Login>


    @Headers("Content-Type: application/json", "Authorization:$APIKEY")
    @GET("api/method/product.list")
    suspend fun getProductList(@Header("Cookie") authHeader: String?): Response<Products>

    @Headers("Content-Type: application/json", "Authorization:$APIKEY")
    @GET("api/method/auth.settings_login")
    suspend fun settingsLogin(
        @Header("Cookie") authHeader: String?,
        @Query("sig") sig: String?,
        @Query("phone") phone: String?,
        @Query("password") password: String?
    ): Response<ResponseSettings>


    @Headers("Content-Type: application/json", "Authorization:$APIKEY")
    @POST("api/method/auth.logout")
    suspend fun logOut(@Header("Cookie") cookie:String?,@Query("sig") sig:String):Response<LogOut>

}