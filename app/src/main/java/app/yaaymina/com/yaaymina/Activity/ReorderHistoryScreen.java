package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
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

import app.yaaymina.com.yaaymina.Adapter.ReOrderHistoryAdapter;
import app.yaaymina.com.yaaymina.Model.ReOrderHistory;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class ReorderHistoryScreen extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView_reorder;

    private TextView textView_noHistory;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String user_id;

    private Locale myLocale;

    private String language_type;

    private ProgressDialog progressDialog;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<ReOrderHistory> reOrderHistories;

    private ReOrderHistoryAdapter reOrderHistoryAdapter;

    private RelativeLayout relativeLayout_nonetwork;

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
        setContentView(R.layout.activity_reorder_history_screen);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(ReorderHistoryScreen.this);

        recyclerView_reorder = findViewById(R.id.reOrder_recycler);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (ReorderHistoryScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);

                finish();            }
        });

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);

        textView_noHistory = findViewById(R.id.no_order_found);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);


        if (isConnectd(this))
        {
            if (user_id.length()!=0)
            {
                doGetReorderHistory(user_id);
                relativeLayout_nonetwork.setVisibility(View.GONE);
                recyclerView_reorder.setVisibility(View.VISIBLE);
            }
        }else
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            recyclerView_reorder.setVisibility(View.GONE);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isConnectd(ReorderHistoryScreen.this))
                {
                    if (user_id.length()!=0)
                    {
                        doGetReorderHistory(user_id);
                        relativeLayout_nonetwork.setVisibility(View.GONE);
                        recyclerView_reorder.setVisibility(View.VISIBLE);
                    }
                }else
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_reorder.setVisibility(View.GONE);
                }


            }
        });


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        reOrderHistories = new ArrayList<>();

        changeLang(language_type);
    }

    private void doGetReorderHistory(final String user_id) {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.REORDER_HISTORY_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("historyresponse "+response);
                        reOrderHistories.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String ststus = jsonObject.getString("status");

                            if (ststus.equalsIgnoreCase("Success"))
                            {

                                progressDialog.dismiss();

                                JSONArray jsonArray = jsonObject.getJSONArray("order_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String schedule_id = jsonObject1.getString("schedule_id");
                                    String order_date = jsonObject1.getString("order_created_date");
                                    String total = jsonObject1.getString("total");
                                    String order_id = jsonObject1.getString("order_id");


                                    ReOrderHistory reOrderHistory = new ReOrderHistory();

                                    reOrderHistory.setSchedule_id(schedule_id);
                                    reOrderHistory.setDate(order_date);
                                    reOrderHistory.setTotal_amount(total);
                                    reOrderHistory.setOrder_id(order_id);

                                    reOrderHistories.add(reOrderHistory);
                                }

                                reOrderHistoryAdapter = new ReOrderHistoryAdapter(ReorderHistoryScreen.this,reOrderHistories);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_reorder.setLayoutManager(mLayoutManager);
                                recyclerView_reorder.setAdapter(reOrderHistoryAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }
                            else
                            {
                                progressDialog.dismiss();
                                if (reOrderHistories.size() == 0)
                                {
                                    textView_noHistory.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    textView_noHistory.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
        (ReorderHistoryScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
