package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import app.yaaymina.com.yaaymina.Activity.MySavedCardScreen;
import app.yaaymina.com.yaaymina.FourDigitCardFormatWatcher;
import app.yaaymina.com.yaaymina.Model.CardModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.CardType;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

/**
 * Created by ADMIN on 06-Dec-17.
 */

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<CardModel> cardModelArrayList;

    private SharedPreferences sharedPreferences;


    public MyCardAdapter(MySavedCardScreen mySavedCardScreen, ArrayList<CardModel> myCardArrayList) {

        this.context =mySavedCardScreen;
        this.cardModelArrayList = myCardArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_saved_card_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardModel cardModel = cardModelArrayList.get(position);

        holder.textView_cardNumber.setText(cardModelArrayList.get(position).getCard_number());
        System.out.println("textcardnumber "+ cardModel.getCard_number());


//        String newString = cardModel.getCard_number().substring(0, cardModel.getCard_number().length() - 4);
//        String checkReplaceStr = "XXXXXXXXXXXX";
//
//        newString = checkReplaceStr;
//
//        int interval = 4;
//        char separator = ' ';
//
//        StringBuilder sb = new StringBuilder(newString);
//
//        for(int j = 0; j < newString.length() / interval; j++) {
//            sb.insert(((j + 1) * interval) + j, separator);
//        }
//
//        String withDashes = sb.toString();
//
//        holder.textView_cardNumber.setText(withDashes + cardModel.getCard_number().substring(cardModel.getCard_number().length() - 4));


//               if (cardModel.getCard_number().matches(CardType.VISA))
//        {
//
//            holder.imageView_card.setImageResource(R.drawable.ic_card_visa);
//
//        }else if (cardModel.getCard_number().matches(CardType.MASTER_CARD))
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_card_mastercard);
//
//        }else if (cardModel.getCard_number().matches(CardType.AMERICAN_EXPRESS))
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_card_american_express);
//
//        }else if (cardModel.getCard_number().matches(CardType.DINERS_CLUB))
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_card_diners_club);
//
//        }else if (cardModel.getCard_number().matches(CardType.DISCOVER))
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_discover);
//
//        }else if (cardModel.getCard_number().matches(CardType.JCB))
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_card_jcb);
//
//        }else
//        {
//            holder.imageView_card.setImageResource(R.drawable.ic_card_defult);
//        }

        if (cardModel.getCard_title().contains("Visa")) {

            holder.imageView_card.setImageResource(R.drawable.ic_card_visa);

        } else if (cardModel.getCard_title().contains("MasterCard ")) {
            holder.imageView_card.setImageResource(R.drawable.ic_card_mastercard);

        } else if (cardModel.getCard_title().contains("AmericanExpress")) {
            holder.imageView_card.setImageResource(R.drawable.ic_card_american_express);

        } else if (cardModel.getCard_title().contains("DinersClub")) {
            holder.imageView_card.setImageResource(R.drawable.ic_card_diners_club);

        } else if (cardModel.getCard_title().contains("Discover")) {
            holder.imageView_card.setImageResource(R.drawable.ic_discover);

        } else if (cardModel.getCard_title().contains("JCB")) {
            holder.imageView_card.setImageResource(R.drawable.ic_card_jcb);

        } else {
            holder.imageView_card.setImageResource(R.drawable.ic_card_defult);
        }


    }

    private void doDeleteCard(final int position, final String card_id) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.DELETE_CARD_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        System.out.println("cardResponse "+ response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String ststus = jsonObject.getString("status");

                            if (ststus.equalsIgnoreCase("Success"))
                            {
                                cardModelArrayList.remove(position);
                                context.recreate();
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                sharedPreferences = context.getSharedPreferences(ConstantData.PREF_NAME,Context.MODE_PRIVATE);

                params.put(Constants.USER_ID,sharedPreferences.getString(ConstantData.USER_ID,""));
                params.put(Constants.CARD_ID,card_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return cardModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_cardNumber;
        private ImageView imageView_card, imageView_deleteCard;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_cardNumber = itemView.findViewById(R.id.cardnumber_textVw);
            imageView_card = itemView.findViewById(R.id.card_image);
            imageView_deleteCard = itemView.findViewById(R.id.delete_card_textView);


            imageView_deleteCard.setOnClickListener(new View.OnClickListener() {

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
                                    doDeleteCard(i,cardModelArrayList.get(i).getCard_id());
                                    dialog.dismiss();
                                    // Whatever...
                                }
                            }).show();

                }
            });
        }
    }
}
