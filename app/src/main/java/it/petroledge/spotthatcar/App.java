package it.petroledge.spotthatcar;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import okhttp3.OkHttpClient;
import com.squareup.picasso.Picasso;
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
    private Picasso mPicasso;

    public static int VERSION_CODE;

    private static Context sContext;

    public static Context getAppContext() { return sContext;}
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();



        DualCacheLogUtils.enableLog();
        DualCacheContextUtils.setContext(getApplicationContext());

        try {
            VERSION_CODE = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionCode;

        }
        catch (PackageManager.NameNotFoundException ex) {}

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        IApis IApis = new Retrofit.Builder()
                .baseUrl("http://192.168.1.77:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(IApis.class);

        Picasso.Builder picassoBuilder = new Picasso.Builder(sContext)
                .downloader(new OkHttp3Downloader(client));

        mPicasso = picassoBuilder.build();
        mPicasso.setIndicatorsEnabled(true);
        mPicasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(mPicasso);

        mApiManager = new ApiManager(mBus, IApis);
        mBus.register(mApiManager);
        mBus.register(this);

    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        //toast("Something went wrong, please try again.");
//        Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        Log.e("App", event.getError());
    }
}
