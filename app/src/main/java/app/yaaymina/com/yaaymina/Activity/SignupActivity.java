package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import app.yaaymina.com.yaaymina.AbstractMethods;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class SignupActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private TextInputEditText textinput_userName,textinput_email,textinput_phone,textinput_password;

    private TextView textView_login;

    private ImageView imageView_username,imageView_password,imageView_email,imageView_phone;

    private Button button_signUp;

    private String user_name, password, email, phone;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type;
    private Locale myLocale;

    private Toolbar toolbar_signup;

    private RelativeLayout relativeLayout_nonetwork;
    private LinearLayout linearLayout_main;
    private Button button_nointernet;


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
        setContentView(R.layout.activity_signup);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        toolbar_signup = findViewById(R.id.toolbar_main);

        toolbar_signup.setTitle(" ");
        setSupportActionBar(toolbar_signup);

        progressDialog = new ProgressDialog(SignupActivity.this);

        textinput_userName = findViewById(R.id.userName_textInput);
        textinput_email = findViewById(R.id.email_textInput);
        textinput_phone = findViewById(R.id.phone_textInput);
        textinput_password = findViewById(R.id.pwd_textInput);

        textView_login = findViewById(R.id.login_textView);

        imageView_username = findViewById(R.id.userName_imgVw);
        imageView_password = findViewById(R.id.pwd_imgVw);
        imageView_email = findViewById(R.id.email_imgVw);
        imageView_phone = findViewById(R.id.phone_imgVw);

        button_signUp = findViewById(R.id.btn_signUp);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        linearLayout_main = findViewById(R.id.layout_main);
        button_nointernet = findViewById(R.id.button_tryagain);


        button_signUp.setOnClickListener(this);
        button_nointernet.setOnClickListener(this);
        textView_login.setOnClickListener(this);

        textinput_email.setOnFocusChangeListener(this);
        textinput_phone.setOnFocusChangeListener(this);
        textinput_password.setOnFocusChangeListener(this);
        textinput_userName.setOnFocusChangeListener(this);


        changeLang(language_type);

    }

    private void doSignUp(final String user_name, final String email, final String phone, final String password) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.SIGN_UP_URL,


                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("Response "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put(Constants.EMAIL,email);
                params.put(Constants.PASSWORD,password);
                params.put(Constants.USER_NAME,user_name);
                params.put(Constants.PHONE,phone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {

        if (view == button_signUp)
        {
            user_name = textinput_userName.getText().toString();
            email = textinput_email.getText().toString();
            password = textinput_password.getText().toString();
            phone = textinput_phone.getText().toString();

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            String MobilePattern = "[0-9]{10}";

            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                linearLayout_main.setVisibility(View.GONE);
            }
            else
            {

                relativeLayout_nonetwork.setVisibility(View.GONE);
                linearLayout_main.setVisibility(View.VISIBLE);

                if (user_name.length() == 0)
                {
                    textinput_userName.setError(getString(R.string.enter_user_name));
                }else if (email.length() == 0)
                {
                    textinput_email.setError(getString(R.string.enter_email));
                }else if (!email.matches(emailPattern))
                {
                    textinput_email.setError(getString(R.string.invalid_email));
                }else  if (phone.length() == 0)
                {
                    textinput_phone.setError(getString(R.string.enter_phone_num));
                }else if (!phone.matches(MobilePattern))
                {
                    textinput_phone.setError(getString(R.string.invalid_phn_num));
                }
                else if (password.length() == 0)
                {
                    textinput_password.setError(getString(R.string.enter_password));
                }
                else if (password.length() < 6 || password.length() > 10)
                {
                    textinput_password.setError(getString(R.string.password_length));
                }
                    else
                {
                    doSignUp(user_name,email,phone,password);
                }
            }


        } else if (view == button_nointernet) {

            ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true) {
                recreate();
                relativeLayout_nonetwork.setVisibility(View.GONE);
                linearLayout_main.setVisibility(View.VISIBLE);
            } else {
                linearLayout_main.setVisibility(View.GONE);
                Toast.makeText(SignupActivity.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

            }

        }else
        {

            finish();
        }
    }


    @Override
    public void onFocusChange(View view, boolean b) {

        if (view == textinput_userName)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user_active);
            AbstractMethods.hideKeyboard(this,view);


        }else if (view == textinput_email)
        {
            imageView_email.setImageResource(R.drawable.ic_email);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(this,view);


        }else if (view == textinput_password)
        {

            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok_active);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(this,view);


        }else if (view == textinput_phone)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok);
            imageView_phone.setImageResource(R.drawable.ic_call_active);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(this,view);


        }
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


}

