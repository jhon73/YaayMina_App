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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.MyCardAdapter;
import app.yaaymina.com.yaaymina.Adapter.TelrAdapter;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.CardModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.CardType;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;
import morxander.editcard.EditCard;

public class MySavedCardScreen extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    Button button_addCard, button_saveNewCard;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String user_id,language_type,email;

    private FrameLayout frameLayout_progress;
    private ProgressDialog progressDialog;

    private ArrayList<CardModel> myCardArrayList;
    private MyCardAdapter myCardAdapter;

    private RecyclerView recyclerView_cards;
    private LinearLayout linearLayout_container;

    private static final char space = ' ';

    private Locale myLocale;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork, relativeLayout_noCard;
    private NestedScrollView nestedScrollView_main;
    private String card_title;

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
        setContentView(R.layout.activity_my_saved_card_screen);

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

        setupBottomNavigation();

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (MySavedCardScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }
        });

        progressDialog = new ProgressDialog(MySavedCardScreen.this);

//        button_addCard = findViewById(R.id.addCard_btn);
        frameLayout_progress = findViewById(R.id.progress_frame);
        recyclerView_cards = findViewById(R.id.recyclerView_adapter);
        linearLayout_container = findViewById(R.id.layout_container);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        relativeLayout_noCard = findViewById(R.id.rel_no_card);
        button_nointernet = findViewById(R.id.button_tryagain);
        button_saveNewCard = findViewById(  R.id.button_addNewCard);

//        nestedScrollView_main = findViewById(R.id.nested_scroll);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        button_nointernet.setOnClickListener(this);
        button_saveNewCard.setOnClickListener(this);

        frameLayout_progress.setVisibility(View.VISIBLE);
        linearLayout_container.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");
        email = sharedPreferences.getString(ConstantData.EMAIL,"");

        myCardArrayList = new ArrayList<>();

        if (!isConnectd(this)) {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            recyclerView_cards.setVisibility(View.GONE);

        } else {

            doGetMySavedCards(user_id);
//            doGetTelRCard(user_id);

            relativeLayout_nonetwork.setVisibility(View.GONE);
            recyclerView_cards.setVisibility(View.VISIBLE);

        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isConnectd(MySavedCardScreen.this)) {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_cards.setVisibility(View.GONE);

                } else {

                    doGetMySavedCards(user_id);
//                    doGetTelRCard(user_id);
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    recyclerView_cards.setVisibility(View.VISIBLE);

                }
            }
        });

//        button_addCard.setOnClickListener(this);

        changeLang(language_type);
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

    private void doGetMySavedCards(final String user_id) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_TELR_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("ResponseCard "+response);

                        myCardArrayList.clear();

                        linearLayout_container.setVisibility(View.VISIBLE);
                        frameLayout_progress.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                progressDialog.dismiss();

                                JSONArray jsonArray = jsonObject.getJSONArray("Card_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String card_id = jsonObject1.getString("card_id");
                                    String card_title = jsonObject1.getString("card_title");
                                    String stripe_token = jsonObject1.getString("stripe_token");
                                    String card_holder_name = jsonObject1.getString("card_holder_name");
                                    String card_number = jsonObject1.getString("card_number");
                                    String card_exp_month = jsonObject1.getString("card_exp_month");
                                    String card_exp_year = jsonObject1.getString("card_exp_year");
                                    String card_user_id = jsonObject1.getString("card_user_id");

                                    CardModel cardModel = new CardModel();

                                    cardModel.setCard_id(card_id);
                                    cardModel.setCard_title(card_title);
                                    cardModel.setStripe_token(stripe_token);
                                    cardModel.setCard_holder_name(card_holder_name);
                                    cardModel.setCard_number(card_number);
                                    cardModel.setCard_exp_month(card_exp_month);
                                    cardModel.setCard_exp_year(card_exp_year);
                                    cardModel.setCard_user_id(card_user_id);

                                    myCardArrayList.add(cardModel);
                                }



                                myCardAdapter = new MyCardAdapter(MySavedCardScreen.this,myCardArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_cards.setLayoutManager(mLayoutManager);
                                recyclerView_cards.setAdapter(myCardAdapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            else
                            {
                                progressDialog.dismiss();
                            }

                            if (myCardArrayList.size() == 0)
                            {
                                relativeLayout_noCard.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                relativeLayout_noCard.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {

        if (view == button_addCard)
        {
            addCard();

        }else if (view == button_nointernet)
        {
            if (!isConnectd(this))
            {
                recyclerView_cards .setVisibility(View.GONE);
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
            }
            else
            {
                recreate();
                recyclerView_cards .setVisibility(View.VISIBLE);
                relativeLayout_nonetwork.setVisibility(View.GONE);
            }
        }else if (view == button_saveNewCard)
        {
          addCard();
        }
    }

    private void addCard() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MySavedCardScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_add_card, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        final AlertDialog alertbox = dialogBuilder.create();


        final EditText EditText_cardHoldername, EditText_month, EditText_year, EditText_cvv;
        final EditCard EditText_cardnumber;
        Button button_save;
        ImageView imageView_cancel,imageView_card;

        EditText_cardHoldername = dialogView.findViewById(R.id.card_holder_name_et);
        EditText_cardnumber = dialogView.findViewById(R.id.card_number_et);
        EditText_month = dialogView.findViewById(R.id.month_et);
        EditText_year = dialogView.findViewById(R.id.year_et);
        EditText_cvv = dialogView.findViewById(R.id.cvv_et);


        imageView_cancel = dialogView.findViewById(R.id.close_imageview);

        imageView_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertbox.dismiss();
            }
        });

