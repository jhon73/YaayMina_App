package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.yaaymina.com.yaaymina.Activity.CartScreen;
import app.yaaymina.com.yaaymina.Activity.ContactUsScreen;
import app.yaaymina.com.yaaymina.Activity.ProductListScreen;
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.CartItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 01-Dec-17.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<CartItemModel> cartItemModelArrayList;
    private UnitInterFace unitInterFace_;
    private String tag ;

    CartItemModel cartItemModel;

    private SharedPreferences sharedPreferences;

    public CartAdapter(Activity activity, ArrayList<CartItemModel> cartItemModelArrayList, UnitInterFace unitInterFace, String dialog) {

        this.context = activity;
        this.cartItemModelArrayList = cartItemModelArrayList;
        this.unitInterFace_ = unitInterFace;
        this.tag = dialog;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item,null,false);
        return new ViewHolder(view);
    }


    private  final Handler mHandler = new Handler(); // globle variable
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) { // Note: final holder
        sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME,Context.MODE_PRIVATE);

        cartItemModel = cartItemModelArrayList.get(position);

        final Double[] count = {Double.valueOf((cartItemModel.getTotal_unit()))};

        holder.textView_name.setText(cartItemModel.getProduct_name());
        holder.textView_weight.setText(String.valueOf(cartItemModel.getTotal_weight())+" "+ cartItemModel.getMeasurement());
        holder.textView_price.setText(String.valueOf(cartItemModel.getTotal_price()) + " " + cartItemModel.getCurrency());

        holder.textView_unit.setText(cartItemModel.getTotal_unit());

        Glide.with(context).load(cartItemModel.getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_productimg);

        holder.textView_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.progress_layout.setVisibility(View.VISIBLE);

                doRemoveCartItem(holder,position,cartItemModelArrayList.get(position).getCart_id(), sharedPreferences.getString(Constants.USER_ID,""));
            }
        });

        holder.imageView_plusItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() == true) {

                    Double unit = count[0];
                    unit = unit + 1.00;

                    final Double finalUnit = unit;
                    final Double finalUnit1 = unit;

                    final Double finalUnit2 = Double.valueOf(unit);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run () {


                            System.out.println("cartPrcie " + cartItemModelArrayList.get(position).getPrice());

                            holder.textView_unit.setText(String.valueOf(finalUnit)); // update new value

                            count[0] = finalUnit1;


                            Double final_weight = finalUnit2 * cartItemModelArrayList.get(position).getWeight();
                            Double final_price = final_weight*cartItemModelArrayList.get(position).getPrice();

                            holder.textView_price.setText((new DecimalFormat("##.##").format(final_price)) + " " + cartItemModelArrayList.get(position).getCurrency());
                            holder.textView_weight.setText(String.valueOf((new DecimalFormat("##.##").format(final_weight)+" "+cartItemModelArrayList.get(position).getMeasurement())));

                            doUpdateCart(cartItemModelArrayList.get(position).getCart_id(),final_weight, String.valueOf(finalUnit2),String.valueOf(final_weight * cartItemModelArrayList.get(position).getPrice()),sharedPreferences.getString(Constants.USER_ID,""));
                        }
                    });
                }
                else
                {

                    Toast.makeText(context, context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                }



            }
        });

        holder.imageView_minusItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() == true) {

                    Double unit = count[0];

                    if (unit>1)
                    {
                        unit = unit - 1;

                        final Double finalUnit = unit;
                        final Double finalUnit1 = unit;
                        final Double finalUnit2 = Double.valueOf(unit);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run () {


                                holder.textView_unit.setText(String.valueOf(finalUnit)); // update new value

                                count[0] = finalUnit1;

                                Double final_weight = finalUnit2 * cartItemModelArrayList.get(position).getWeight();
                                Double final_price = final_weight*cartItemModelArrayList.get(position).getPrice();

                                holder.textView_price.setText((new DecimalFormat("##.##").format(final_price)) + " " + cartItemModelArrayList.get(position).getCurrency());
                                holder.textView_weight.setText(String.valueOf((new DecimalFormat("##.##").format(final_weight)+" "+cartItemModelArrayList.get(position).getMeasurement())));

                                doUpdateCart(cartItemModelArrayList.get(position).getCart_id(),final_weight, String.valueOf(finalUnit2),String.valueOf(final_weight * cartItemModelArrayList.get(position).getPrice()),sharedPreferences.getString(Constants.USER_ID,""));

                            }
                        });
                    }
                    else
                    {
                        Log.d("Tag","do nothing");
                    }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                }



            }
        });


    }

    private void doUpdateCart(final String cart_id, final Double weight, final String unit, final String total_price, final String string) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.UPDATE_CART_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("updtcartres "+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            {
                                if (status.equalsIgnoreCase("Success"))
                                {
                                    unitInterFace_.onTagClicked(jsonObject.getString("totalamount"));

                                }
                                else
                                {
                                    Toast.makeText(context, R.string.err_updating_cart, Toast.LENGTH_SHORT).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                params.put(Constants.USER_ID,string);
                params.put(Constants.CART_ID,cart_id);
                params.put(Constants.PRODUCT_WEIGHT, String.valueOf(weight));
                params.put(Constants.UNIT,unit);
                params.put(Constants.TOTAL_PRICE,total_price);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void doRemoveCartItem(final ViewHolder holder, final int position, final String user_id, final String cart_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.DELETE_CART_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                System.out.println("removeres "+response);
                                holder.progress_layout.setVisibility(View.GONE);
                                cartItemModelArrayList.remove(position);
                                notifyDataSetChanged();
                                context.startActivity(new Intent(context, CartScreen.class));
                                (context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                                ((Activity)context).finish();
                            }
                            else
                            {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                holder.progress_layout.setVisibility(View.GONE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put(Constants.USER_ID,cart_id);
                params.put(Constants.CART_ID,user_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return cartItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_unit;
        private TextView textView_name, textView_weight, textView_price, textView_remove;
        private ImageView imageView_productimg, imageView_plusItem, imageView_minusItem;
        public FrameLayout progress_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.product_textView);
            textView_weight = itemView.findViewById(R.id.productWeight_textView);
            textView_price = itemView.findViewById(R.id.productPrice_textView);
            textView_remove = itemView.findViewById(R.id.productRemove_textView);
            textView_unit = itemView.findViewById(R.id.unit_textView);

            imageView_productimg = itemView.findViewById(R.id.thumbnail_imageview);
            imageView_plusItem = itemView.findViewById(R.id.plus_imageView);
            imageView_minusItem = itemView.findViewById(R.id.minus_imageView);
            progress_layout = itemView.findViewById(R.id.progress_layout);


            if (tag!=null && tag.equalsIgnoreCase("dialog"))
            {
                imageView_plusItem.setVisibility(View.GONE);
                imageView_minusItem.setVisibility(View.GONE);
                textView_unit.setVisibility(View.GONE);
            }else if (tag!=null && tag.equalsIgnoreCase("checkout"))
            {
                imageView_plusItem.setVisibility(View.GONE);
                imageView_minusItem.setVisibility(View.GONE);
                textView_unit.setVisibility(View.GONE);
                textView_remove.setVisibility(View.GONE);
            }else
            {
                Log.d("tag","do nothing");
            }
        }
    }
}
