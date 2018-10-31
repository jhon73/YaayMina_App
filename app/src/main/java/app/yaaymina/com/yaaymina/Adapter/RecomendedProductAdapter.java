package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.yaaymina.com.yaaymina.Activity.ProductDetailScreen;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 25-Nov-17.
 */

public class RecomendedProductAdapter extends RecyclerView.Adapter<RecomendedProductAdapter.ViewHolder> {

    private Activity context;
    private List<ProductListModel> productListModelList;
    private String product_id;

    public RecomendedProductAdapter(Activity applicationContext, ArrayList<ProductListModel> productListModelsArrayList, String is_product_id) {

        this.context = applicationContext;
        this.productListModelList = productListModelsArrayList;
        this.product_id = is_product_id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommended,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



        ProductListModel productListModel = productListModelList.get(position);

        List<TagItemModel> productListModels = productListModel.getTagItemModels();

        final TagItemModel tagItemModel = productListModels.get(position);

        holder.textView_name.setText(productListModel.getProduct_name());
        holder.textView_price.setText(productListModel.getProduct_price() + " " + productListModel.getProduct_currency());

        System.out.println("pro_name "+productListModel.getProduct_name());

        Glide.with(context).load(productListModel.getImage_url())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_product);

        Glide.with(context).load(tagItemModel.getTag_image())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_tag);

    }

    @Override
    public int getItemCount() {
        return productListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_name, textView_price;
        private ImageView imageView_tag, imageView_product;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.product_title);
            textView_price = itemView.findViewById(R.id.product_price);
            imageView_product = itemView.findViewById(R.id.product_img);
            imageView_tag = itemView.findViewById(R.id.product_tag_img);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();

                    Intent intent = new Intent(context, ProductDetailScreen.class);
                    intent.putExtra("product_id", productListModelList.get(i).getProduct_id());
                    intent.putExtra("category_id", productListModelList.get(i).getCategory_id());
                    context.overridePendingTransition(R.anim.translate_right_to_left,R.anim.stable);
                    ((Activity)context).startActivity(intent);
                    ((Activity)context).finish();


                }
            });
        }
    }
}
