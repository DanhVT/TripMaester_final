package vn.edu.hcmut.its.tripmaester;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import vn.edu.hcmut.its.tripmaester.controller.ControllerFactory;
import vn.edu.hcmut.its.tripmaester.helper.ImageLoaderHelper;
import vn.edu.hcmut.its.tripmaester.setting.TMPref;


/**
 * Created by thuanle on 12/16/15.
 */
public class TMApp extends Application {
    private static TMApp _INSTANCE;

    public static TMApp getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _INSTANCE = this;

        ControllerFactory.createControllers();

        //initial crashlytics
        Fabric.with(this, new Crashlytics());

        //initial Facebook
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);

        ImageLoaderHelper.init(this);
        //initial Preference
        TMPref.initialize(this);

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("vn.edu.hcmut.its.tripmaester", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
