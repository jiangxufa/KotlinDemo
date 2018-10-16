//package com.jiangxufa.baselibrary.data.net
//
//import android.util.Log
//import okhttp3.*
//import okio.Buffer
//import java.io.IOException
//
///** 后续所有的网络相关公共参数以及缓存配置可以在该类实现
// * Created by hq on 2017/9/12.
// */
//
//class LogInterceptor : Interceptor {
//
//    @Throws(IOException::class)
//    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//        val oldRequest = chain.request()
//        var newRequestBuild: Request.Builder? = null
//        val method = oldRequest.method()
//        var postBodyString = ""
//        if ("POST" == method) {
//            val oldBody = oldRequest.body()
//            if (oldBody is FormBody) {
//                val formBodyBuilder = FormBody.Builder()
//                formBodyBuilder.add("deviceOs", "")
//                formBodyBuilder.add("appVersion", "")
//                formBodyBuilder.add("appName", "")
//                newRequestBuild = oldRequest.newBuilder()
//
//                val formBody = formBodyBuilder.build()
//                postBodyString = bodyToString(oldRequest.body())
//                postBodyString += (if (postBodyString.length > 0) "&" else "") + bodyToString(formBody)
//                newRequestBuild!!.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString))
//            } else if (oldBody is MultipartBody) {
//                val oldBodyMultipart = oldBody as MultipartBody?
//                val oldPartList = oldBodyMultipart!!.parts()
//                val builder = MultipartBody.Builder()
//                builder.setType(MultipartBody.FORM)
//                val requestBody1 = RequestBody.create(MediaType.parse("text/plain"), "")
//                val requestBody2 = RequestBody.create(MediaType.parse("text/plain"), "")
//                val requestBody3 = RequestBody.create(MediaType.parse("text/plain"), "")
//                for (part in oldPartList) {
//                    builder.addPart(part)
//                    postBodyString += bodyToString(part.body()) + "\n"
//                }
//                postBodyString += bodyToString(requestBody1) + "\n"
//                postBodyString += bodyToString(requestBody2) + "\n"
//                postBodyString += bodyToString(requestBody3) + "\n"
//                //              builder.addPart(oldBody);  //不能用这个方法，因为不知道oldBody的类型，可能是PartMap过来的，也可能是多个Part过来的，所以需要重新逐个加载进去
//                builder.addPart(requestBody1)
//                builder.addPart(requestBody2)
//                builder.addPart(requestBody3)
//                newRequestBuild = oldRequest.newBuilder()
//                newRequestBuild!!.post(builder.build())
//                Log.e(TAG, "MultipartBody," + oldRequest.url())
//            } else {
//                newRequestBuild = oldRequest.newBuilder()
//            }
//        } else {
//            // 添加新的参数
//            val commonParamsUrlBuilder = oldRequest.url()
//                    .newBuilder()
//                    .scheme(oldRequest.url().scheme())
//                    .host(oldRequest.url().host())
//                    .addQueryParameter("deviceOs", "")
//                    .addQueryParameter("appVersion", "")
//                    .addQueryParameter("appName", "")
//            newRequestBuild = oldRequest.newBuilder()
//                    .method(oldRequest.method(), oldRequest.body())
//                    .url(commonParamsUrlBuilder.build())
//        }
//        val newRequest = newRequestBuild!!
//                .addHeader("Accept", "application/json")
//                .addHeader("Accept-Language", "zh")
//                .build()
//
//        val startTime = System.currentTimeMillis()
//        val response = chain.proceed(newRequest)
//        val endTime = System.currentTimeMillis()
//        val duration = endTime - startTime
//        val mediaType = response.body()!!.contentType()
//        val content = response.body()!!.string()
//        val httpStatus = response.code()
//        val logSB = StringBuilder()
//        logSB.append("-------start:$method|")
//        logSB.append(newRequest.toString() + "\n|")
//        logSB.append(if (method.equals("POST", ignoreCase = true)) "post参数{$postBodyString}\n|" else "")
//        logSB.append("httpCode=$httpStatus;Response:$content\n|")
//        logSB.append("----------End:" + duration + "毫秒----------")
//        Log.d(TAG, logSB.toString())
//        return response.newBuilder()
//                .body(okhttp3.ResponseBody.create(mediaType, content))
//                .build()
//    }
//
//    companion object {
//
//        var TAG = "LogInterceptor"
//        private fun bodyToString(request: RequestBody?): String {
//            try {
//                val buffer = Buffer()
//                if (request != null)
//                    request.writeTo(buffer)
//                else
//                    return ""
//                return buffer.readUtf8()
//            } catch (e: IOException) {
//                return "did not work"
//            }
//
//        }
//    }
//}
