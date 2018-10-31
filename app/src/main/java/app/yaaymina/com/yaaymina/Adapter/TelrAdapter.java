package app.yaaymina.com.yaaymina.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.yaaymina.com.yaaymina.CommonInterface.SendTokenCourousol;
import app.yaaymina.com.yaaymina.CommonInterface.TelrSendDetail;
import app.yaaymina.com.yaaymina.Model.CardModel;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 14-Feb-18.
 */

public class TelrAdapter extends RecyclerView.Adapter<TelrAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardModel> cardModelArrayList;
    TelrSendDetail sendTokenCourousol;

    public TelrAdapter(FragmentActivity activity, ArrayList<CardModel> myCardArrayList, TelrSendDetail sendTokenCourousol) {
        this.context = activity;
        this.cardModelArrayList = myCardArrayList;
        this.sendTokenCourousol = sendTokenCourousol;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_card_item,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CardModel cardModel = cardModelArrayList.get(position);

        holder.textView_cardNum.setText(cardModel.getCard_number());

        holder.imageView_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendTokenCourousol.onTokenSend(cardModel.getTransaction_ref_no(),cardModel.getCard_number(), cardModel.getCard_title());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cardModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_cardNum;
        private ImageView imageView_card;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_cardNum = itemView.findViewById(R.id.card_number_text);
            imageView_card = itemView.findViewById(R.id.img_card);
        }
    }
}

