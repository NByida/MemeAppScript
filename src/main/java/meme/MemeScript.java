package meme;

import bean.AppInfo;
import bean.Crash;
import cn.hutool.core.text.StrBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.CrashUtils;
import utils.DingDingUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static utils.CrashUtils.getTimes;

public class MemeScript {


    /**
     * 获取token的参数，使用 博睿 的 Encipher_Ticket.jar生成
     */
    private static String ticket="?g=s?d?s/o;v?g?h9.?s?h.t?.+m?sAtLa\\y\\h]MLs?g(t\\<_j+%?s[`<f]hBs?gbv`__ZVod7A2avYx.F@1-^?d`=KiPB`2a9Qe[VENVn3&KkZz/F9b?l?m4d";

    private static String gitPath="  https://github.com/NByida/MemeAppScript \r\n";

    /**
     * true 不发生到钉钉群
     */
    private static boolean isTest = false;

    private static ArrayList<AppInfo.DataDTO> listApps;


    /**
     * 请勿删除，提供给python调用
     * 使用 博睿 的 Encipher_Ticket.jar生成ticket
     * @param ticket
     * @return
     */
    public String[] getString(String ticket){
        return new String[]{ticket};
    }

    static Retrofit retrofit;

    public static void main(String[] args) throws IOException {
        if(args!=null && args.length>0){
            ticket=args[0];
        }
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
                getLag(token,"51309","么么直播Android");
                getcrash(token,"51310","么么直播iOS");
                getLag(token,"51310","么么直播iOS");
            }

            public void onFailure(Call<Repo> call, Throwable throwable) {
                System.out.println(" 获取token异常：" +throwable.getMessage());
            }
        });
    }

    /**
     * 按 version
     * 获取指定app 的日崩溃率
     * @param token
     * @param appid
     * @param appName
     */
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
                sb.append(gitPath);
                sb.append("\n");
                sb.append(appName + "    "+crash.getStartTime()+" 日崩溃率：" + crash.getCrashRate()+" %    " +"崩溃数:" + crash.getTotalCrashNum()
                        +"   会话数: " + crash.getTotalActiveNum()+"\n");
                sb.append("\r\n");


                List<Crash.HourCrash> data = crash.getData();
                List<Crash.HourCrash> printData=new ArrayList<>();

                int length=0;
                for (Crash.HourCrash crash1:data){
                    if(length++<5){
                        printData.add(crash1);
                    }
                }
                length=0;
                for ( Crash.HourCrash crash1:crash.getCrashData() ){
                    if( length++< 5 && !printData.contains(crash1) ){
                        printData.add(crash1);
                    }
                }
                sb.append("活跃会话数目最多 \n");
                length=0;


                for (Crash.HourCrash crash1:printData){
                    if( length++==5){
                        sb.append("崩溃率最高 \n");
                    }
                    sb.append(" 崩溃率："+CrashUtils.FormatButtonText( crash1.getCrashRate().toString())+" 崩溃数："+CrashUtils.FormatButtonText(crash1.getCrashNum()+"")+"  会话数： "+CrashUtils.FormatButtonText(crash1.getStatemainId()+"")+" 版本： "+crash1.getName());
                    sb.append("\n");
                }
                    String crashMsg= sb.toString();
                    System.out.println(crashMsg);
                    try {
                        if(!isTest){
                            DingDingUtil.senDingMsg(crashMsg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            public void onFailure(Call<Crash> call, Throwable throwable) {
                System.out.println("获取"+appName+"崩溃率异常：" +throwable.getMessage());
            }
        });
    }


    /**
     * 按 version
     * 获取指定app 的日崩溃率
     * @param token
     * @param appid
     * @param appName
     */
    public static void getLag(String token, String   appid, final String appName){
        GitHubService service = retrofit.create(GitHubService.class);
        Call<Crash> crashs =  service.getCrash(
                token,
                "memezhibo",
                appid+"",
                getTimes(0),
                getTimes(24),
                -1,
                500001,
                "{\"demisionType\":\"version\",\"moduleType\":\"lag\"}"
        );
        crashs.enqueue(new Callback<Crash>(){
            public void onResponse(Call<Crash> call, Response<Crash> response) {
                Crash crash = CrashUtils.getLagRate(response.body());
                StrBuilder sb=new StrBuilder();
                sb.append(gitPath);
                sb.append("\n");
                sb.append(appName + "    "+crash.getStartTime()+" 日卡顿率：" + crash.getLagRate()+" %    " +"卡顿会话数:" + crash.getTotalLagNum()
                        +"   会话数: " + crash.getTotalActiveNum()+"\n");
                sb.append("\r\n");
                List<Crash.HourCrash> data=crash.getData();
                List<Crash.HourCrash> printData=new ArrayList<>();

                int length=0;
                for (Crash.HourCrash crash1:data){
                    if(length++<5){
                        printData.add(crash1);
                    }
                }

                length=0;

                for ( Crash.HourCrash crash1:crash.getLagData() ){
                    if( length++<5 && !printData.contains(crash1) ){
                        printData.add(crash1);
                    }
                }
                sb.append("活跃会话数目最多 \n");
                length=0;

                for (Crash.HourCrash crash1:printData){
                    if( length++==5){
                        sb.append("卡顿率最高 \n");
                    }
                    sb.append(" 卡顿率："+CrashUtils.FormatButtonText( crash1.getLagRate().toString())+" 卡顿数："+CrashUtils.FormatButtonText(crash1.getAppLagNum()+"")+"  会话数： "+CrashUtils.FormatButtonText(crash1.getStatemainId()+"")+" 版本： "+crash1.getName());
                    sb.append("\n");
                }

                String crashMsg= sb.toString();
                System.out.println(crashMsg);
                try {
                    if(!isTest){
                        DingDingUtil.senDingMsg(crashMsg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call<Crash> call, Throwable throwable) {
                System.out.println("获取"+appName+"崩溃率异常：" +throwable.getMessage());
            }
        });
    }


    /**
     * 获取 崩溃/时间 折线图
     * @param token
     * @param appid
     */
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


    /**
     * 获取app信息
     * @param token
     */
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
