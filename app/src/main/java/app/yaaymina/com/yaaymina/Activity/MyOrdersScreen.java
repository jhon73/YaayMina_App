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
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.MyAddressAdapter;
import app.yaaymina.com.yaaymina.Adapter.MyOrderAdapter;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.MyOrderModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class MyOrdersScreen extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private ArrayList<MyOrderModel> myOrderModels;

    private RecyclerView recyclerView_myOrder;
    private MyOrderAdapter myOrderAdapter;

    private LinearLayout linearLayout_container;
    private FrameLayout frameLayout_progress;
    private String language_type;

    private Locale myLocale;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progressDialog;

    private TextView textView_noOrders;

    private LinearLayout main_linearLayout;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;

    private NestedScrollView nestedScrollView;

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
        setContentView(R.layout.activity_my_orders_screen);

        progressDialog = new ProgressDialog(MyOrdersScreen.this);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

        setupBottomNavigation();

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        recyclerView_myOrder = findViewById(R.id.recyclerView_adapter);
        frameLayout_progress = findViewById(R.id.progress_frame);
        linearLayout_container = findViewById(R.id.layout_container);

        textView_noOrders = findViewById(R.id.no_order_found);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);

        nestedScrollView = findViewById(R.id.nested_scroll);

        button_nointernet.setOnClickListener(this);

        linearLayout_container.setVisibility(View.GONE);
        frameLayout_progress.setVisibility(View.VISIBLE);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (MyOrdersScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                finish();            }
        });

        myOrderModels = new ArrayList<>();

        if (user_id.length()!=0)
        {

            if (!isConnectd(this))
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);

            }else
            {
                doGetOrderData(user_id);
                relativeLayout_nonetwork.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);

            }
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isConnectd(MyOrdersScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);

                }else
                {
                    doGetOrderData(user_id);
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);

                }
            }
        });

        changeLang(language_type);
    }

    private void doGetOrderData(final String user_id) {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ORDER_HISTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        myOrderModels.clear();

                        frameLayout_progress.setVisibility(View.GONE);
                        linearLayout_container.setVisibility(View.VISIBLE);

                        System.out.println("Myorderresponse "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status_ = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status_.equalsIgnoreCase("Success"))
                            {

                                progressDialog.dismiss();

                                JSONArray jsonArray = jsonObject.getJSONArray("order_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String order_id = jsonObject1.getString("order_id");
                                    String order_date = jsonObject1.getString("order_date");
                                    String total = jsonObject1.getString("total");
                                    String status = jsonObject1.getString("status");

                                    MyOrderModel myOrderModel = new MyOrderModel();

                                    myOrderModel.setOrder_id(order_id);
                                    myOrderModel.setOrder_date(order_date);
                                    myOrderModel.setTotal(total);
                                    myOrderModel.setStatus(status);

                                    myOrderModels.add(myOrderModel);
                                }

                                if (myOrderModels.size() == 0)
                                {
                                    textView_noOrders.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    textView_noOrders.setVisibility(View.GONE);
                                }

                                myOrderAdapter = new MyOrderAdapter(MyOrdersScreen.this,myOrderModels);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_myOrder.setLayoutManager(mLayoutManager);
                                recyclerView_myOrder.setAdapter(myOrderAdapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                if (myOrderModels.size() == 0)
                                {
                                    textView_noOrders.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    textView_noOrders.setVisibility(View.GONE);
                                }
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

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

        if (!isConnectd(this))
        {
            nestedScrollView .setVisibility(View.GONE);
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);

            Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
        }
        else
        {
            nestedScrollView .setVisibility(View.VISIBLE);
            relativeLayout_nonetwork.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
        (MyOrdersScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
