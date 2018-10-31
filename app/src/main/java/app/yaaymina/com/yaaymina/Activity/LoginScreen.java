package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import app.yaaymina.com.yaaymina.AbstractMethods;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    private TextInputEditText textInput_email, textInput_pwd;

    private CheckBox checkbox_tNc;

    private Button button_signIn;

    private ImageView imageView_email, imageView_password;

    private TextView textView_signUp, textView_forgotPwd, textView_guestUser;

    private Toolbar toolbar_login;

    private String username, password;
    private String emailPattern;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    SharedPref session;
    private String language_type;

    private Locale myLocale;

    private ImageView imageView_noNetwork;
    private RelativeLayout relativeLayout_nonetwork;
    private Button button_nointernet;

    private LinearLayout ll_mainlayout;

    SharedPreferences guest_pref;
    SharedPreferences.Editor getEditor;

//    String lang = "en";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        session = new SharedPref(LoginScreen.this);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);

        editor = sharedPreferences.edit();

        guest_pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        getEditor = guest_pref.edit();

        if (sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "").equalsIgnoreCase("")) {
            editor.putString(ConstantData.LANGUAGE_SELECTION, "en").commit();
        } else {
            Log.d("TAG", "Do nothing");
        }

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");

        System.out.println("languageType " + sharedPreferences.getString(ConstantData.USER_ID, ""));

        progressDialog = new ProgressDialog(LoginScreen.this);

        toolbar_login = findViewById(R.id.toolbar_main);
        textView_signUp = findViewById(R.id.signup_textView);
        textView_forgotPwd = findViewById(R.id.forogtPwd_textView);
        textView_guestUser = findViewById(R.id.guestUser_textView);

        ll_mainlayout = findViewById(R.id.ll_mainlayout);

        textInput_email = findViewById(R.id.email_textInput);
        textInput_pwd = findViewById(R.id.pwd_textInput);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);


        checkbox_tNc = findViewById(R.id.tnc_checkbox);

        button_signIn = findViewById(R.id.btn_signIn);

        imageView_email = findViewById(R.id.email_imgVw);
        imageView_password = findViewById(R.id.password_imgVw);

        textView_signUp.setOnClickListener(this);
        textView_guestUser.setOnClickListener(this);
        textView_forgotPwd.setOnClickListener(this);
        button_signIn.setOnClickListener(this);
        button_nointernet.setOnClickListener(this);

        textInput_email.setOnFocusChangeListener(this);
        textInput_pwd.setOnFocusChangeListener(this);


        textView_signUp.setText(R.string.sign_up_tip);


        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        toolbar_login.setTitle(" ");
        setSupportActionBar(toolbar_login);

        changeLang(language_type);
    }


    @Override
    public void onClick(View view) {

        if (view == textView_signUp) {
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            (LoginScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

        } else if (view == textView_guestUser) {

            System.out.println("guestchara " + guest_pref.getString("guest_name",""));

            if (!guest_pref.getString("guest_id","").equalsIgnoreCase("")) {

                session.createLoginSession(guest_pref.getString("guest_name",""), guest_pref.getString("guest_password",""), "guest");
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                (LoginScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);


                editor.putString(ConstantData.USER_ID,guest_pref.getString("guest_id",""));
                editor.putString(ConstantData.FULL_NAME,guest_pref.getString("guest_name",""));
                editor.putString(ConstantData.PASSWORD,guest_pref.getString("guest_password",""));
                editor.putString(ConstantData.USER_TYPE,"guest");
                editor.apply();

                finish();

            } else {

                doGuestLogin();
            }


        } else if (view == textView_forgotPwd) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.layout_forogt_pwd, null);
            dialogBuilder.setView(dialogView);

            final TextInputEditText editText_forgotPwd = (TextInputEditText) dialogView.findViewById(R.id.email_textInput_forgotPwd);
            Button btn_forgotPwd = (Button) dialogView.findViewById(R.id.forgotPwd_btn);

            final AlertDialog alertDialog = dialogBuilder.create();

            btn_forgotPwd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (editText_forgotPwd.getText().toString().length() == 0) {
                        editText_forgotPwd.setError(getString(R.string.enter_email));
                    } else if (!editText_forgotPwd.getText().toString().matches(emailPattern)) {
                        editText_forgotPwd.setError(getString(R.string.invalid_email));
                    } else {
                        doForGotPwd(editText_forgotPwd.getText().toString(), alertDialog);
                    }
                }
            });


            alertDialog.show();
        } else if (view == button_nointernet) {

            ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true) {
                recreate();
                relativeLayout_nonetwork.setVisibility(View.GONE);
                ll_mainlayout.setVisibility(View.VISIBLE);
            } else {
                ll_mainlayout.setVisibility(View.GONE);
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

            }


        } else {


            ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true) {
                username = textInput_email.getText().toString();
                password = textInput_pwd.getText().toString();

                if (username.length() == 0) {
                    textInput_email.setError(getString(R.string.enter_email));
                } else if (!username.matches(emailPattern)) {
                    textInput_email.setError(getString(R.string.invalid_email));
                } else if (password.length() == 0) {
                    textInput_pwd.setError(getString(R.string.enter_password));
                }else if(password.length() < 6)
                {
                    textInput_pwd.setError(getString(R.string.password_length));
                } else if (!checkbox_tNc.isChecked()) {
                    Toast.makeText(this, getResources().getString(R.string.accept_t_n_c), Toast.LENGTH_SHORT).show();
                } else {

                    doLogin(username, password);
                }

                relativeLayout_nonetwork.setVisibility(View.GONE);
                ll_mainlayout.setVisibility(View.VISIBLE);

            } else {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                ll_mainlayout.setVisibility(View.GONE);
                Toast.makeText(LoginScreen.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

            }


        }

    }


    private void doGuestLogin() {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GUEST_USER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        System.out.println("guestres " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("user_info");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String memeber_id = jsonObject1.getString("member_id");
                                    String username = jsonObject1.getString("username");
                                    String password = jsonObject1.getString("password");
                                    String login_type = jsonObject1.getString("user_type");

                                    editor.putString(ConstantData.USER_ID, memeber_id).apply();
                                    editor.putString(ConstantData.FULL_NAME, username).apply();
                                    editor.putString(ConstantData.PASSWORD, password).apply();
                                    editor.putString(ConstantData.USER_TYPE, login_type).commit();
                                    editor.putString(ConstantData.GUEST_NAME, username).apply();
                                    editor.putString(ConstantData.GUEST_PASSWORD, password).apply();

                                    editor.putString(ConstantData.GUEST_USER, "1").apply();

                                    session.createLoginSession(username, password, login_type);



                                    getEditor.putString("guest_id", memeber_id);
                                    getEditor.putString("guest_name", username);
                                    getEditor.putString("guest_password", password);
                                    getEditor.putString("type", "guest");
                                    getEditor.apply();

                                }

                                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                                (LoginScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                                finish();
                            } else {

                                Toast.makeText(LoginScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("guestres " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void doForGotPwd(final String email, final AlertDialog alertDialog) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.FORGET_PASSWORD_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("response " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {
                                Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put(Constants.EMAIL, email);

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void doLogin(final String username, final String password) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.SIGN_IN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        System.out.println("Response " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {

                                Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                                (LoginScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                finish();

                                JSONArray jsonArray = jsonObject.getJSONArray("user_info");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String user_id = jsonObject1.getString("member_id");
                                    String user_name = jsonObject1.getString("username");
                                    String email = jsonObject1.getString("email");
                                    String phone = jsonObject1.getString("phone");
                                    String password = jsonObject1.getString("password");
                                    String login_type = jsonObject1.getString("user_type");

//                                    editor.putBoolean(String.valueOf(SharedPref.IS_LOGIN), true);
                                    editor.putString(ConstantData.USER_ID, user_id).commit();
                                    editor.putString(ConstantData.FULL_NAME, user_name).commit();
                                    editor.putString(ConstantData.EMAIL, email).commit();
                                    editor.putString(ConstantData.PHONE, phone).commit();
                                    editor.putString(ConstantData.PASSWORD, password).commit();
                                    editor.putString(ConstantData.USER_TYPE, login_type).commit();

                                    editor.putString(ConstantData.GUEST_USER, "0").apply();
                                    session.createLoginSession(email, password, login_type);
                                }


                            } else {
                                Toast.makeText(LoginScreen.this, message, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.EMAIL, username);
                params.put(Constants.PASSWORD, password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (view == textInput_email) {
            imageView_email.setImageResource(R.drawable.ic_email);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok);
            AbstractMethods.hideKeyboard(this, view);
        } else {

            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_password.setImageResource(R.drawable.ic_paaword_lok_active);
            AbstractMethods.hideKeyboard(this, view);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_localization_option, menu);

        MenuItem m = menu.getItem(0);


        if (language_type.equalsIgnoreCase("en")) {
            m.setIcon(R.drawable.ic_english_logo);

        } else if (language_type.equalsIgnoreCase("ar")) {
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

//                    lang = "en";
                    editor.putString(ConstantData.LANGUAGE_SELECTION, "en").commit();
                    changeLang("en");
                    recreate();

                } else if (item.getTitle().equals("Arabic")) {

//                    lang = "ar";
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
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}
