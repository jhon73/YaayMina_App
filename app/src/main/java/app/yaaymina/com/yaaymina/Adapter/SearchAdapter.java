package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.yaaymina.com.yaaymina.Activity.ProductDetailScreen;
import app.yaaymina.com.yaaymina.Activity.SearchScreen;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 05-Dec-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<ProductListModel> listModelArrayList;

    public SearchAdapter(SearchScreen searchScreen, ArrayList<ProductListModel> arrayListSearch, ArrayList<TagItemModel> productList1) {

        this.context = searchScreen;
        this.listModelArrayList = arrayListSearch;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_item,null,false);

       return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductListModel model = listModelArrayList.get(position);

        holder.textView_search.setText(model.getProduct_name());
        holder.textView_price.setText(model.getProduct_price() + " " + "AED");

        Glide.with(context).load(model.getImage_url())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_product);

    }

    @Override
    public int getItemCount() {
        return listModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_search, textView_price;
        private ImageView imageView_product;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_search = itemView.findViewById(R.id.product_name);
            textView_price = itemView.findViewById(R.id.product_price);
            imageView_product = itemView.findViewById(R.id.product_img);


           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int i = getAdapterPosition();

                    Intent intent = new Intent(context, ProductDetailScreen.class);
                    intent.putExtra("product_id", listModelArrayList.get(i).getProduct_id());
                    intent.putExtra("category_id", listModelArrayList.get(i).getCategory_id());
                    context.overridePendingTransition(R.anim.translate_right_to_left,R.anim.stable);
                    ((Activity)context).startActivity(intent);
                }
            });
        }
    }
}
