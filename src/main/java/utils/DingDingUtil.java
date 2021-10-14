package utils;

import cn.hutool.core.convert.impl.StringConverter;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;


@Slf4j
public class DingDingUtil {
    //发送超时时间10s
    private static final int TIME_OUT = 10000;

    /**
     * 钉钉机器人文档地址https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
     *
     * @param webhook
     * @param secret     安全设置 3选1【方式一，自定义关键词 】 【方式二，加签 ，创建机器人时选择加签 secret以SE开头】【方式三，IP地址（段）】
     * @param content    发送内容
     * @param mobileList 通知具体人的手机号码列表 （可选）
     * @return
     */
    private static String sendMsg(String webhook, String secret, String content, List<String> mobileList) {
        try {
            //钉钉机器人地址（配置机器人的webhook）
            if (!StringUtils.isEmpty(secret)) {
                Long timestamp = System.currentTimeMillis();
                String sign = getSign(timestamp, secret);
                webhook = new StringBuilder(webhook)
                        .append("&timestamp=")
                        .append(timestamp)
                        .append("&sign=")
                        .append(sign)
                        .toString();
            }
            System.out.println("webhook:" + webhook);
            //是否通知所有人
            boolean isAtAll = false;
            //组装请求内容
            String reqStr = buildReqStr(content, isAtAll, mobileList);
            //推送消息（http请求）
            getDing(reqStr);
//            LogUtils.d("推送结果result == " + result);
            return null;
        } catch (Exception e) {
//            LogUtils.d("发送群通知异常 异常原因：{}");
            return null;
        }
    }

    /**
     * 组装请求报文
     * 发送消息类型 text
     *
     * @param content
     * @return
     */
    private static String buildReqStr(String content, boolean isAtAll, List<String> mobileList) {
        //消息内容
        Map<String, String> contentMap = new HashMap<String, String>();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = new HashMap<String, Object>();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);
        Map<String, Object> reqMap =  new HashMap<String, Object>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        return JSON.toJSONString(reqMap);
    }



    /**
     * 自定义机器人获取签名
     * 创建机器人时选择加签获取secret以SE开头
     *
     * @param timestamp
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     */
    private static String getSign(Long timestamp, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        System.out.println("singn:" + sign);
        return sign;
    }




    private static void getDing(String body) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别
        builder.addInterceptor(loggingInterceptor);
        Retrofit  retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("https://api.dingtalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DingServices service = retrofit.create(DingServices.class);
        MediaType mediaType = MediaType.parse("application/json");
        Call<String> repos = service.sendMessage(RequestBody.create(mediaType, body));
        repos.execute();
    }

    public static void senDingMsg(String args) throws IOException {
        String webhook = "https://oapi.dingtalk.com/robot/send?access_token=3736d8adab78c746d85033ee8dbb5a2f20a2721a98dbb5f9b165d3b34ae80288";
        DingDingUtil.sendMsg(webhook, null, args, null);
    }
}
