package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import app.yaaymina.com.yaaymina.AbstractMethods;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 28-Nov-17.
 */

public class MyProfileFragment extends Fragment implements View.OnFocusChangeListener {

    private TextInputEditText textinput_userName,textinput_email,textinput_phone;
    private ImageView imageView_username,imageView_email,imageView_phone;
    private Button button_update;
    private String user_name,email,phone;

    private SharedPreferences sharedPreferences;
    private String user_id;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);

        sharedPreferences = getActivity().getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);

        progressDialog = new ProgressDialog(getActivity());

        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");

        if (user_id.length()!=0)
        {
            doGetProfileData(user_id);
        }

        textinput_userName = view.findViewById(R.id.userName_textInput);
        textinput_email = view.findViewById(R.id.email_textInput);
        textinput_phone = view.findViewById(R.id.phone_textInput);

        imageView_username = view.findViewById(R.id.userName_imgVw);
        imageView_email = view.findViewById(R.id.email_imgVw);
        imageView_phone = view.findViewById(R.id.phone_imgVw);

        button_update = view.findViewById(R.id.updt_btn);

        textinput_email.setOnFocusChangeListener(this);
        textinput_phone.setOnFocusChangeListener(this);
        textinput_userName.setOnFocusChangeListener(this);

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_name = textinput_userName.getText().toString();
                email = textinput_email.getText().toString();
                phone = textinput_phone.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String MobilePattern = "[0-9]{10}";

                if (user_name.length() == 0)
                {
                    textinput_userName.setError("Enter user name");
                }else if (email.length() == 0)
                {
                    textinput_email.setError("Enter email address");
                }else if (!email.matches(emailPattern))
                {
                    textinput_email.setError("Invalid email address");
                }else  if (phone.length() == 0)
                {
                    textinput_phone.setError("Enter phone number");
                }else if (!phone.matches(MobilePattern))
                {
                    textinput_phone.setError("Invalid phone number");
                } else
                {
                    doUpdate(user_name,email,phone);
                }

            }
        });

        return view;

    }

    private void doGetProfileData(final String user_id) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.USER_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String username = jsonObject1.getString("username");
                                    String email = jsonObject1.getString("email");
                                    String phone = jsonObject1.getString("phone");

                                    textinput_userName.setText(username);
                                    textinput_email.setText(email);
                                    textinput_phone.setText(phone);



                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("profileresonse " +response);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void doUpdate(final String user_name, final String email, final String phone) {

        progressDialog.setMessage("Please wait... ");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PROFILE_UPDATE_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        System.out.println("updt_utl "+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                getActivity().getFragmentManager().popBackStack();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("profileresonse " +response);
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
                params.put(Constants.USER_NAME,user_name);
                params.put(Constants.EMAIL,email);
                params.put(Constants.PHONE,phone);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public static MyProfileFragment newInstance() {

        return new MyProfileFragment();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == textinput_userName)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user_active);
            AbstractMethods.hideKeyboard(getActivity(),view);


        }else if (view == textinput_email)
        {
            imageView_email.setImageResource(R.drawable.ic_email);
            imageView_phone.setImageResource(R.drawable.ic_call);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(getActivity(),view);


        }else if (view == textinput_phone)
        {
            imageView_email.setImageResource(R.drawable.ic_email_active);
            imageView_phone.setImageResource(R.drawable.ic_call_active);
            imageView_username.setImageResource(R.drawable.ic_user);
            AbstractMethods.hideKeyboard(getActivity(),view);


        }
    }
}
