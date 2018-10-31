package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import app.yaaymina.com.yaaymina.Adapter.CartAdapter;
import app.yaaymina.com.yaaymina.Adapter.ProductSliderAdapter;
import app.yaaymina.com.yaaymina.Adapter.RecomendedProductAdapter;
import app.yaaymina.com.yaaymina.Adapter.TagAdapter;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.Model.CartModelClass;
import app.yaaymina.com.yaaymina.Model.ProductDetailTag;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductDetailScreen extends AppCompatActivity implements View.OnClickListener {


    private TextView textView_prev, textView_next, textView_toolbar_title;
    private TextView textView_product_name, textView_productweight, textView_productPrice;
    private TextView textView_avg_price, textView_desc;
    private Button button_addToCart, button_buyNow;
    private ImageView imageView_plus, imageView_minus;

    private ViewPager viewPager_productDetail;
    private RecyclerView recyclerView_tag, recyclerView_recommended;

    private ProductSliderAdapter homeSliderAdapter;
    private TagAdapter tagAdapter;
    private RecomendedProductAdapter recomendedProductAdapter;

    private Double product_weight, product_price, global_product_price;
    private String next_product_id,privious_product_id;

    private ArrayList<ProductDetailTag> productDetailTagArrayList;

    private ArrayList<ProductListModel> productListModelsArrayList;
    private List<TagItemModel> productList1;
    private ArrayList<String> sliderList;

    private String product_currency;
    private ImageView imageView_productprev, imageView_productnxt,imageView_productback;

    private android.support.v7.widget.Toolbar toolbar_productdetail;

    private LinearLayout linearLayout_dots;

    private TextView[] dots;

    private Handler handler;
    private final int delay = 2000;
    private int page = 0 , unit = 1;

    private ProgressDialog progressDialog;

    private LinearLayout linearLayout_next, linearLayout_previous;
    String user_id,category_id, product_id_, measurement_;
    Double pro_interval;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type;
    private String selected_product_name, selected_product_desc, selected_category_title;
    private String main_product_name, main_product_desc, main_category_title;

    private ArrayList<CartModelClass> cartModelClassArrayList;

    private FrameLayout frameLayout_progredssBar;
    private NestedScrollView nestedScrollView;

    private RecyclerView recyclerView_cart;
    private CartAdapter cartAdapter;

    private SharedPreferences login_sharedPref;
    private SharedPreferences.Editor  login_editor;

    private TextView textView_measurement;

    private Locale myLocale;
    private String privious_product_name,next_product_name;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;

    TextView textCartItemCount;
    int mCartItemCount = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

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
        setContentView(R.layout.layout_product_detail_screen_);

        page = 0;
        cartModelClassArrayList = new ArrayList<>();

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(SharedPref.USER_ID, "");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        System.out.println("userId "+user_id);

        Intent intent = getIntent();
        final String product_id = intent.getStringExtra("product_id");
        category_id = intent.getStringExtra("category_id");

        progressDialog = new ProgressDialog(ProductDetailScreen.this);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        if (product_id.length()!=0)
        {

            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);

            }else
            {
                doGetProductDetail(product_id);
                doGetRecommendedDetail(category_id,product_id);
                GetCartItem();
                relativeLayout_nonetwork.setVisibility(View.GONE);

            }


        }
        else
        {
            Log.d("message ","null");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (product_id.length()!=0)
                {

                    if (!isConnectd(ProductDetailScreen.this))
                    {
                        relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                    }else
                    {
                        doGetProductDetail(product_id);
                        doGetRecommendedDetail(category_id,product_id);
                        GetCartItem();
                        relativeLayout_nonetwork.setVisibility(View.GONE);

                    }


                }
                else
                {
                    Log.d("message ","null");
                }


            }
        });

        handler = new Handler();

        productDetailTagArrayList = new ArrayList<>();
        productListModelsArrayList = new ArrayList<>();
        productList1 = new ArrayList<>();
        sliderList = new ArrayList<>();

        linearLayout_dots = findViewById(R.id.ll_dots);
        recyclerView_tag = findViewById(R.id.productDetail_recyclerView);
        recyclerView_recommended = findViewById(R.id.productDetail_recommended_recyclerView);
        imageView_plus = findViewById(R.id.productDetail_plus);
        imageView_minus = findViewById(R.id.productDetail_minus);

        linearLayout_next = findViewById(R.id.productDetail_next);
        linearLayout_previous = findViewById(R.id.productDetail_prev);

        frameLayout_progredssBar = findViewById(R.id.proGressLayout);
        nestedScrollView = findViewById(R.id.nestedScroll);

