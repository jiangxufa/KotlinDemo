package com.jiangxufa.baselibrary.data.net

import okhttp3.*
import okio.Buffer
import java.io.IOException


/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
class ParamsInterceptor(val params: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val newRequestBuild: Request.Builder?
        val method = oldRequest.method()
        var postBodyString: String = ""
        if ("POST" == method) {
            val oldBody = oldRequest.body()
            if (oldBody is FormBody) {
                val formBodyBuilder = FormBody.Builder()
                params.forEach { k, v -> formBodyBuilder.add(k, v) }
                newRequestBuild = oldRequest.newBuilder()

                val formBody = formBodyBuilder.build()
                postBodyString = bodyToString(oldRequest.body())
                postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(formBody)
                newRequestBuild!!.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString))
            } else if (oldBody is MultipartBody) {
                val oldBodyMultipart = oldBody as MultipartBody?
                val oldPartList = oldBodyMultipart!!.parts()
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val requestBody1 = RequestBody.create(MediaType.parse("text/plain"), "")
                val requestBody2 = RequestBody.create(MediaType.parse("text/plain"), "")
                val requestBody3 = RequestBody.create(MediaType.parse("text/plain"), "")
                for (part in oldPartList) {
                    builder.addPart(part)
                    postBodyString += bodyToString(part.body()) + "\n"
                }
                postBodyString += bodyToString(requestBody1) + "\n"
                postBodyString += bodyToString(requestBody2) + "\n"
                postBodyString += bodyToString(requestBody3) + "\n"
                //builder.addPart(oldBody);
                //不能用这个方法，因为不知道oldBody的类型，可能是PartMap过来的，也可能是多个Part过来的，所以需要重新逐个加载进去
                builder.addPart(requestBody1)
                builder.addPart(requestBody2)
                builder.addPart(requestBody3)
                newRequestBuild = oldRequest.newBuilder()
                newRequestBuild!!.post(builder.build())
            } else {
                newRequestBuild = oldRequest.newBuilder()
            }
        }else {
            // 添加新的参数
            val commonParamsUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
            params.forEach { k, v -> commonParamsUrlBuilder.addQueryParameter(k, v) }

            newRequestBuild = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(commonParamsUrlBuilder.build())
        }

        val newRequest = newRequestBuild!!
                .addHeader("Accept-Language", "zh")
                .build()

        return chain.proceed(newRequest)
    }

    companion object {

        private fun bodyToString(request: RequestBody?): String {
            try {
                val buffer = Buffer()
                if (request != null)
                    request.writeTo(buffer)
                else
                    return ""
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "did not work"
            }

        }
    }

}