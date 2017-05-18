package com.tigon.biniam.android.command_deligation_demo;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by USER on 5/12/2017.
 */

public class AlarmSetter {
    public MainActivity context;
    public AlarmSetter( MainActivity context){
        this.context = context;
    }
    public int getFirstNumbers(String s){
        int num = 0;
        if(s.contains("አስራ") || s.contains("ዓስራ") || s.contains("ዐስራ") ){
            num = 10;
        }

        else if(s.contains("ሃያ") || s.contains("ሀያ") || s.contains("ሐያ") ){
            num = 20;
        }
        else if(s.contains("ሰላሳ") || s.contains("ሠላሳ") || s.contains("ሰላሣ") || s.contains("ሠላሣ")){
            num = 30;
        }
        else if(s.contains("አርባ") || s.contains("ዐርባ") || s.contains("ዓርባ")){
            num = 40;
        }
        else if(s.contains("ሃምሳ") || s.contains("ሀምሳ") || s.contains("ሐምሳ") || s.contains("ሀምሣ") || s.contains("ሃምሣ")){
            num = 40;
        }
        else{
            num = -1;
        }

       return num;
    }
    public int getLastNumbers(String s){

        if(s.contains("ዘጠኝ")){
            return 9;
        }
        else if(s.contains("ስምንት")){
            return 8;
        }
        else if(s.contains("ሰባት")){
            return 7;
        }
        else if(s.contains("ስድስት")){
            return 6;
        }
        else if(s.contains("አምስት")){
            return 5;
        }
        else if(s.contains("አራት")){
            return 4;
        }
        else if(s.contains("ሶስት")){
            return 3;
        }
        else if(s.contains("ሁለት")){
            return 2;
        }
        else if(s.contains("አንድ")){
            return 1;
        }
        else if(s.contains("ዜሮ")){
            return 0;
        }
        else if(s.contains("ተኩል")){
            return 30;
        }
        else if(s.contains("ከሩብ")){
            return 15;
        }
        else if(s.contains("ሩብ")){
            return 15;
        }
        else{
            return -1;
        }
    }
    public String getPM_AM(String s){
        String s1 = "";

        if(s.contains("ጠዋት")){
            s1 = "AM";
        }
        else if(s.contains("ከሰአት")){
            s1 = "PM";
        }
        else if(s.contains("ማታ")){
            s1 = "PM";
        }
        else if(s.contains("ለሊት")){
            s1 = "AM";
        }
        else{
            s1 = "MM";
        }
        return s1;

    }
    private int getDayOfWeek(String day){


        if(day == "sunday"){
            return Calendar.SUNDAY;
        }
        else if(day == "monday"){
            return Calendar.MONDAY;
        }
        else if(day == "tuesday"){
            return Calendar.TUESDAY;
        }
        else if(day == "wednesday"){
            return Calendar.WEDNESDAY;
        }
        else if(day == "thursday"){
            return Calendar.THURSDAY;
        }
        else if(day == "friday"){
            return Calendar.FRIDAY;
        }
        else if(day == "saturday"){
            return Calendar.SATURDAY;
        }
        else{
            return Calendar.DAY_OF_WEEK;
        }

    }
    public String getDayText(String s){
        if(s.contains("ዛሬ")){
            return "today";
        }
        else if(s.contains("ሰኞ")){
            return "monday";
        }
        else if(s.contains("ሠኞ")){
            return "monday";
        }
        else if(s.contains("ማክሰኞ")){
            return "tuesday";

        }
        else if(s.contains("ማክሠኞ")){
            return "tuesday";

        }
        else if(s.contains("ማክሠኞ")){
            return "tuesday";

        }
        else if(s.contains("ማክሠኞ")){
            return "tuesday";

        }
        else if(s.contains("ሮብ")){
            return "wednesday";

        }
        else if(s.contains("ዕሮብ")){
            return "wednesday";

        }
        else if(s.contains("ሮዕብ")){
            return "wednesday";

        }
        else if(s.contains("ሐሙስ")){
            return "thursday";

        }
        else if(s.contains("ሀሙስ")){
            return "thursday";

        }
        else if(s.contains("አርብ")){
            return "friday";

        }
        else if(s.contains("ቅዳሜ")){
            return "saturday";

        }
        else if(s.contains("አሁድ")){
            return "sunday";

        }
        else {
            return "";
        }
    }

    public void setAlarm(int etHour, int etMinute,String AM_PM, String days) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        ArrayList<Integer> alarmDays= new ArrayList<Integer>();

        alarmDays.add(getDayOfWeek(days));

        if(AM_PM == "PM"){
            etHour = etHour + 12;
        }

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_DAYS, alarmDays);
        i.putExtra(AlarmClock.EXTRA_HOUR, etHour);
        i.putExtra(AlarmClock.EXTRA_MINUTES, minute + etMinute);

        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.changeLayout(R.layout.activity_main);
        context.startActivity(i);

    }


}
