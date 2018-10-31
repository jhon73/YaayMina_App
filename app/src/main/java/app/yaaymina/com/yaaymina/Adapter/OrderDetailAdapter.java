package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.yaaymina.com.yaaymina.Activity.OrderDetailScreen;
import app.yaaymina.com.yaaymina.Model.OrderDetail;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 22-Dec-17.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetail> orderDetailArrayList;


    public OrderDetailAdapter(Activity orderDetailScreen, ArrayList<OrderDetail> orderDetailArrayList) {

        this.context = orderDetailScreen;
        this.orderDetailArrayList = orderDetailArrayList;
    }

    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_detail_final, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.ViewHolder holder, int position) {

        OrderDetail orderDetail = orderDetailArrayList.get(position);

        holder.textView_name.setText(orderDetail.getName());
        holder.textView_price.setText(orderDetail.getTotal_price() + orderDetail.getCurrency_in());
        holder.textView_weight.setText(orderDetail.getTotal_weight() + " " + orderDetail.getMessarment());

        Glide.with(context).load(orderDetail.getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_productimg);

    }

    @Override
    public int getItemCount() {
        return orderDetailArrayList.size();
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

            imageView_productimg = itemView.findViewById(R.id.thumbnail_imageview);
            progress_layout = itemView.findViewById(R.id.progress_layout);


        }
    }
}
