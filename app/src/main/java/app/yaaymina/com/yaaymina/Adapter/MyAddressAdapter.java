package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Map;

import app.yaaymina.com.yaaymina.Activity.LoginScreen;
import app.yaaymina.com.yaaymina.Model.MyAddress;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 02-Dec-17.
 */

public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {

    private Activity activity_;
    private ArrayList<MyAddress> myAddressArrayList;

    private SharedPreferences sharedPreferences;
    private static LayoutInflater inflater = null;
    private String selectedCountry, selectedCity;


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
            " Al Bateen",
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

    public MyAddressAdapter(Activity activity, ArrayList<MyAddress> myAddressArrayList) {

        this.activity_ = activity;
        this.myAddressArrayList = myAddressArrayList;
        inflater = (LayoutInflater) activity_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public MyAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address_item, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAddressAdapter.ViewHolder holder, final int position) {

        final MyAddress myAddress = myAddressArrayList.get(position);

        System.out.println("naem " + myAddress.getCommunity() + " " + myAddress.getCity());

        holder.textView_title.setText(myAddress.getFirstname() + " " + myAddress.getLastname());
        holder.textView_address.setText(myAddress.getAddress1() + " " + myAddress.getAddress2() + " " + myAddress.getCity() + " " + myAddress.getCommunity());
        holder.textView_phone.setText(myAddress.getPhone_no());

        holder.button_change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                final View dialogView = inflater.inflate(R.layout.layout_add_address, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                final AlertDialog alertbox = dialogBuilder.create();

                final TextInputEditText textInputEditText_fname, textInputEditText_lname, textInputEditText_addressLine1, textInputEditText_addressLine2;
                final TextInputEditText textInputEditText_phone, textInputEditText_houseNo;
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

                TextView textView_title = dialogView.findViewById(R.id.title_text);

                imageView_cancel = dialogView.findViewById(R.id.close_imageview);

                imageView_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertbox.dismiss();
                    }
                });


                ArrayAdapter aa = new ArrayAdapter(activity_, R.layout.layout_spinner, country)
                {
                    @Override
                    public boolean isEnabled(int position) {

                        if (position == 1)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }

                    }
                };

                aa.setDropDownViewResource(R.layout.layout_spinner);
                spinner_city.setAdapter(aa);

                ArrayAdapter aa_community = new ArrayAdapter(activity_, R.layout.layout_spinner, community_arae)
                {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0)
                        {
                            return false;

                        }else
                        {
                            return true;
                        }
                    }
                };
                aa.setDropDownViewResource(R.layout.layout_spinner);
                spinner_community.setAdapter(aa_community);


                int i = getIndexFromElement(aa,myAddress.getCity());
                int j = getIndexFromElement(aa_community, myAddress.getCommunity());


                System.out.println("listingAdd " + myAddressArrayList.get(position).getCommunity() + " " + i);

                spinner_city.setSelection(i);
                spinner_community.setSelection(j);

                spinner_community.setOnItemSelectedListener(new MyOnItemSelectedListener());
                spinner_city.setOnItemSelectedListener(new MyOnItemSelectedListener());

                button_save = dialogView.findViewById(R.id.save_btn);

                button_save.setText(R.string.update);
                textView_title.setText(R.string.update_shipping_address);

                textInputEditText_fname.setText(myAddressArrayList.get(position).getFirstname());
                textInputEditText_lname.setText(myAddressArrayList.get(position).getLastname());
                textInputEditText_addressLine1.setText(myAddressArrayList.get(position).getAddress1());
                textInputEditText_addressLine2.setText(myAddressArrayList.get(position).getAddress2());
