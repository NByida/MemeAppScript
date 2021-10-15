package meme;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Repo {

    private Integer code;

   private String msg;

   private Tocken data;

    public Tocken getData() {
        return data;
    }

    public void setData(Tocken data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Tocken{
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
