package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.yaaymina.com.yaaymina.Activity.ReorderHistoryScreen;
import app.yaaymina.com.yaaymina.Model.ReOrderHistory;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 28-Dec-17.
 */

public class ReOrderHistoryAdapter extends RecyclerView.Adapter<ReOrderHistoryAdapter.ViewHolder>{

    private Activity context;
    private ArrayList<ReOrderHistory> reOrderHistoryArrayList;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ReOrderHistoryAdapter(ReorderHistoryScreen listener, ArrayList<ReOrderHistory> reOrderHistories) {

        this.context = listener;
        this.reOrderHistoryArrayList = reOrderHistories;
    }

    @Override
    public ReOrderHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reorderhistory,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReOrderHistoryAdapter.ViewHolder holder, int position) {

        ReOrderHistory reOrderHistory = reOrderHistoryArrayList.get(position);

        holder.textView_total.setText(reOrderHistory.getTotal_amount() +" " + "AED");
        holder.textView_orderid.setText(context.getResources().getString(R.string.order_id) + " " + reOrderHistory.getSchedule_id());
        holder.textView_date.setText(reOrderHistory.getDate());

    }

    @Override
    public int getItemCount() {
        return reOrderHistoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_date, textView_total, textView_orderid;
        private ImageView imageView_del;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_date = itemView.findViewById(R.id.textview_date);
            textView_orderid = itemView.findViewById(R.id.textview_orderid);
            textView_total = itemView.findViewById(R.id.textview_total);

            imageView_del = itemView.findViewById(R.id.imageview_del);

            imageView_del.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    final int i = getAdapterPosition();

                    new AlertDialog.Builder(context)
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
                                    doDeleteAddress(reOrderHistoryArrayList.get(i).getSchedule_id(), i, reOrderHistoryArrayList.get(i).getOrder_id());
                                    dialog.dismiss();
                                    // Whatever...
                                }
                            }).show();

                }
            });

        }
    }

    private void doDeleteAddress(final String schedule_id, final int i, final String order_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.SCHEDULE_ORDER_REMOVE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("removeschduleOrder " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String ststus = jsonObject.getString("status");

                            if (ststus.equalsIgnoreCase("Success"))
                            {
                                reOrderHistoryArrayList.remove(i);
                                notifyDataSetChanged();
                                context.recreate();
//                                context.finish();

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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                final String final_schedule_id  = schedule_id.substring(1);

                System.out.println("orderHistorydel " + final_schedule_id + " "+ schedule_id);
                params.put(Constants.SCHEDULE_ID, final_schedule_id);
                params.put(Constants.ORDER_DETAIL_ID, order_id);

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
