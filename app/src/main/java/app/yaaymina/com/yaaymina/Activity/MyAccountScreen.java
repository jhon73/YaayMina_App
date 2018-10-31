package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiba.bottomnavigation.BottomNavigationItem;
import com.kiba.bottomnavigation.OnNavigationItemSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class MyAccountScreen extends AppCompatActivity implements View.OnClickListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private BottomNavigationView bottomNavigationView;

    private com.kiba.bottomnavigation.BottomNavigationView bottomNavigationView_;

    private Toolbar toolbar;
    private TextView textView_toolbar;

    private LinearLayout linearLayout_order, linearLayout_address, linearLayout_savec_card, linearLayout_change_pwd, linearLayout_reOrder;
    private TextView linearLayout_profile;

    private SharedPref session;

    private String language_type,is_guest;

    private SharedPreferences sharedPreferences,login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private Locale myLocale;

    private boolean doubleBackToExitPressedOnce = false;


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
        setContentView(R.layout.activity_my_account_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_sharedPref = getSharedPreferences(SharedPref.PREF_NAME,MODE_PRIVATE);
        login_editor = login_sharedPref.edit();

        session = new SharedPref(MyAccountScreen.this);

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");
        is_guest = sharedPreferences.getString(ConstantData.GUEST_USER,"");

        System.out.println("accountlangtype "+language_type);

        textView_toolbar = findViewById(R.id.textToolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        setUpNavigationView();


        bottomNavigationView_ = (com.kiba.bottomnavigation.BottomNavigationView) findViewById(R.id.bottomNavigationView);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest"))
        {

            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer, R.drawable.ic_cart_footer_active);
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer, R.drawable.ic_search_footer_active);

            final List<BottomNavigationItem> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);
            list.add(item3);

            bottomNavigationView_.addItems(list);

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.navigation_account).setVisible(false);

        }
        else
        {
            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer, R.drawable.ic_cart_footer_active);
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer, R.drawable.ic_search_footer_active);
            BottomNavigationItem item4 = new BottomNavigationItem(getResources().getString(R.string.title_account), R.drawable.ic_account_footer_active, R.drawable.ic_account_footer_active);

            final List<BottomNavigationItem> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);
            list.add(item3);
            list.add(item4);

            bottomNavigationView_.addItems(list);

        }

        bottomNavigationView_.setNavigationItemSelectListener(new OnNavigationItemSelectListener() {

            @Override
            public void onSelected(com.kiba.bottomnavigation.BottomNavigationView bottomNavigationView, View itemView, int position) {


                if (position == 0)
                {
//                    int number = 1;
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                    (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                }
                else if (position == 1)
                {
                    if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),CartScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                } else if (position == 2)
                {
                    startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                    (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 3)
                {
                    startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                    (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();
                }
            }
        });


        linearLayout_profile = findViewById(R.id.profile_layout);
        linearLayout_order = findViewById(R.id.order_layout);
        linearLayout_address = findViewById(R.id.address_layout);
        linearLayout_savec_card = findViewById(R.id.card_layout);
        linearLayout_change_pwd = findViewById(R.id.pwd_layout);
        linearLayout_reOrder = findViewById(R.id.reorder_layout);


        linearLayout_profile.setOnClickListener(this);
        linearLayout_order.setOnClickListener(this);
        linearLayout_address.setOnClickListener(this);
        linearLayout_savec_card.setOnClickListener(this);
        linearLayout_change_pwd.setOnClickListener(this);
        linearLayout_reOrder.setOnClickListener(this);

        changeLang(language_type);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest")) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_signin = menu.findItem(R.id.navigation_signout);
            nav_signin.setTitle(getString(R.string.login));


        }

        if (!isConnectd(this))
        {
            Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
        }
        else
        {
            doGetCartItem();
        }

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
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.rotate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_cart:
                        startActivity(new Intent(getApplicationContext(),CartScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                        finish();

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                            finish();
                        }

                        break;

                    case R.id.navigation_contact:
                        startActivity(new Intent(getApplicationContext(),ContactUsScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (MyAccountScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_signout:

                        if (is_guest.equalsIgnoreCase("1"))
                        {
                            finish();
                            session.guestLogoutUser();


                        }
                        else
                        {
                            finish();
                            editor.clear();
                            login_editor.clear();
                            editor.commit();
                            login_editor.commit();

                            session.logoutUser();

                        }

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


    public ActionBarDrawerToggle setupDrawerToggle() {

        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    @Override
    public void onClick(View view) {

        if (view == linearLayout_profile)
        {

            startActivity(new Intent(getApplicationContext(),MyProfileScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

        }
        else if (view == linearLayout_order)
        {
            startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

        }else if (view == linearLayout_address)
        {

            startActivity(new Intent(getApplicationContext(),MyAddressScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();


        }else if (view == linearLayout_savec_card)
        {
            startActivity(new Intent(getApplicationContext(),MySavedCardScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

        }else if (view == linearLayout_change_pwd)
        {
            startActivity(new Intent(getApplicationContext(),ChangePassowrdScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

        }else if (view == linearLayout_reOrder)
        {
            startActivity(new Intent(getApplicationContext(),ReorderHistoryScreen.class));
            (MyAccountScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

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
                    editor.putString(ConstantData.LANGUAGE_SELECTION, "en").commit();
                    changeLang("en");
                    recreate();
                }
                else if (item.getTitle().equals("Arabic"))
                {
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

    private void doGetCartItem() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CART_PRODUCT_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Double price, weight;
                        String product_name, product_description;

                        ArrayList<CartItemModel> cartItemModelArrayList = new ArrayList<>();

                        System.out.println("CartResponse " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {

                                String message = jsonObject.getString("message");
                                String total = jsonObject.getString("total");

                                bottomNavigationView_.setBadgeViewNumberByPosition(1, Integer.parseInt(total));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.USER_ID, sharedPreferences.getString(ConstantData.USER_ID,""));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    } @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }else
        {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }




}
