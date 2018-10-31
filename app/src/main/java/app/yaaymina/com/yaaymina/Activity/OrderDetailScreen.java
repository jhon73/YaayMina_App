package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.OrderDetailAdapter;
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.OrderDetail;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class OrderDetailScreen extends AppCompatActivity {

    private Toolbar toolbar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String user_id, order_id, final_order_id;

    private TextView textView_order_status, textView_order_id, textView_address, textView_total_amount, textView_total_cost;
    private TextView textView_paymentMethod, textView_paymentStatus, textView_discount;

    private ArrayList<OrderDetail> orderDetailArrayList;

    private RecyclerView recyclerView_order_detail;

    private OrderDetailAdapter orderDetailAdapter;

    private String language_type;
    private Locale myLocale;

    UnitInterFace unitInterFace;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_screen);

        sharedPreferences = getApplicationContext().getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        Intent intent = getIntent();

        order_id = intent.getStringExtra("order_id");
        final_order_id = order_id.substring(1);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
                (OrderDetailScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }

        });

        orderDetailArrayList = new ArrayList<>();

        textView_order_id = findViewById(R.id.order_id_tv);
        textView_order_status = findViewById(R.id.orderStatus_tv);
        textView_address = findViewById(R.id.address_textview);
        textView_total_amount = findViewById(R.id.order_total_textview);
        textView_paymentStatus = findViewById(R.id.paymentStts_textview);
        textView_paymentMethod = findViewById(R.id.paymentMethod_textview);
        textView_total_cost = findViewById(R.id.totalCost_textView);
        textView_discount = findViewById(R.id.discount_textview);

        recyclerView_order_detail = findViewById(R.id.recycler_order_detail);

        if (user_id.length()!=0)
        {
            doGetOrderDetails(user_id,final_order_id);
        }

        changeLang(language_type);
    }

    private void doGetOrderDetails(final String user_id, final String order_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ORDER_DETAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("detailres "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("order_info");

                                String order_id = jsonObject1.getString("id");
                                String member_id = jsonObject1.getString("member_id");
                                String cost = jsonObject1.getString("cost");
                                String total = jsonObject1.getString("total");
                                String currency = jsonObject1.getString("currency");
                                String delivery_time = jsonObject1.getString("delivery_time");
                                String payment_type = jsonObject1.getString("payment_type");
                                String payment_status = jsonObject1.getString("payment_status");
                                String payment_entered = jsonObject1.getString("payment_entered");
                                String note = jsonObject1.getString("note");
                                String coupon_code = jsonObject1.getString("coupon_code");
                                String order_status = jsonObject1.getString("order_status");
                                String schedule_date = jsonObject1.getString("schedule_date");
                                String discount_amount = jsonObject1.getString("discount_amount");

                                String CurrentString = schedule_date;
                                String[] separated = CurrentString.split("-");
                                String year = separated[0];
                                String month = separated[1];
                                String date = separated[2];

                                String month_name = getMonthShortName(Integer.parseInt(month));

                                System.out.println("currentString "+ month_name +" " + month);

                                textView_order_status.setText(date+"-"+month+"-"+year);
                                textView_order_id.setText(order_id);
                                textView_total_amount.setText(total + " " + currency);
                                textView_total_cost.setText(cost + " " + currency);
                                textView_discount.setText(discount_amount + " " + currency);
                                textView_paymentStatus.setText(order_status);
                                textView_paymentMethod.setText(payment_type);

                                JSONObject jsonObject3 = jsonObject1.getJSONObject("customer_delivery");

                                System.out.println("SystemData " + jsonObject3);

//                                String user_id = jsonObject3.getString("user_id");
                                String firstname_ = jsonObject3.getString("firstname");
                                String lastname_ = jsonObject3.getString("lastname");
                                String address1 = jsonObject3.getString("address1");
                                String address2 = jsonObject3.getString("address2");
                                String building_flatno = jsonObject3.getString("building_flatno");
                                String phone_no = jsonObject3.getString("phone_no");
                                String city = jsonObject3.getString("city");
                                String community_area = jsonObject3.getString("community_area");
                                String addressid = jsonObject3.getString("addressid");

                                System.out.println("addressDet "+ building_flatno +" "+address1 + " " +address2 + "\n" + community_area + " " + city + " " +phone_no);
                                System.out.println("addressfirstname "+ firstname_);

                                textView_address.setText(firstname_ + " "+ lastname_ +"\n"+building_flatno +" "+address1 + " " +address2 + "\n" + community_area + " " + city + " " +phone_no);

                                JSONArray jsonArray = jsonObject1.getJSONArray("item");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                                    String product_id = jsonObject2.getString("product_id");
                                    String cart_id = jsonObject2.getString("cart_id");
                                    String cost_in = jsonObject2.getString("cost");
                                    String currency_in = jsonObject2.getString("currency");
                                    String discount = jsonObject2.getString("discount");
                                    String entered = jsonObject2.getString("entered");
                                    String image_url = jsonObject2.getString("image_url");
                                    String messarment = jsonObject2.getString("messarment");
                                    String price = jsonObject2.getString("price");
                                    String total_in = jsonObject2.getString("total");
                                    String total_cost = jsonObject2.getString("total_cost");
                                    String total_grand = jsonObject2.getString("total_grand");
                                    String total_price = jsonObject2.getString("total_price");
                                    String total_unit = jsonObject2.getString("total_unit");
                                    String total_weight = jsonObject2.getString("total_weight");
                                    String unit = jsonObject2.getString("unit");
                                    String unit_price = jsonObject2.getString("unit_price");
                                    String weight = jsonObject2.getString("weight");


                                    if (language_type.equalsIgnoreCase("en"))
                                    {
                                        name = jsonObject2.getString("name");

                                    }else
                                    {
                                        name = jsonObject2.getString("name_ar");

                                    }

                                    OrderDetail orderDetail = new OrderDetail();

                                    orderDetail.setProduct_id(product_id);
                                    orderDetail.setCart_id(cart_id);
                                    orderDetail.setCost_in(cost_in);
                                    orderDetail.setCurrency_in(currency_in);
                                    orderDetail.setDiscount(discount);
                                    orderDetail.setEntered(entered);
                                    orderDetail.setImage_url(image_url);
                                    orderDetail.setMessarment(messarment);
                                    orderDetail.setName(name);
                                    orderDetail.setPrice(price);
                                    orderDetail.setTotal_in(total_in);
                                    orderDetail.setTotal_grand(total_grand);
                                    orderDetail.setTotal_price(total_price);
                                    orderDetail.setTotal_unit(total_unit);
                                    orderDetail.setTotal_weight(total_weight);
                                    orderDetail.setUnit(unit);
                                    orderDetail.setUnit_price(unit_price);
                                    orderDetail.setWeight(weight);
                                    orderDetail.setTotal_cost(total_cost);

                                    orderDetailArrayList.add(orderDetail);

                                }

                                orderDetailAdapter = new OrderDetailAdapter(OrderDetailScreen.this,orderDetailArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_order_detail.setLayoutManager(mLayoutManager);
                                recyclerView_order_detail.setItemAnimator(new DefaultItemAnimator());
                                recyclerView_order_detail.setAdapter(orderDetailAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("jsonExceptio "+e.getMessage());
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

                System.out.println("user_order_detial "+ order_id);
                params.put(Constants.USER_ID,user_id);
                params.put(Constants.ORDER_DETAIL_ID,order_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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

    public static String getMonthShortName(int monthNumber)
    {
        String monthName="";

        if(monthNumber>=0 && monthNumber<12)
            try
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber-1);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            }
            catch (Exception e)
            {
                if(e!=null)
                    e.printStackTrace();
            }
        return monthName;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MyOrdersScreen.class));
        (OrderDetailScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
