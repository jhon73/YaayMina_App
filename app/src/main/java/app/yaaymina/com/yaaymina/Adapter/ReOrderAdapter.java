package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import app.yaaymina.com.yaaymina.Activity.MyReorderScreen;
import app.yaaymina.com.yaaymina.CommonInterface.UnitInterFace;
import app.yaaymina.com.yaaymina.Model.ReOrder;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 23-Dec-17.
 */

public class ReOrderAdapter extends RecyclerView.Adapter<ReOrderAdapter.ViewHolder> {
    
    private Activity context;
    private ArrayList<ReOrder> reOrderArrayList;
    private UnitInterFace unitInterFace_;

    private String orderid;

    private SharedPreferences sharedPreferences;

    public ReOrderAdapter(MyReorderScreen myReorderScreen, ArrayList<ReOrder> reOrderArrayList, UnitInterFace unitInterFace, String order_id) {

        this.context = myReorderScreen;
        this.reOrderArrayList = reOrderArrayList;
        this.unitInterFace_ = unitInterFace;
        this.orderid = order_id;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_item,null,false);
        return new ViewHolder(view);
    }

    private  final Handler mHandler = new Handler();
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME,Context.MODE_PRIVATE);

        ReOrder reOrder = reOrderArrayList.get(position);

        final Double[] count = {Double.valueOf((reOrder.getTotal_unit()))};

        holder.textView_name.setText(reOrder.getProduct_name());
        holder.textView_price.setText(String.valueOf(reOrder.getTotal_price()+" " + reOrderArrayList.get(position).getCurrency()));
        holder.textView_weight.setText(reOrder.getTotal_weight()+ " " + reOrder.getMessarment());

        holder.textView_unit.setText(reOrder.getTotal_unit());

        Glide.with(context).load(reOrder.getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_productimg);

        holder.textView_remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                holder.progress_layout.setVisibility(View.VISIBLE);

                doRemoveCartItem(holder,position,reOrderArrayList.get(position).getCart_id(), sharedPreferences.getString(Constants.USER_ID,""));
            }
        });

        holder.imageView_plusItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Double unit = count[0];
                unit = unit + 1;

                final Double finalUnit = unit;
                final Double finalUnit1 = unit;

                final Double finalUnit2 = Double.valueOf(unit);
                mHandler.post(new Runnable() {

                    @Override
                    public void run () {



                        holder.textView_unit.setText(String.valueOf(finalUnit)); // update new value

                        count[0] = finalUnit1;


                        Double final_weight = finalUnit2 * reOrderArrayList.get(position).getWeight();
                        Double final_price = final_weight*reOrderArrayList.get(position).getPrice();

                        holder.textView_price.setText((new DecimalFormat("##.##").format(final_price)) + " " + reOrderArrayList.get(position).getCurrency());
                        holder.textView_weight.setText(String.valueOf(new DecimalFormat("##.##").format(final_weight)+" " + reOrderArrayList.get(position).getMessarment()));

                        doUpdateCart(reOrderArrayList.get(position).getCart_id(),final_weight, String.valueOf(finalUnit1),String.valueOf(final_weight * reOrderArrayList.get(position).getPrice()),sharedPreferences.getString(Constants.USER_ID,""),orderid);

                    }
                });
            }
        });

        holder.imageView_minusItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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

                            Double final_weight = finalUnit2 * reOrderArrayList.get(position).getWeight();
                            Double final_price = final_weight*reOrderArrayList.get(position).getPrice();

                            holder.textView_price.setText((new DecimalFormat("##.##").format(final_price)) + " " + reOrderArrayList.get(position).getCurrency());
                            holder.textView_weight.setText(String.valueOf(String.valueOf(new DecimalFormat("##.##").format(final_weight)+" "+ reOrderArrayList.get(position).getMessarment())));

                            doUpdateCart(reOrderArrayList.get(position).getCart_id(),final_weight, String.valueOf(finalUnit1),String.valueOf(final_weight * reOrderArrayList.get(position).getPrice()),sharedPreferences.getString(Constants.USER_ID,""),orderid);


                        }
                    });
                }
                else
                {
                    Log.d("Tag","do nothing");
                }

            }
        });
    }

    private void doUpdateCart(final String cart_id, final Double weight, final String unit, final String total_price, final String string, final String orderid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.REORDER_CART_UPDATE,
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
                                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

                System.out.println("reordeParams "+ string +" "+cart_id +" "+ weight+" " + unit +" " + total_price + " " + orderid);

                params.put(Constants.USER_ID,string);
                params.put(Constants.CART_ID,cart_id);
                params.put(Constants.PRODUCT_WEIGHT, String.valueOf(weight));
                params.put(Constants.UNIT,unit);
                params.put(Constants.TOTAL_PRICE,total_price);
                params.put(Constants.ORDER_DETAIL_ID,orderid);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return reOrderArrayList.size();
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

        }
    }

    private void doRemoveCartItem(final ViewHolder holder, final int position, final String user_id, final String cart_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.REORDER_CART_DELETE,
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
                                reOrderArrayList.remove(position);
                                notifyDataSetChanged();
                                context.recreate();
                            }
                            else
                            {
                                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



//                        context.startActivity(new Intent(context, MyReorderScreen.class));
//                        ((Activity)context).finish();
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

}
