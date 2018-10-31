package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.yaaymina.com.yaaymina.CommonInterface.SelectAddressConfirm;
import app.yaaymina.com.yaaymina.Model.MyAddress;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 04-Dec-17.
 */

public class MySavedAddressAdapter extends RecyclerView.Adapter<MySavedAddressAdapter.ViewHolder> {

    private Activity activity_;
    private ArrayList<MyAddress> myAddressArrayList;

    private SharedPreferences sharedPreferences;

    private SelectAddressConfirm selectAddressConfirm;

    public MySavedAddressAdapter(Activity activity, ArrayList<MyAddress> myAddressArrayList, SelectAddressConfirm selectAddressConfirm) {

        this.activity_= activity;
        this.myAddressArrayList = myAddressArrayList;
        this.selectAddressConfirm = selectAddressConfirm;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_saved_address_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final MyAddress myAddress = myAddressArrayList.get(position);

        System.out.println("naem "+myAddress.getFirstname() + " " + myAddress.getLastname());

        holder.textView_title.setText(myAddress.getFirstname() + " " + myAddress.getLastname());
        holder.textView_address.setText(myAddress.getAddress1() + " " + myAddress.getAddress2() + " " + myAddress.getCommunity() + " " +myAddress.getCity() );
        holder.textView_phone.setText(myAddress.getPhone_no());




    }


    @Override
    public int getItemCount() {
        return myAddressArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_title, textView_address, textView_phone;
        private Button button_select;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.userName_textView);
            textView_address = itemView.findViewById(R.id.address_textView);
            textView_phone = itemView.findViewById(R.id.phone_textView);

            button_select = itemView.findViewById(R.id.btn_select_address);

            button_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int i = getAdapterPosition();

                    selectAddressConfirm.onAddressSelected("dismiss",myAddressArrayList.get(i).getFirstname(),myAddressArrayList.get(i).getLastname(),myAddressArrayList.get(i).getAddress1(),myAddressArrayList.get(i).getAddress2()
                    ,myAddressArrayList.get(i).getCommunity(),myAddressArrayList.get(i).getCity(),myAddressArrayList.get(i).getPhone_no(), myAddressArrayList.get(i).getBuilding_flatno(), myAddressArrayList.get(i).getAddress_id(), myAddressArrayList.get(i).getEntered());
                }
            });

        }
    }
}

