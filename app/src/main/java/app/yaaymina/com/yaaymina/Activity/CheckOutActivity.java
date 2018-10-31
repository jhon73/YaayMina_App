package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.yaaymina.com.yaaymina.Adapter.MyAddressAdapter;
import app.yaaymina.com.yaaymina.Adapter.MySavedAddressAdapter;
import app.yaaymina.com.yaaymina.Adapter.OrderConfirmAdapter;
import app.yaaymina.com.yaaymina.Adapter.TelrAdapter;
import app.yaaymina.com.yaaymina.Adapter.ViewCardAdapter;
import app.yaaymina.com.yaaymina.BuildConfig;
import app.yaaymina.com.yaaymina.CommonInterface.SelectAddressConfirm;
import app.yaaymina.com.yaaymina.CommonInterface.SendTokenCourousol;
import app.yaaymina.com.yaaymina.CommonInterface.TelrSendDetail;
import app.yaaymina.com.yaaymina.Model.CardModel;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.Model.MyAddress;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.CardType;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;
import info.hoang8f.android.segmented.SegmentedGroup;
import morxander.editcard.EditCard;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;

    SegmentedGroup segmented5;
    RadioButton radioButton;

    RadioGroup radioGroup_saved_card, radioGroup_cod;

    private MySavedAddressAdapter myAddressAdapter;

    SelectAddressConfirm selectAddressConfirm;

    private String tag_name;
    AlertDialog alertbox;

    private TextInputEditText textInputEditText_fname, textInputEditText_lname, textInputEditText_address1, textInputEditText_address2;
    private TextInputEditText textInputEditText_building_number, textInputEditText_mobNumber, textInputEditText_note;

    private TextInputEditText textInputEditText_date;

    private Button button_toPayment;
    private Button button_checkout;

    private Spinner spinner_community, spinner_city;

    private RadioGroup radioGroup, radioGroup_telr, radioGroup_saved_card_telr;

    private String selectedCountry = null;
    private String selectedCity = null;

    TabHost.TabSpec spec, childspec;

    TabHost host, childhost;

    String paymentType, cardtype, telrType;

    private EditText editText_coupon, editText_cvv_code;
    private Button button_apply_coupon;
    private TextView textView_total_confirmation;

    private String coupon_text, totaT, cvv_code, email, confirm_total, discount_total;

    private String saved_card_num, saved_card_holder_name, saved_card_month, saved_card_year;

    private TextView textView_address, textView_coupon_total, textView_total_amount, textview_note, textView_savedDetails, textView_noCard;
    private RecyclerView recyclerView_cart;
    private Button button_orderNow;
    private String product_detail;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type, user_id, first_name, last_name, address1, address2, city, phone_number, pincode, delivary_time, date, note, address_id, payment_type;
    private String coupon_code, building_num, guestType;

    private String card_title, card_num, card_holder_num, card_card_exp_month, card_exp_year, card_cvc;
    private String stripe_token, customer_id, total;

    private OrderConfirmAdapter cartAdapter;
    String totalamount;

    private RadioButton radioButton_savedcard, radioButton_savedcard_telr;
    private ImageView imageView_card, imageView_cvv;

    private RecyclerView recyclerView_savedCard, recyclerView_savedTelrcard;
    private LinearLayout frameLayout_recycler;
    private ViewCardAdapter myCardAdapter;
    private TelrAdapter mytelrAdapter;

    private ArrayList<CardModel> myCardArrayList, myTelCarlist;

    private SendTokenCourousol sendTokenCourousol;
    private TelrSendDetail telrSendDetail;

    String transactioRef;

    private RadioButton radioButton_cod, radioButton_telr;
    private Button button_checkout_savedCard, button_addedCard, button_checkout_cod, button_checkout_telr;

    private TextView textView_telrcardnum;
    private ImageView imageView_telrcardimg;

    private ProgressDialog progressDialog;

    EditText EditText_cardHoldername, EditText_month, EditText_year, EditText_cvv;
    EditCard EditText_cardnumber;

    Button button_save;
    ImageView imageView_cancel, imageView_card_;

    LinearLayout lm;

    private Locale myLocale;

    private int type;

    private ArrayList<String> slotList;

    ArrayAdapter aa;
    ArrayAdapter aa_community;

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
            "Sas Al Nakhl Island",
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

    private String exp_month, exp_year, cvv;
    private String card_holder_name;
    private TextView textView_order_total, textView_discount_total;
    private String card_id;
    private String order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        type = 0;

        discount_total = "0.0";

        toolbar = (Toolbar) findViewById(R.id.toolbar_native);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartScreen.class);
                startActivity(intent);
                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
                finish();

            }
        });

        progressDialog = new ProgressDialog(CheckOutActivity.this);

        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        user_id = sharedPreferences.getString(ConstantData.USER_ID, "");
        total = sharedPreferences.getString(ConstantData.CART_TOTAL, "");
        email = sharedPreferences.getString(ConstantData.EMAIL, "");
        guestType = sharedPreferences.getString(ConstantData.USER_TYPE, "");

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION, "");

        System.out.println("totalamount " + sharedPreferences.getString(ConstantData.CART_TOTAL, ""));

        confirm_total = total;

        System.out.println("oreder_id " + order_id);

        host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        childhost = (TabHost) findViewById(R.id.tabHost_);
        childhost.setup();

        segmented5 = (SegmentedGroup) findViewById(R.id.segmented2);

        segmented5.setOnCheckedChangeListener(CheckOutActivity.this);

        radioButton = findViewById(R.id.button21);
        radioButton.setChecked(true);

        //Tab 1
        spec = host.newTabSpec(getString(R.string.Shipping));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.Shipping));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getString(R.string.Payment));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.Payment));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec(getString(R.string.Confirmation));
        spec.setContent(R.id.tab3);
        spec.setIndicator(getString(R.string.Confirmation));
        host.addTab(spec);

        host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
        host.getTabWidget().getChildTabViewAt(2).setEnabled(false);

//        if (guestType.equalsIgnoreCase("guest"))
//        {
//
//        }
//        else
//        {
//            childspec = childhost.newTabSpec(getString(R.string.SAVED_CARD));
//            childspec.setContent(R.id.childtab1);
//            childspec.setIndicator(getString(R.string.SAVED_CARD));
//            childhost.addTab(childspec);
//        }


        //Tab 2
