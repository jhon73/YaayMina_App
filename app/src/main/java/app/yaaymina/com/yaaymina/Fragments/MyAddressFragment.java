package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import app.yaaymina.com.yaaymina.Adapter.MyAddressAdapter;
import app.yaaymina.com.yaaymina.Model.MyAddress;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 28-Nov-17.
 */

public class MyAddressFragment extends Fragment {

    Button button_addAddress;

    private SharedPreferences sharedPreferences;
    private String user_id;

    private FrameLayout frameLayout_progress;

    private ArrayList<MyAddress> myAddressArrayList;
    private MyAddressAdapter myAddressAdapter;

    private RecyclerView recyclerView_address;
    private LinearLayout linearLayout_container;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myaddress,container,false);

        button_addAddress = view.findViewById(R.id.addAdress_btn);
        frameLayout_progress = view.findViewById(R.id.progress_frame);
        recyclerView_address = view.findViewById(R.id.recyclerView_adapter);
        linearLayout_container = view.findViewById(R.id.layout_container);

        linearLayout_container.setVisibility(View.GONE);
        frameLayout_progress.setVisibility(View.VISIBLE);


        sharedPreferences = getActivity().getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");

        myAddressArrayList = new ArrayList<>();

        doGetMyAddress(user_id);

        button_addAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                final View dialogView = inflater.inflate(R.layout.layout_add_address, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                final AlertDialog alertbox = dialogBuilder.create();


                final TextInputEditText textInputEditText_fname, textInputEditText_lname, textInputEditText_addressLine1, textInputEditText_addressLine2;
                final TextInputEditText textInputEditText_city, textInputEditText_pincode, textInputEditText_phone, textInputEditText_houseNo;
                Button button_save;
                ImageView imageView_cancel;

                textInputEditText_fname = dialogView.findViewById(R.id.firstName_textInput);
                textInputEditText_lname = dialogView.findViewById(R.id.lastName_textInput);
                textInputEditText_addressLine1 = dialogView.findViewById(R.id.addressLine1_textInput);
                textInputEditText_addressLine2 = dialogView.findViewById(R.id.addressLine2_textInput);
                textInputEditText_city = dialogView.findViewById(R.id.city_textInput);
                textInputEditText_pincode = dialogView.findViewById(R.id.pincode_textInput);
                textInputEditText_phone = dialogView.findViewById(R.id.phone_textInput);
                textInputEditText_houseNo = dialogView.findViewById(R.id.houseno_textInput);

                imageView_cancel = dialogView.findViewById(R.id.close_imageview);

                imageView_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertbox.dismiss();
                    }
                });

                button_save = dialogView.findViewById(R.id.save_btn);

                button_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String MobilePattern = "[0-9]{10}";

                        String fname = textInputEditText_fname.getText().toString();
                        String lname = textInputEditText_lname.getText().toString();
                        String add_line1 = textInputEditText_addressLine1.getText().toString();
                        String add_line2 = textInputEditText_addressLine2.getText().toString();
                        String city = textInputEditText_city.getText().toString();
                        String pincode = textInputEditText_pincode.getText().toString();
                        String phone = textInputEditText_phone.getText().toString();
                        String house = textInputEditText_houseNo.getText().toString();

                        if (fname.length()==0)
                        {
                            textInputEditText_fname.setError("Enter First name");
                        }else if (lname.length()==0)
                        {
                            textInputEditText_lname.setError("Enter Last name");
                        }else if (add_line1.length()==0)
                        {
                            textInputEditText_addressLine1.setError("Enter Address line 1");
                        }else if (add_line2.length()==0)
                        {
                            textInputEditText_addressLine2.setError("Enter Address line 2");
                        }else if (city.length()==0)
                        {
                            textInputEditText_city.setError("Enter City");
                        }else if (pincode.length()==0)
                        {
                            textInputEditText_pincode.setError("Enter Pincode");
                        }else if (phone.length()==0)
                        {
                            textInputEditText_phone.setError("Enter Phone number");
                        }else if (!phone.matches(MobilePattern))
                        {
                            textInputEditText_phone.setError("Invalid Phone number");

                        }else if (house.length()==0)
                        {
                            textInputEditText_houseNo.setError("Enter Building/Flat number");
                        }else
                        {
                            frameLayout_progress.setVisibility(View.VISIBLE);
                            doSaveMyAddress(fname,lname,add_line1,add_line2,city,pincode,phone,house,alertbox);
                        }
                    }
                });


                alertbox.show();
            }
        });
        return view;
    }

    private void doGetMyAddress(final String user_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.GET_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        frameLayout_progress.setVisibility(View.GONE);
                        linearLayout_container.setVisibility(View.VISIBLE);

                        System.out.println("Address response "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {

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
                                    String pincode = jsonObject1.getString("pincode");
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
//                                    myAddress.setPincode(pincode);
                                    myAddress.setUser_id(user_id);
                                    myAddress.setEntered(entered);

                                    myAddressArrayList.add(myAddress);

                                    System.out.println("addresslist "+myAddressArrayList.size());

                                }
                            }

                            else
                            {
                            }

                            myAddressAdapter = new MyAddressAdapter(getActivity(),myAddressArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView_address.setLayoutManager(mLayoutManager);
                            recyclerView_address.setAdapter(myAddressAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                frameLayout_progress.setVisibility(View.GONE);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void doSaveMyAddress(final String fname, final String lname, final String add_line1, final String add_line2, final String city, final String pincode, final String phone, final String house, final AlertDialog alertbox) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.ADD_ADDRESS_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        myAddressArrayList.clear();
                        frameLayout_progress.setVisibility(View.GONE);
                        alertbox.dismiss();

                        System.out.println("Address response "+response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                JSONArray jsonArray = jsonObject.getJSONArray("Address_info");

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
                                    String pincode = jsonObject1.getString("pincode");
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
//                                    myAddress.setPincode(pincode);
                                    myAddress.setUser_id(user_id);
                                    myAddress.setEntered(entered);

                                    myAddressArrayList.add(myAddress);

                                    System.out.println("addresslist "+myAddressArrayList.size());

                                }
                            }

                            else
                            {
                            }

                            myAddressAdapter = new MyAddressAdapter(getActivity(),myAddressArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView_address.setLayoutManager(mLayoutManager);
                            recyclerView_address.setAdapter(myAddressAdapter);


                        } catch (JSONException e) {
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
                params.put(Constants.PINCODE,pincode);
                params.put(Constants.BUILDING_NO,house);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public static MyAddressFragment newInstance() {

        return new MyAddressFragment();
    }
}
