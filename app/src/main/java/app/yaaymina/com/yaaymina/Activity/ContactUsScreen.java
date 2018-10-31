package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class ContactUsScreen extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private SharedPreferences sharedPreferences,login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private SharedPref session;

    private TextInputEditText textInputEditText_email, textInputEditText_msg;
    private Button button_send;

    private ProgressDialog progressDialog;

    private Locale myLocale;
    private String language_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_screen);

        session = new SharedPref(this);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_sharedPref = getSharedPreferences(SharedPref.PREF_NAME,MODE_PRIVATE);
        login_editor = login_sharedPref.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        progressDialog = new ProgressDialog(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        setUpNavigationView();

        bottomNavigationView = findViewById(R.id.navigation);
//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(false);

        setupBottomNavigation();

        textInputEditText_email = findViewById(R.id.email_textInput);
        textInputEditText_msg = findViewById(R.id.message_textInput);

        button_send = findViewById(R.id.send_btn);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        button_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = textInputEditText_email.getText().toString();
                String message = textInputEditText_msg.getText().toString();

                if (email.length() == 0)
                {
                    textInputEditText_email.setError("Enter email address");
                }else if (!email.matches(emailPattern))
                {
                    textInputEditText_email.setError("Invalid email address");
                }else if (message.length() == 0)
                {
                    textInputEditText_msg.setError("Enter message");
                }else
                {
                    doContactUs(email,message);
                }
            }
        });

        changeLang(language_type);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest")) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_signin = menu.findItem(R.id.navigation_signout);
            nav_signin.setTitle("Login");

        }

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.navigation_account).setVisible(false);


    }

    private void doContactUs(final String email, final String message) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CONTACT_US,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("Contactres "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("Success"))
                            {
                                Toast.makeText(ContactUsScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                                (ContactUsScreen.this).overridePendingTransition(R.anim.rotate_left_to_right, R.anim.stable);

                                finish();
                            }
                            else
                            {
                                Toast.makeText(ContactUsScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.EMAIL,email);
                params.put(Constants.MESSAGE,message);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setUpNavigationView() {

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                        finish();
                        break;

                    case R.id.navigation_cart:

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                            finish();
                        }

                        break;

                    case R.id.navigation_contact:
                        startActivity(new Intent(getApplicationContext(),ContactUsScreen.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                        finish();

                        break;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                        finish();
                        break;

                    case R.id.navigation_signout:
                        editor.clear();
                        login_editor.clear();
                        editor.commit();
                        login_editor.commit();

                        session.logoutUser();
                        finish();
                        break;


                    default:
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                menuItem.setChecked(true);
                // Set action bar title
                setTitle(menuItem.getTitle());
                // Close the navigation drawer
                drawer.closeDrawers();

                return true;
            }
        });


        toggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(toggle);
    }


    private void setupBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                    case R.id.navigation_cart:

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        return true;
                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (ContactUsScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    public ActionBarDrawerToggle setupDrawerToggle() {

        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);

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
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
        (ContactUsScreen.this).overridePendingTransition(R.anim.rotate_left_to_right, R.anim.stable);
        finish();
    }
}
