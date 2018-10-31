package app.yaaymina.com.yaaymina.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class ThankyouScreen extends AppCompatActivity {

    private TextView textView;
    private WebView webView;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private RelativeLayout relativeLayout_nonetwork;
    private Button retry_btn;
    Intent intent;

    private SharedPreferences sharedPreferences;

    private String currenturl, order_id, paymenttype;

    private static int SPLASH_TIME_OUT = 3000;

    public static boolean isConnectd(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;

    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE)
        {

        }
        else
        {
            startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_screen);



        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);

        intent = getIntent();

        currenturl = intent.getStringExtra("url");
        order_id = intent.getStringExtra("Order_id");
        paymenttype = intent.getStringExtra("payment_type");

        System.out.println("paymenttype_thnky "+ paymenttype+" "+currenturl);


        textView = findViewById(R.id.textview_orderId);
        linearLayout = findViewById(R.id.linearLayout);
        imageView = findViewById(R.id.img_truck);
        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        retry_btn = findViewById(R.id.button_tryagain);

        webView = findViewById(R.id.webView);

        if (!isConnectd(this))
        {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
        }
        else
        {
            relativeLayout_nonetwork.setVisibility(View.GONE);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadUrl(currenturl);
//            webView.setWebViewClient(new MyWebViewClient(currenturl));
        }


        if (paymenttype.equalsIgnoreCase("Telr Payment"))
        {
            webView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(currenturl);
            webView.setWebViewClient(new MyWebViewClient(currenturl));
        }
//        else if (paymenttype.equalsIgnoreCase("Savedtelr"))
//        {
//            if (!isConnectd(ThankyouScreen.this))
//            {
//                relativeLayout_nonetwork.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                relativeLayout_nonetwork.setVisibility(View.GONE);
//                getPaymentStatus();
//            }
//
//        }
        else
        {
            webView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.order_id) +" "+"#"+ intent.getStringExtra("Order_id"));

            new Handler().postDelayed(new Runnable() {

                                    /*
                                     * Showing splash screen with a timer. This will be useful when you
                                     * want to show case your app logo / company
                                     */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(ThankyouScreen.this, DashBoardActivity.class);
                    (ThankyouScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                    startActivity(i);
                    finish();

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }

        retry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectd(ThankyouScreen.this))
                {
                    recreate();
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                }
                else
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private class MyWebViewClient extends WebViewClient {

        String weburl;

        public MyWebViewClient(String currenturl) {
            this.weburl = currenturl;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String currenturl) {
            if (currenturl.contains("http://workbenchitsolution.com/yaamina/"))
            {
                webView.destroy();
                webView.setVisibility(View.GONE);


                if (!isConnectd(ThankyouScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                }
                else
                {
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    getPaymentStatus();
                }

                textView.setText(getString(R.string.order_id) +" "+"#"+ order_id);

                return false;
            }
            return true;
        }
    }

    private void getPaymentStatus() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PAYMENT_STATUS_API,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("ststuspay " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String ststus = jsonObject.getString("status");
                            if (jsonObject.has("order_id"))
                            {
                                order_id = jsonObject.getString("order_id");
                            }


                            if (ststus.equalsIgnoreCase("Success")) {
                                linearLayout.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                textView.setText(getString(R.string.order_id) +" "+"#"+ order_id);

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                new Handler().postDelayed(new Runnable() {

                                    /*
                                     * Showing splash screen with a timer. This will be useful when you
                                     * want to show case your app logo / company
                                     */

                                    @Override
                                    public void run() {
                                        // This method will be executed once the timer is over
                                        // Start your app main activity
                                        Intent i = new Intent(ThankyouScreen.this, DashBoardActivity.class);
                                        (ThankyouScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                        startActivity(i);

                                        // close this activity
                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);

                            } else if (ststus.equalsIgnoreCase("fail")) {

                                webView.destroy();
                                linearLayout.setVisibility(View.GONE);
                                imageView.setVisibility(View.GONE);

                                Intent i = new Intent(ThankyouScreen.this, DashBoardActivity.class);
                                (ThankyouScreen.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(i);
                                finish();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("exceptionE "+ e.getMessage());
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

                params.put(Constants.ORDER_DETAIL_ID, order_id);
                params.put(Constants.USER_ID, sharedPreferences.getString(SharedPref.USER_ID,""));
                params.put(Constants.PAYMENT_TYPE, "Telr Payment");
                if (paymenttype.equalsIgnoreCase("Telr Payment"))
                {
                    params.put(Constants.FIRSTNAME, intent.getStringExtra("first_name"));
                    params.put(Constants.LASTNAME, intent.getStringExtra("last_name"));
                    params.put(Constants.ADDRESS_LINE1, intent.getStringExtra("address1"));
                    params.put(Constants.ADDRESS_LINE2, intent.getStringExtra("address2"));
                    params.put(Constants.BUILDING_NO, intent.getStringExtra("building_num"));
                    params.put(Constants.PHONE_NUMBER, intent.getStringExtra("phone_number"));
                    params.put(Constants.CITY, intent.getStringExtra("selectedCity"));
                    params.put(Constants.COMMUNITY_AREA, intent.getStringExtra("selectedCountry"));
                    params.put(Constants.DELIVERY_TIME, intent.getStringExtra("delivary_time"));
                    params.put(Constants.NOTE, intent.getStringExtra("note"));
                    params.put(Constants.COUPON_CODE, intent.getStringExtra("coupon_code"));
                    params.put(Constants.ADDRESS_ID, intent.getStringExtra("address_id"));
                    params.put(Constants.SCHEDULE_DATE, intent.getStringExtra("date"));
                    params.put(Constants.TOTAL, intent.getStringExtra("confirm_total"));
                }

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
