package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import app.yaaymina.com.yaaymina.Adapter.ProductListAdapter;
import app.yaaymina.com.yaaymina.Adapter.SearchAdapter;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class SearchScreen extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;
    private String language_type;

    private ProgressDialog progressDialog;

    private EditText editText_search;
    private RecyclerView recyclerView_search;

    private ArrayList<ProductListModel> arrayListSearch;

    private String selected_category_title,selected_product_name,selected_product_description,is_guest;

    private List<TagItemModel> productList1;

    private SearchAdapter productListAdapter;

    private SharedPref session;

    private SharedPreferences sharedPreferences,login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private Locale myLocale;

    private LinearLayout layout_mainlayer;

    private RelativeLayout relativeLayout_nonetwork;
    private Button button_nointernet;

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
        setContentView(R.layout.activity_search_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login_sharedPref = getSharedPreferences(SharedPref.PREF_NAME,MODE_PRIVATE);
        login_editor = login_sharedPref.edit();

        session = new SharedPref(SearchScreen.this);

        is_guest = sharedPreferences.getString(ConstantData.GUEST_USER,"");


        progressDialog = new ProgressDialog(this);

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");
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
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer_active, R.drawable.ic_search_footer_active);

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
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer_active, R.drawable.ic_search_footer_active);
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
                    (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                }
                else if (position == 1)
                {
                    if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),CartScreen.class));
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                } else if (position == 2)
                {
                    startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                    (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 3)
                {
                    startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                    (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();
                }
            }
        });

        editText_search = findViewById(R.id.edit_search);
        recyclerView_search = findViewById(R.id.recycler_search);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);
        button_nointernet.setOnClickListener(this);


        layout_mainlayer = findViewById(R.id.layout_mainlayer);

        editText_search.addTextChangedListener(this);

        arrayListSearch = new ArrayList<>();
        productList1 = new ArrayList<>();

        changeLang(language_type);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest")) {
            Menu menu = navigationView.getMenu();
            MenuItem nav_signin = menu.findItem(R.id.navigation_signout);
            nav_signin.setTitle(getString(R.string.login));


        }

        if (isConnectd(this))
        {
            doGetCartItem();
        }
        else
        {
            Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
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
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                        finish();
                        break;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_cart:
                      ;

                        if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                        {
                            startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                            (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(),CartScreen.class));
                            (SearchScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                            finish();
                        }

                        break;

                    case R.id.navigation_contact:

                        startActivity(new Intent(getApplicationContext(),ContactUsScreen.class));
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        break;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                        (SearchScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        System.out.println("textChanges" + charSequence);

        if (!isConnectd(this))
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            layout_mainlayer.setVisibility(View.GONE);
        }
        else
        {
            doSearchItem(charSequence);
            relativeLayout_nonetwork.setVisibility(View.GONE);
            layout_mainlayer.setVisibility(View.VISIBLE);
        }

    }

    private void doSearchItem(final CharSequence charSequence) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.SEARCH_PRODUCT_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("searchresponse "+ response);

                        arrayListSearch.clear();
                        progressDialog.dismiss();
                        System.out.println("productlistResponse "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    System.out.println("jsonObject1Response "+jsonObject1);

                                    String product_id = jsonObject1.getString("product_id");
                                    String category_id = jsonObject1.getString("category_id");
                                    String product_code = jsonObject1.getString("product_code");
                                    String product_tag = jsonObject1.getString("product_tag");
                                    String product_name = jsonObject1.getString("product_name");
                                    String product_rating = jsonObject1.getString("product_rating");
                                    String product_description = jsonObject1.getString("product_description");
                                    String product_overview = jsonObject1.getString("product_overview");
                                    String product_specifications = jsonObject1.getString("product_specifications");
                                    Double product_weight = Double.valueOf(jsonObject1.getString("product_weight"));
                                    String product_minorder = jsonObject1.getString("product_minorder");
                                    String color_option = jsonObject1.getString("color_option");
                                    String size_option = jsonObject1.getString("size_option");
                                    String product_cost = jsonObject1.getString("product_cost");
                                    String product_tax = jsonObject1.getString("product_tax");
                                    String product_price = jsonObject1.getString("product_price");
                                    String product_price_cross = jsonObject1.getString("product_price_cross");
                                    String product_currency = jsonObject1.getString("product_currency");
                                    String product_availability = jsonObject1.getString("product_availability");
                                    String product_status = jsonObject1.getString("product_status");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String product_updated = jsonObject1.getString("product_updated");
                                    String product_entered = jsonObject1.getString("product_entered");
                                    String category_title = jsonObject1.getString("category_title");
                                    String category_title_ar = jsonObject1.getString("category_title_ar");
                                    String product_name_ar = jsonObject1.getString("product_name_ar");
                                    String product_description_ar = jsonObject1.getString("product_description_ar");
                                    String image_url = jsonObject1.getString("image_url");


                                    if (language_type.equalsIgnoreCase("en"))
                                    {
                                        selected_category_title = jsonObject1.getString("category_title");
                                        selected_product_name = jsonObject1.getString("product_name");
                                        selected_product_description = jsonObject1.getString("product_description");

                                    }else if (language_type.equalsIgnoreCase("ar"))
                                    {
                                        selected_category_title = jsonObject1.getString("category_title_ar");
                                        selected_product_name = jsonObject1.getString("product_name_ar");
                                        selected_product_description = jsonObject1.getString("product_description_ar");
                                    }

//                                    linearLayout_main.setTitle(selected_category_title);

                                    ProductListModel productListModel = new ProductListModel();
                                    productListModel.setProduct_id(product_id);
                                    productListModel.setCategory_id(category_id);
                                    productListModel.setProduct_code(product_code);
                                    productListModel.setProduct_tag(product_tag);
                                    productListModel.setProduct_name(selected_product_name);
                                    productListModel.setProduct_rating(product_rating);
                                    productListModel.setProduct_description(selected_product_description);
                                    productListModel.setProduct_overview(product_overview);
                                    productListModel.setProduct_specifications(product_specifications);
                                    productListModel.setProduct_weight(product_weight);
                                    productListModel.setProduct_minorder(product_minorder);
                                    productListModel.setColor_option(color_option);
                                    productListModel.setSize_option(size_option);
                                    productListModel.setProduct_cost(product_cost);
                                    productListModel.setProduct_tax(product_tax);
                                    productListModel.setProduct_price(product_price);
                                    productListModel.setProduct_price_cross(product_price_cross);
                                    productListModel.setProduct_currency(product_currency);
                                    productListModel.setProduct_availability(product_availability);
                                    productListModel.setProduct_status(product_status);
                                    productListModel.setThumbnail(thumbnail);
                                    productListModel.setProduct_updated(product_updated);
                                    productListModel.setProduct_entered(product_entered);
                                    productListModel.setCategory_title(selected_category_title);
                                    productListModel.setCategory_title_ar(category_title_ar);
                                    productListModel.setProduct_name_ar(product_name_ar);
                                    productListModel.setProduct_description_ar(product_description_ar);
                                    productListModel.setImage_url(image_url);

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("tag");

                                    for (int j = 0; j < jsonArray1.length(); j++)
                                    {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);

                                        System.out.println("jsonObject2Response "+jsonObject1);

                                        String tag_id = jsonObject2.getString("tag_id");
                                        String tag_name = jsonObject2.getString("tag_name");
                                        String tag_image = jsonObject2.getString("tag_image");
                                        String tag_desc = jsonObject2.getString("tag_desc");
                                        String tag_create_date = jsonObject2.getString("tag_create_date");

                                        TagItemModel productListModel1 = new TagItemModel();

                                        productListModel1.setTag_id(tag_id);
                                        productListModel1.setTag_name(tag_name);
                                        productListModel1.setTag_image(tag_image);
                                        productListModel1.setTag_desc(tag_desc);
                                        productListModel1.setTag_create_date(tag_create_date);

                                        productList1.add(productListModel1);
                                        productListModel.setTagItemModels(productList1);
                                        productListModel.addDataObject(productListModel1);

                                    }

                                    arrayListSearch.add(productListModel);
                                }



                                productListAdapter = new SearchAdapter(SearchScreen.this, (ArrayList<ProductListModel>) arrayListSearch,(ArrayList<TagItemModel>)productList1);
                                recyclerView_search.setLayoutManager(new LinearLayoutManager(SearchScreen.this, LinearLayoutManager.VERTICAL, true));
                                recyclerView_search.setAdapter(productListAdapter);



                            }
                            else
                            {
                                Log.d("message",message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

                params.put(Constants.SEARCH_BY, String.valueOf(charSequence));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void afterTextChanged(Editable editable) {

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
            layout_mainlayer.setVisibility(View.GONE);
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            Toast.makeText(SearchScreen.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

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
