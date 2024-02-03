package com.handsome.lib.util.network

import android.app.Activity
import com.google.gson.annotations.SerializedName
import com.handsome.lib.api.server.service.IAccountService
import com.handsome.lib.api.server.service.ILoginService
import com.handsome.lib.util.service.impl
import java.io.Serializable

/**
 * 定义的数据类包装类
 */

interface IApiWrapper : Serializable {
    val status_code: Int
    val status_msg: String

    fun isExpired() : Boolean{
        return status_code == 500 && status_msg == "token is expired"
    }

    fun isSuccess(activity : Activity) : Boolean{
        return if (isExpired()){
            IAccountService::class.impl.tokenExpired()
            ILoginService::class.impl.startLoginActivity()
            activity.finishAfterTransition()
            false
        }else status_code == 0
    }
}

data class ApiStatus(
    @SerializedName("status_code")
    override val status_code: Int,
    @SerializedName("status_msg")
    override val status_msg: String
) : IApiWrapper