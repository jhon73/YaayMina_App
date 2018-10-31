package app.yaaymina.com.yaaymina.Model;

import android.app.Application;
import android.content.Context;

/**
 * Created by ADMIN on 28-Nov-17.
 */

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