//                spinner_community.setSe().toString() = myAddressArrayList.get(position).getCity();
//                textInputEditText_pincode.setText(myAddressArrayList.get(position).getCity());
                textInputEditText_houseNo.setText(myAddressArrayList.get(position).getBuilding_flatno());
                textInputEditText_phone.setText(myAddressArrayList.get(position).getPhone_no());


                button_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ConnectivityManager ConnectionManager = (ConnectivityManager) activity_.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {

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
                                textInputEditText_fname.setError(activity_.getString(R.string.empty_first_name));
                            } else if (lname.length() == 0) {
                                textInputEditText_lname.setError(activity_.getString(R.string.empty_last_name));
                            } else if (add_line1.length() == 0) {
                                textInputEditText_addressLine1.setError(activity_.getString(R.string.empty_address_line1));
                            } else if (add_line2.length() == 0) {
                                textInputEditText_addressLine2.setError(activity_.getString(R.string.empty_address_line2));
                            } else if (spinner_city.getSelectedItem().toString().trim().equals(activity_.getString(R.string.select_city))) {
                                Toast.makeText(activity_, activity_.getString(R.string.select_city), Toast.LENGTH_SHORT).show();
                            } else if (spinner_community.getSelectedItem().toString().trim().equals(activity_.getString(R.string.empty_community))) {
                                Toast.makeText(activity_, activity_.getString(R.string.empty_community), Toast.LENGTH_SHORT).show();
                            } else if (phone.length() == 0) {
                                textInputEditText_phone.setError(activity_.getString(R.string.enter_phone_num));
                            } else if (!phone.matches(MobilePattern)) {
                                textInputEditText_phone.setError(activity_.getString(R.string.invalid_phn_num));

                            } else if (house.length() == 0) {
                                textInputEditText_houseNo.setError(activity_.getString(R.string.building_flat_no));
                            } else {

                                System.out.println("spinner data " + selectedCountry +" "+ selectedCity);
                                doUpdateMyAddress(fname, lname, add_line1, add_line2, selectedCountry, selectedCity, phone, house, myAddressArrayList.get(position).getAddress_id(), alertbox);
                            }

                        } else {

                            Toast.makeText(activity_, activity_.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                        }


                    }
                });


                alertbox.show();
            }
        });

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity_)
                        .setMessage(R.string.want_to_del)
                        .setCancelable(true)
                        .setNegativeButton(R.string.cancrl_, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ConnectivityManager ConnectionManager = (ConnectivityManager) activity_.getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                                if (networkInfo != null && networkInfo.isConnected() == true) {

                                    doDeleteAddress(myAddress.getAddress_id(), position);
                                    dialog.dismiss();

                                } else {

                                    Toast.makeText(activity_, activity_.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                                }

                                // Whatever...
                            }
                        }).show();

            }
        });

    }

    private void doUpdateMyAddress(final String fname, final String lname, final String add_line1, final String add_line2, final String city, final String pincode, final String phone, final String house, final String address_id, final AlertDialog alertbox) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.UPDATE_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        alertbox.dismiss();

                        System.out.println("Address response " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success")) {

                            activity_.recreate();

                            } else {

                                Toast.makeText(activity_, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                            notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                alertbox.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                System.out.println("saveAdd "+ pincode + " " + city);
                sharedPreferences = activity_.getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);

                params.put(Constants.USER_ID, sharedPreferences.getString(Constants.USER_ID, ""));
                params.put(Constants.FIRSTNAME, fname);
                params.put(Constants.LASTNAME, lname);
                params.put(Constants.ADDRESS_LINE1, add_line1);
                params.put(Constants.ADDRESS_LINE2, add_line2);
                params.put(Constants.CITY, pincode);
                params.put(Constants.PHONE_NUMBER, phone);
                params.put(Constants.COMMUNITY_AREA, city);
                params.put(Constants.BUILDING_NO, house);
                params.put(Constants.ADDRESS_ID, address_id);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_);
        requestQueue.add(stringRequest);

    }


    private void doDeleteAddress(final String address_id, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.DELETE_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                myAddressArrayList.remove(position);
                                notifyDataSetChanged();
                                activity_.recreate();
                            }
                            else
                            {
                                Toast.makeText(activity_, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                sharedPreferences = activity_.getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);

                params.put(Constants.ADDRESS_ID, address_id);
                params.put(Constants.USER_ID, sharedPreferences.getString(ConstantData.USER_ID, ""));

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity_);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return myAddressArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_title, textView_address, textView_phone;
        private Button button_change, button_delete;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.userName_textView);
            textView_address = itemView.findViewById(R.id.address_textView);
            textView_phone = itemView.findViewById(R.id.phone_textView);

            button_change = itemView.findViewById(R.id.btn_change);
            button_delete = itemView.findViewById(R.id.btn_delete);


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
                    if (selectedCountry != null) {

                        selectedCountry = selectedItem;
//                        Toast.makeText(getApplicationContext(),  selectedItem +" " + "Selected" ,
//                                Toast.LENGTH_LONG).show();
                    }

                    selectedCountry = selectedItem;

                    break;

                case R.id.spinner_city_item:

                    selectedCity = selectedItem;
//                    Toast.makeText(activity_, selectedCity, Toast.LENGTH_SHORT).show();
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
}