//        childspec = childhost.newTabSpec(getString(R.string.credit_debit));
//        childspec.setContent(R.id.childtab2);
//        childspec.setIndicator(getString(R.string.credit_debit));
//        childhost.addTab(childspec);

        //Tab 3
        childspec = childhost.newTabSpec(getString(R.string.cod));
        childspec.setContent(R.id.childtab3);
        childspec.setIndicator(getString(R.string.cod));
        childhost.addTab(childspec);

        childspec = childhost.newTabSpec(getString(R.string.telR));
        childspec.setContent(R.id.childtab4);
        childspec.setIndicator(getString(R.string.telR));
        childhost.addTab(childspec);

        textInputEditText_fname = findViewById(R.id.firstName_textInput);
        textInputEditText_lname = findViewById(R.id.lastName_textInput);
        textInputEditText_address1 = findViewById(R.id.addressLine1_textInput);
        textInputEditText_address2 = findViewById(R.id.addressLine2_textInput);
        textInputEditText_building_number = findViewById(R.id.houseno_textInput);
        textInputEditText_mobNumber = findViewById(R.id.phone_textInput);
        textInputEditText_note = findViewById(R.id.note_textInput);
        textInputEditText_date = findViewById(R.id.date_textInput);
        button_toPayment = findViewById(R.id.continue_payment);

        radioGroup = findViewById(R.id.radio_grp);

        slotList = new ArrayList<>();

        spinner_community = findViewById(R.id.spinner_item);
        spinner_city = findViewById(R.id.spinner_city_item);

        button_toPayment.setOnClickListener(this);
        textInputEditText_date.setOnClickListener(this);

        if (language_type.equalsIgnoreCase("en")) {
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

        } else {
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

            aa_community.setDropDownViewResource(R.layout.layout_spinner);
            spinner_community.setAdapter(aa_community);

        }


        spinner_community.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinner_city.setOnItemSelectedListener(new MyOnItemSelectedListener());

        selectAddressConfirm = new SelectAddressConfirm() {

            @Override
            public void onAddressSelected(String tag, String fname, String lname, String address1, String address2, String community, String city, String phone, String building_no, String addressid, String date_) {


                paymentType = "Credit Card";

                tag_name = tag;

                if (!tag.equalsIgnoreCase("")) {

                    alertbox.dismiss();

                    radioButton.setChecked(true);

                    textInputEditText_fname.setText(fname);
                    textInputEditText_lname.setText(lname);
                    textInputEditText_address1.setText(address1);
                    textInputEditText_address2.setText(address2);
                    textInputEditText_mobNumber.setText(phone);
                    textInputEditText_building_number.setText(building_no);

                    selectedCity = city;
                    selectedCountry = community;


                    System.out.println("hellocity " + city + " " + community);

                    address_id = addressid;

                    int i = getIndexFromElement(aa, city);
                    int j = getIndexFromElement(aa_community, community);

//                    System.out.println("hellocityposition " + i);
                    spinner_city.setSelection(i);
                    spinner_community.setSelection(j);
                }
            }

        };

        doGetSlotDetails();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                delivary_time = String.valueOf(radioBtn.getText());

            }

        });

        //------------------------------------- payment section -----------------------------------//

        editText_coupon = findViewById(R.id.couponcode_editText);
        button_apply_coupon = findViewById(R.id.apply_coupon_button);
        textView_coupon_total = findViewById(R.id.totalAmount_textView);

        radioGroup_saved_card = findViewById(R.id.saved_card_group);

        button_checkout_savedCard = findViewById(R.id.continue_checkout_1);
        button_addedCard = findViewById(R.id.continue_checkout_2);
        button_checkout_cod = findViewById(R.id.continue_checkout_3);
        button_checkout_telr = findViewById(R.id.continue_checkout_4);


        imageView_cvv = findViewById(R.id.image_cvv);
        textView_savedDetails = findViewById(R.id.text_savedDetails);
        textView_noCard = findViewById(R.id.text_noCards);

        textView_coupon_total.setText(total + " " + "AED");

        button_checkout_savedCard.setOnClickListener(this);
        button_addedCard.setOnClickListener(this);
        button_checkout_cod.setOnClickListener(this);
        button_checkout_telr.setOnClickListener(this);

        myCardArrayList = new ArrayList<>();
        myTelCarlist = new ArrayList<>();

        recyclerView_savedCard = findViewById(R.id.recycler_card);
        recyclerView_savedTelrcard = findViewById(R.id.recycler_telr_card);
        frameLayout_recycler = findViewById(R.id.frame_card);

        imageView_card = findViewById(R.id.image_card);
        imageView_card.setImageResource(R.drawable.ic_card_defult);

        radioButton_savedcard = findViewById(R.id.card_number_radio);

        radioButton_savedcard.setChecked(false);

        editText_cvv_code = findViewById(R.id.edt_cvv_card);

        EditText_cardHoldername = findViewById(R.id.card_holder_name_et);
        EditText_cardnumber = findViewById(R.id.card_number_et);
        EditText_month = findViewById(R.id.month_et);
        EditText_year = findViewById(R.id.year_et);
        EditText_cvv = findViewById(R.id.cvv_et);

