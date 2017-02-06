package com.example.tacademy.recycleviewtest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 앱 구동간 필요에 의해서 저장하는 저장소 기능
 */
public class StorageHelper {
    // 저장소 메인키
    public static final String STORAGE_KEY      = "pref";
    private static StorageHelper ourInstance = new StorageHelper();
    public static StorageHelper getInstance() {
        return ourInstance;
    }
    private StorageHelper() {
    }
    // ------------------- 저장 타입별 기능 제공 ----------------------

    //static {
        // static{} 코드블럭 : NDK로 만들어지는 lib, o등등 파일을 (c/c++라이브러리) 로드할 때 사용
        // libxxxxx.o
        // static{ System.LoadLibrary("xxxx.o"); }
    //}
    //{
        // {} 코드블럭 : 객체가 생성되고 1회만
        //SharedPreferences.Editor editor =
    //}

    public void setString(Context context, String key, String value) {

        // 저장소 획득
        SharedPreferences.Editor editor = context.getSharedPreferences(STORAGE_KEY, 0).edit();

        // 저장
        editor.putString(key, value);

        // 커밋
        editor.commit();
    }

    public String getString(Context context, String key) {

        return
        // defvalue : 값이 없을 때에는 어떤 값을 보여줄까 ""
        context.getSharedPreferences(STORAGE_KEY, 0).getString(key, "");
    }

    //=================================================================

    public void setBoolean(Context context, String key, Boolean value) {

        // 저장소 획득
        SharedPreferences.Editor editor = context.getSharedPreferences(STORAGE_KEY, 0).edit();

        // 커밋
        editor.commit();
    }

    public Boolean getBoolean(Context context, String key) {

        return
                // defvalue : 값이 없을 때에는 어떤 값을 보여줄까 ""
                context.getSharedPreferences(STORAGE_KEY, 0).getBoolean(key, false);
    }



}
