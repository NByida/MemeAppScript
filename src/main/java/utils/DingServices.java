package utils;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface DingServices {

//    @FormUrlEncoded
@Headers("Content-Type: application/json;charset=UTF-8")
@POST("robot/send?access_token=3736d8adab78c746d85033ee8dbb5a2f20a2721a98dbb5f9b165d3b34ae80288")
    Call<String> sendMessage(
            @Body RequestBody   bodey
    );
}
