package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.yaaymina.com.yaaymina.Activity.CheckOutScreen;
import app.yaaymina.com.yaaymina.Adapter.CartAdapter;
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 22-Nov-17.
 */

public class CartFragment extends Fragment implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView textView_toolbar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String user_id;
    private String language_type;

    private ArrayList<CartItemModel> cartItemModelArrayList;
    private String product_description,product_name;

    private RecyclerView recyclerView_cart;
    private CartAdapter cartAdapter;

    private TextView textView_total;

    private Button button_place_order;

    private Double final_weight;
    private Double final_totalPrice, price , weight;

    UnitInterFace unitInterFace;

    private Double sub_price, totalamount;

    private FrameLayout frameLayout_progress;
    private NestedScrollView nestedScrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart,container,false);


        cartItemModelArrayList = new ArrayList<>();

        sharedPreferences = getActivity().getSharedPreferences(ConstantData.PREF_NAME, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(ConstantData.USER_ID,"");
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        toolbar = getActivity().findViewById(R.id.toolbar_main);
        textView_toolbar = getActivity().findViewById(R.id.textToolbar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.mainColor));
        textView_toolbar.setText("My Cart");

        textView_total = view.findViewById(R.id.total_textView);
        recyclerView_cart = view.findViewById(R.id.cartRecycler);
        button_place_order = view.findViewById(R.id.place_order_btn);

        frameLayout_progress = view.findViewById(R.id.progress_framelayout);
        nestedScrollView = view.findViewById(R.id.nestedScroll);

        nestedScrollView.setVisibility(View.GONE);

        button_place_order.setOnClickListener(this);

        doGetCartItem();


        return view;
    }

    private void doGetCartItem() {

        frameLayout_progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+Constants.CART_PRODUCT_LIST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("CartResponse "+response);
                        frameLayout_progress.setVisibility(View.GONE);
                        nestedScrollView.setVisibility(View.VISIBLE);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            String total = jsonObject.getString("total");
                            totalamount = Double.valueOf(jsonObject.getString("totalamount"));

                            textView_total.setText(String.valueOf(totalamount) + " " + "AED");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
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

                                    if (language_type.equalsIgnoreCase("English"))
                                    {
                                        product_name = jsonObject1.getString("product_name");
                                        product_description = jsonObject1.getString("product_description");

                                    }else
                                    {
                                        product_name = jsonObject1.getString("product_name_ar");
                                        product_description= jsonObject1.getString("product_description_ar");

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

                                    cartItemModelArrayList.add(cartItemModel);
                                }

//                                cartAdapter = new CartAdapter(getActivity(),cartItemModelArrayList, unitInterFace);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView_cart.setLayoutManager(mLayoutManager);
                                recyclerView_cart.setAdapter(cartAdapter);

                            } else
                            {
                                Log.d("TAG","do nothing");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                frameLayout_progress.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
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

    public static CartFragment newInstance() {

        return new CartFragment();
    }

    @Override
    public void onClick(View view) {

        if (view == button_place_order)
        {
//
//            final_weight;
//
//            final_totalPrice =
            startActivity(new Intent(getActivity(), CheckOutScreen.class));
        }
    }
}
