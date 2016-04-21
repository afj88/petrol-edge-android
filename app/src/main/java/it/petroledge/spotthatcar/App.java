package it.petroledge.spotthatcar;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheContextUtils;
import com.vincentbrison.openlibraries.android.dualcache.lib.DualCacheLogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import it.petroledge.spotthatcar.manager.ApiManager;
import it.petroledge.spotthatcar.service.ApiErrorEvent;
import it.petroledge.spotthatcar.service.IApis;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by friz on 03/04/16.
 */
public class App extends Application {

    private ApiManager mApiManager;
    private EventBus mBus = EventBus.getDefault();

    public static int VERSION_CODE;

    private static Context sContext;

    public static Context getAppContext() { return sContext;}
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        mApiManager = new ApiManager(mBus, buildApi());
        mBus.register(mApiManager);
        mBus.register(this);

        DualCacheLogUtils.enableLog();
        DualCacheContextUtils.setContext(getApplicationContext());

        try {
            VERSION_CODE = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;

        }
        catch (PackageManager.NameNotFoundException ex) {}

    }

    private IApis buildApi() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.159:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IApis.class);
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        //toast("Something went wrong, please try again.");
//        Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        Log.e("App", event.getError());
    }
}
