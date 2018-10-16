package com.jiangxufa.splashmodule.data.net;

import com.jiangxufa.baselibrary.data.protocol.DoResponse;
import com.jiangxufa.businesslibrary.model.Account;
import com.jiangxufa.businesslibrary.model.AppConfig;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/*
 * 创建时间：2018/1/6
 * 编写人：lenovo
 * 功能描述：
 */

public interface CommonApi {

    /**
//     * @param appType 应用类型
     * @return AppConfig
     */
    @GET("appconfig")
    Observable<DoResponse<AppConfig>> getAppConfig();

    /**
     * @param code         应用版本号
     * @param appType      应用类型
     * @param terminalType 终端类型
     * @return List[Version]，List[0]是最新的
     */
    @GET("version")
    Observable<String> getAppVersion(@Query("code") int code, @Query("appType") int appType, @Query("terminalType") int terminalType);

    /**
     * @param identitykey 账号
     * @param password    密码
     * @return api
     */
    @FormUrlEncoded
    @POST("user/account")
    Observable<DoResponse<Account>> login(@Field("identitykey") String identitykey, @Field("password") String password);

    /**
     * 发送短信验证码
     *
     * @param identitykey 登录账号，如13847563647（维修工），lxj00001（门店），lxj-admin（管理员）
     * @param phone       手机号码
     * @return 验证码id
     */
    @FormUrlEncoded
    @POST("captcha")
    Observable<DoResponse<String>> sendCaptcha(@Field("identitykey") String identitykey,
                                               @Field("phone") String phone);

    /**
     * 获得一次性授权 在登录的情况下captchaCode为空
     * 未登录时，可以通过短信验证码的方式获得一次性授权，此时需要传递captchaId和captchaCode；登录状态下，
     * 可以通过原密码获得一次性授权，此时需要传递token和password。
     *
     * @param captchaId   验证码id，通过验证码时必填
     * @param captchaCode 验证码，通过验证码时必填
     * @return tempauth(string)    临时授权
     */
    @FormUrlEncoded
    @POST("tempauth")
    Observable<DoResponse<String>> sendTempauth(@Field("captchaId") String captchaId,
                                                 @Field("captchaCode") String captchaCode,
                                                 @Field("password") String password);

    /**
     * 修改密码
     * 注意，修改完密码后，用户的token将会失效，此时需要客户端静默登录一次拉取新的token
     *
     * @param tempauth  临时授权
     * @param captchaId 验证码id
     * @param password  新密码
     * @return 不返回
     */
    @FormUrlEncoded
    @PUT("user/password")
    Observable<DoResponse<String>> modifyPassword(@Field("tempauth") String tempauth,
                                                   @Field("captchaId") String captchaId,
                                                   @Field("password") String password);

    /**
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意：使用{@code @Multipart}注解方法，必须使用{@code @Part}/<br>
     * {@code @PartMap}注解其参数<br>
     * 本接口中将文本数据和文件数据分为了两个参数，是为了方便将封装<br>
     * {@link MultipartBody.Part}的代码抽取到工具类中<br>
     * 也可以合并成一个{@code @Part}参数
     *
     * @param params 用于封装文本数据
     * @param parts  用于封装文件数据
     * @return 看服务器
     */
    @Multipart
    @POST("file")
    Observable<String> requestUploadWork(@PartMap Map<String, RequestBody> params,
                                         @Part List<MultipartBody.Part> parts);

    /**
     * 注意1：必须使用{@code @POST}注解为post请求<br>
     * 注意2：使用{@code @Body}注解参数，则不能使用{@code @Multipart}注解方法了<br>
     * 直接将所有的{@link MultipartBody.Part}合并到一个{@link MultipartBody}中
     */
    @POST("file")
    Observable<String> requestUploadWork(@Body MultipartBody body);

}
