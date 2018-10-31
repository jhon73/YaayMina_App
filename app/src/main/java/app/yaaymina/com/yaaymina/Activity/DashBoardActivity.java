package app.yaaymina.com.yaaymina.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kiba.bottomnavigation.BottomNavigationItem;
import com.kiba.bottomnavigation.OnNavigationItemSelectListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.CartAdapter;
import app.yaaymina.com.yaaymina.Adapter.HomeGridAdapter;
import app.yaaymina.com.yaaymina.Adapter.HomeSliderAdapter;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.Model.CategoryModel;
import app.yaaymina.com.yaaymina.Model.SliderModelClass;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private BottomNavigationView bottomNavigationView;

    private ArrayList<SliderModelClass> arrayList_image;
    private ArrayList<CategoryModel> arrayList_categorydata;

    private HomeSliderAdapter homeSliderAdapter;
    private HomeGridAdapter homeGridAdapter;

    private TextView[] dots;

    private ViewPager viewPager_home;
    private LinearLayout linearLayout_dots;
    private GridView gridView_home;

    private Handler handler;
    private final int delay = 2000;
    private int page = 0;

    private ProgressDialog progressDialog;

    private SharedPref session;

    private String language_type, user_id;
    String category_title, is_guest;

    private Toolbar toolbar;

    private SharedPreferences sharedPreferences, login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private Locale myLocale;

    private com.kiba.bottomnavigation.BottomNavigationView bottomNavigationView_;

    private boolean flag = false;

    private RelativeLayout relativeLayout_nonetwork;
    private Button button_nointernet;

    private TextView textView_noProductFound;

    private LinearLayout layout_mainlayer;

    TelephonyManager telephonyManager;
    private boolean doubleBackToExitPressedOnce = false;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_sharedPref = getSharedPreferences(SharedPref.PREF_NAME, MODE_PRIVATE);
        login_editor = login_sharedPref.edit();

        session = new SharedPref(DashBoardActivity.this);

        user_id = sharedPreferences.getString(ConstantData.USER_ID, "");
        is_guest = sharedPreferences.getString(ConstantData.GUEST_USER, "");


        System.out.println("chckuId " + user_id);
        Intent intent = getIntent();

        if (intent.hasExtra("action")) {
            Log.d("tag", "doNothing");
        } else {
            session.checkLogin();
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);

        textView_noProductFound = findViewById(R.id.no_product_found);

        if (sharedPreferences.getString(ConstantData.USER_TYPE, "").equalsIgnoreCase("guest")) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_signin = menu.findItem(R.id.navigation_signout);
            nav_signin.setTitle(getString(R.string.login));
        }


        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        layout_mainlayer = findViewById(R.id.layout_mainlayer);

        button_nointernet.setOnClickListener(this);

        setUpNavigationView();

        navigationView.getMenu().getItem(0).setChecked(true);


        bottomNavigationView_ = (com.kiba.bottomnavigation.BottomNavigationView) findViewById(R.id.bottomNavigationView);


        if (sharedPreferences.getString(ConstantData.USER_TYPE, "").equalsIgnoreCase("guest")) {

            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer_active, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer, R.drawable.ic_cart_footer_active);
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer, R.drawable.ic_search_footer_active);

            final List<BottomNavigationItem> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);
            list.add(item3);

            bottomNavigationView_.addItems(list);

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.navigation_account).setVisible(false);

        } else {
            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer_active, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer, R.drawable.ic_cart_footer_active);
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer, R.drawable.ic_search_footer_active);
            BottomNavigationItem item4 = new BottomNavigationItem(getResources().getString(R.string.title_account), R.drawable.ic_account_footer, R.drawable.ic_account_footer_active);

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


                if (position == 0) {
//                    int number = 1;
                    startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                    (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 1) {
                    if (sharedPreferences.getString(ConstantData.USER_ID, "").equalsIgnoreCase("")) {
                        startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), CartScreen.class));
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                } else if (position == 2) {
                    startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                    (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 3) {
                    startActivity(new Intent(getApplicationContext(), MyAccountScreen.class));
                    (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();
                }
            }
        });


        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");

        arrayList_image = new ArrayList<>();
        arrayList_categorydata = new ArrayList<>();

        handler = new Handler();

        progressDialog = new ProgressDialog(DashBoardActivity.this);

        viewPager_home = findViewById(R.id.homeSlider_pager);
        linearLayout_dots = findViewById(R.id.ll_dots);
        gridView_home = findViewById(R.id.grid_view_home);

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {

            doGetSliderData();
            doGetCategoryList();
            doGetCartItem();
            relativeLayout_nonetwork.setVisibility(View.GONE);
            layout_mainlayer.setVisibility(View.VISIBLE);

        } else {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            layout_mainlayer.setVisibility(View.GONE);
            Toast.makeText(DashBoardActivity.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (networkInfo != null && networkInfo.isConnected() == true) {

                    doGetSliderData();
                    doGetCategoryList();
                    doGetCartItem();
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    layout_mainlayer.setVisibility(View.VISIBLE);

                } else {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    layout_mainlayer.setVisibility(View.GONE);
                    Toast.makeText(DashBoardActivity.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                }


            }
        });

        viewPager_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        changeLang(language_type);


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
                params.put(Constants.USER_ID, user_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                        finish();
                        break;

                    case R.id.navigation_cart:


                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }

                        break;

                    case R.id.navigation_contact:

                        startActivity(new Intent(getApplicationContext(),ContactUsScreen.class));
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();

                        break;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (DashBoardActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

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

                            editor.remove(ConstantData.USER_ID);
                            editor.remove(ConstantData.FULL_NAME);
                            editor.remove(ConstantData.PASSWORD);
                            editor.remove(ConstantData.EMAIL);
                            editor.remove(ConstantData.PHONE);
                            editor.remove(ConstantData.LANGUAGE_SELECTION);
                            editor.remove(ConstantData.USER_TYPE);
                            editor.remove(ConstantData.LANGUAGE);
                            editor.remove(ConstantData.PAYMENT_TYPE);
                            editor.remove(ConstantData.CART_TOTAL);
                            editor.remove(ConstantData.STRIPE_TOKEN);
                            editor.remove(ConstantData.CUSTOMER_ID);
                            editor.remove(ConstantData.PRODUCT_DETAIL);
                            editor.remove(ConstantData.AMOUNT);
                            editor.remove(ConstantData.ORDER_ID);
                            editor.apply();




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



    private void doGetCategoryList() {


        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.CATEGORY_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        arrayList_categorydata.clear();
                        System.out.println("CategoryData "+response);

                        try {



                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("category_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String category_id = jsonObject1.getString("category_id");
                                    String parent_id = jsonObject1.getString("parent_id");
                                    String category_slug = jsonObject1.getString("category_slug");
//                                    String category_title = jsonObject1.getString("category_title");
                                    String category_meta = jsonObject1.getString("category_meta");
                                    String category_description = jsonObject1.getString("category_description");
                                    String category_display_order = jsonObject1.getString("category_display_order");
                                    String category_active = jsonObject1.getString("category_active");
                                    String category_count_item = jsonObject1.getString("category_count_item");
                                    String category_published_article = jsonObject1.getString("category_published_article");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String category_updated = jsonObject1.getString("category_updated");
                                    String category_entered = jsonObject1.getString("category_entered");

                                    if (language_type.equalsIgnoreCase("en"))
                                    {
                                        category_title = jsonObject1.getString("category_title");

                                    }else if (language_type.equalsIgnoreCase("ar"))
                                    {
                                        category_title = jsonObject1.getString("category_title_ar");
                                    }
//                                    String category_title_ar = jsonObject1.getString("category_title_ar");
                                    String image_url = jsonObject1.getString("image_url");


                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategory_id(category_id);
                                    categoryModel.setParent_id(parent_id);
                                    categoryModel.setCategory_title(category_title);
                                    categoryModel.setImage_url(image_url);
                                    categoryModel.setCategory_slug(category_slug);
                                    categoryModel.setCategory_meta(category_meta);
                                    categoryModel.setCategory_description(category_description);
                                    categoryModel.setCategory_display_order(category_display_order);
                                    categoryModel.setCategory_active(category_active);
                                    categoryModel.setCategory_count_item(category_count_item);
                                    categoryModel.setCategory_published_article(category_published_article);
                                    categoryModel.setThumbnail(thumbnail);
                                    categoryModel.setCategory_updated(category_updated);
                                    categoryModel.setCategory_entered(category_entered);
//                                    categoryModel.setCategory_title_ar(category_title_ar);

                                    arrayList_categorydata.add(categoryModel);
                                }

                                if (arrayList_categorydata.size() == 0)
                                {
                                    textView_noProductFound.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    textView_noProductFound.setVisibility(View.GONE);
                                }

                                homeGridAdapter = new HomeGridAdapter(DashBoardActivity.this,arrayList_categorydata);
                                gridView_home.setAdapter(homeGridAdapter);
                                swipeRefreshLayout.setRefreshing(false);


                            }else{


                                Toast.makeText(DashBoardActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                Log.d("message ",message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void doGetSliderData() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.BASE_URL+Constants.SLIDER_LIST_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        arrayList_image.clear();

                        System.out.println("sliderres "+response);
                        // Do something with respons
                        // e

                        try {


                            String status = response.getString("status");
                            String message = response.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = response.getJSONArray("slider_info");

                                System.out.println("jsonArraylenght "+jsonArray.length());

                                for (int i = 0; i < jsonArray.length(); i++)
                                {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String slider_id = jsonObject1.getString("slide_id");
                                    String upload_id = jsonObject1.getString("upload_id");
                                    String image_url = jsonObject1.getString("image_url");

                                    SliderModelClass sliderModelClass = new SliderModelClass();

                                    sliderModelClass.setImage_url(image_url);
                                    sliderModelClass.setProduct_id(upload_id);
                                    sliderModelClass.setSlider_id(slider_id);

                                    arrayList_image.add(sliderModelClass);
                                }

                                homeSliderAdapter = new HomeSliderAdapter(getApplicationContext(), arrayList_image);
                                viewPager_home.setAdapter(homeSliderAdapter);
                                addBottomDots(0);


                            }else
                            {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
;
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    private void addBottomDots(int current_page) {

        dots = new TextView[arrayList_image.size()];

        linearLayout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getApplicationContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#9E9E9E"));
            linearLayout_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[current_page].setTextColor(getResources().getColor(R.color.mainColor));
    }


    Runnable runnable = new Runnable() {
        public void run() {
            if (homeSliderAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager_home.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }

    };

    @Override
    public void onResume() {
        super.onResume();

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            handler.postDelayed(runnable, delay);
            relativeLayout_nonetwork.setVisibility(View.GONE);
            layout_mainlayer.setVisibility(View.VISIBLE);
        }else
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            layout_mainlayer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            handler.removeCallbacks(runnable);
        }else
        {

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

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("English"))
                {

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
    public void onClick(View view) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            recreate();
            relativeLayout_nonetwork.setVisibility(View.GONE);
            layout_mainlayer.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(DashBoardActivity.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();
            layout_mainlayer.setVisibility(View.GONE);
        }

    }


}
