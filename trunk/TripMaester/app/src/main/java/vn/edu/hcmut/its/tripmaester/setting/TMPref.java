package vn.edu.hcmut.its.tripmaester.setting;

import android.content.Context;
import android.content.SharedPreferences;

import cse.its.helper.Constant;

/**
 * Created by thuanle on 12/22/15.
 */
public class TMPref {
    private static TMPref _INSTANCE;
    private SharedPreferences mPref;

    private TMPref(Context context, String name){
        mPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static TMPref getInstances() {
        return _INSTANCE;
    }

    public static void initialize(Context context) {
        _INSTANCE = new TMPref(context, Constant.PREFERENCES_NAME);
    }

    public int getInt(String key, int init) {
        return mPref.getInt(key, init);
    }
}
