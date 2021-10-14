import bean.AppInfo;
import bean.Crash;
import cn.hutool.core.text.StrBuilder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.CrashUtils;
import utils.DingDingUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static utils.CrashUtils.getTimes;

public class MemeScript {



    private static String ticket="?g?/?d0b`_?g?.$;)k2z?s?g/e1i4z@rN.Ev^f/QDv?g?gb/Im%&%kFV?g_fOm/p[:6bG7DhUtG4WNdKZBA2FK?da-`<7)AcEgCn=*^`L[^L\\mLw/F<x?l?a?f";


    static Retrofit retrofit;

    private static ArrayList<AppInfo.DataDTO> listApps;
    public static void main(String[] args) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
        builder.addInterceptor(loggingInterceptor);
         retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("https://sdk.bonree.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getToken();
    }

    public static void getToken(){
        GitHubService service = retrofit.create(GitHubService.class);
        Call<Repo> repos = service.getTocken(ticket);
        repos.enqueue(new Callback<Repo>(){
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                String token=response.body().getData().getToken();
                System.out.println("tocken:" +token);
                getcrash(token,"51309","么么直播Android");
                getcrash(token,"51310","么么直播iOS");
            }

            public void onFailure(Call<Repo> call, Throwable throwable) {
                System.out.println(" onFailure" +throwable.getMessage());
            }
        });
    }

    public static void getcrash(String token, String   appid, final String appName){
        GitHubService service = retrofit.create(GitHubService.class);
        Call<Crash> crashs =  service.getCrash(
                token,
                "memezhibo",
                    appid+"",
                getTimes(0),
                getTimes(24),
                -1,
                500001,
                "{\"demisionType\":\"version\",\"moduleType\":\"crash\"}"
                );
        crashs.enqueue(new Callback<Crash>(){
            public void onResponse(Call<Crash> call, Response<Crash> response) {
                Crash crash = CrashUtils.getCrashRate(response.body());

                StrBuilder sb=new StrBuilder();
                sb.append(appName + " "+crash.getStartTime()+"日崩溃率：" + crash.getCrashRate()+"%   " +"崩溃数:" + crash.getTotalCrashNum()
                        +"   会话数:" + crash.getTotalActiveNum()+"\n");
//                System.out.println(appName + " "+crash.getStartTime()+"日崩溃率：" + crash.getCrashRate()+"%   " +"崩溃数:" + crash.getTotalCrashNum()
//                        +"   会话数:" + crash.getTotalActiveNum()
//                );
                List<Crash.HourCrash> data=crash.getData();
                for (Crash.HourCrash crash1:data){
                    sb.append(" 崩溃率："+CrashUtils.FormatButtonText( crash1.getCrashRate().toString())+" 崩溃数："+CrashUtils.FormatButtonText(crash1.getCrashNum()+"")+" 会话数："+CrashUtils.FormatButtonText(crash1.getStatemainId()+"")+"   版本： "+crash1.getName());
                    sb.append("\n");
//                    System.out.println(" 崩溃率："+CrashUtils.FormatButtonText( crash1.getCrashRate().toString())+" 崩溃数："+CrashUtils.FormatButtonText(crash1.getCrashNum()+"")+" 会话数："+CrashUtils.FormatButtonText(crash1.getStatemainId()+"")+"   版本： "+crash1.getName());

                }
                System.out.println(sb);

                try {
                    DingDingUtil.senDingMsg(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call<Crash> call, Throwable throwable) {
                System.out.println("getcrash  onFailure" +throwable.getMessage());
            }
        });
    }

    public static void getcrashRate(String token,String   appid){
        GitHubService service = retrofit.create(GitHubService.class);
        Call<Crash> crashs =  service.getCrashRate(
                token,
                "memezhibo",
                appid+"",
                getTimes(0),
                getTimes(24),
                101006
        );
        crashs.enqueue(new Callback<Crash>(){
            public void onResponse(Call<Crash> call, Response<Crash> response) {

            }

            public void onFailure(Call<Crash> call, Throwable throwable) {
                System.out.println("getcrash  onFailure" +throwable.getMessage());
            }
        });
    }






    public static void  getApps(String token){
        GitHubService service = retrofit.create(GitHubService.class);

        Call<AppInfo> repos =service.getApps(token,"memezhibo"," {\"isPage\":true, \"pageNo\":1, \"pageSize\":10}");
        repos.enqueue(new Callback<AppInfo>(){
            public void onResponse(Call<AppInfo> call, Response<AppInfo> response) {
                if(response.body() instanceof AppInfo){
                    listApps = (ArrayList<AppInfo.DataDTO>) ( (AppInfo) response.body()).getData();
                }
                System.out.println("tocken:" +response.body());
                if(listApps!=null){
                    for (AppInfo.DataDTO  app : listApps){
                        //么么直播
                        if(app.getAppmd5().equals("54827817-2a30-454f-a47e-243211e09501")){
                            int appid=app.getAppid();
//                            getcrash(app);
                        }
                        //么么直播iOS
                        if(app.getAppmd5()=="e0a078d1-a3a7-4c75-aa95-1750858e965d"){
                            int appid=app.getAppid();

                        }

                    }
                }
            }

            public void onFailure(Call<AppInfo> call, Throwable throwable) {
                System.out.println(" onFailure" +throwable.getMessage());
            }
        });
    }
}
