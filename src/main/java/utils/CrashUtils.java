package utils;

import bean.Crash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CrashUtils {



    public static Crash getCrashRate(Crash crash){
        if(crash == null){
            return crash;
        }
        crash.setStartTime(getStringTimes(0));
        crash.setEndTime(getStringTimes(24));
         List<Crash.HourCrash> data=crash.getData();
        if(data!=null&&data.size()>0){
            for (Crash.HourCrash crash1:data){
                crash.addTotalActiveNum(crash1.getStatemainId());
                crash.addTotalCrashNum(crash1.getCrashNum());
            }
        }
        return crash;
    }

    public static Crash getLagRate(Crash crash){
        if(crash == null){
            return crash;
        }
        crash.setStartTime(getStringTimes(0));
        crash.setEndTime(getStringTimes(24));
        List<Crash.HourCrash> data=crash.getData();
        if(data!=null&&data.size()>0){
            for (Crash.HourCrash crash1:data){
                crash.addTotalActiveNum(crash1.getStatemainId());
                crash.addTotalLagNum(crash1.getAppLagNum());
            }
        }
        return crash;
    }


    public static long getTimes(int hour) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        return (cal.getTimeInMillis())-24*60*60*1000;
    }


    public static String getStringTimes(int hour) {
        Date dateTime = new Date(getTimes(hour));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dateTime);
    }


    public   static   final   int  strLength  =   8 ;
    public   static  String FormatButtonText(String targetStr) {
        int curLength = targetStr.getBytes().length;
        if(targetStr!=null && curLength>strLength)
            targetStr = SubStringByte(targetStr);
        String newString = "";
        int cutLength = strLength-targetStr.getBytes().length;
        for(int i=0;i<cutLength;i++)
            newString +=" ";
        return targetStr+newString;
    }
    public   static  String SubStringByte(String targetStr) {
        while(targetStr.getBytes().length>strLength)
            targetStr = targetStr.substring(0,targetStr.length()-1);
        return targetStr;
    }

}
