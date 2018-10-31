package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.support.v4.widget.NestedScrollView;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class CartScreen extends AppCompatActivity implements View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private TextView textView_toolbar;

    private String user_id,is_guest, order_id;
    private String language_type;

    private ArrayList<CartItemModel> cartItemModelArrayList;
    private String product_description,product_name;

    private RecyclerView recyclerView_cart;
    private CartAdapter cartAdapter;

    private TextView textView_total;

    private Button button_place_order, button_shop_noew;

    private Double final_weight;
    private Double final_totalPrice, price , weight;

    UnitInterFace unitInterFace;

    private Double sub_price, totalamount;

    private FrameLayout frameLayout_progress;
    private NestedScrollView nestedScrollView;

    private SharedPreferences sharedPreferences,login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private SharedPref session;

    private LinearLayout linearLayout_fillCart;
    private FrameLayout frameLayout_empty_cart;

    private Locale myLocale;

    private LinearLayout layout_mainlayer;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;
    private RelativeLayout rel_layout_cart;

    private SwipeRefreshLayout swipeRefreshLayout;

    private com.kiba.bottomnavigation.BottomNavigationView bottomNavigationView_;

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
        setContentView(R.layout.activity_cart_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_sharedPref = getSharedPreferences(SharedPref.PREF_NAME,MODE_PRIVATE);
        login_editor = login_sharedPref.edit();

        session = new SharedPref(CartScreen.this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        setUpNavigationView();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        bottomNavigationView_ = (com.kiba.bottomnavigation.BottomNavigationView) findViewById(R.id.bottomNavigationView);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest"))
        {

            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer_active, R.drawable.ic_cart_footer_active);
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
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer_active, R.drawable.ic_cart_footer_active);
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


                if (position == 0)
                {
//                    int number = 1;
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                    (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                }
                else if (position == 1)
                {
                    if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                        (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),CartScreen.class));
                        (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                } else if (position == 2)
                {
                    startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                    (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 3)
                {
                    startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                    (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();
                }
            }
        });

        cartItemModelArrayList = new ArrayList<>();

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");
        is_guest = sharedPreferences.getString(ConstantData.GUEST_USER,"");

        System.out.println("languagetypeCart "+ language_type);

        toolbar = findViewById(R.id.toolbar_main);
        textView_toolbar = findViewById(R.id.textToolbar);

        textView_total = findViewById(R.id.total_textView);
        recyclerView_cart = findViewById(R.id.cartRecycler);
        button_place_order = findViewById(R.id.place_order_btn);
        button_shop_noew = findViewById(R.id.shopBtn);

        frameLayout_empty_cart = findViewById(R.id.empty_cart);
        linearLayout_fillCart = findViewById(R.id.filled_cart);

        layout_mainlayer = findViewById(R.id.layout_mainlayer);

        frameLayout_progress = findViewById(R.id.progress_framelayout);

        rel_layout_cart = findViewById(R.id.rel_layout_cart);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        button_nointernet.setOnClickListener(this);


        button_place_order.setOnClickListener(this);

        button_shop_noew.setOnClickListener(this);

        if (!isConnectd(this))
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);

        }else
        {
            doGetCartItem();
            relativeLayout_nonetwork.setVisibility(View.GONE);

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isConnectd(CartScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                }else
                {
                    doGetCartItem();
                    relativeLayout_nonetwork.setVisibility(View.GONE);

                }
            }
        });


        unitInterFace = new UnitInterFace() {

            @Override
            public void onTagClicked(String total_price) {

                totalamount = Double.parseDouble(total_price);

                textView_total.setText(String.valueOf(totalamount) + " " +"AED");
                System.out.println("Systotalamount1 "+total_price);

            }


        };

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest")) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_signin = menu.findItem(R.id.navigation_signout);
            nav_signin.setTitle(getString(R.string.login));

        }


        changeLang(language_type);
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
                        (CartScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_cart:

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }

                        break;

                    case R.id.navigation_contact:
                        startActivity(new Intent(getApplicationContext(),ContactUsScreen.class));
                        (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();

                        break;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

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

    private void doGetCartItem() {

        frameLayout_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+Constants.CART_PRODUCT_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("CartResponse "+response);
                        frameLayout_progress.setVisibility(View.GONE);
//                        nestedScrollView.setVisibility(View.VISIBLE);
                        cartItemModelArrayList.clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                String message = jsonObject.getString("message");
                                String total = jsonObject.getString("total");

                                totalamount = Double.valueOf(jsonObject.getString("totalamount"));
                                bottomNavigationView_.setBadgeViewNumberByPosition(1, Integer.parseInt(total));

                                textView_total.setText(String.valueOf(totalamount) + " " + "AED");

                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String cart_id = jsonObject1.getString("cart_id");
                                    String user_id = jsonObject1.getString("user_id");
                                    String product_id = jsonObject1.getString("product_id");
                                    String unit = jsonObject1.getString("unit");
                                    String cost = jsonObject1.getString("cost");
                                    price = Double.valueOf(jsonObject1.getString("price"));
                                    String inner_total = jsonObject1.getString("total");
                                    String total_grand = jsonObject1.getString("total_grand");
                                    weight = Double.valueOf(jsonObject1.getString("weight"));
                                    String currency = jsonObject1.getString("currency");
                                    String total_unit = jsonObject1.getString("total_unit");
                                    String total_price = jsonObject1.getString("total_price");
                                    String total_weight = jsonObject1.getString("total_weight");
                                    String category_id = jsonObject1.getString("category_id");
                                    String product_code = jsonObject1.getString("product_code");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String image_url = jsonObject1.getString("image_url");
                                    String measurement = jsonObject1.getString("messarment");


                                    if (language_type.equalsIgnoreCase("en"))
                                    {
                                        product_name = jsonObject1.getString("product_name");
                                        product_description = jsonObject1.getString("product_description");

                                    }else
                                    {
                                        product_name = jsonObject1.getString("product_name_ar");
                                        product_description= jsonObject1.getString("product_description_ar");

                                    }

                                    CartItemModel cartItemModel = new CartItemModel();

                                    cartItemModel.setCart_id(cart_id);
                                    cartItemModel.setUser_id(user_id);
                                    cartItemModel.setProduct_id(product_id);
                                    cartItemModel.setUnit(unit);
                                    cartItemModel.setCost(cost);
                                    cartItemModel.setPrice(price);
                                    cartItemModel.setInner_total(inner_total);
                                    cartItemModel.setTotal_grand(total_grand);
                                    cartItemModel.setWeight(weight);
                                    cartItemModel.setCurrency(currency);
                                    cartItemModel.setTotal_unit(total_unit);
                                    cartItemModel.setTotal_price(total_price);
                                    cartItemModel.setTotal_weight(total_weight);
                                    cartItemModel.setCategory_id(category_id);
                                    cartItemModel.setProduct_name(product_name);
                                    cartItemModel.setProduct_description(product_description);
                                    cartItemModel.setThumbnail(thumbnail);
                                    cartItemModel.setImage_url(image_url);
                                    cartItemModel.setMeasurement(measurement);

                                    cartItemModelArrayList.add(cartItemModel);
                                }

                                cartAdapter = new CartAdapter(CartScreen.this,cartItemModelArrayList, unitInterFace, null);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_cart.setLayoutManager(mLayoutManager);
                                recyclerView_cart.setAdapter(cartAdapter);
                                Log.d("TAG1","do nothing");

                                swipeRefreshLayout.setRefreshing(false);

                            } else if (status.equalsIgnoreCase("fail"))
                            {
                                frameLayout_empty_cart.setVisibility(View.VISIBLE);
                                linearLayout_fillCart.setVisibility(View.GONE);

                                swipeRefreshLayout.setRefreshing(false);

                                Log.d("TAG","do nothing");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                frameLayout_progress.setVisibility(View.GONE);
//                nestedScrollView.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {

        if (view == button_place_order)
        {
            System.out.println("totalAmt "+ totalamount);

            editor.putString(ConstantData.CART_TOTAL, String.valueOf(totalamount)).apply();

            Intent intent = new Intent(getApplicationContext(),CheckOutActivity.class);
            startActivity(intent);
            (CartScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();

        }

        else if (view == button_shop_noew)
        {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            (CartScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

            finish();
        }

        else if (view == button_nointernet)
        {
            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
            }
            else
            {
                recreate();
                relativeLayout_nonetwork.setVisibility(View.GONE);
            }
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
    protected void onPostResume() {
        super.onPostResume();
        if (!isConnectd(CartScreen.this))
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
        }
        else
        {
            relativeLayout_nonetwork.setVisibility(View.GONE);
        }
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
}
