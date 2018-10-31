package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import app.yaaymina.com.yaaymina.Adapter.ProductListAdapter;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class ProductListScreen extends AppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private GridView gridView_productList;
    private List<ProductListModel> productList;
    private List<TagItemModel> productList1;

    private ProductListAdapter productListAdapter;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type;
    private String selected_category_title,selected_product_name,selected_product_description;

    int index;
    int pos;


    private String data,category_name;

    private TextView textView_toolbar_name;

    private Locale myLocale;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;

    private TextView textView_noproduct_found;

    private com.kiba.bottomnavigation.BottomNavigationView bottomNavigationView_;



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
        setContentView(R.layout.activity_product_list_screen);

        Intent intent = getIntent();

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        textView_toolbar_name = findViewById(R.id.textToolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        button_nointernet.setOnClickListener(this);


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        bottomNavigationView_ = (com.kiba.bottomnavigation.BottomNavigationView) findViewById(R.id.bottomNavigationView);

        if (sharedPreferences.getString(ConstantData.USER_TYPE,"").equalsIgnoreCase("guest"))
        {

            BottomNavigationItem item1 = new BottomNavigationItem(getResources().getString(R.string.title_home), R.drawable.ic_home_footer_active, R.drawable.ic_home_footer_active);
            BottomNavigationItem item2 = new BottomNavigationItem(getResources().getString(R.string.title_cart), R.drawable.ic_cart_footer, R.drawable.ic_cart_footer_active);
            BottomNavigationItem item3 = new BottomNavigationItem(getResources().getString(R.string.title_search), R.drawable.ic_search_footer, R.drawable.ic_search_footer_active);

            final List<BottomNavigationItem> list = new ArrayList<>();
            list.add(item1);
            list.add(item2);
            list.add(item3);

            bottomNavigationView_.addItems(list);

        }
        else
        {
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


                if (position == 0)
                {
//                    int number = 1;
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                    (ProductListScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                }
                else if (position == 1)
                {
                    if (sharedPreferences.getString(ConstantData.USER_ID,"").equalsIgnoreCase(""))
                    {
                        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                        (ProductListScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(getApplicationContext(),CartScreen.class));
                        (ProductListScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                    }
                } else if (position == 2)
                {
                    startActivity(new Intent(getApplicationContext(),SearchScreen.class));
                    (ProductListScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();

                } else if (position == 3)
                {
                    startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                    (ProductListScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    finish();
                }
            }
        });



        sharedPreferences = getApplicationContext().getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        progressDialog = new ProgressDialog(this);


        if (intent.hasExtra("data"))
        {

         data = intent.getStringExtra("data");
         category_name = intent.getStringExtra("category_name");


            if (data.length()!=0)
            {
                if (!isConnectd(this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                }else
                {
                    doGetProductList(data,null);
                    doGetCartItem();
                    relativeLayout_nonetwork.setVisibility(View.GONE);

                }


            }else
            {
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (data.length()!=0)
                {
                    if (!isConnectd(ProductListScreen.this))
                    {
                        relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                    }else
                    {
                        doGetProductList(data,null);
                        doGetCartItem();
                        relativeLayout_nonetwork.setVisibility(View.GONE);

                    }


                }else
                {
                    Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                }


            }
        });


        productList = new ArrayList<>();
//        productList1 = new ArrayList<>();

        gridView_productList = findViewById(R.id.grid_view_productlist);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        textView_noproduct_found = findViewById(R.id.no_product_found);
        button_nointernet.setOnClickListener(this);

        changeLang(language_type);



    }



    private void doGetProductList(final String getArgument, final String sort_by) {

        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PRODUCT_DETAIL_CATEGORY_ID_BASED_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        productList.clear();
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
                                    productList1 = new ArrayList<>();
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
                                        textView_toolbar_name.setText(selected_category_title);

                                    }else if (language_type.equalsIgnoreCase("ar"))
                                    {
                                        selected_category_title = jsonObject1.getString("category_title_ar");
                                        selected_product_name = jsonObject1.getString("product_name_ar");
                                        selected_product_description = jsonObject1.getString("product_description_ar");
                                        textView_toolbar_name.setText(selected_category_title);

                                    }

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

                                    for (int j = 0; j < 1; j++)
                                    {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);

                                        System.out.println("jsonObject2Response "+jsonObject2);
                                        String tag_name = null, tag_desc = null;

                                        String tag_id = jsonObject2.getString("tag_id");
                                        String tag_image = jsonObject2.getString("tag_image");

                                        productListModel.setTag_img(tag_image);
                                        System.out.println("jsonObject2img "+tag_image);


                                        String tag_create_date = jsonObject2.getString("tag_create_date");

                                        if (language_type.equalsIgnoreCase("en"))
                                        {
                                            tag_name = jsonObject2.getString("tag_name");
                                            tag_desc = jsonObject2.getString("tag_desc");

                                        }else if (language_type.equalsIgnoreCase("ar"))
                                        {
                                            tag_name = jsonObject2.getString("tag_name_ar");
                                            tag_desc = jsonObject2.getString("tag_desc_ar");
                                        }


                                        TagItemModel productListModel1 = new TagItemModel();

                                        productListModel1.setTag_id(tag_id);
                                        productListModel1.setTag_name(tag_name);
                                        productListModel1.setTag_image(tag_image);
                                        productListModel1.setTag_desc(tag_desc);
                                        productListModel1.setTag_create_date(tag_create_date);

                                        productList1.add(productListModel1);
                                        productListModel.setTagItemModels(productList1);
                                       // productListModel.addDataObject(productListModel1);

                                    }

                                    productList.add(productListModel);
                                }


                                if (productList.size() == 0)
                                {
                                    textView_noproduct_found.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    textView_noproduct_found.setVisibility(View.GONE);
                                }

                                productListAdapter = new ProductListAdapter(ProductListScreen.this, (ArrayList<ProductListModel>) productList,(ArrayList<TagItemModel>)productList1);
                                gridView_productList.setAdapter(productListAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }
                            else
                            {
                                finish();
                                Toast.makeText(ProductListScreen.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                Log.d("message",message);
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

                System.out.println("cat_id "+getArgument);

                HashMap<String,String> params = new HashMap<>();
                params.put(Constants.CATEGORY_ID,getArgument);
                params.put(Constants.SORT_BY,sort_by);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sorting, menu);
        MenuItem m =  menu.getItem(1);


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

            case R.id.navigation_sorting:
                doSorting();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void doSorting() {


        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_sorting_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        final AlertDialog alertbox = dialogBuilder.create();

        RadioGroup radioGroup = dialogView.findViewById(R.id.radio_grp);
        ((RadioButton)radioGroup.getChildAt(pos)).setChecked(true);
        ImageView imageView = dialogView.findViewById(R.id.close_imageview);

        System.out.println("checkedOne " + pos);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertbox.dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                View radioButton = radioGroup.findViewById(i);
                index = radioGroup.indexOfChild(radioButton);



                if (index == 0) {

                    if (data.length() != 0) {
                        if (!isConnectd(ProductListScreen.this)) {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        } else {

                            pos = 0;

                            doGetProductList(data, null);
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }
                    }
                }

                 else if (index == 1){
                    {

                        if (!isConnectd(ProductListScreen.this))
                        {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        }else
                        {
                            pos = 1;

                            doGetProductList(data,"new_arrival");
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }
                    }



                    }else if (index == 2)
                    {

                        if (!isConnectd(ProductListScreen.this))
                        {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        }else
                        {
                            pos = 2;
                            doGetProductList(data,"price_low_to_high");
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }




                    }else if (index == 3)
                    {

                        if (!isConnectd(ProductListScreen.this))
                        {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        }else
                        {
                            pos = 3;
                            doGetProductList(data,"price_high_to_low");
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }




                    }else if (index == 4)
                    {

                        if (!isConnectd(ProductListScreen.this))
                        {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        }else
                        {
                            pos = 4;
                            doGetProductList(data,"name_a_to_z");
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }




                    }else if (index == 5)
                    {

                        if (!isConnectd(ProductListScreen.this))
                        {
                            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                            alertbox.dismiss();

                        }else
                        {
                            pos = 5;
                            doGetProductList(data,"name_z_to_a");
                            alertbox.dismiss();
                            relativeLayout_nonetwork.setVisibility(View.GONE);

                        }


                    }


                    }

        });


        alertbox.show();

    }

    @SuppressLint("RestrictedApi")
    public void showPopup()
    {

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



    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
        overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
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

    @Override
    public void onClick(View view) {

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            recreate();
            relativeLayout_nonetwork.setVisibility(View.GONE);
        } else {
            Toast.makeText(ProductListScreen.this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

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

}