//        EditText_cardnumber.addTextChangedListener(this);


        if (user_id.length() != 0) {

            doGetSavedCard(user_id);

        } else {

            Log.d("tag", "do nothing");
        }

        button_apply_coupon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (user_id != null) {

                    if (editText_coupon.getText().toString().length() == 0) {
                        editText_coupon.setError(getString(R.string.empty_coupon));
                    } else {

                        System.out.println("EditCoupon " + editText_coupon.getText().toString());
                        doApplyCouponcode(user_id, total, editText_coupon.getText().toString());
                        coupon_code = editText_coupon.getText().toString();
                    }
                }
            }
        });

        radioButton_savedcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                frameLayout_recycler.setVisibility(View.GONE);
                myCardArrayList.clear();
                frameLayout_recycler.setVisibility(View.VISIBLE);
                recyclerView_savedTelrcard.setVisibility(View.GONE);
                recyclerView_savedCard.setVisibility(View.VISIBLE);
                doGetCouroselCard(user_id);

                radioGroup_cod.clearCheck();
                radioGroup_telr.clearCheck();
                radioGroup_saved_card_telr.clearCheck();


                paymentType = "Credit Card";
            }
        });

        sendTokenCourousol = new SendTokenCourousol() {

            @Override
            public void onTokenSend(String tag, String card_num_, String card_holder, String exp_month, String exp_year, String stripetoken, String customerid) {

                System.out.println("toeknnum " + card_num);
                frameLayout_recycler.setVisibility(View.GONE);

                card_num = card_num_;
                card_holder_num = card_holder;
                card_card_exp_month = exp_month;
                card_exp_year = exp_year;
                card_id = tag;
                stripe_token = stripetoken;
                customer_id = customerid;

                if (card_num_.matches(CardType.VISA)) {

                    imageView_card.setImageResource(R.drawable.ic_card_visa);
                    card_title = "VISA";

                } else if (card_num_.matches(CardType.MASTER_CARD)) {
                    imageView_card.setImageResource(R.drawable.ic_card_mastercard);
                    card_title = "MASTER CARD";

                } else if (card_num_.matches(CardType.AMERICAN_EXPRESS)) {
                    imageView_card.setImageResource(R.drawable.ic_card_american_express);
                    card_title = "AMERICAN EXPRESS";

                } else if (card_num_.matches(CardType.DINERS_CLUB)) {
                    imageView_card.setImageResource(R.drawable.ic_card_diners_club);
                    card_title = "DINERS CLUB";

                } else if (card_num_.matches(CardType.DISCOVER)) {
                    imageView_card.setImageResource(R.drawable.ic_discover);
                    card_title = "DISCOVER";

                } else if (card_num_.matches(CardType.JCB)) {
                    imageView_card.setImageResource(R.drawable.ic_card_jcb);
                    card_title = "JCB";

                } else {
                    imageView_card.setImageResource(R.drawable.ic_card_defult);
                    card_title = "DEFAULT";
                }

                String newString = card_num_.substring(0, card_num_.length() - 4);
                String checkReplaceStr = "XXXXXXXXXXXX";

                newString = checkReplaceStr;

                int interval = 4;
                char separator = ' ';

                StringBuilder sb = new StringBuilder(newString);

                for (int i = 0; i < newString.length() / interval; i++) {
                    sb.insert(((i + 1) * interval) + i, separator);
                }

                String withDashes = sb.toString();
                radioButton_savedcard.setText(withDashes + card_num_.substring(card_num_.length() - 4));
                myCardArrayList.clear();


            }
        };

        textView_telrcardnum = findViewById(R.id.telRcardnum);
        imageView_telrcardimg = findViewById(R.id.telRcardimg);
        telrSendDetail = new TelrSendDetail() {

            @Override
            public void onTokenSend(String transactionRef, String cardnum, String cardtype) {

                frameLayout_recycler.setVisibility(View.GONE);

                telrType = "saved_card";

                transactioRef = transactionRef;
                System.out.println("transactionref " + transactioRef);

                textView_telrcardnum.setVisibility(View.VISIBLE);
                imageView_telrcardimg.setVisibility(View.VISIBLE);
                textView_telrcardnum.setText(cardnum);

                if (cardtype.contains("Visa")) {

                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_visa);
                    card_title = "VISA";

                } else if (cardtype.contains("MasterCard ")) {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_mastercard);
                    card_title = "MASTER CARD";

                } else if (cardtype.contains("AmericanExpress")) {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_american_express);
                    card_title = "AMERICAN EXPRESS";

                } else if (cardtype.contains("DinersClub")) {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_diners_club);
                    card_title = "DINERS CLUB";

                } else if (cardtype.contains("Discover")) {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_discover);
                    card_title = "DISCOVER";

                } else if (cardtype.contains("JCB")) {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_jcb);
                    card_title = "JCB";

                } else {
                    imageView_telrcardimg.setImageResource(R.drawable.ic_card_defult);
                    card_title = "DEFAULT";
                }

                myCardArrayList.clear();

            }
        };

        radioButton_cod = findViewById(R.id.radio_cod);
        radioButton_cod.setChecked(false);

        radioGroup_cod = findViewById(R.id.radio_group_cod);

        radioButton_cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                frameLayout_recycler.setVisibility(View.GONE);
                recyclerView_savedCard.setVisibility(View.GONE);
                editor.putString(ConstantData.PAYMENT_TYPE, "COD").apply();
                radioGroup_saved_card.clearCheck();
                radioGroup_telr.clearCheck();
                radioGroup_saved_card_telr.clearCheck();

                paymentType = "COD";


            }
        });


        radioButton_telr = findViewById(R.id.radio_telr);
        radioButton_telr.setChecked(false);

        radioGroup_telr = findViewById(R.id.radio_group_telr);

        radioButton_telr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
                editor.putString(ConstantData.PAYMENT_TYPE, "Telr Payment").apply();
                radioGroup_saved_card.clearCheck();
                radioGroup_cod.clearCheck();
                radioGroup_saved_card_telr.clearCheck();
                frameLayout_recycler.setVisibility(View.GONE);
                textView_telrcardnum.setVisibility(View.GONE);
                imageView_telrcardimg.setVisibility(View.GONE);

                paymentType = "Telr Payment";
                telrType = "add card";


            }
        });


        radioButton_savedcard_telr = findViewById(R.id.radio_saved_card_telr);
        radioButton_savedcard_telr.setChecked(false);

        radioGroup_saved_card_telr = findViewById(R.id.radio_group_saved_card_telr);

        if (guestType.equalsIgnoreCase("guest")) {
            radioGroup_saved_card_telr.setVisibility(View.GONE);
        } else {
            radioGroup_saved_card_telr.setVisibility(View.VISIBLE);
        }

        radioButton_savedcard_telr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                frameLayout_recycler.setVisibility(View.GONE);
                myCardArrayList.clear();
