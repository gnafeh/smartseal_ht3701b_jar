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
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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



/**
 * Created by xiongchengguang on 2016/10/13.
 */


public class UUIDS {

    private static final String TAG = "hefang_uuids";
    private static UUIDS device;
    private Context context;
    private final static String DEFAULT_NAME = "system_device_id";
    private final static String DEFAULT_FILE_NAME = "system_device_id";
    private final static String DEFAULT_DEVICE_ID = "dervice_id";
    private final static String FILE_ANDROID = Environment.getExternalStoragePublicDirectory("Android") + File.separator + DEFAULT_FILE_NAME;
    public final static String FILE_PROTECT_F_SN = "/vendor/protect_f/sn_result";
    public final static String FILE_SYSTEM_THIRD_SN = "/system/third/sn_result";
    private static SharedPreferences preferences = null;

    public UUIDS(Context context) {
        this.context = context;
    }

    private String uuid;

    public static UUIDS buidleID(Context context) {
        if (device == null) {
            synchronized (UUIDS.class) {
                if (device == null) {
                    device = new UUIDS(context);
                }
            }
        }
        return device;
    }

    public static String getUUID() {
        if (preferences == null) {
            Log.d(TAG, "Please check the UUIDS.buidleID in Application (this).Check ()");
            return "dervice_id";
        }
        return preferences.getString("dervice_id", "dervice_id");
    }

    //生成一个128位的唯一标识符
    private String createUUID() {
        String str = java.util.UUID.randomUUID().toString();
            Log.d(TAG, "createUUID() str= " + str);
         String[] idd = str.split("-");

            Log.d(TAG, "createUUID() idd= " + idd);
        //str = str.substring(0,5);
            Log.d(TAG, "createUUID() str= " + str);
        return java.util.UUID.randomUUID().toString();
    }


    public void check() {
        preferences = context.getSharedPreferences(DEFAULT_NAME, 0);
        uuid = preferences.getString(DEFAULT_DEVICE_ID, null);
        Log.d(TAG, "check(),uuid= " + uuid);
        if(uuid !=null){
            Log.d(TAG, "check(),uuid_length= " + uuid.length());
            if(uuid.length() == 36){
                Log.d(TAG, "uuid_length == 36 "  );
                String[] idd=uuid.toString().split("-");
                Log.d(TAG, "idd[0]= " + idd[0]);
                Log.d(TAG, "idd[1]= " + idd[1]);
                uuid = idd[0] + idd[1];
            }
            if(uuid.length() != 12) {
            Log.d(TAG, "uuid_length!=12, === " + uuid.length());
            return;
            }
        }
        Log.d(TAG, "check(),uuid= " + uuid);
        if (uuid == null) {
            if (checkFile(FILE_PROTECT_F_SN) == null && checkFile(FILE_SYSTEM_THIRD_SN) == null) {
                uuid = createUUID();
                //saveAndroidFile(uuid);
                createFile(uuid,FILE_PROTECT_F_SN);
                //saveDCIMFile(uuid);
                createFile(uuid,FILE_SYSTEM_THIRD_SN);
                Log.d(TAG, "new devices,create only id");
            }

            if (checkFile(FILE_PROTECT_F_SN) == null) {
                uuid = checkFile(FILE_SYSTEM_THIRD_SN);
                //saveAndroidFile(uuid);
                createFile(uuid,FILE_PROTECT_F_SN);
                Log.d(TAG, "Android directory was not found in UUID, from the DCIM directory to take out UUID\n");
            }

            if (checkFile(FILE_SYSTEM_THIRD_SN) == null) {
                uuid = checkFile(FILE_PROTECT_F_SN);
                //saveDCIMFile(uuid);
                createFile(uuid,FILE_SYSTEM_THIRD_SN);
                Log.d(TAG, "DCIM directory was not found in UUID, from the Android directory to take out UUID");
            }


            uuid = checkFile(FILE_PROTECT_F_SN);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEFAULT_DEVICE_ID, uuid);
            editor.commit();
            Log.d(TAG,"save uuid SharePref:" + uuid);
        } else {

            if (checkFile(FILE_PROTECT_F_SN) == null) {
                //saveAndroidFile(uuid);
                createFile(uuid,FILE_PROTECT_F_SN);
            }

            if (checkFile(FILE_SYSTEM_THIRD_SN) == null) {
                //saveDCIMFile(uuid);
                createFile(uuid,FILE_SYSTEM_THIRD_SN);
            }
        }
        Log.d(TAG,"result uuid:" + uuid);
    }
/*
    private String checkAndroidFile() {
        BufferedReader reader = null;ro.sw.ver.cust
        try {
            File file = new File(FILE_ANDROID);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void saveAndroidFile(String id) {
        try {
            File file = new File(FILE_ANDROID);
            //Log.d(file);
            System.out.println(TAG + " saveAndroidFile() file= " + file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String checkDCIMFile() {
        BufferedReader reader = null;
        try {
            File file = new File(FILE_DCIM);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDCIMFile(String id) {
        try {
            File file = new File(FILE_DCIM);
            //Log.d(file);
            System.out.println(TAG + " saveDCIMFile() file= " + file);
            FileWriter writer = new FileWriter(file);
            writer.write(id);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

*/

    public void isFileExist(File file, String path){
        String str = "null";
        if(!file.exists()){
            System.out.println(TAG + " 文件不存在！: " + file.getName());
            createFile(str,path);
        }
        if(file.isFile()){
            file.setReadable(true,false);
            file.setWritable(true,false);
            file.setExecutable(true,false);
            System.out.println(TAG + " 文件名称："+file.getName());
        }
    }

    public void createFile(String id, String path) {
        File file=new File(path);
            System.out.println(TAG + " createFile() file= " + file);
        try {
            FileWriter a = new FileWriter(file);
            a.write(id);
            a.flush();
            a.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    


     public static void writeToFile(String fileName, String content) {
       try {
           // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件   
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {   
            e.printStackTrace();   
        }
    }

    public String checkFile(String path) {
        BufferedReader reader = null;
        try {
            File file = new File(path);
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
    public void rwProtect_f () {
        File file = new File(PATH_TEST_R);
        if(!file.exists()){
            android.util.Log.e(TAG,"protect file is not exsit!");
            createFile(DEFAULT_NAME,PATH_TEST_R);
            return;
        }else{
            file.setReadable(true,false);
            file.setWritable(true,false);
            file.setExecutable(true,false);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                String[] resaultStringArray = tempString.trim().split(" ");
                android.util.Log.e(TAG, "resaultStringArray[0]: " + resaultStringArray[0] + ", resaultStringArray[1]: " + resaultStringArray[1]);
                //editor.putString(resaultStringArray[0], resaultStringArray[1]);
                //editor.commit();
                line++;
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){

                }
            }
        }
    }
*/
    public String readFromPath() {


        File file = new File(FILE_PROTECT_F_SN);
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


        System.out.println(TAG + " getSNFromPath() = str = " + str);
        return str;
    }

    
}
