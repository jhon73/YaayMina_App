package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import app.yaaymina.com.yaaymina.Activity.ManageScheduleScreen;
import app.yaaymina.com.yaaymina.Activity.MyOrdersScreen;
import app.yaaymina.com.yaaymina.Activity.MyReorderScreen;
import app.yaaymina.com.yaaymina.Activity.OrderDetailScreen;
import app.yaaymina.com.yaaymina.Activity.ProductListScreen;
import app.yaaymina.com.yaaymina.Model.MyOrderModel;
import app.yaaymina.com.yaaymina.Model.ReOrder;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 06-Dec-17.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<MyOrderModel> myOrderModellist;
    private SharedPreferences sharedPreferences;

    public MyOrderAdapter(MyOrdersScreen myOrdersScreen, ArrayList<MyOrderModel> myOrderModels) {

        this.context = myOrdersScreen;
        this.myOrderModellist = myOrderModels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_order_item,null,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MyOrderModel myOrderModel = myOrderModellist.get(position);

        holder.textView_date.setText(myOrderModel.getOrder_date());
        holder.textView_price.setText(myOrderModel.getTotal() + " " + "AED");
        holder.textView_order_id.setText(context.getString(R.string.order_id) +" "+myOrderModel.getOrder_id());
        holder.textView_order_status.setText(myOrderModel.getStatus());

        if (myOrderModel.getStatus().equalsIgnoreCase(context.getString(R.string.dispatched)))
        {
            holder.textView_order_status.setTextColor(context.getResources().getColor(R.color.dispatched));
        }else if (myOrderModel.getStatus().equalsIgnoreCase(context.getString(R.string.onway)))
        {
            holder.textView_order_status.setTextColor(context.getResources().getColor(R.color.onway));
        }else if (myOrderModel.getStatus().equalsIgnoreCase(context.getString(R.string.pending)))
        {
            holder.textView_order_status.setTextColor(context.getResources().getColor(R.color.pending));
        }else if (myOrderModel.getStatus().equalsIgnoreCase(context.getString(R.string.delivered)))
        {
            holder.textView_order_status.setTextColor(context.getResources().getColor(R.color.delivered));
        }
    }

    @Override
    public int getItemCount() {
        return myOrderModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_date, textView_order_id, textView_price;
        private TextView textView_order_status;
        private ImageView imageView_reOrder;
        private LinearLayout linearLayout_row;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_date = itemView.findViewById(R.id.date_tv);
            textView_order_id = itemView.findViewById(R.id.order_id_tv);
            textView_price = itemView.findViewById(R.id.price_tv);
            textView_order_status = itemView.findViewById(R.id.status_tv);
            linearLayout_row = itemView.findViewById(R.id.ll_item_row);

            imageView_reOrder = itemView.findViewById(R.id.reOrder_image);

            linearLayout_row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected() == true) {


                        int i = getAdapterPosition();

                        Intent intent = new Intent(context,OrderDetailScreen.class);
                        intent.putExtra("order_id",myOrderModellist.get(i).getOrder_id());
                        (context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                        context.startActivity(intent);
                        context.finish();

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                    }

                }
            });

            imageView_reOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected() == true) {


                        int i = getAdapterPosition();

                        sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME, MODE_PRIVATE);

                        doGetReorderItem(sharedPreferences.getString(ConstantData.USER_ID,""),myOrderModellist.get(i).getOrder_id(),i);


                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                    }



                }
            });

        }
    }

    private void doGetReorderItem(final String user_id, final String order_id, final int i) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.RE_ORDER_CART_DETAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {


                        System.out.println("reOrderres " + response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {

                                String total = jsonObject.getString("total");

                                if (total.equalsIgnoreCase("0"))
                                {
                                    Toast.makeText(context, R.string.cant_reorder, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Intent intent = new Intent(context,ManageScheduleScreen.class);
                                    intent.putExtra("order_id",myOrderModellist.get(i).getOrder_id());
                                    (context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                                    context.startActivity(intent);
                                    context.finish();
                                }


                            }else if (status.equalsIgnoreCase("fail"))
                            {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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

                HashMap<String, String> params = new HashMap<>();

                String final_order_id = order_id.substring(1);

                System.out.println("reoder " + order_id);
                params.put(Constants.USER_ID, user_id);
                params.put(Constants.ORDER_DETAIL_ID, final_order_id);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


}