//            EditText_cardnumber.addTextChangedListener(this);


//            EditText_cardnumber.onT


        button_save = dialogView.findViewById(R.id.save_btn);

        button_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!isConnectd(MySavedCardScreen.this)) {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_cards.setVisibility(View.GONE);

                    alertbox.dismiss();

                    Toast.makeText(MySavedCardScreen.this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
                } else {

                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    recyclerView_cards.setVisibility(View.VISIBLE);

                    final String place_holder_name = EditText_cardHoldername.getText().toString();
                    final String card = EditText_cardnumber.getCardNumber();
                    final String month = EditText_month.getText().toString();
                    final String year = EditText_year.getText().toString();

                    card_title = EditText_cardnumber.getCardType();

                    System.out.println("cardTitle "+card_title);

                    String cvv = EditText_cvv.getText().toString();

                    if (place_holder_name.length()==0)
                    {
                        EditText_cardHoldername.setError(getString(R.string.card_holder_name));
                    }else if (card.length()==0) {
                        EditText_cardnumber.setError(getString(R.string.card_number));
                    }else if (!EditText_cardnumber.isValid())
                    {
                        EditText_cardnumber.setError(getString(R.string.invalide_card_num));
                    }else if (month.length()==0)
                    {
                        EditText_month.setError(getString(R.string.empty_expirt_dt));
                    }else if (year.length()==0)
                    {
                        EditText_year.setError(getString(R.string.empty_expiry_yeaer));
                    }else if (cvv.length()==0)
                    {
                        EditText_cvv.setError(getString(R.string.empty_cvv));
                    }else if (card_title.equalsIgnoreCase("Discover") || card_title.equalsIgnoreCase("Diners_Club") || card_title.equalsIgnoreCase("JCB"))
                    {
                        EditText_cardnumber.setError(getString(R.string.card_validation));
                    }else
                    {

                        progressDialog.setMessage(getString(R.string.please_wait));
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        new Stripe(MySavedCardScreen.this).createToken(
                                new Card(card,Integer.parseInt(month),Integer.parseInt(year),cvv),
                                "pk_test_MnYdjiV4RxJ7xp3fdm51QVDH",
                                new TokenCallback() {
                                    public void onSuccess(Token token) {
                                        System.out.println("sTRIPDEdATDToken" +token.getId());


                                        doGetCustomerId(place_holder_name,token.getId(),card,month,year,user_id,card_title,alertbox);

                                    }
                                    public void onError(Exception error) {

                                        progressDialog.dismiss();
                                        Toast.makeText(MySavedCardScreen.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        System.out.println("ErrDara_ "+error.getLocalizedMessage());
                                    }
                                });


                    }
                }

            }



        });


        alertbox.show();
    }

    private void doGetCustomerId(final String place_holder_name, final String id, final String card, final String month, final String year, final String user_id, final String card_title, final AlertDialog alertbox) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STRIPE_CUSTOMER_IS_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {



                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {
                               JSONObject jsonObject1 = jsonObject.getJSONObject("customer_info");
                               String customer_id = jsonObject1.getString("id");

                                doSaveMyCard(place_holder_name,id,card,month,year,user_id,card_title,alertbox,customer_id);
                            }else if (status.equalsIgnoreCase("fail"))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MySavedCardScreen.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        alertbox.dismiss();
                        System.out.println("GetCustomercardRes "+response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

//                alertbox.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.STRIPETOKE,id);
                params.put(Constants.EMAIL,email);


                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void doSaveMyCard(final String place_holder_name, final String id, final String card, final String month, final String year, final String user_id, final String card_title, final AlertDialog alertbox, final String customer_id) {

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getString(R.string.please_wait));
//        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ADD_CARD_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                progressDialog.dismiss();
                                alertbox.dismiss();
                                recreate();
                                System.out.println("SavecardRes "+response);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(MySavedCardScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                alertbox.dismiss();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.CARD_TITLE,card_title);
                params.put(Constants.USER_ID,user_id);
                params.put(Constants.CARD_EXP_YEAR,year);
                params.put(Constants.CARD_EXP_MONTH,month);
                params.put(Constants.CARD_NUMBER,card);
                params.put(Constants.CARD_HOLDER_NAME,place_holder_name);
                params.put(Constants.STRIPE_TOKEN,id);
                params.put(Constants.CUSTOMER_ID,customer_id);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        System.out.println("tesct1 "+charSequence);
    }

    @Override
    public void afterTextChanged(Editable s) {



        if (s.length() > 0 && (s.length() % 5) == 0) {
            final char c = s.charAt(s.length() - 1);

            if (space == c) {
                s.delete(s.length() - 1, s.length());

            }
        }
        // Insert char where needed.
        if (s.length() > 0 && (s.length() % 5) == 0) {
            char c = s.charAt(s.length() - 1);

            System.out.println("tesct2 "+c);
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                s.insert(s.length() - 1, String.valueOf(space));
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
        (MySavedCardScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }

    private void doGetTelRCard(final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_TELR_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Responsetelr " + response);
                        myCardArrayList.clear();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("Card_info");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String card_id = jsonObject1.getString("card_id");
                                    String card_title = jsonObject1.getString("card_title");
                                    String card_holder_name = jsonObject1.getString("card_holder_name");
                                    String card_number = jsonObject1.getString("card_number");
                                    String card_exp_month = jsonObject1.getString("card_exp_month");
                                    String card_exp_year = jsonObject1.getString("card_exp_year");
                                    String card_user_id = jsonObject1.getString("card_user_id");
                                    String order_id = jsonObject1.getString("order_id");
                                    String refrence_no = jsonObject1.getString("refrence_no");
                                    String transaction_ref = jsonObject1.getString("transaction_ref");
                                    String type = jsonObject1.getString("type");

                                    CardModel cardModel = new CardModel();

                                    cardModel.setCard_id(card_id);
                                    cardModel.setCard_title(card_title);
                                    cardModel.setCard_holder_name(card_holder_name);
                                    cardModel.setCard_number(card_number);
                                    cardModel.setCard_exp_month(card_exp_month);
                                    cardModel.setCard_exp_year(card_exp_year);
                                    cardModel.setCard_user_id(card_user_id);
                                    cardModel.setPrder_id(order_id);
                                    cardModel.setRef_no(refrence_no);
                                    cardModel.setTransaction_ref_no(transaction_ref);
                                    cardModel.setType(type);

                                    myCardArrayList.add(cardModel);


                                }

                                System.out.println("listsize "+ myCardArrayList.size());
                                myCardAdapter = new MyCardAdapter(MySavedCardScreen.this, myCardArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_cards.setLayoutManager(mLayoutManager);
                                recyclerView_cards.setAdapter(myCardAdapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }
                            else
                            {
                                Toast.makeText(MySavedCardScreen.this, message, Toast.LENGTH_SHORT).show();

                                if (myCardArrayList.size() == 0)
                                {
                                    relativeLayout_noCard.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    relativeLayout_noCard.setVisibility(View.GONE);
                                }
                            }

                            if (myCardArrayList.size() == 0)
                            {
                                relativeLayout_noCard.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                relativeLayout_noCard.setVisibility(View.GONE);
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

}
