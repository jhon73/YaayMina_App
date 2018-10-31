package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class ChangePassowrdScreen extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private TextInputEditText textInputEditText_olPwd, textInputEditText_newPwd;

    private Button button_update;

    private String oldPwd, newPwd, user_id;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String language_type;

    private ProgressDialog progressDialog;

    private Locale myLocale;

    private RelativeLayout relativeLayout_nonetwork;
    private Button button_nointernet;

    private LinearLayout layout_mainlayer;


    public static boolean isConnectd(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passowrd_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        progressDialog = new ProgressDialog(ChangePassowrdScreen.this);

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");


        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (ChangePassowrdScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                finish();            }
        });

        textInputEditText_olPwd = findViewById(R.id.oldPwd_textInput);
        textInputEditText_newPwd = findViewById(R.id.newPwd_textInput);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        layout_mainlayer = findViewById(R.id.layout_main);

        button_nointernet.setOnClickListener(this);


        button_update = findViewById(R.id.updt_btn);

        button_update.setOnClickListener(this);

        changeLang(language_type);

    }

    @Override
    public void onClick(View view) {

        if (view == button_update)

        {

            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                layout_mainlayer.setVisibility(View.VISIBLE);

            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                layout_mainlayer.setVisibility(View.VISIBLE);

                oldPwd = textInputEditText_olPwd.getText().toString();
                newPwd = textInputEditText_newPwd.getText().toString();

                if (oldPwd.length() == 0)
                {
                    textInputEditText_olPwd.setError(getString(R.string.enter_old_pwd));
                }else if (newPwd.length() == 0)
                {
                    textInputEditText_newPwd.setError(getString(R.string.enter_new_pwd));
                }else
                {
                    doChangePwd(user_id, oldPwd, newPwd);
                }

            }


        }else if (view == button_nointernet)
        {
            if (!isConnectd(this))
            {
                Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                layout_mainlayer.setVisibility(View.GONE);
            }else
            {

                relativeLayout_nonetwork.setVisibility(View.GONE);
                layout_mainlayer.setVisibility(View.VISIBLE);
                recreate();
            }
        }

    }

    private void doChangePwd(final String user_id, final String oldPwd, final String newPwd) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("Success"))
                    {
                        Toast.makeText(ChangePassowrdScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if (status.equalsIgnoreCase("fail"))
                    {
                        Toast.makeText(ChangePassowrdScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("chngresponse "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.USER_ID,user_id);
                params.put(Constants.OLD_PASSWORD,oldPwd);
                params.put(Constants.NEW_PASSWORD,newPwd);

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_localization_option, menu);

        MenuItem m =  menu.getItem(0);


        if (language_type.equalsIgnoreCase("en"))
        {
            m.setIcon(R.drawable.ic_english_logo);

        }else if (language_type.equalsIgnoreCase("ar"))
        {
            m.setIcon(R.drawable.ic_aerabic_logo);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_eng:
                showPopup();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("RestrictedApi")
    public void showPopup() {

        View menuItemView = findViewById(R.id.navigation_eng);
        PopupMenu popup = new PopupMenu(this, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_select_language, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("English")) {

                    editor.putString(ConstantData.LANGUAGE_SELECTION, "en").commit();
                    changeLang("en");
                    recreate();

                } else if (item.getTitle().equals("Arabic")) {

                    editor.putString(ConstantData.LANGUAGE_SELECTION, "ar").commit();
                    changeLang("ar");
                    recreate();
                }
                return false;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(this, (MenuBuilder) popup.getMenu(), menuItemView);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();

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
    }



    private void saveLocale(String lang) {

        SharedPreferences prefs = getSharedPreferences(ConstantData.PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ConstantData.LANGUAGE, lang);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
        (ChangePassowrdScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
