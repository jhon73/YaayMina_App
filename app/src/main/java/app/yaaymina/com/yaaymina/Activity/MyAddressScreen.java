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
import android.support.design.widget.TextInputEditText;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.Model.MyAddress;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

public class MyAddressScreen extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    Button button_addAddress, button_addNewAddress;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String user_id, language_type;

    private FrameLayout frameLayout_progress;

    private ArrayList<MyAddress> myAddressArrayList;
    private MyAddressAdapter myAddressAdapter;

    private RecyclerView recyclerView_address;
    private LinearLayout linearLayout_container;

    private String selectedCountry = null;
    private String selectedCity = null;

    ProgressDialog progressDialog;
    private Locale myLocale;

    private Button button_nointernet;
    private RelativeLayout relativeLayout_nonetwork, relativeLayout_noAddress;
    private NestedScrollView nestedScrollView_main;

    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayAdapter aa_community;
    ArrayAdapter aa;

    String[] country = {

            "Select City",
            "Abu Dhabi",
            "Dubai (Coming Soon)",
            "RAK (Coming Soon)",
            "Al Ain (Coming Soon)",
            "Sharjah (Coming Soon)",
            "Fujairah(Coming Soon)",
            "Umm al quwain (Coming Soon)",
            "Ajman (Coming Soon)",};

    String[] countr_ar = {

            "اختر مدينة",
            "أبو ظبي",
            "دبي (قريبا)",
            "رأس الخيمة (قريبا)",
            "العين (قريبا)",
            "الشارقة (قريبا)",
            "الفجيرة (قريبا)",
            "أم آل كوين (قريبا)",
            "عجمان (قريبا)",
    };


    String[] community_arae = {

            "Select Community / Area",
            "Airport Road",
            "Al Adla",
            "Al Aman",
            "Al Bahya",
            "Al Bandar",
            "Al Bateen",
            "Al Dafrah",
            "Al Etihad",
            "Al Falah City",
            "Al Hosn",
            "Al Karamah",
            "Al Khalidiyah",
            "Al Khubeirah",
            "Al Mafraq",
            "Al Manhal",
            "Al Maqta",
            "Al Markaziyah West",
            "Al Markaziyah",
            "Al Maryah Island",
            "Al Mina",
            "Al Muneera",
            "Al Muntazah",
            "Al Musalla",
            "Al Mushrif",
            "Al Muzoon",
            "Al Nahyan",
            "Al Raha",
            "Al Rahba",
            "Al Ras Al Akhdar",
            "Al Reef",
            "Al Rowdah",
            "Al Safarat",
            "Al Samha",
            "Al Seef",
            "Al Shamkha",
            "Al Shawamekh",
            "Al Wahdah",
            "Al Wathba",
            "Al Zaab",
            "Al Zafranah",
            "Al Zahraa",
            "Al Zeina",
            "Bain Al Jesrain",
            "Bani Yas",
            "Belghailam",
            "BreakWater",
            "Capital Centre",
            "Corniche",
            "Dhow Harbour",
            "Hydra City",
            "Khor Al Maqtaa",
            "Madinat Khalifa - A",
            "Madinat Khalifa - B",
            "Madinat Khalifa - C",
            "Madinat Zayed",
            "Mangroves",
            "Masdar City",
            "Mohammed Bin Zayed City",
            "Mussafah Industrial community",
            "New Shahama",
            "Port Zayed",
            "Qasr El Bahr",
            "Qasr El Shantie",
            "Reem Island",
            "Saadiyat Island",
            " Sas Al Nakhl Island",
            "Shahama",
            "Tourist Club Area",
            "Yas Island",
            "Zayed Sports City",

    };

    String[] community_arae_arr = {

            "حدد المجتمع / المنطقة",
            "طريق المطار",
            "العدلا",
            "الأمان",
            "الباهية",
            "البندر",
            "البطين",
            "الدفرة",
            "الاتحاد",
            "مدينة الفلاح",
            "الحصن",
            "الكرامة",
            "الخالدية",
            "الخبيرة",
            "المفرق",
            "المنهل",
            "المقطع",
            "الغربية الغربية",
            "المركزية",
            "جزيرة الماريه",
            "الميناء",
            "المنيرة",
            "المنتزه",
            "المصلى",
            "المشرف",
            "المزون",
            "آل نهيان",
            "الراحة",
            "الرحبة",
            "رأس الأخضر",
            "الريف",
            "الروضة",
            "السفارات",
            "السمحة",
            "السيف",
            "الشامخة",
            "الشوامخ",
            "الوحدة",
            "الوثبة",
            "الزعاب",
            "الزعفرانة",
            "الزهراء",
            "الزينة",
            "بين الجسرين",
            "بني ياس",
            "سمالية",
            "حائل الأمواج",
            "مركز كابيتال",
            "الكورنيش",
            "ميناء داو",
            "هيدرا سيتي",
            "خور المقطع",
            "مدينة خليفة - A",
            "مدينة خليفة - ب",
            "مدينة خليفة - ج",
            "مدينة زايد",
            "القرم",
            "مدينة مصدر",
            "مدينة محمد بن زايد",
            "مجموعة المصفح الصناعية",
            "الشهامة الجديدة",
            "ميناء زايد",
            "قصر البحر",
            "قصر الشنطي",
            "جزيرة ريم",
            "جزيرة السعديات",
            "جزيرة ساس النخل",
            "الشهامة",
            "منطقة النادي السياحي",
            "جزيرة ياس",
            "مدينة زايد الرياضية",

    };


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
        setContentView(R.layout.activity_my_address_screen);

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.navigation_account).setChecked(true);

        setupBottomNavigation();

        toolbar = findViewById(R.id.toolbar_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(MyAddressScreen.this);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyAccountScreen.class));
                (MyAddressScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();
            }
        });

        button_addAddress = findViewById(R.id.addAdress_btn);
        button_addNewAddress = findViewById(R.id.button_addNewAddress);

        button_addNewAddress.setOnClickListener(this);

        frameLayout_progress = findViewById(R.id.progress_frame);
        recyclerView_address = findViewById(R.id.recyclerView_adapter);
        linearLayout_container = findViewById(R.id.layout_container);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        relativeLayout_nonetwork = findViewById(R.id.relativeLayout_noInternet);
        relativeLayout_noAddress = findViewById(R.id.rel_no_address);
        button_nointernet = findViewById(R.id.button_tryagain);

