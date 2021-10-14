import bean.AppInfo;
import bean.Crash;
import retrofit2.Call;
import retrofit2.http.*;


public interface GitHubService {

    @FormUrlEncoded
    @POST("token/getToken")
    Call<Repo> getTocken(@Field("ticket") String ticket);

    @FormUrlEncoded
    @POST("api/report/getData")
    Call<Crash> getCrash(
            @Header("Authorization")String ticket,
            @Field("userName") String userName,
                         @Field("appId") String appId,
                         @Field("startTime") long startTime,
                         @Field("endTime") long endTime,
                         @Field("appVerId") int appVerId,
                         @Field("businessType") int businessType,
                         @Field("other") String other
    );

    @FormUrlEncoded
    @POST("api/report/getData")
    Call<Crash> getCrashRate(
            @Header("Authorization")String ticket,
            @Field("userName") String userName,
            @Field("appId") String appId,
            @Field("startTime") long startTime,
            @Field("endTime") long endTime,
            @Field("businessType") int businessType
    );


    @FormUrlEncoded
    @POST("api/app/getAppInfos")
    Call<AppInfo> getApps(@Header("Authorization")String ticket,
                          @Field("userName") String userName,
                          @Field("params") String params
    );



}

