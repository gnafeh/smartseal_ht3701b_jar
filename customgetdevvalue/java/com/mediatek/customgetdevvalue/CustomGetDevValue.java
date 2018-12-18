/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.mediatek.customgetdevvalue;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.util.List;
//hefang add AutoClick 20180928
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.SystemProperties;
import android.os.BatteryManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import com.mediatek.customgetdevvalue.UUIDS;


public class CustomGetDevValue {

    private String TAG = "hefang_CustomGetDevValue";
    private byte devID[] = new byte[16];
    public final String PATH_SN_VALUE = "/vendor/protect_f/test_result";
    public final String PATH_BAT_INFO_BATVOLT = "/sys/class/power_supply/battery/BatterySenseVoltage";
    public final String PATH_BAT_INFO_CAPACITY = "/sys/class/power_supply/battery/capacity";
        String str_sn = "null";


	


    /*
       描述：获取设备序列号
       参数：devID： 字节数组，存储设备序列号
       返回：0表示读取成功，-1,表示读取失败
       int  getDeviceSN(byte devID[])
       */
    public String getDeviceSN(){
        //String str_sn = android.os.Build.SERIAL;
        //str_sn = UUIDS.readFromPath(PATH_SN_VALUE);
        /*
           int int_sn = 0;
           try {
           int_sn = Integer.parseInt(str_sn);
           } catch (NumberFormatException e) {
           e.printStackTrace();
           }
           */


        return str_sn;
    }


    /*
       描述：写入新的设备序列号
       参数：devID ：字节数组，要写入的设备序列号
       返回：0表示写入成功返回，-1,表示写入失败
       int  writeDeviceSN(byte devID[])
       */
    public String writeDeviceSN(byte devID[]){
        System.out.println(TAG + " writeDeviceSN() str_sn= " + str_sn);
        String temp_str= new String (devID);
        temp_str = checkStrFormat(temp_str);
        System.out.println(TAG + " writeDeviceSN() temp_str= " + temp_str);
        if(temp_str.equals("null")){
            System.out.println(TAG + " writeDeviceSN() temp_str=null = " + temp_str);

            return str_sn + " noooooo";
        }else{
            System.out.println(TAG + " writeDeviceSN() temp_str !=null = " + temp_str);
            str_sn = temp_str;
            UUIDS.writeToFile(UUIDS.FILE_PROTECT_F_SN, str_sn);
            return str_sn + " yeeeeeeeeeees";
        }
    }

    /*
       描述：打开/关闭公章
       参数：ctrl:  0表示关闭公章，1 表示打开公章
       返回：void
       void setSealStatus(int ctrl)
       */
    public int setSealStatus(int ctrl){
        int result=0;
        System.out.println(TAG + " setSealStatus() ctrl= " + ctrl + ", result= "+ result);
        return result;
    }

    /*
       描述：获取公章状态
       参数：void
       返回：0表示关闭公章，1 表示打开公章
       int getSealStatus()
       */
    public int getSealStatus(){
        int result=0;
        System.out.println(TAG + " getSealStatus() int_status= " + result);
        return result;
    }

    /*
       描述：获取设备电量
       参数：void
       返回：返回电池的电压，单位毫伏
       int getVolt()
       */     
    public int getVolt() {
        int int_volt = 999;
        int_volt = getBatInfoFromPath(PATH_BAT_INFO_BATVOLT);
        System.out.println(TAG + " getVolt() = " + int_volt);
        return int_volt;
    }

    /*
       描述：获取设备电量百分比
       参数：void
       返回：电量百分比范围0-100%
       int getBatteryPercent ()
       */
    public int getBatteryPercent (Context context){
        int int_bat_percentage = 99;
        /*
        BatteryManager batteryManager = (BatteryManager)context.getSystemService(context.BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        int battery1 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        int battery2 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        int battery3 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
        int battery4 = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
        System.out.println("hefang_cgdv getBatteryPercent() battery= " + battery + ",b1= " + battery1 + ",b2= "+ battery2 + ",b3= "+ battery3 + ",b4= " + battery4);
        int_bat_percentage = battery;
        */

        int_bat_percentage = getBatInfoFromPath(PATH_BAT_INFO_CAPACITY);
        System.out.println(TAG + " getBatteryPercent() int_bat_percentage= " + int_bat_percentage );
        return int_bat_percentage;
    }

    /*
       描述：删除指纹ID号
       参数：fingerID, 要删除的指纹ID号
       返回：0表示删除成功，1 表示删除失败
       int delFinger(short fingerID)
       */     
    public int delFinger(short fingerID){
        int result = 0;
        System.out.println(TAG + " delFinger() fingerID= " + fingerID + ", result= " +result);
        return result;
    }


    public int getBatInfoFromPath(String path) {


        File file = new File(path);
        String str = null;
        int temp_i = 0;
        try {
            InputStream is = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
            str = reader.readLine();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(str != null){
            try {
                temp_i=Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println(TAG + " getBatInfoFromPath() = int_volt = " + temp_i);
        return temp_i;
    }

    

    public String checkStrFormat(String sn) {

        System.out.println(TAG + " checkStrFormat() = sn = " + sn);
        System.out.println(TAG + " checkStrFormat() = str_sn = " + str_sn);
        boolean bool = isCorrectFormat(sn);
        System.out.println(TAG + " checkStrFormat() = bool = " + bool);

        if((sn.length() == 12) && bool){

            return sn;
        }else{
            return str_sn;
        }
    }

    public boolean isCorrectFormat(String str){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]*");
        return pattern.matcher(str).matches();
    }

}
