package com.example.lndonesiablend.http;
import com.example.lndonesiablend.bean.ApplyLoanBean;
import com.example.lndonesiablend.bean.BaseBean;
import java.util.List;
import java.util.TreeMap;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * @Author: mosquito
 * @ClassName: ApiService
 * @Description:
 * @CreateDate: 2019/6/15 20:25
 */
public interface Api {

    //提交联系人信息
    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("fetch/user/addressbook")
    Observable<BaseBean> upLoadContactsInfo(@Body TreeMap requestParams);

    //提交贷款
    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("loan/v2/loanapp")
    Observable<BaseBean<ApplyLoanBean>> submitLoan(@Body TreeMap requestBodyMap);

    //提交设备信息
    @POST("fetch/user/device")
    Observable<BaseBean> fetchUserDevice(@Body TreeMap requestParam);

    //app下载统计
    @POST("comm/downoknotify")
    Observable<BaseBean> appDownloadStatistic(@Body TreeMap requestParams);

    //上传单张图片
    @Multipart
    @POST("user/userMediaSingle")
    Observable<BaseBean> uploadSingleImage(@Part List<MultipartBody.Part> requestBodyMap);

    //上传MAC,蓝牙信息
    @POST("user/device")
    Observable<BaseBean> upMacBluetooth(@Body TreeMap requestParams);

    @Multipart
    @POST("user/userMediaSingle")
    Call<BaseBean>uploadSingleImg(@Part List<MultipartBody.Part> requestBodyMap);
}
