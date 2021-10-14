package bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Crash {


    @SerializedName("code")
    private Integer code;
    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<HourCrash> data;

    public List<HourCrash> getData() {
        return data;
    }

    public static class HourCrash {
        @SerializedName("monitorTime")
        private Long monitorTime;
        @SerializedName("launchNum")
        private Double launchNum;
        @SerializedName("crashRate")
        private Double crashRate;
        private String name;

        public String getName() {
            return name;
        }

        public Double getCrashRate() {
                   return((double)Math.round(crashRate*1000))/1000f;
        }

        //活跃会话数
        @SerializedName("statemainId")
        private Integer statemainId;
        @SerializedName("oldNum")
        private Integer oldNum;
        @SerializedName("crashNum")
        private Integer crashNum;

        public Integer getStatemainId() {
            return statemainId;
        }

        public Integer getCrashNum() {
            return crashNum;
        }
    }

    //日活跃数目
    private int totalActiveNum;
    //日闪退数目
    private int totalCrashNum;
    //闪退比列
    private float crashRate;


    public void addTotalCrashNum(int add) {
        this.totalCrashNum = totalCrashNum+add;
    }

    public void addTotalActiveNum(int add) {
        this.totalActiveNum = totalActiveNum+add;
    }



    public int getTotalCrashNum() {
        return totalCrashNum;
    }

    public int getTotalActiveNum() {
        return totalActiveNum;
    }


    public double getCrashRate() {
        if(totalActiveNum!=0){
            crashRate= (float)totalCrashNum/(float)totalActiveNum*100;
        }

        return((double)Math.round(crashRate*1000))/1000f;
    }

    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
