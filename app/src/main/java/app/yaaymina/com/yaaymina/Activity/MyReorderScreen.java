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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.ReOrderAdapter;
import app.yaaymina.com.yaaymina.CommonInterface.OnCompleteListener;
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.ReOrder;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class MyReorderScreen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, OnCompleteListener {

    private BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView_reOrder;

    private Button button_place_order,button_shopBtn;

    private EditText editText_schedule, editText_date, editText_time, editText_days;

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences, login_sharedPref;
    private SharedPreferences.Editor editor, login_editor;

    private String user_id, order_id, date_, itemPos, itemval, custom_days;
    private String language_type;

    private ArrayList<ReOrder> reOrderArrayList;

    ReOrderAdapter reOrderAdapter;
    private Double price;
    private Double weight;

    public String date, time, itemValue;
    int itemPosition;
    private int type;

    private Toolbar toolbar;
    private Locale myLocale;
    private Double totalamount;
    private String product_name, product_description;
    String dayValue;
    private TextView textView_total, reorder_label;

    private LinearLayout linearLayout_custom_days;

    UnitInterFace unitInterFace;

    private FrameLayout frameLayout_empty_cart;
    private LinearLayout linearLayout_fillCart;

    private LinearLayout linearLayout_main;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork;

    private FragmentManager manager;


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
        setContentView(R.layout.activity_my_reorder_screen);

        progressDialog = new ProgressDialog(MyReorderScreen.this);

        textView_total = findViewById(R.id.total_amount);
        reorder_label = findViewById(R.id.textLabel);

        linearLayout_custom_days = findViewById(R.id.custom_layout);

        Intent intent = getIntent();

        order_id = intent.getStringExtra("order_id");
        type = intent.getIntExtra("type", 0);

        if (type==0)
        {
            reorder_label.setText(R.string.calendar_delivary);

        }else if (type==1)
        {
            reorder_label.setText(R.string.time_delivery);

        }else if (type==2)
        {
            reorder_label.setText(R.string.repeat_automatically_every);

        }

        date = "";
        time = "";
        dayValue = "";

        reOrderArrayList = new ArrayList<>();

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_cart).setChecked(true);

        recyclerView_reOrder = findViewById(R.id.reOrderRecycler);

        editText_schedule = findViewById(R.id.edit_schedule_type);
        button_place_order = findViewById(R.id.place_order_btn);
        editText_date = findViewById(R.id.select_date);
        editText_time = findViewById(R.id.select_time);
        editText_days = findViewById(R.id.select_days);
        button_shopBtn = findViewById(R.id.shopBtn);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        button_nointernet = findViewById(R.id.button_tryagain);
        linearLayout_main = findViewById(R.id.layout_mainlayer);

        button_nointernet.setOnClickListener(this);

        button_shopBtn.setOnClickListener(this);

        frameLayout_empty_cart = findViewById(R.id.empty_cart);
        linearLayout_fillCart = findViewById(R.id.filled_cart);

        editText_days.setOnClickListener(this);

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");

        toolbar = findViewById(R.id.toolbar_main);

        manager = getSupportFragmentManager();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
                (MyReorderScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }
        });


        button_place_order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (type == 0) {
                    if (date.equalsIgnoreCase("")) {
                        editText_schedule.setError(getString(R.string.date_selection));
                    } else {


                        doScheduleOrder(date, user_id, itemValue, order_id, custom_days, totalamount);

                    }

                } else if (type == 1) {
                    if (time.equalsIgnoreCase("")) {
                        editText_schedule.setError(getString(R.string.time_selection));
                    } else {

                        doScheduleOrder(date, user_id, itemValue, order_id, custom_days, totalamount);
                    }

                } else if (type == 2)
                {
                    if (date.equalsIgnoreCase(""))
                    {
                        Toast.makeText(MyReorderScreen.this, getString(R.string.date_selection), Toast.LENGTH_SHORT).show();
                    }
                    else if (itemValue.equalsIgnoreCase(""))
                    {
                        editText_schedule.setError(getString(R.string.select_schedule_type));
                    }else
                    {
                        if (itemPosition == 3)
                        {
//                            if (time.equalsIgnoreCase(""))
//                            {
//                                editText_time.setError("Select time");
//                            }
//                            else
                                if (dayValue.equalsIgnoreCase(""))
                            {
                                editText_days.setError(getString(R.string.select_interval_days));
                            }
                            else
                            {
                                doScheduleOrder(date, user_id, itemValue, order_id, custom_days, totalamount);
                            }
                        }
                        else {

                            doScheduleOrder(date, user_id, itemValue, order_id, custom_days, totalamount);
                        }
                    }
                }
            }
        });

        setupBottomNavigation();

        user_id = sharedPreferences.getString(ConstantData.USER_ID, "");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");

        if (user_id.length() != 0) {

            if (isConnectd(this))
            {
                doGetReorderItem(user_id, order_id);
                relativeLayout_nonetwork.setVisibility(View.GONE);
                linearLayout_main.setVisibility(View.VISIBLE);
            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                linearLayout_main.setVisibility(View.GONE);
            }

        }

        editText_schedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Calendar now = Calendar.getInstance();

                if (type == 0) {

                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            MyReorderScreen.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.setMinDate(now);
                    dpd.show(getFragmentManager(), "Datepickerdialog");

                } else if (type == 1) {

                    DialogPickup newFragment = new DialogPickup();
                    newFragment.show(manager, "abc");
                    newFragment.setCancelable(false);

                } else if (type == 2) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyReorderScreen.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.layout_repeat_order, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(true);
                    final AlertDialog alertbox = dialogBuilder.create();

                    String[] testArray = getResources().getStringArray(R.array.re_schedule);

                    final ListView listView = (ListView) dialogView.findViewById(R.id.list);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyReorderScreen.this,
                            android.R.layout.simple_dropdown_item_1line, android.R.id.text1, testArray);


                    // Assign adapter to ListView
                    listView.setAdapter(adapter);

                    // ListView Item Click Listener
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                final int position, long id) {

                            alertbox.dismiss();

                            itemPosition = position;

                            itemValue = (String) listView.getItemAtPosition(position);

                            editText_schedule.setText(itemValue);

                            if (itemPosition == 3) {

                                linearLayout_custom_days.setVisibility(View.VISIBLE);
                                editText_days.setVisibility(View.VISIBLE);


                                editText_date.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                                MyReorderScreen.this,
                                                now.get(Calendar.YEAR),
                                                now.get(Calendar.MONTH),
                                                now.get(Calendar.DAY_OF_MONTH)
                                        );
                                        dpd.setMinDate(now);
                                        dpd.show(getFragmentManager(), "Datepickerdialog");
                                    }
                                });


                            } else {

                                linearLayout_custom_days.setVisibility(View.VISIBLE);
                                editText_date.setVisibility(View.VISIBLE);
                                editText_days.setVisibility(View.GONE);
                                editText_time.setVisibility(View.GONE);

                                DatePickerDialog dpd = DatePickerDialog.newInstance(
                                        MyReorderScreen.this,
                                        now.get(Calendar.YEAR),
                                        now.get(Calendar.MONTH),
                                        now.get(Calendar.DAY_OF_MONTH)
                                );
                                dpd.setMinDate(now);
                                dpd.show(getFragmentManager(), "Datepickerdialog");

                                editText_date.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                                MyReorderScreen.this,
                                                now.get(Calendar.YEAR),
                                                now.get(Calendar.MONTH),
                                                now.get(Calendar.DAY_OF_MONTH)
                                        );
                                        dpd.setMinDate(now);
                                        dpd.show(getFragmentManager(), "Datepickerdialog");
                                    }
                                });
                            }

                        }

                    });

                    alertbox.show();
                }


            }
        });

        if (type == 0) {
            editText_schedule.setHint(getResources().getString(R.string.date_selection));
        } else if (type == 1) {
            editText_schedule.setHint(getResources().getString(R.string.time_selection));
        } else if (type == 2) {
            editText_schedule.setHint(getResources().getString(R.string.select_repeat_schedule));

        }

        unitInterFace = new UnitInterFace() {

            @Override
            public void onTagClicked(String total_price) {

                totalamount = Double.parseDouble(total_price);

                textView_total.setText(String.valueOf(totalamount) + " " +"AED");
                System.out.println("Systotalamount1 "+total_price);

            }


        };


        changeLang(language_type);

    }

    private void doScheduleOrder(final String date, final String user_id, final String itemValue, final String order_id, String custom_days, final Double totalamount) {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PLACE_REORDER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("doscheduleOrder " + response);

                        progressDialog.dismiss();
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {

                                startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
                                finish();

                                Toast.makeText(MyReorderScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else if (status.equalsIgnoreCase("fail"))
                            {


                                Toast.makeText(MyReorderScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            System.out.println("errmsh "+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                    String final_order_id = order_id.substring(1);
                    System.out.println("reOrderData " + user_id + " " + final_order_id + " " + totalamount + " " + date + " " + time + itemValue);

                    params.put(Constants.SCHEDULE_DATE, date);
                    params.put(Constants.USER_ID, user_id);
                    params.put(Constants.ORDER_DETAIL_ID, final_order_id);
                    params.put(Constants.TOTAL, String.valueOf(totalamount));
                    params.put(Constants.TYPE_OF_SCHEDULE, itemValue);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void doGetReorderItem(final String user_id, final String order_id) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.RE_ORDER_CART_DETAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        System.out.println("reOrderres " + response);
                        linearLayout_fillCart.setVisibility(View.VISIBLE);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                String message = jsonObject.getString("message");
                                String total = jsonObject.getString("total");

                                if (total.equalsIgnoreCase("0"))
                                {
                                    Toast.makeText(MyReorderScreen.this, R.string.cant_reorder, Toast.LENGTH_SHORT).show();
                                    button_place_order.setEnabled(false);
                                }
                                else
                                {
                                    totalamount = Double.valueOf(jsonObject.getString("totalamount"));

                                    textView_total.setText(String.valueOf(totalamount) +" "+"AED");

                                }

                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String cart_id = jsonObject1.getString("cart_id");
                                    String user_id = jsonObject1.getString("user_id");
                                    String product_id = jsonObject1.getString("product_id");
                                    String unit = jsonObject1.getString("unit");
                                    String messarment = jsonObject1.getString("messarment");
                                    String cost = jsonObject1.getString("cost");
                                    price = Double.parseDouble(jsonObject1.getString("price"));
                                    String total_ = jsonObject1.getString("total");
                                    String total_grand = jsonObject1.getString("cart_id");
                                    weight = Double.parseDouble(jsonObject1.getString("weight"));
                                    String currency = jsonObject1.getString("currency");
                                    String total_unit = jsonObject1.getString("total_unit");
                                    String total_price = jsonObject1.getString("total_price");
                                    String total_weight = jsonObject1.getString("total_weight");
                                    String category_id = jsonObject1.getString("category_id");
                                    String product_name_ar = jsonObject1.getString("product_name_ar");
                                    String product_code = jsonObject1.getString("product_code");
                                    String product_description_ar = jsonObject1.getString("product_description_ar");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String image_url = jsonObject1.getString("image_url");

                                    if (language_type.equalsIgnoreCase("en")) {

                                        product_name = jsonObject1.getString("product_name");
                                        product_description = jsonObject1.getString("product_description");

                                    } else {

                                        product_name = jsonObject1.getString("product_name_ar");
                                        product_description = jsonObject1.getString("product_description_ar");

                                    }

                                    ReOrder reOrder = new ReOrder();

                                    reOrder.setCart_id(cart_id);
                                    reOrder.setUser_id(user_id);
                                    reOrder.setProduct_id(product_id);
                                    reOrder.setUnit(unit);
                                    reOrder.setMessarment(messarment);
                                    reOrder.setCost(cost);
                                    reOrder.setPrice(price);
                                    reOrder.setTotal_(total_);
                                    reOrder.setTotal_grand(total_grand);
                                    reOrder.setWeight(weight);
                                    reOrder.setCurrency(currency);
                                    reOrder.setTotal_unit(total_unit);
                                    reOrder.setTotal_price(total_price);
                                    reOrder.setTotal_weight(total_weight);
                                    reOrder.setCategory_id(category_id);
                                    reOrder.setProduct_name(product_name);
                                    reOrder.setProduct_name_ar(product_name_ar);
                                    reOrder.setProduct_code(product_code);
                                    reOrder.setProduct_description(product_description);
                                    reOrder.setProduct_description_ar(product_description_ar);
                                    reOrder.setThumbnail(thumbnail);
                                    reOrder.setImage_url(image_url);

                                    reOrderArrayList.add(reOrder);

                                }

                                String final_order_id = order_id.substring(1);

                                reOrderAdapter = new ReOrderAdapter(MyReorderScreen.this, reOrderArrayList, unitInterFace, final_order_id);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_reOrder.setLayoutManager(mLayoutManager);
                                recyclerView_reOrder.setItemAnimator(new DefaultItemAnimator());
                                recyclerView_reOrder.setAdapter(reOrderAdapter);

                            }else if (status.equalsIgnoreCase("fail"))
                            {
                                frameLayout_empty_cart.setVisibility(View.VISIBLE);
                                linearLayout_fillCart.setVisibility(View.GONE);
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

                HashMap<String, String> params = new HashMap<>();

                String final_order_id = order_id.substring(1);

                System.out.println("reoder " + order_id);
                params.put(Constants.USER_ID, user_id);
                params.put(Constants.ORDER_DETAIL_ID, final_order_id);


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
                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                        (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                    case R.id.navigation_cart:

                        if (sharedPreferences.getString(ConstantData.USER_ID, "").equalsIgnoreCase("")) {
                            startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                            (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), CartScreen.class));
                            (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                            finish();
                        }
                        return true;

                    case R.id.navigation_search:
                        startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                        (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                    case R.id.navigation_account:
                        startActivity(new Intent(getApplicationContext(), MyAccountScreen.class));
                        (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

        if (type == 0) {
            editText_schedule.setText(date);
            itemValue = "date";
        } else {
            editText_date.setText(date);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_localization_option, menu);

        MenuItem m = menu.getItem(0);

        if (language_type.equalsIgnoreCase("en")) {
            m.setIcon(R.drawable.ic_english_logo);

        } else if (language_type.equalsIgnoreCase("ar")) {
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
//                    Toast.makeText(HomeScreen.this, "English", Toast.LENGTH_SHORT).show();
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
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }


    @Override
    public void onClick(View view) {

        if (view == editText_days) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyReorderScreen.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.layout_repeat_order, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(true);
            final AlertDialog alertbox = dialogBuilder.create();

            String[] testArray = getResources().getStringArray(R.array.re_schedule_days);

            final ListView listView = (ListView) dialogView.findViewById(R.id.list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyReorderScreen.this,
                    android.R.layout.simple_dropdown_item_1line, android.R.id.text1, testArray);


            // Assign adapter to ListView
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    alertbox.dismiss();

                    int itemPosition = i;

                    dayValue = (String) listView.getItemAtPosition(itemPosition);

                    editText_days.setText(dayValue);
                }
            });

            alertbox.show();
        }
        else if (view == button_shopBtn)
        {
            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
            (MyReorderScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            finish();
        }else if (view == button_nointernet)
        {
            if (isConnectd(this))
            {
                recreate();
                relativeLayout_nonetwork.setVisibility(View.GONE);
                linearLayout_main.setVisibility(View.VISIBLE);
            }
            else
            {
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                linearLayout_main.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onComplete(String time_) {
        time = time_;

        if (type == 1) {
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + " " +time;
            editText_schedule.setText(date);
            itemValue = "time";
        } else {
            editText_time.setText(time);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
        (MyReorderScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
