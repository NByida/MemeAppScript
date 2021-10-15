package utils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface DingServices {

//    @FormUrlEncoded
@Headers("Content-Type: application/json;charset=UTF-8")
@POST("robot/send?access_token=539408aef7934c5b3a5a36605c6022e43593df523a0912af8838318e22b49a7d")
    Call<String> sendMessage(
            @Body RequestBody   bodey
    );

}
