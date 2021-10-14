package bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppInfo {
    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<DataDTO> data;

    public List<DataDTO> getData() {
        return data;
    }

    public static class DataDTO {
        @SerializedName("pageNo")
        private Integer pageNo;
        @SerializedName("pageSize")
        private Integer pageSize;
        @SerializedName("totalRecord")
        private Integer totalRecord;
        @SerializedName("totalPage")
        private Integer totalPage;

        public Integer getPageNo() {
            return pageNo;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public Integer getTotalRecord() {
            return totalRecord;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public Integer getAppid() {
            return appid;
        }

        public String getAppmd5() {
            return appmd5;
        }

        public Integer getUserId() {
            return userId;
        }

        public String getAppname() {
            return appname;
        }

        public Long getCreatetime() {
            return createtime;
        }

        public Integer getFlag() {
            return flag;
        }

        public Integer getPaltform() {
            return paltform;
        }

        public Integer getResource() {
            return resource;
        }

        public Integer getStatus() {
            return status;
        }

        public Boolean getPage() {
            return page;
        }

        @SerializedName("appid")
        private Integer appid;
        @SerializedName("appmd5")
        private String appmd5;
        @SerializedName("userId")
        private Integer userId;
        @SerializedName("appname")
        private String appname;
        @SerializedName("createtime")
        private Long createtime;
        @SerializedName("flag")
        private Integer flag;
        @SerializedName("paltform")
        private Integer paltform;
        @SerializedName("resource")
        private Integer resource;
        @SerializedName("status")
        private Integer status;
        @SerializedName("page")
        private Boolean page;
    }
}
