package app.yaaymina.com.yaaymina.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.yaaymina.com.yaaymina.CommonInterface.SendTokenCourousol;
import app.yaaymina.com.yaaymina.Model.CardModel;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 07-Dec-17.
 */

public class ViewCardAdapter extends RecyclerView.Adapter<ViewCardAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardModel> cardModelArrayList;
    SendTokenCourousol sendTokenCourousol;

    public ViewCardAdapter(FragmentActivity activity, ArrayList<CardModel> myCardArrayList, SendTokenCourousol sendTokenCourousol) {
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

                sendTokenCourousol.onTokenSend(cardModel.getCard_id(),cardModel.getCard_number(), cardModel.getCard_holder_name(), cardModel.getCard_exp_month(), cardModel.getCard_exp_year(), cardModel.getStripe_token(), cardModel.getCustomer_id());
            }
        });



    }

    @Override
    public int getItemCount() {
        return cardModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_cardNum;
        private RelativeLayout imageView_card;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_cardNum = itemView.findViewById(R.id.card_number_text);
            imageView_card = itemView.findViewById(R.id.img_card);
        }
    }
}