//        nestedScrollView.setVisibility(View.GONE);
//        frameLayout_progredssBar.setVisibility(View.VISIBLE);


        button_nointernet.setOnClickListener(this);

        toolbar_productdetail = findViewById(R.id.toolbar_native);
        toolbar_productdetail.setTitle("");

        setSupportActionBar(toolbar_productdetail);

        toolbar_productdetail.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar_productdetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        textView_prev = findViewById(R.id.productDetail_prevText);
        textView_next = findViewById(R.id.productDetail_nextText);
        textView_product_name = findViewById(R.id.productDetail_title);
        textView_productweight = findViewById(R.id.productDetail_weight);
        textView_productPrice = findViewById(R.id.productDetail_price);
        textView_avg_price = findViewById(R.id.productDetail_avgPrice);
        textView_desc = findViewById(R.id.productDetail_description);
        textView_toolbar_title = findViewById(R.id.title_productDetail);
        textView_measurement = findViewById(R.id.textview_measurement);

        imageView_productnxt = findViewById(R.id.productDetail_nextImg);
        imageView_productprev = findViewById(R.id.productDetail_prevImg);
//        imageView_productback = findViewById(R.id.back_productDetail);

        button_addToCart = findViewById(R.id.productDetail_addtocart);
        button_buyNow = findViewById(R.id.productDetail_buynow);

        button_addToCart.setOnClickListener(this);
        button_buyNow.setOnClickListener(this);
        imageView_plus.setOnClickListener(this);
        imageView_minus.setOnClickListener(this);
        linearLayout_previous.setOnClickListener(this);
        linearLayout_next.setOnClickListener(this);

        viewPager_productDetail = findViewById(R.id.productDetail_viewPager);

        viewPager_productDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    private void doGetRecommendedDetail(final String category_id, final String is_product_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PRODUCT_DETAIL_CATEGORY_ID_BASED_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                        progressDialog.dismiss();
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
                                        selected_product_desc = jsonObject1.getString("product_description");


                                    }else if (language_type.equalsIgnoreCase("ar"))
                                    {
                                        selected_category_title = jsonObject1.getString("category_title_ar");
                                        selected_product_name= jsonObject1.getString("product_name_ar");
                                        selected_product_desc = jsonObject1.getString("product_description_ar");
                                    }

                                    ProductListModel productListModel = new ProductListModel();
                                    productListModel.setProduct_id(product_id);
                                    productListModel.setCategory_id(category_id);
                                    productListModel.setProduct_code(product_code);
                                    productListModel.setProduct_tag(product_tag);
                                    productListModel.setProduct_name(selected_product_name);
                                    productListModel.setProduct_rating(product_rating);
                                    productListModel.setProduct_description(selected_product_desc);
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

                                    productListModelsArrayList.add(productListModel);
                                }

                                System.out.println("productlistArraylist "+productListModelsArrayList.size());


                                recomendedProductAdapter = new RecomendedProductAdapter(ProductDetailScreen.this,productListModelsArrayList,is_product_id);
                                recyclerView_recommended.setLayoutManager(new LinearLayoutManager(ProductDetailScreen.this, LinearLayoutManager.HORIZONTAL, false));
                                recyclerView_recommended.setAdapter(recomendedProductAdapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            else
                            {
                                Toast.makeText(ProductDetailScreen.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                Log.d("message",message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.CATEGORY_ID,category_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailScreen.this);
        requestQueue.add(stringRequest);

    }

    private void doGetProductDetail(final String product_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PRODUCT_DETAIL_PRODUCT_ID_BASED_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

//                        nestedScrollView.setVisibility(View.VISIBLE);
//                        frameLayout_progredssBar.setVisibility(View.GONE);

                        System.out.println("resose "+response);
                        sliderList.clear();
                        productDetailTagArrayList.clear();
//                        productListModelsArrayList.clear();
//                        progressDialog1.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("product_info");

                                System.out.println("ProductdetialRespone "+jsonObject1);

                                product_id_ = jsonObject1.getString("product_id");
                                String category_id = jsonObject1.getString("category_id");
                                String product_code = jsonObject1.getString("product_code");
                                String product_tag = jsonObject1.getString("product_tag");
                                String product_name = jsonObject1.getString("product_name");
                                String product_rating = jsonObject1.getString("product_rating");
                                String product_description = jsonObject1.getString("product_description");
                                String product_overview = jsonObject1.getString("product_overview");
                                String product_specifications = jsonObject1.getString("product_specifications");
                                product_weight = Double.valueOf(jsonObject1.getString("product_weight"));
                                String product_minorder = jsonObject1.getString("product_minorder");
                                String color_option = jsonObject1.getString("color_option");
                                String size_option = jsonObject1.getString("size_option");
                                String product_cost = jsonObject1.getString("product_cost");
                                String product_tax = jsonObject1.getString("product_tax");
                                product_price = Double.valueOf(jsonObject1.getString("product_price"));
                                String product_price_cross = jsonObject1.getString("product_price_cross");
                                product_currency = jsonObject1.getString("product_currency");
                                String product_availability = jsonObject1.getString("product_availability");
                                String product_status = jsonObject1.getString("product_status");
                                String thumbnail = jsonObject1.getString("thumbnail");
                                String product_updated = jsonObject1.getString("product_updated");
                                String product_entered = jsonObject1.getString("product_entered");
                                String category_title = jsonObject1.getString("category_title");
                                String category_title_ar = jsonObject1.getString("category_title_ar");
                                String product_name_ar = jsonObject1.getString("product_name_ar");
                                String product_description_ar = jsonObject1.getString("product_description_ar");
                                next_product_id = jsonObject1.getString("next_product_id");
                                privious_product_id = jsonObject1.getString("privious_product_id");
                                measurement_ = jsonObject1.getString("messarment");
                                pro_interval = Double.valueOf((jsonObject1.getString("product_interval")));




                                if (language_type.equalsIgnoreCase("en"))
                                {
                                    main_category_title = jsonObject1.getString("category_title");
                                    main_product_name = jsonObject1.getString("product_name");
                                    main_product_desc = jsonObject1.getString("product_description");
                                    privious_product_name = jsonObject1.getString("privious_product_name");
                                    next_product_name = jsonObject1.getString("next_product_name");



                                }else if (language_type.equalsIgnoreCase("ar"))
                                {
                                    main_category_title = jsonObject1.getString("category_title_ar");
                                    main_product_name= jsonObject1.getString("product_name_ar");
                                    main_product_desc = jsonObject1.getString("product_description_ar");
                                    privious_product_name = jsonObject1.getString("privious_product_name_ar");
                                    next_product_name = jsonObject1.getString("next_product_name_ar");


                                }
                                JSONArray tagArray = jsonObject1.getJSONArray("tag");

                                for (int i = 0; i < tagArray.length(); i ++)
                                {
                                    JSONObject jsonObject2 = tagArray.getJSONObject(i);

                                    String tag_name = null, tag_desc = null;

                                    String tag_id = jsonObject2.getString("tag_id");
                                    String tag_image = jsonObject2.getString("tag_image");
                                    String tag_create_date = jsonObject2.getString("tag_create_date");

                                    if (language_type.equalsIgnoreCase("en"))
                                    {
                                        tag_name = jsonObject2.getString("tag_name");
                                        tag_desc = jsonObject2.getString("tag_desc");


                                    }else if (language_type.equalsIgnoreCase("ar"))
                                    {
                                        tag_name= jsonObject2.getString("tag_name_ar");
                                        tag_desc = jsonObject2.getString("tag_desc_ar");

                                    }

                                    ProductDetailTag productDetailTag = new ProductDetailTag();

                                    productDetailTag.setTag_id(tag_id);
                                    productDetailTag.setTag_name(tag_name);
                                    productDetailTag.setTag_image(tag_image);
                                    productDetailTag.setTag_desc(tag_desc);
                                    productDetailTag.setTag_create_date(tag_create_date);

                                    productDetailTagArrayList.add(productDetailTag);

                                }

                                tagAdapter = new TagAdapter(getApplicationContext(),productDetailTagArrayList);
                                recyclerView_tag.setLayoutManager(new LinearLayoutManager(ProductDetailScreen.this, LinearLayoutManager.HORIZONTAL, true));
                                recyclerView_tag.setAdapter(tagAdapter);

                                JSONArray sliderArray = jsonObject1.getJSONArray("image_url");

                                for (int i = 0; i< sliderArray.length(); i ++)
                                {
                                    sliderList.add((String) sliderArray.get(i));
                                }

                                global_product_price = product_weight * product_price;
                                textView_product_name.setText(main_product_name);
                                textView_desc.setText(main_product_desc);
                                textView_productPrice.setText(product_price + " " + product_currency);
                                textView_toolbar_title.setText(main_category_title);
                                textView_avg_price.setText(String.valueOf(global_product_price) + " " +product_currency);
                                textView_productweight.setText(String.valueOf(product_weight)+" "+measurement_);
                                textView_measurement.setText("Per" + " " + measurement_);


                                if (privious_product_name.length()!=0)
                                {
                                    textView_prev.setText(privious_product_name);
                                    imageView_productprev.setVisibility(View.VISIBLE);
                                    textView_prev.setVisibility(View.VISIBLE);
                                    linearLayout_previous.setVisibility(View.VISIBLE);
                                }else
                                {
                                    linearLayout_previous.setVisibility(View.GONE);
                                }

                                if (next_product_name.length()!=0)
                                {
                                    textView_next.setText(next_product_name);
                                    imageView_productnxt.setVisibility(View.VISIBLE);
                                    textView_next.setVisibility(View.VISIBLE);
                                    linearLayout_next.setVisibility(View.VISIBLE);

                                }else
                                {
                                    linearLayout_next.setVisibility(View.GONE);
                                }

                                homeSliderAdapter = new ProductSliderAdapter(ProductDetailScreen.this, sliderList);
                                viewPager_productDetail.setAdapter(homeSliderAdapter);
                                addBottomDots(0);

                            }else
                            {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog1.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.PRODUCT_ID,product_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addBottomDots(int current_page) {

        dots = new TextView[sliderList.size()];

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

            if (isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                if (homeSliderAdapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager_productDetail.setCurrentItem(page, true);
                handler.postDelayed(this, delay);

            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            }

        }

    };

    @Override
    public void onResume() {
        super.onResume();

        if (isConnectd(ProductDetailScreen.this))
        {
            relativeLayout_nonetwork.setVisibility(View.GONE);
            handler.postDelayed(runnable, delay);

        }
        else
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isConnectd(ProductDetailScreen.this))
        {
            relativeLayout_nonetwork.setVisibility(View.GONE);
            handler.removeCallbacks(runnable);

        }
        else
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        if (view == button_addToCart)
        {

            if (isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                doAddToCart(user_id,product_id_,global_product_price,product_weight,unit);

            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            }

        }else if (view == button_buyNow)
        {

            if (isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                doBuyNow(user_id,product_id_,global_product_price,product_weight,unit);
                editor.putString(ConstantData.CART_TOTAL, String.valueOf(global_product_price)).apply();


            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            }



        }else if (view == imageView_plus)
        {
             unit = (int) (unit + pro_interval);
             product_weight = product_weight + pro_interval;
             global_product_price = product_weight * product_price;

             textView_productweight.setText(String.valueOf(product_weight)+" "+measurement_);
             textView_avg_price.setText(String.valueOf(global_product_price) + " " +product_currency);


        }else if (view == imageView_minus)
        {
            if (product_weight.equals(pro_interval))
            {
               Log.d("weight", "donothing");
            }
            else
            {
                product_weight = product_weight - pro_interval;
                global_product_price = product_weight * product_price;
                unit = (int) (unit - pro_interval);

                textView_productweight.setText(String.valueOf(product_weight)+" "+measurement_);
                textView_avg_price.setText(String.valueOf(global_product_price) + " " +product_currency);

            }

        }
        else if (view == linearLayout_next)
        {
//            frameLayout_progredssBar.setVisibility(View.VISIBLE);
//            nestedScrollView.setVisibility(View.GONE);

            if (isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                doGetProductDetail(next_product_id);
                GetCartItem();

            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            }
//           doGetNextRecommendedDetail(category_id);
        }else if (view == linearLayout_previous)
        {
//            frameLayout_progredssBar.setVisibility(View.VISIBLE);
//            nestedScrollView.setVisibility(View.GONE);

            if (isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.GONE);
                doGetProductDetail(privious_product_id);
                GetCartItem();

            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            }


        }else if ( view == button_nointernet)
        {
            if (!isConnectd(ProductDetailScreen.this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                Toast.makeText(this, getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
            }else
            {
                recreate();
                relativeLayout_nonetwork.setVisibility(View.GONE);
            }
        }
    }

    private void GetCartItem() {

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
                                setupBadge(total);

                            }
                            else
                            {
                                setupBadge("0");
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

    private void doBuyNow(final String user_id, final String product_id_, final Double global_product_price, final Double product_weight, final int unit) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ADD_TO_CART_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("cartResponse "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                startActivity(new Intent(getApplicationContext(),CheckOutActivity.class));
                                (ProductDetailScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);



                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i ++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String cart_id = jsonObject1.getString("cart_id");
                                    String cost = jsonObject1.getString("cost");
                                    String currency = jsonObject1.getString("currency");
                                    String entered_date = jsonObject1.getString("entered");
                                    String options = jsonObject1.getString("options");
                                    String price = jsonObject1.getString("price");
                                    String product_id = jsonObject1.getString("product_id");
                                    String session_id = jsonObject1.getString("session_id");
                                    String tax = jsonObject1.getString("tax");
                                    String total = jsonObject1.getString("total");
                                    String total_grand = jsonObject1.getString("total_grand");
                                    String total_price = jsonObject1.getString("total_price");
                                    String total_tax = jsonObject1.getString("total_tax");
                                    String total_unit = jsonObject1.getString("total_unit");
                                    String total_weight = jsonObject1.getString("total_weight");
                                    String unit = jsonObject1.getString("unit");
                                    String updated = jsonObject1.getString("updated");
                                    String user_id = jsonObject1.getString("user_id");
                                    String weight = jsonObject1.getString("weight");

                                    CartModelClass cartModelClass = new CartModelClass();
                                    cartModelClass.setCart_id(cart_id);
                                    cartModelClass.setCost(cost);
                                    cartModelClass.setCurrency(currency);
                                    cartModelClass.setEntered(entered_date);
                                    cartModelClass.setOptions(options);
                                    cartModelClass.setPrice(price);
                                    cartModelClass.setProduct_id(product_id);
                                    cartModelClass.setSession_id(session_id);
                                    cartModelClass.setTax(tax);
                                    cartModelClass.setTotal(total);
                                    cartModelClass.setTotal_grand(total_grand);
                                    cartModelClass.setTotal_price(total_price);
                                    cartModelClass.setTotal_tax(total_tax);
                                    cartModelClass.setTotal_unit(total_unit);
                                    cartModelClass.setWeight(total_weight);
                                    cartModelClass.setUnit(unit);
                                    cartModelClass.setUpdated(updated);
                                    cartModelClass.setUser_id(user_id);
                                    cartModelClass.setWeight(weight);

                                }
                            }
                            else
                            {
                                Toast.makeText(ProductDetailScreen.this, message, Toast.LENGTH_SHORT).show();
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

                params.put(Constants.USER_ID,user_id);
                params.put(Constants.PRODUCT_ID,product_id_);
                params.put(Constants.TOTAL_PRICE, String.valueOf(global_product_price));
                params.put(Constants.PRODUCT_WEIGHT, String.valueOf(product_weight));
                params.put(Constants.UNIT, String.valueOf(unit));
                params.put(Constants.MEASUREMENT, measurement_);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailScreen.this);
        requestQueue.add(stringRequest);

    }

    private void doAddToCart(final String user_id, final String product_id_, final Double global_product_price, final Double product_weight, final int unit) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();

        System.out.println("product_id "+product_id_);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ADD_TO_CART_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("cartResponse "+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
//                                startActivity(new Intent(getApplicationContext(),CheckOutActivity.class));
                                finish();

                               JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                               System.out.println("jsonArrylen "+jsonArray.length() );
                               for (int i = 0; i < jsonArray.length(); i ++)
                               {
                                   JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                   String cart_id = jsonObject1.getString("cart_id");
                                   String cost = jsonObject1.getString("cost");
                                   String currency = jsonObject1.getString("currency");
                                   String entered_date = jsonObject1.getString("entered");
                                   String options = jsonObject1.getString("options");
                                   String price = jsonObject1.getString("price");
                                   String product_id = jsonObject1.getString("product_id");
                                   String session_id = jsonObject1.getString("session_id");
                                   String tax = jsonObject1.getString("tax");
                                   String total = jsonObject1.getString("total");
                                   String total_grand = jsonObject1.getString("total_grand");
                                   String total_price = jsonObject1.getString("total_price");
                                   String total_tax = jsonObject1.getString("total_tax");
                                   String total_unit = jsonObject1.getString("total_unit");
                                   String total_weight = jsonObject1.getString("total_weight");
                                   String unit = jsonObject1.getString("unit");
                                   String updated = jsonObject1.getString("updated");
                                   String user_id = jsonObject1.getString("user_id");
                                   String weight = jsonObject1.getString("weight");

                                   CartModelClass cartModelClass = new CartModelClass();
                                   cartModelClass.setCart_id(cart_id);
                                   cartModelClass.setCost(cost);
                                   cartModelClass.setCurrency(currency);
                                   cartModelClass.setEntered(entered_date);
                                   cartModelClass.setOptions(options);
                                   cartModelClass.setPrice(price);
                                   cartModelClass.setProduct_id(product_id);
                                   cartModelClass.setSession_id(session_id);
                                   cartModelClass.setTax(tax);
                                   cartModelClass.setTotal(total);
                                   cartModelClass.setTotal_grand(total_grand);
                                   cartModelClass.setTotal_price(total_price);
                                   cartModelClass.setTotal_tax(total_tax);
                                   cartModelClass.setTotal_unit(total_unit);
                                   cartModelClass.setWeight(total_weight);
                                   cartModelClass.setUnit(unit);
                                   cartModelClass.setUpdated(updated);
                                   cartModelClass.setUser_id(user_id);
                                   cartModelClass.setWeight(weight);

                               }
                            }
                            else
                            {
                                Toast.makeText(ProductDetailScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                params.put(Constants.USER_ID,user_id);
                params.put(Constants.PRODUCT_ID,product_id_);
                params.put(Constants.TOTAL_PRICE, String.valueOf(global_product_price));
                params.put(Constants.PRODUCT_WEIGHT, String.valueOf(product_weight));
                params.put(Constants.UNIT, String.valueOf(unit));
                params.put(Constants.MEASUREMENT, measurement_);

                return params;
        }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailScreen.this);
        requestQueue.add(stringRequest);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);

        final MenuItem menuItem = menu.findItem(R.id.navigation_sorting);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        GetCartItem();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

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

                if (user_id.equalsIgnoreCase(""))
                {
                    startActivity(new Intent(getApplicationContext(),LoginScreen.class));
                    (ProductDetailScreen.this).overridePendingTransition(R.anim.rotate_left_to_right, R.anim.stable);

                    finish();
                }else
                {
                    showCartPopUp();
                }


                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCartPopUp() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProductDetailScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_cart_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        final AlertDialog alertbox = dialogBuilder.create();

        LinearLayout linearLayout_fillCart = dialogView.findViewById(R.id.filled_cart);
        FrameLayout frameLayout_empty_cart = dialogView.findViewById(R.id.empty_cart);

        FrameLayout frameLayout_progress = dialogView.findViewById(R.id.progress_framelayout);
//        NestedScrollView nestedScrollView = dialogView.findViewById(R.id.nestedScroll);

        TextView textView = dialogView.findViewById(R.id.total_textView);
        ImageView imageView = dialogView.findViewById(R.id.close_imageview);

        RecyclerView recyclerView = dialogView.findViewById(R.id.cartRecycler);

        Button button = dialogView.findViewById(R.id.shopBtn);
        Button button_viewCart = dialogView.findViewById(R.id.view_cart_btn);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                (ProductDetailScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                finish();
            }
        });

        button_viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),CartScreen.class));
                (ProductDetailScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertbox.dismiss();
            }
        });

        if (!isConnectd(ProductDetailScreen.this))
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
        }else
        {
            doGetCartItem(frameLayout_progress,nestedScrollView,frameLayout_empty_cart,linearLayout_fillCart,textView,recyclerView);
            relativeLayout_nonetwork.setVisibility(View.GONE);
        }

        alertbox.show();
    }

    private void doGetCartItem(final FrameLayout frameLayout_progress, NestedScrollView nestedScrollView, final FrameLayout frameLayout_empty_cart, final LinearLayout linearLayout_fillCart, final TextView textView, final RecyclerView recyclerView) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+Constants.CART_PRODUCT_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Double price,weight;
                        String product_name,product_description;

                        ArrayList<CartItemModel> cartItemModelArrayList = new ArrayList<>();

                        System.out.println("CartResponse "+response);
                        frameLayout_progress.setVisibility(View.GONE);
//                        ProductDetailScreen.this.nestedScrollView.setVisibility(View.VISIBLE);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                linearLayout_fillCart.setVisibility(View.VISIBLE);
                                frameLayout_empty_cart.setVisibility(View.GONE);

                                String message = jsonObject.getString("message");
                                String total = jsonObject.getString("total");
                                Double totalamount = Double.valueOf(jsonObject.getString("totalamount"));


                                textView.setText(String.valueOf(totalamount) + " " + "AED");

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

                                cartAdapter = new CartAdapter(ProductDetailScreen.this,cartItemModelArrayList, null,"dialog");
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(cartAdapter);
                                Log.d("TAG1","do nothing");

                            } else if (status.equalsIgnoreCase("fail"))
                            {
                                frameLayout_empty_cart.setVisibility(View.VISIBLE);
                                linearLayout_fillCart.setVisibility(View.GONE);

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
//                ProductDetailScreen.this.nestedScrollView.setVisibility(View.VISIBLE);
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

    private void setupBadge(String total) {

        System.out.println("totalVountbage " + total);

        if (total.equalsIgnoreCase(""))
        {

            textCartItemCount.setText("0");
        }
        else
        {
            textCartItemCount.setText(total);
        }


    }


}
