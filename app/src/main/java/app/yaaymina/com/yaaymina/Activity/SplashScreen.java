package app.yaaymina.com.yaaymina.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    private TextView textView_chooseLang;
    private ImageView imageView_logo;
    private LinearLayout linearLayout_lang;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type;

    private Locale myLocale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(SharedPref.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        loadLocale();

        linearLayout_lang = findViewById(R.id.chooseLanguagell);
        imageView_logo = findViewById(R.id.logo_imgVw);
        textView_chooseLang = findViewById(R.id.lang_textview);


        if (!language_type.equalsIgnoreCase(""))
        {
            linearLayout_lang.setVisibility(View.GONE);

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, DashBoardActivity.class);
                    (SplashScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }
        else
        {
            linearLayout_lang.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.layout_language_dialog, null);
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                    dialogBuilder.setView(dialoglayout);
                    dialogBuilder.setCancelable(true);
                    final AlertDialog alertbox = dialogBuilder.create();


                    LinearLayout linearLayout_english = dialoglayout.findViewById(R.id.layout_english);
                    LinearLayout linearLayout_aerabic= dialoglayout.findViewById(R.id.layout_aerabic);

                    linearLayout_english.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

//                        editor.putString(ConstantData.LANGUAGE_SELECTION,"English").apply();
                            editor.putString(ConstantData.LANGUAGE_SELECTION,"en").apply();
                            textView_chooseLang.setText("English");
                            imageView_logo.setImageResource(R.drawable.ic_english_logo);
                            alertbox.dismiss();
                            changeLang("en");
                            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                            (SplashScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                            finish();

                        }
                    });


                    linearLayout_aerabic.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

//                        editor.putString(ConstantData.LANGUAGE_SELECTION,"Arabic").apply();
//                        editor.putBoolean(String.valueOf(ConstantData.ONCE_SELECTION),true).apply();
                            editor.putString(ConstantData.LANGUAGE_SELECTION,"ar").apply();
                            textView_chooseLang.setText("Arabic");
                            imageView_logo.setImageResource(R.drawable.ic_aerabic_logo);
                            alertbox.dismiss();
                            changeLang("ar");
                            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                            (SplashScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                    });

                    alertbox.show();

                }
            });

        }


    }

    private void loadLocale() {

        SharedPreferences prefs = getSharedPreferences(ConstantData.PREF_NAME, Activity.MODE_PRIVATE);
        String language = prefs.getString(ConstantData.LANGUAGE, "");
        changeLang(language);
    }
    private void changeLang(String lang) {

        System.out.println("languade " + lang);
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
//        updateTexts();
    }

    private void saveLocale(String lang) {

        SharedPreferences prefs = getSharedPreferences(ConstantData.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ConstantData.LANGUAGE_SELECTION, lang);
        editor.commit();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }


}
