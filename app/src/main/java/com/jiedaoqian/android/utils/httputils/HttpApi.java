package com.jiedaoqian.android.utils.httputils;

import com.jiedaoqian.android.models.ProductHotInfo;
import com.jiedaoqian.android.models.ReqLogin;
import com.jiedaoqian.android.models.ResBanner;
import com.jiedaoqian.android.models.ResLogin;
import com.jiedaoqian.android.models.ResStatistics;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zenghui on 2017/7/6.
 */

public interface HttpApi {

    @GET("/rest/param/amounts")
    Observable<Response<HttpBaseResponse<Object>>> amounts();

    @GET("/rest/param/tags")
    Observable<Response<HttpBaseResponse<Object>>> tags();


    @GET("/rest/param/statistics")
    Observable<Response<HttpBaseResponse<ResStatistics>>> statistics();

    /**
     * 渠道列表
     * @param offset
     * @param limit
     * @return
     */
    @GET("/rest/loanchannel/hots")
    Observable<Response<HttpBaseResponse<List<ProductHotInfo>>>> loanchannel(@Query("offset") int offset, @Query("limit") int limit);


    @POST("/rest/user/login")
    Observable<Response<HttpBaseResponse<ResLogin>>> login(@Body ReqLogin reqLogin);



    @GET("/rest/user/logout")
    Observable<Response<HttpBaseResponse<Object>>> logout();


    /**
     * 首页banner
     * @return
     */
    @GET("/rest/loanchannel/banners")
    Observable<Response<HttpBaseResponse<ResBanner>>> banners();

    @GET("/rest/loanchannel")
    Observable<Response<HttpBaseResponse<List<ProductHotInfo>>>> loanchannel(@Query("offset") int offset, @Query("limit") int limit,@Query("amountRange") String amountRange,@Query("tag") String tag);



}
