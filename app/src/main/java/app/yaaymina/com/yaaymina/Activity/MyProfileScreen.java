package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.AbstractMethods;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class MyProfileScreen extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {


    private BottomNavigationView bottomNavigationView;

    private TextInputEditText textinput_userName,textinput_email,textinput_phone,textinput_pwd;
    private ImageView imageView_username,imageView_email,imageView_phone,imageView_pwd;
    private Button button_update;
    private String user_name,email,phone,pwd;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;

    private String language_type;

    private LinearLayout main_linearLayout;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;

    private Locale myLocale;

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
        setContentView(R.layout.activity_my_profile_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(this);

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        button_nointernet.setOnClickListener(this);

        changeLang(language_type);

        if (user_id.length()!=0)
        {
            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);

            }else
            {
                doGetProfileData(user_id);
                relativeLayout_nonetwork.setVisibility(View.GONE);

            }


        }

        textinput_userName = findViewById(R.id.userName_textInput);
        textinput_email = findViewById(R.id.email_textInput);
        textinput_phone = findViewById(R.id.phone_textInput);

        imageView_username = findViewById(R.id.userName_imgVw);
        imageView_email = findViewById(R.id.email_imgVw);
        imageView_phone = findViewById(R.id.phone_imgVw);

        button_update = findViewById(R.id.updt_btn);

        toolbar = findViewById(R.id.toolbar_main);

        main_linearLayout = findViewById(R.id.layout_mainlayer);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (MyProfileScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }
        });


        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

        setupBottomNavigation();


        textinput_email.setOnFocusChangeListener(this);
        textinput_phone.setOnFocusChangeListener(this);
        textinput_userName.setOnFocusChangeListener(this);

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isConnectd(MyProfileScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    main_linearLayout.setVisibility(View.GONE);
                }
                else
                {

                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    main_linearLayout.setVisibility(View.VISIBLE);

                    user_name = textinput_userName.getText().toString();
                    email = textinput_email.getText().toString();
                    phone = textinput_phone.getText().toString();

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    String MobilePattern = "[0-9]{10}";

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
                    } else
                    {
                        doUpdate(user_name,email,phone);
                    }

                }

            }
        });

    }

    private void doGetProfileData(final String user_id) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.USER_PROFILE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("responeGet"+ response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("profile_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String username = jsonObject1.getString("username");
                                    String email = jsonObject1.getString("email");
                                    String phone = jsonObject1.getString("phone");

                                    textinput_userName.setText(username);
                                    textinput_email.setText(email);
                                    textinput_phone.setText(phone);



                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("profileresonse " +response);
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
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.USER_ID,user_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void doUpdate(final String userName, final String email, final String phone) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PROFILE_UPDATE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        System.out.println("updt_utl "+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                Toast.makeText(MyProfileScreen.this, message, Toast.LENGTH_SHORT).show();
                                finish();

                            }else
                            {
                                Toast.makeText(MyProfileScreen.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("profileresonse " +response);
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
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.USER_ID,user_id);
                params.put(Constants.USER_NAME,userName);
                params.put(Constants.EMAIL,email);
                params.put(Constants.PHONE,phone);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == textinput_userName)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user_active);
            AbstractMethods.hideKeyboard(this,view);


        }else if (view == textinput_email)
        {
            imageView_email.setImageResource(R.drawable.ic_email);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(this,view);


        }else if (view == textinput_phone)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_phone.setImageResource(R.drawable.ic_call_active);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(this,view);


        }
    }

    private void setupBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                        finish();
                        return true;

                    case R.id.navigation_cart:

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            finish();
                        }
                        return true;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        finish();
                        return true;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        finish();
                        return true;

                }
                return false;
            }
        });
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
    public void showPopup(){

        View menuItemView = findViewById(R.id.navigation_eng);
        PopupMenu popup = new PopupMenu(this, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_select_language, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("English"))
                {
//                    Toast.makeText(HomeScreen.this, "English", Toast.LENGTH_SHORT).show();

                    editor.putString(ConstantData.LANGUAGE_SELECTION,"en").commit();
                    changeLang("en");
                    recreate();
                }
                else if (item.getTitle().equals("Arabic"))
                {

                    editor.putString(ConstantData.LANGUAGE_SELECTION,"ar").commit();
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


    @Override
    public void onClick(View view) {

        if (!isConnectd(this))
        {
            Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();

            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            main_linearLayout.setVisibility(View.GONE);
        }else
        {
            recreate();
            relativeLayout_nonetwork.setVisibility(View.GONE);
            main_linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
        (MyProfileScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

        finish();
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
