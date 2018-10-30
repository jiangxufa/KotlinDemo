package com.jiangxufa.homemodule.data.protocol;

import com.jiangxufa.homemodule.model.bandumi.BangumiDetailComment;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 * @date 创建时间：2017/5/23 16:25
 * 描述:番剧
 */

public interface ApiService {
    /**
     * 话题中心
     */
//    @GET("topic/getlist?device=phone&mobi_app=iphone&page=1&pagesize=137")
//    Flowable<TopicCenter> getTopicCenter();

    /**
     * 活动中心
     */
//    @GET("event/getlist?device=phone&mobi_app=iphone")
//    Flowable<ActivityCenter> getActivityCenter(@Query("page") int page, @Query("pagesize") int pageSize);

    /**
     * 番剧详情评论
     */
    @GET(
            "x/v2/reply?access_key=ccfbb1b10ce8ab8418a2e00b9ca9a3a0&appkey=1d8b6e7d45233436&build=505000&mobi_app=" +
                    "android&oid=9716141&plat=2&platform=android&pn=1&ps=20&sort=0&ts=1497169314&type=1&sign=ecca925ba55cecd151b5839f19d57657")
    Observable<BangumiDetailComment> getBangumiDetailComment();

    /**
     * 视频评论
     */
//    @GET("/x/v2/reply?access_key=0e6adb874025dfabaa3ced3a7b22049d&appkey=1d8b6e7d45233436&build=505000&mobi_app=android&oid=9938411&plat=2&platform=android&pn=1&ps=20&sort=0&ts=1497422373&type=1&sign=071c418a32ebc452e078308a04e1be4e")
//    Flowable<VideoDetailComment> getVideoDetailComment();
}
