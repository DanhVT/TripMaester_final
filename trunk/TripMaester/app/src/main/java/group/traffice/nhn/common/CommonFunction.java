package group.traffice.nhn.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

/**
 * Created by Xinh on 5/23/2015.
 */
public class CommonFunction {

    /**
     * Get string from SharedPreferences
     *
     * @param context
     * @param key
     * @return
     */
    public static String getSavedString(Context context, String key) {

        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);

        return saved.getString(key, null);
    }

    /**
     * Save string to SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saved.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get int from SharedPreferences
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getSavedInt(Context context, String key, int defValue) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        return saved.getInt(key, defValue);
    }

    /**
     * Save int to SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String key, int value) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saved.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Get float from SharedPreferences
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static float getSavedFloat(Context context, String key, float defValue) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        return saved.getFloat(key, defValue);
    }

    /**
     * Save float to SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String key, float value) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saved.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Get long from SharedPreferences
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static long getSavedLong(Context context, String key, long defValue) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        return saved.getLong(key, defValue);
    }

    /**
     * Save long to SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, long value) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saved.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Get boolean from SharedPreferences
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getSavedBoolean(Context context, String key, boolean defValue) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        return saved.getBoolean(key, defValue);
    }

    /**
     * Save boolean to SharedPreferences
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String key, boolean value) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saved.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Delete a SharedPreferences by key
     *
     * @param context
     * @param key
     */
    public static void deleteSavedData(Context context, String key) {
        SharedPreferences saved = context.getSharedPreferences("localdb",
                Context.MODE_PRIVATE);
        saved.edit().remove(key).commit();
    }

    /**
     * Check GPS availability
     * @param context
     * @return
     */
    public static boolean checkGPSAvailability(Context context){
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return !manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
