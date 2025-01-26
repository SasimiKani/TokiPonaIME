package com.example.tokiponaime;

import android.content.Context;
import android.content.SharedPreferences;

public class LayoutPreferences {

    private static final String PREF_NAME = "layout_preferences";
    private static final String LAYOUT_ID_KEY = "layout_id";
    private final SharedPreferences sharedPreferences;

    /**
     * レイアウトIDを保存する
     * @param context context
     */
    public LayoutPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    /**
     * レイアウトIDを保存する
     * @param layoutId レイアウトID
     */
    public void saveLayoutId(int layoutId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LAYOUT_ID_KEY, layoutId);
        editor.apply();
    }

    /**
     * レイアウトIDを取得する
     * @param defaultLayoutId デフォルトのレイアウトID
     * @return レイアウトID
     */
    public int getLayoutId(int defaultLayoutId) {
        return sharedPreferences.getInt(LAYOUT_ID_KEY, defaultLayoutId);
    }

}
