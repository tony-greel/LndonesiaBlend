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

    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("fetch/user/addressbook")
    Observable<BaseBean> contactsInfoUpLoad(@Body TreeMap requestParams);

    @Headers("Content-Type:application/json; charset=utf-8")
    @POST("loan/v2/loanapp")
    Observable<BaseBean<ApplyLoanBean>>loanSubmit(@Body TreeMap requestBodyMap);

    @POST("fetch/user/device")
    Observable<BaseBean> submitDevice(@Body TreeMap requestParam);

    @POST("comm/downoknotify")
    Observable<BaseBean> statisticAppDownload(@Body TreeMap requestParams);

    @Multipart
    @POST("user/userMediaSingle")
    Observable<BaseBean> uploadSingleImg(@Part List<MultipartBody.Part> requestBodyMap);

//    //上传MAC,蓝牙信息
//    @POST("user/device")
//    Observable<BaseBean> bluetoothUpload(@Body TreeMap requestParams);
}