//                recyclerView_savedCard.removeAllViews();

                editor.putString(ConstantData.PAYMENT_TYPE, "Telr Payment").apply();
                radioGroup_saved_card.clearCheck();
                radioGroup_cod.clearCheck();
                radioGroup_telr.clearCheck();
                frameLayout_recycler.setVisibility(View.VISIBLE);
                recyclerView_savedTelrcard.setVisibility(View.VISIBLE);
                recyclerView_savedCard.setVisibility(View.GONE);
                doGetTelRCard(user_id);
                paymentType = "Telr Payment";


            }
        });


        //------------------------------------ confirmation section ---------------------------------//

        recyclerView_cart = findViewById(R.id.recycler_cart);
        textView_address = findViewById(R.id.address_textview);
        textView_order_total = findViewById(R.id.order_total_textview);
        textView_total_amount = findViewById(R.id.total_textview);
        textview_note = findViewById(R.id.textview_note);
        textView_discount_total = findViewById(R.id.order_discount_textview);

        button_orderNow = findViewById(R.id.continue_order);


        System.out.println("hello1 " + customer_id + " " + card_title + " " + card_holder_num + " " + card_num + " " + card_exp_year + " " + card_card_exp_month + " " + card_cvc);

        System.out.println("card_details " + card_title + " " + card_holder_num + " " + card_num + " " + card_exp_year + " " + card_card_exp_month + " " + card_cvc);


        System.out.println("datae " + first_name + " " + last_name + "\n" + address1 + "\n" + address2 + selectedCity + "-" + selectedCountry + "\n" + phone_number);

        button_orderNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                System.out.println("payment_type " + pincode + city);

                if (paymentType.equalsIgnoreCase("COD")) {

//                    if (!order_id.equalsIgnoreCase("0"))
//                    {
//                        doUpdatecodNow(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);
//                    }else {
                    doOrderNow(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);

//                    }


                } else if (paymentType.equalsIgnoreCase("Credit Card")) {

                    if (cardtype.equalsIgnoreCase("saved_card")) {

                        doSavedCardStripeOrder(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code, card_title, card_holder_num, card_num, card_exp_year, card_card_exp_month, card_cvc, card_id);

                    } else {

                        GetStripeToken(card_num, card_card_exp_month, card_exp_year, card_cvc);
                    }

                } else if (paymentType.equalsIgnoreCase("Telr Payment")) {


                    if (telrType.equalsIgnoreCase("saved_card")) {
//                        if (!order_id.equalsIgnoreCase("0"))
//                        {
//                            dosavedupdtpayment(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);
//                        }else {
                        doTelrsavedRpayment(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);

//                        }

                    } else {
//                        if (!order_id.equalsIgnoreCase("0"))
//                        {
//                            doUpdateOrderNow(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);
//
//                        }else {
                        doTelRpayment(user_id, language_type, first_name, last_name, address1, address2, selectedCity, phone_number, selectedCountry, delivary_time, date, paymentType, address_id, coupon_code);

//                        }

                    }


                } else {

                    Toast.makeText(getApplicationContext(), R.string.payment_type, Toast.LENGTH_SHORT).show();
                }

            }
        });

        changeLang(language_type);


    }

    private void dosavedupdtpayment(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.UPDATE_ORDER_CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");
                                String card_id = jsonObject.getString("card_id");
                                String url = jsonObject.getString("url");
                                String refrence_no = jsonObject.getString("refrence_no");

                                editor.putString(ConstantData.PAYMENT_TYPE, "").apply();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("card_id", card_id);
                                intent.putExtra("url", url);
                                intent.putExtra("refrence_no", refrence_no);
                                intent.putExtra("payment_type", "Savedtelr");
                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutA " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " +
                        building_num + " " + phone_number + " " + selectedCity + " " + selectedCountry + " " + paymentType + " " + coupon_code + " "
                        + address_id
                        + " " + date + " " + confirm_total + " " + delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);
                params.put(Constants.TRANSACTION_REF_NO, transactioRef);
                params.put(Constants.UPDATE_ORDER_ID, order_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void doUpdatecodNow(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.UPDATE_ORDER_CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");

                                editor.putString(ConstantData.PAYMENT_TYPE, "").apply();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("payment_type", paymentType);
                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutcos " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " +
                        building_num + " " + phone_number + " " + selectedCity + " " + selectedCountry + " " + paymentType + " " + coupon_code + " " + address_id
                        + " " + date + " " + totalamount + " " + delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);
                params.put(Constants.UPDATE_ORDER_ID, order_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void doUpdateOrderNow(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.UPDATE_ORDER_CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codupdtresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {

                                String orderid = jsonObject.getString("order_id");

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                System.out.println("paymenttype " + paymentType);
                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("payment_type", paymentType);
                                intent.putExtra("url", jsonObject.getString("url"));
                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutA " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " +
                        building_num + " " + phone_number + " " + selectedCity + " " + selectedCountry + " " + payment_type + " " + coupon_code + " " + address_id
                        + " " + date + " " + totalamount + " " + delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);
                params.put(Constants.UPDATE_ORDER_ID, order_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    private void doTelrsavedRpayment(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");
                                String card_id = jsonObject.getString("card_id");
                                String url = jsonObject.getString("url");
                                String refrence_no = jsonObject.getString("refrence_no");

                                editor.putString(ConstantData.PAYMENT_TYPE, "").apply();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("payment_type", "");

                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutA " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " +
                        building_num + " " + phone_number + " " + selectedCity + " " + selectedCountry + " " + paymentType + " " + coupon_code + " "
                        + address_id + " " + note + " "
                        + " " + date + " " + confirm_total + " " + delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);
                params.put(Constants.TRANSACTION_REF_NO, transactioRef);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void doGetTelRCard(final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_TELR_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Responsetelr " + response);
                        myTelCarlist.clear();

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

                                    myTelCarlist.add(cardModel);


                                }

                                mytelrAdapter = new TelrAdapter(CheckOutActivity.this, myTelCarlist, telrSendDetail);
                                recyclerView_savedTelrcard.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView_savedTelrcard.setAdapter(mytelrAdapter);

                            } else {
                                frameLayout_recycler.setVisibility(View.GONE);
                                Toast.makeText(CheckOutActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void doTelRpayment(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");
                                String card_id = jsonObject.getString("card_id");
                                String url = jsonObject.getString("url");
                                String refrence_no = jsonObject.getString("refrence_no");

                                editor.putString(ConstantData.PAYMENT_TYPE, "").apply();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("card_id", card_id);
                                intent.putExtra("url", url);
                                intent.putExtra("refrence_no", refrence_no);
                                intent.putExtra("payment_type", paymentType);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("card_id", card_id);
                                intent.putExtra("url", url);
                                intent.putExtra("refrence_no", refrence_no);
                                intent.putExtra("first_name", first_name);
                                intent.putExtra("last_name", last_name);
                                intent.putExtra("address1", address1);
                                intent.putExtra("address2", address2);
                                intent.putExtra("building_num", building_num);
                                intent.putExtra("phone_number", phone_number);
                                intent.putExtra("selectedCity", selectedCity);
                                intent.putExtra("selectedCountry", selectedCountry);
                                intent.putExtra("paymentType", paymentType);
                                intent.putExtra("note", textInputEditText_note.getText().toString());
                                intent.putExtra("coupon_code", coupon_code);
                                intent.putExtra("address_id", address_id);
                                intent.putExtra("date", date);
                                intent.putExtra("confirm_total", confirm_total);
                                intent.putExtra("delivary_time", delivary_time);
                                intent.putExtra("transactioRef", transactioRef);
                                intent.putExtra("payment_type", "Telr Payment");

                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutA " + CheckOutActivity.this.user_id + " " + CheckOutActivity.this.first_name + " " + CheckOutActivity.this.last_name + " " + CheckOutActivity.this.address1 + " " + CheckOutActivity.this.address2 + " " +
                        building_num + " " + CheckOutActivity.this.phone_number + " " + CheckOutActivity.this.selectedCity + " " + CheckOutActivity.this.selectedCountry + " " + paymentType + " " + CheckOutActivity.this.coupon_code + " " + CheckOutActivity.this.address_id
                        + " " + CheckOutActivity.this.date + " " + totalamount + " " + CheckOutActivity.this.delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void GetStripeToken(String card_num, String card_card_exp_month, String card_exp_year, String card_cvc) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        final String publishableApiKey = BuildConfig.DEBUG ?
                "pk_test_MnYdjiV4RxJ7xp3fdm51QVDH" :
                getString(R.string.com_stripe_publishable_key);

        Card card = new Card(card_num,
                Integer.valueOf(card_card_exp_month),
                Integer.valueOf(card_exp_year),
                card_cvc);

        final Stripe stripe = new Stripe(this, "pk_test_MnYdjiV4RxJ7xp3fdm51QVDH");

        stripe.createToken(card, publishableApiKey, new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                progressDialog.dismiss();
                System.out.println("sTRIPDEdATDToken" + token.getId() + " " + token.getType() + " " + token.getCard());

//
                GetCustomerId(token.getId());

            }

            public void onError(Exception error) {
                progressDialog.dismiss();
                Log.d("Stripe", error.getLocalizedMessage());
                Toast.makeText(CheckOutActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetCustomerId(final String id) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STRIPE_CUSTOMER_IS_TOKEN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            progressDialog.dismiss();
                            System.out.println("getcustomer " + response);
//                            CheckOutActivity.this.progressDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("customer_info");
                                String customer_id = jsonObject1.getString("id");

                                OrderViaStripe(customer_id, id);

                                System.out.println("paymentCheckoutType " + sharedPreferences.getString(ConstantData.PAYMENT_TYPE, ""));

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            CheckOutActivity.this.progressDialog.dismiss();
                        }

                        System.out.println("GetCustomercardRes " + response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                params.put(Constants.STRIPETOKE, id);
                params.put(Constants.EMAIL, email);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void OrderViaStripe(final String customer_id, final String id) {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("responseOrder " + response);

                        try {

                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");
                                String card_id = jsonObject.getString("card_id");

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                                ChargeAPI(orderid, card_id, customer_id, id, confirm_total);

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                System.out.println("responseOrder " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                System.out.println("StripOrdr " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " + building_num + " " + phone_number
                        + " " + city + " " + pincode + " " + paymentType + " " + address_id + " " + date + " " + card_title + " " + card_holder_num
                        + " " + card_num + " " + card_exp_year + " " + card_card_exp_month + " " + card_cvc + " " + confirm_total);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, city);
                params.put(Constants.COMMUNITY_AREA, pincode);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.CARD_ID, "");
                params.put(Constants.CARD_TITLE, card_title);
                params.put(Constants.CARD_HOLDER_NAME, card_holder_num);
                params.put(Constants.CARD_NUMBER, card_num);
                params.put(Constants.CARD_EXP_YEAR, card_exp_year);
                params.put(Constants.CARD_EXP_MONTH, card_card_exp_month);
                params.put(Constants.CARD_CVV, card_cvc);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void ChargeAPI(final String orderid, final String card_id, final String customer_id, final String id, final String totalamount) {


        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STRIPE_CHARGE_API,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("chargeApi " + response);

                        try {

                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                doGetPaymentStatus(orderid, card_id, id, customer_id, progressDialog);


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
                System.out.println("chargeErr " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                System.out.println("ChargeData " + stripe_token + " " + product_detail + " " + totalamount + " " + orderid + " " + customer_id);
                params.put(ConstantData.STRIPE_TOKEN, id);
                params.put(ConstantData.PRODUCT_DETAIL, product_detail);
                params.put(ConstantData.AMOUNT, totalamount);
                params.put(ConstantData.ORDER_ID, orderid);
                params.put(ConstantData.CUSTOMER_ID, customer_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void doSavedCardStripeOrder(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String selectedCity, final String phone_number, final String selectedCountry, final String delivary_time, final String date, final String paymentType, final String address_id, final String coupon_code, final String card_title, final String card_holder_num, final String card_num, final String card_exp_year, final String card_card_exp_month, final String card_cvc, final String card_id) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("responseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                doChargeApi(progressDialog, orderid, confirm_total, product_detail, stripe_token, customer_id, card_id);

                            } else if (status.equalsIgnoreCase("fail")) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                System.out.println("StripOrdr " + user_id + " " +
                        first_name + " " +
                        last_name + " " +
                        address1 + " " +
                        address2 + " " +
                        building_num + " " +
                        phone_number + " " +
                        selectedCity + " " +
                        selectedCountry + " " +
                        paymentType + " " +
                        note + " " +
                        coupon_code + " " +
                        address_id + " " +
                        date + " " +
                        card_id + " " +
                        card_title + " " +
                        card_holder_num + " " +
                        card_num + " " +
                        card_exp_year + " " +
                        card_card_exp_month + " " +
                        card_cvc + " " +
                        confirm_total);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.CARD_ID, card_id);
                params.put(Constants.CARD_TITLE, card_title);
                params.put(Constants.CARD_HOLDER_NAME, card_holder_num);
                params.put(Constants.CARD_NUMBER, card_num);
                params.put(Constants.CARD_EXP_YEAR, card_exp_year);
                params.put(Constants.CARD_EXP_MONTH, card_card_exp_month);
                params.put(Constants.CARD_CVV, card_cvc);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void doGetSlotDetails() {

        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.DELIVARY_SLOT_LIST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("slotres " + response);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String ststus = jsonObject.getString("status");

                    if (ststus.equalsIgnoreCase("Success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("slot_info");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String slot_id = jsonObject1.getString("slot_id");
                            String slot_title = jsonObject1.getString("slot_title");

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                            slotList.add(slot_title);
                        }

                        createRadioButton(slotList.size());

                    } else {
                        Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void doGetCouroselCard(final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("ResponseCard " + response);
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
                                    String stripe_token = jsonObject1.getString("stripe_token");
                                    String card_holder_name = jsonObject1.getString("card_holder_name");
                                    String card_number = jsonObject1.getString("card_number");
                                    String card_exp_month = jsonObject1.getString("card_exp_month");
                                    String card_exp_year = jsonObject1.getString("card_exp_year");
                                    String card_user_id = jsonObject1.getString("card_user_id");
                                    String customer_id = jsonObject1.getString("customer_id");

                                    CardModel cardModel = new CardModel();

                                    cardModel.setCard_id(card_id);
                                    cardModel.setCard_title(card_title);
                                    cardModel.setStripe_token(stripe_token);
                                    cardModel.setCard_holder_name(card_holder_name);
                                    cardModel.setCard_number(card_number);
                                    cardModel.setCard_exp_month(card_exp_month);
                                    cardModel.setCard_exp_year(card_exp_year);
                                    cardModel.setCard_user_id(card_user_id);
                                    cardModel.setCustomer_id(customer_id);

                                    myCardArrayList.add(cardModel);


                                }

                                myCardAdapter = new ViewCardAdapter(CheckOutActivity.this, myCardArrayList, sendTokenCourousol);
                                recyclerView_savedCard.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                recyclerView_savedCard.setAdapter(myCardAdapter);

                            } else {
                                frameLayout_recycler.setVisibility(View.VISIBLE);
                                Toast.makeText(CheckOutActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i) {

            case R.id.button21:
                break;

            case R.id.button22:

                doGetMyView();
                break;

            default:
                break;
            // Nothing to do
        }
    }

    private void doGetMyView() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CheckOutActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_saved_address_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        alertbox = dialogBuilder.create();

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView_savedAddress);
        ArrayList myAddressArrayList;
        MyAddressAdapter myAddressAdapter = null;
        myAddressArrayList = new ArrayList();

        doGetMyAddress(alertbox, sharedPreferences.getString(ConstantData.USER_ID, ""), recyclerView, myAddressArrayList);

        alertbox.show();

    }

    private void doGetMyAddress(final AlertDialog alertbox, final String user_id, final RecyclerView recyclerView, final ArrayList myAddressArrayList) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("Address response " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("Card_info");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    address_id = jsonObject1.getString("address_id");
                                    String firstname = jsonObject1.getString("firstname");
                                    String lastname = jsonObject1.getString("lastname");
                                    String address1 = jsonObject1.getString("address1");
                                    String address2 = jsonObject1.getString("address2");
                                    String building_flatno = jsonObject1.getString("building_flatno");
                                    String phone_no = jsonObject1.getString("phone_no");
                                    String city = jsonObject1.getString("city");
                                    String pincode = jsonObject1.getString("community_area");
                                    String user_id = jsonObject1.getString("user_id");
                                    String entered = jsonObject1.getString("entered");

                                    MyAddress myAddress = new MyAddress();

                                    myAddress.setAddress_id(address_id);
                                    myAddress.setFirstname(firstname);
                                    myAddress.setLastname(lastname);
                                    myAddress.setAddress1(address1);
                                    myAddress.setAddress2(address2);
                                    myAddress.setBuilding_flatno(building_flatno);
                                    myAddress.setPhone_no(phone_no);
                                    myAddress.setCity(city);
                                    myAddress.setCommunity(pincode);
                                    myAddress.setUser_id(user_id);
                                    myAddress.setEntered(entered);

                                    myAddressArrayList.add(myAddress);

                                    System.out.println("addresslist " + myAddressArrayList.size());

                                }
                            } else {

                                alertbox.dismiss();
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                            if (myAddressArrayList.size() == 0) {
                                alertbox.dismiss();
                                Toast.makeText(CheckOutActivity.this, getString(R.string.noAdreess), Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            myAddressAdapter = new MySavedAddressAdapter(CheckOutActivity.this, myAddressArrayList, selectAddressConfirm);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CheckOutActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(myAddressAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                alertbox.dismiss();

//                frameLayout_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put(Constants.USER_ID, user_id);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {

        if (view == textInputEditText_date) {
            Calendar now = Calendar.getInstance();
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                    CheckOutActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.setMinDate(now);
            dpd.show(getFragmentManager(), "Datepickerdialog");

        } else if (view == button_toPayment) {
            first_name = textInputEditText_fname.getText().toString();
            last_name = textInputEditText_lname.getText().toString();
            address1 = textInputEditText_address1.getText().toString();
            address2 = textInputEditText_address2.getText().toString();
//            String community = textInputEditText_city.getText().toString();
            building_num = textInputEditText_building_number.getText().toString();
//            String city = textInputEditText_pincode.getText().toString();
            phone_number = textInputEditText_mobNumber.getText().toString();
            date = textInputEditText_date.getText().toString();
            note = textInputEditText_note.getText().toString();

            String MobilePattern = "[0-9]{10}";

            int radioBtnid = radioGroup.getCheckedRadioButtonId();

            System.out.println("radioId " + radioBtnid);

            if (first_name.length() == 0) {
                textInputEditText_fname.setError(getString(R.string.empty_first_name));
            } else if (last_name.length() == 0) {
                textInputEditText_lname.setError(getString(R.string.empty_last_name));
            } else if (address1.length() == 0) {
                textInputEditText_address1.setError(getString(R.string.empty_address_line1));
            } else if (address2.length() == 0) {
                textInputEditText_address2.setError(getString(R.string.empty_address_line2));
            } else if (spinner_city.getSelectedItem().toString().trim().equals(getString(R.string.select_city))) {
                Toast.makeText(getApplicationContext(), getString(R.string.select_city), Toast.LENGTH_SHORT).show();
            } else if (building_num.length() == 0) {
                textInputEditText_building_number.setError(getString(R.string.empty_flat_no));
            } else if (spinner_community.getSelectedItem().toString().trim().equals(getString(R.string.empty_community))) {
                Toast.makeText(getApplicationContext(), getString(R.string.empty_community), Toast.LENGTH_SHORT).show();
            } else if (phone_number.length() == 0) {
                textInputEditText_mobNumber.setError(getString(R.string.enter_phone_num));
            } else if (!phone_number.matches(MobilePattern)) {
                textInputEditText_mobNumber.setError(getString(R.string.invalid_phn_num));
            } else if (date.length() == 0) {
                textInputEditText_date.setError(getString(R.string.empty_date));

            } else if (note.length() == 0) {
                textInputEditText_note.setError(getString(R.string.empty_note));
            } else if (radioBtnid == -1) {
                Toast.makeText(this, R.string.empty_delivery_time, Toast.LENGTH_SHORT).show();
            } else {
                type = 1;
                doContinuetoPayment(first_name, last_name, address1, address2, selectedCountry, selectedCity, building_num, phone_number);
            }

        } else if (view == button_checkout_savedCard) {
            card_cvc = editText_cvv_code.getText().toString();

            System.out.println("savedCardRadio " + radioGroup_saved_card.getCheckedRadioButtonId());
            if (radioGroup_saved_card.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, R.string.select_saved_card, Toast.LENGTH_SHORT).show();
            } else if (editText_cvv_code.length() == 0) {
                Toast.makeText(this, R.string.empty_cvv, Toast.LENGTH_SHORT).show();
            } else {
                host.setCurrentTab(2);
                paymentType = "Credit Card";
                cardtype = "saved_card";
                host.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
            }

            doGetCartItem();


        } else if (view == button_addedCard) {
            card_holder_num = EditText_cardHoldername.getText().toString();
            card_num = EditText_cardnumber.getCardNumber();
            card_card_exp_month = EditText_month.getText().toString();
            card_exp_year = EditText_year.getText().toString();
            card_cvc = EditText_cvv.getText().toString();
            card_title = EditText_cardnumber.getCardType();
            card_id = "";

            if (card_holder_num.length() == 0) {
                EditText_cardHoldername.setError(getString(R.string.empty_card_holder));
            } else if (card_num.length() == 0) {
                EditText_cardnumber.setError(getString(R.string.empty_card_num));
            } else if (!EditText_cardnumber.isValid()) {
                EditText_cardnumber.setError(getString(R.string.invalide_card_num));
            } else if (card_card_exp_month.length() == 0) {
                EditText_month.setError(getString(R.string.empty_expirt_dt));
            } else if (card_exp_year.length() == 0) {
                EditText_year.setError(getString(R.string.empty_expiry_yeaer));
            } else if (card_cvc.length() == 0) {
                EditText_cvv.setError(getString(R.string.empty_cvv));
            } else {

                host.setCurrentTab(2);
                paymentType = "Credit Card";
                cardtype = "added_card";
                host.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                host.getTabWidget().getChildTabViewAt(1).setEnabled(false);

            }

            doGetCartItem();

        } else if (view == button_checkout_cod) {
            if (radioGroup_cod.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, R.string.select_cod_method, Toast.LENGTH_SHORT).show();
            } else {
                paymentType = "COD";
                host.setCurrentTab(2);
                host.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
            }
        } else if (view == button_checkout_telr) {

            if (radioGroup_telr.getCheckedRadioButtonId() == -1 && radioGroup_saved_card_telr.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, R.string.select_telr_method, Toast.LENGTH_SHORT).show();
//            }
//            else if (radioGroup_saved_card_telr.getCheckedRadioButtonId() == -1)
//            {
//                Toast.makeText(this, R.string.select_telr_method, Toast.LENGTH_SHORT).show();

//            }else
            } else if (radioGroup_saved_card_telr.getCheckedRadioButtonId() != -1 && myTelCarlist.size() == 0) {
                paymentType = "Telr Payment";
                Toast.makeText(this, "Select direct TelR Payment", Toast.LENGTH_SHORT).show();
            } else {


                paymentType = "Telr Payment";
                host.setCurrentTab(2);
                host.getTabWidget().getChildTabViewAt(0).setEnabled(false);
                host.getTabWidget().getChildTabViewAt(1).setEnabled(false);

            }
        }

        doGetCartItem();

    }


    private void doApplyCouponcode(final String user_id, final String total, final String s) {

        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.APPLY_COUPON_CODE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        System.out.println("responseCoupon  " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {

                                button_apply_coupon.setEnabled(false);

                                String total_amount = jsonObject.getString("total_amount");
                                discount_total = jsonObject.getString("discount_amount");
                                textView_coupon_total.setText(total_amount + " " + "AED");
                                textView_discount_total.setText(discount_total + " " + "AED");

                                confirm_total = total_amount;

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            } else if (status.equalsIgnoreCase("fail")) {

                                progressDialog.dismiss();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                System.out.println("couponCode " + user_id + " " + total + " " + s);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.CART_TOTAL, total);
                params.put(Constants.CODE, s);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void doContinuetoPayment(String fname, String lname, String address1, String address2, String city, String pincode, String building_num, String mob_num) {

        host.getTabWidget().getChildTabViewAt(1).setEnabled(true);
        host.getTabWidget().getChildTabViewAt(0).setEnabled(false);
        host.setCurrentTab(1);

    }


    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        textInputEditText_date.setText(date);
    }

    private class MyOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            String selectedItem = adapterView.getItemAtPosition(i).toString();

            //check which spinner triggered the listener
            switch (adapterView.getId()) {
                //country spinner
                case R.id.spinner_item:
                    //make sure the country was already selected during the onCreat

                    selectedCountry = selectedItem;

                    break;

                case R.id.spinner_city_item:

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

    public int getIndexFromElement(ArrayAdapter adapter, String element) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(element)) {
                return i;
            }
        }
        return 0;
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

                                totalamount = (jsonObject.getString("totalamount"));

                                System.out.println("confirm Total " + confirm_total);
                                textView_total_amount.setText(confirm_total + " " + "AED");
                                textView_order_total.setText(totalamount + " " + "AED");
                                textview_note.setText(note);
                                textView_discount_total.setText(discount_total + " " + "AED");

                                textView_address.setText(first_name + " " + last_name + "\n" + address1 + "\n" + address2 + selectedCity + "-" + selectedCountry + "\n" + phone_number);

                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++) {
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

                                    if (language_type.equalsIgnoreCase("en")) {
                                        product_name = jsonObject1.getString("product_name");
                                        product_description = jsonObject1.getString("product_description");

                                    } else {
                                        product_name = jsonObject1.getString("product_name_ar");
                                        product_description = jsonObject1.getString("product_description_ar");

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

                                cartAdapter = new OrderConfirmAdapter(CheckOutActivity.this, cartItemModelArrayList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView_cart.setLayoutManager(mLayoutManager);
                                recyclerView_cart.setAdapter(cartAdapter);
                                Log.d("TAG1", "do nothing");
                            } else if (status.equalsIgnoreCase("fail")) {
                                Log.d("TAG", "do nothing");
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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


    private void doOrderNow(final String user_id, String language_type, final String first_name, final String last_name, final String address1, final String address2, final String city, final String phone_number, final String community, final String delivary_time, final String date, final String payment_type, final String address_id, final String coupon_code) {

        final ProgressDialog progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.CHECKOUT__URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        System.out.println("codresponseOrder " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                String orderid = jsonObject.getString("order_id");

                                editor.putString(ConstantData.PAYMENT_TYPE, "").apply();

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("payment_type", paymentType);
                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();

                            } else if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("responseOrder " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("checkoutA " + user_id + " " + first_name + " " + last_name + " " + address1 + " " + address2 + " " +
                        building_num + " " + phone_number + " " + selectedCity + " " + selectedCountry + " " + payment_type + " " + coupon_code + " " + address_id
                        + " " + date + " " + totalamount + " " + delivary_time);

                params.put(Constants.USER_ID, user_id);
                params.put(Constants.FIRSTNAME, first_name);
                params.put(Constants.LASTNAME, last_name);
                params.put(Constants.ADDRESS_LINE1, address1);
                params.put(Constants.ADDRESS_LINE2, address2);
                params.put(Constants.BUILDING_NO, building_num);
                params.put(Constants.PHONE_NUMBER, phone_number);
                params.put(Constants.CITY, selectedCity);
                params.put(Constants.COMMUNITY_AREA, selectedCountry);
                params.put(Constants.PAYMENT_TYPE, paymentType);
                params.put(Constants.NOTE, note);
                params.put(Constants.COUPON_CODE, coupon_code);
                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.SCHEDULE_DATE, date);
                params.put(Constants.TOTAL, confirm_total);
                params.put(Constants.DELIVERY_TIME, delivary_time);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void doChargeApi(final ProgressDialog progressDialog, final String orderid, final String totalamount, final String product_detail, final String stripe_token, final String customer_id, final String card_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STRIPE_CHARGE_API,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("chargeApi " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                doGetPaymentStatus(orderid, card_id, stripe_token, customer_id, progressDialog);

                            } else {

                                progressDialog.dismiss();
                                Toast.makeText(CheckOutActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                System.out.println("chargeErr " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();

                System.out.println("ChargeData " + stripe_token + " " + product_detail + " " + totalamount + " " + orderid + " " + customer_id);
                params.put(ConstantData.STRIPE_TOKEN, stripe_token);
                params.put(ConstantData.PRODUCT_DETAIL, product_detail);
                params.put(ConstantData.AMOUNT, totalamount);
                params.put(ConstantData.ORDER_ID, orderid);
                params.put(ConstantData.CUSTOMER_ID, customer_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void doGetPaymentStatus(final String orderid, final String card_id, final String stripe_token, final String customer_id, final ProgressDialog progressDialog) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.PAYMENT_STATUS_API,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("ststuspay " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String ststus = jsonObject.getString("status");

                            if (ststus.equalsIgnoreCase("Success")) {
                                progressDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), ThankyouScreen.class);
                                intent.putExtra("Order_id", orderid);
                                intent.putExtra("payment_type", paymentType);

                                (CheckOutActivity.this).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                startActivity(intent);
                                finish();
                            } else if (ststus.equalsIgnoreCase("fail")) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

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

                params.put(Constants.ORDER_DETAIL_ID, orderid);
                params.put(Constants.CARD_ID, card_id);
                params.put(Constants.STRIPE_TOKEN, stripe_token);
                params.put(Constants.CUSTOMER_ID, customer_id);
                params.put(Constants.PAYMENT_TYPE, "Credit Card");

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void doGetSavedCard(final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_CARD_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("ResponseCard " + response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("Card_info");

                                for (int i = 0; i < 1; i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String card_id = jsonObject1.getString("card_id");
                                    String card_title = jsonObject1.getString("card_title");
                                    String stripe_token = jsonObject1.getString("stripe_token");
                                    String card_holder_name = jsonObject1.getString("card_holder_name");
                                    String card_number = jsonObject1.getString("card_number");
                                    String card_exp_month = jsonObject1.getString("card_exp_month");
                                    String card_exp_year = jsonObject1.getString("card_exp_year");
                                    String card_user_id = jsonObject1.getString("card_user_id");

                                    String newString = card_number.substring(0, card_number.length() - 4);
                                    String checkReplaceStr = "XXXXXXXXXXXX";

                                    newString = checkReplaceStr;

                                    int interval = 4;
                                    char separator = ' ';

                                    StringBuilder sb = new StringBuilder(newString);

                                    for (int j = 0; j < newString.length() / interval; j++) {
                                        sb.insert(((j + 1) * interval) + j, separator);
                                    }

                                    String withDashes = sb.toString();
                                    radioButton_savedcard.setText(withDashes + card_number.substring(card_number.length() - 4));

                                    if (card_number.matches(CardType.VISA)) {

                                        imageView_card.setImageResource(R.drawable.ic_card_visa);

                                    } else if (card_number.matches(CardType.MASTER_CARD)) {
                                        imageView_card.setImageResource(R.drawable.ic_card_mastercard);

                                    } else if (card_number.matches(CardType.AMERICAN_EXPRESS)) {
                                        imageView_card.setImageResource(R.drawable.ic_card_american_express);

                                    } else if (card_number.matches(CardType.DINERS_CLUB)) {
                                        imageView_card.setImageResource(R.drawable.ic_card_diners_club);

                                    } else if (card_number.matches(CardType.DISCOVER)) {
                                        imageView_card.setImageResource(R.drawable.ic_discover);

                                    } else if (card_number.matches(CardType.JCB)) {
                                        imageView_card.setImageResource(R.drawable.ic_card_jcb);

                                    } else {
                                        imageView_card.setImageResource(R.drawable.ic_card_defult);
                                    }
                                }


                                System.out.println("savedcardlen " + jsonArray.length());

                            } else {
                                radioButton_savedcard.setVisibility(View.GONE);
                                editText_cvv_code.setVisibility(View.GONE);
                                imageView_cvv.setVisibility(View.GONE);
                                imageView_card.setVisibility(View.GONE);
                                textView_noCard.setVisibility(View.VISIBLE);
                                textView_savedDetails.setVisibility(View.GONE);

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


    private void createRadioButton(int size) {
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(slotList.get(i));
            radioButton.setId(i);
            radioGroup.addView(radioButton);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), CartScreen.class);
        startActivity(intent);
        (CheckOutActivity.this).overridePendingTransition(R.anim.translate_left_to_right, R.anim.stable);
        finish();
    }
}