//        nestedScrollView_main = findViewById(R.id.nested_scroll);

        button_nointernet.setOnClickListener(this);

        linearLayout_container.setVisibility(View.GONE);
        frameLayout_progress.setVisibility(View.VISIBLE);


        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID, "");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");


        myAddressArrayList = new ArrayList<>();

        if (!isConnectd(this)) {
            relativeLayout_nonetwork.setVisibility(View.VISIBLE);
            recyclerView_address.setVisibility(View.GONE);

        } else {
            doGetMyAddress(user_id);
            relativeLayout_nonetwork.setVisibility(View.GONE);
            recyclerView_address.setVisibility(View.VISIBLE);

        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            public void onRefresh() {
                if (!isConnectd(MyAddressScreen.this)) {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_address.setVisibility(View.GONE);

                } else {
                    doGetMyAddress(user_id);
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    recyclerView_address.setVisibility(View.VISIBLE);

                }

            }
        });


        button_addAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!isConnectd(MyAddressScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_address.setVisibility(View.GONE);
                }
                else
                {
                    addNewAddreess();
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    recyclerView_address.setVisibility(View.VISIBLE);

                }


            }
        });

        changeLang(language_type);
    }

    private void addNewAddreess() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyAddressScreen.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_add_address, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        final AlertDialog alertbox = dialogBuilder.create();

        final TextInputEditText textInputEditText_fname, textInputEditText_lname, textInputEditText_addressLine1, textInputEditText_addressLine2;
        final TextInputEditText textInputEditText_city, textInputEditText_pincode, textInputEditText_phone, textInputEditText_houseNo;
        Button button_save;
        final Spinner spinner_community, spinner_city;
        ImageView imageView_cancel;

        textInputEditText_fname = dialogView.findViewById(R.id.firstName_textInput);
        textInputEditText_lname = dialogView.findViewById(R.id.lastName_textInput);
        textInputEditText_addressLine1 = dialogView.findViewById(R.id.addressLine1_textInput);
        textInputEditText_addressLine2 = dialogView.findViewById(R.id.addressLine2_textInput);
        spinner_city = dialogView.findViewById(R.id.spinner_city_item);
        spinner_community = dialogView.findViewById(R.id.spinner_item);
        textInputEditText_phone = dialogView.findViewById(R.id.phone_textInput);
        textInputEditText_houseNo = dialogView.findViewById(R.id.houseno_textInput);

        imageView_cancel = dialogView.findViewById(R.id.close_imageview);

        imageView_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                alertbox.dismiss();
            }
        });


        spinner_community.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinner_city.setOnItemSelectedListener(new MyOnItemSelectedListener());

        if (language_type.equalsIgnoreCase("en"))
        {
            aa = new ArrayAdapter(getApplicationContext(), R.layout.layout_spinner, country) {
                @Override
                public boolean isEnabled(int position) {

                    if (position == 1) {
                        return true;
                    } else {
                        return false;
                    }

                }
            };

            aa.setDropDownViewResource(R.layout.layout_spinner);
            spinner_city.setAdapter(aa);

             aa_community = new ArrayAdapter(getApplicationContext(), R.layout.layout_spinner, community_arae) {
                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            };
            aa_community.setDropDownViewResource(R.layout.layout_spinner);
            spinner_community.setAdapter(aa_community);
        }
        else
        {
             aa = new ArrayAdapter(getApplicationContext(), R.layout.layout_spinner, countr_ar) {
                @Override
                public boolean isEnabled(int position) {

                    if (position == 1) {
                        return true;
                    } else {
                        return false;
                    }

                }
            };

            aa.setDropDownViewResource(R.layout.layout_spinner);
            spinner_city.setAdapter(aa);

            aa_community = new ArrayAdapter(getApplicationContext(), R.layout.layout_spinner, community_arae_arr) {
                @Override
                public boolean isEnabled(int position) {

                    if (position == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            };
            aa.setDropDownViewResource(R.layout.layout_spinner);
            spinner_community.setAdapter(aa_community);
        }




        button_save = dialogView.findViewById(R.id.save_btn);

        button_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!isConnectd(MyAddressScreen.this))
                {
                    relativeLayout_nonetwork.setVisibility(View.VISIBLE);
                    recyclerView_address.setVisibility(View.GONE);

                    Toast.makeText(MyAddressScreen.this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
                }else
                {
                    relativeLayout_nonetwork.setVisibility(View.GONE);
                    recyclerView_address.setVisibility(View.VISIBLE);

                    String MobilePattern = "[0-9]{10}";

                    String fname = textInputEditText_fname.getText().toString();
                    String lname = textInputEditText_lname.getText().toString();
                    String add_line1 = textInputEditText_addressLine1.getText().toString();
                    String add_line2 = textInputEditText_addressLine2.getText().toString();
//                        String community = textInputEditText_city.getText().toString();
//                        String city = textInputEditText_pincode.getText().toString();
                    String phone = textInputEditText_phone.getText().toString();
                    String house = textInputEditText_houseNo.getText().toString();

                    if (fname.length() == 0) {
                        textInputEditText_fname.setError(getString(R.string.empty_first_name));
                    } else if (lname.length() == 0) {
                        textInputEditText_lname.setError(getString(R.string.empty_last_name));
                    } else if (add_line1.length() == 0) {
                        textInputEditText_addressLine1.setError(getString(R.string.empty_address_line1));
                    } else if (add_line2.length() == 0) {
                        textInputEditText_addressLine2.setError(getString(R.string.empty_address_line2));
                    } else if (spinner_city.getSelectedItem().toString().trim().equals(getString(R.string.select_city))) {
                        Toast.makeText(MyAddressScreen.this, getString(R.string.select_city), Toast.LENGTH_SHORT).show();
                    } else if (spinner_community.getSelectedItem().toString().trim().equals(getString(R.string.empty_community))) {
                        Toast.makeText(MyAddressScreen.this, getString(R.string.empty_community), Toast.LENGTH_SHORT).show();
                    } else if (phone.length() == 0) {
                        textInputEditText_phone.setError(getString(R.string.enter_phone_num));
                    } else if (!phone.matches(MobilePattern)) {
                        textInputEditText_phone.setError(getString(R.string.invalid_phn_num));

                    } else if (house.length() == 0) {
                        textInputEditText_houseNo.setError(getString(R.string.building_flat_no));
                    } else {
                        frameLayout_progress.setVisibility(View.VISIBLE);
                        doSaveMyAddress(fname, lname, add_line1, add_line2, selectedCity, selectedCountry, phone, house, alertbox);
                    }

                }


            }
        });

        alertbox.show();

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

    private void doGetMyAddress(final String user_id) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        myAddressArrayList.clear();

                        frameLayout_progress.setVisibility(View.GONE);
                        linearLayout_container.setVisibility(View.VISIBLE);

                        System.out.println("Address response "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                progressDialog.dismiss();

                                JSONArray jsonArray = jsonObject.getJSONArray("Card_info");

                                for (int i = 0; i < jsonArray.length(); i ++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String address_id = jsonObject1.getString("address_id");
                                    String firstname = jsonObject1.getString("firstname");
                                    String lastname = jsonObject1.getString("lastname");
                                    String address1 = jsonObject1.getString("address1");
                                    String address2 = jsonObject1.getString("address2");
                                    String building_flatno = jsonObject1.getString("building_flatno");
                                    String phone_no = jsonObject1.getString("phone_no");
                                    String city = jsonObject1.getString("city");
                                    String community = jsonObject1.getString("community_area");
                                    String user_id = jsonObject1.getString("user_id");
                                    String entered = jsonObject1.getString("entered");


                                    System.out.println("Getadd "+ community + " " + city);

                                    MyAddress myAddress = new MyAddress();

                                    myAddress.setAddress_id(address_id);
                                    myAddress.setFirstname(firstname);
                                    myAddress.setLastname(lastname);
                                    myAddress.setAddress1(address1);
                                    myAddress.setAddress2(address2);
                                    myAddress.setBuilding_flatno(building_flatno);
                                    myAddress.setPhone_no(phone_no);
                                    myAddress.setCity(city);
                                    myAddress.setCommunity(community);
                                    myAddress.setUser_id(user_id);
                                    myAddress.setEntered(entered);

                                    myAddressArrayList.add(myAddress);

                                    System.out.println("addresslist "+myAddressArrayList.size());

                                }
                            }

                            else
                            {
                                progressDialog.dismiss();
                            }

                            if (myAddressArrayList.size() == 0)
                            {
                                relativeLayout_noAddress.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                relativeLayout_noAddress.setVisibility(View.GONE);
                            }

                            myAddressAdapter = new MyAddressAdapter(MyAddressScreen.this,myAddressArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView_address.setLayoutManager(mLayoutManager);
                            recyclerView_address.setAdapter(myAddressAdapter);
                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                frameLayout_progress.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void doSaveMyAddress(final String fname, final String lname, final String add_line1, final String add_line2, final String city, final String pincode, final String phone, final String house, final AlertDialog alertbox) {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ADD_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

//                        myAddressArrayList.clear();
                        frameLayout_progress.setVisibility(View.GONE);
                        alertbox.dismiss();
                        recreate();

                        System.out.println("Address response "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                relativeLayout_noAddress.setVisibility(View.GONE);


                            }

                            else
                            {
                                relativeLayout_noAddress.setVisibility(View.GONE);
                                Toast.makeText(MyAddressScreen.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                frameLayout_progress.setVisibility(View.GONE);
                alertbox.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                System.out.println("dataVal "+ user_id + fname + lname + add_line1 + add_line2 + city + pincode + house);
                params.put(Constants.USER_ID,user_id);
                params.put(Constants.FIRSTNAME,fname);
                params.put(Constants.LASTNAME,lname);
                params.put(Constants.ADDRESS_LINE1,add_line1);
                params.put(Constants.ADDRESS_LINE2,add_line2);
                params.put(Constants.CITY,city);
                params.put(Constants.PHONE_NUMBER,phone);
                params.put(Constants.COMMUNITY_AREA,pincode);
                params.put(Constants.BUILDING_NO,house);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
    public void onClick(View view) {

        if (view == button_nointernet)
        {
            if (!isConnectd(this))
            {
                recyclerView_address .setVisibility(View.GONE);
                relativeLayout_nonetwork.setVisibility(View.VISIBLE);

                Toast.makeText(this, getString(R.string.no_Intenrnet), Toast.LENGTH_SHORT).show();
            }
            else
            {
                recreate();
                recyclerView_address .setVisibility(View.VISIBLE);
                relativeLayout_nonetwork.setVisibility(View.GONE);
            }
        }
        else if (view == button_addNewAddress)
        {
            addNewAddreess();
        }


    }

    private class MyOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            String selectedItem = adapterView.getItemAtPosition(i).toString();

            //check which spinner triggered the listener
            switch (adapterView.getId()) {
                //country spinner
                case R.id.spinner_item:
                    //make sure the country was already selected during the onCreate
                    if(selectedCountry != null){
//                        Toast.makeText(getApplicationContext(),  selectedItem +" " + "Selected" ,
//                                Toast.LENGTH_LONG).show();
                    }

                    selectedCountry = selectedItem;

                    break;

                case  R.id.spinner_city_item:

                    selectedCity = selectedItem;
                //animal spinner
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
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
        (MyAddressScreen.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
