package poitot.com.cryptoexample;

import android.app.Application;

import timber.log.Timber;

/**
 * Light wrapper class around {@link Application} to instantiate some global stuff (like logging).
 */
public class CrytoExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
