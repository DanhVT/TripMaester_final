package vn.edu.hcmut.its.tripmaester;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;

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
        ImageLoaderHelper.init(this);
        //initial Preference
        TMPref.initialize(this);
    }
}
