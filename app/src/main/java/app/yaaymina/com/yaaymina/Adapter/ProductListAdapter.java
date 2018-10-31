package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.yaaymina.com.yaaymina.Activity.ProductDetailScreen;
import app.yaaymina.com.yaaymina.Activity.ProductListScreen;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 24-Nov-17.
 */

public class ProductListAdapter extends BaseAdapter {

    private Activity context;
    private List<ProductListModel> productList;
    private List<TagItemModel> itemTagList;
    private static LayoutInflater inflater = null;

    public ProductListAdapter(Activity activity, ArrayList<ProductListModel> productList, ArrayList<TagItemModel> productList1) {

        this.context = activity;
        this.productList = productList;
        this.itemTagList = productList1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = null;

        if (view1 == null)
        {
            view = inflater.inflate(R.layout.layout_productlist_item,null);

        }else
        {
            view1 = view;
        }

        TextView textView_product_name = view.findViewById(R.id.product_title);
        TextView textView_product_price = view.findViewById(R.id.product_price);
        ImageView imageView_product = view.findViewById(R.id.product_img);
        ImageView imageView_tag = view.findViewById(R.id.product_tag_img);

        ProductListModel productListModel = productList.get(i);

        final List<TagItemModel> productListModels = productList.get(i).getTagItemModels();


        System.out.println("itamlist "+productListModels.get(0).getTag_image());
        textView_product_name.setText(productListModel.getProduct_name());
        textView_product_price.setText(String.valueOf(productListModel.getProduct_price()) + " " +productListModel.getProduct_currency());

//        Glide.with(context).load(productListModel.getImage_url())
//                .into(imageView_product);

        Glide.with(context)
                .load(productListModel.getImage_url())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView_product);

        Glide.with(context).load(productListModels.get(0).getTag_image())
                .into(imageView_tag);

        imageView_tag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                final View dialogView = inflater.inflate(R.layout.layout_tag_desc, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);
                final AlertDialog alertbox = dialogBuilder.create();

                ImageView imageview_close = dialogView.findViewById(R.id.close_imageview);
                ImageView imageview_tag = dialogView.findViewById(R.id.tag_image);
                TextView textView_desc = dialogView.findViewById(R.id.tag_desc_textvw);

                imageview_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertbox.dismiss();
                    }
                });

                textView_desc.setText("-"+ " " +productListModels.get(0).getTag_desc());

                Glide.with(context).load(productListModels.get(0).getTag_image())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.ic_launcher)
                        .into(imageview_tag);


                alertbox.show();
            }
        });

        final ProductListModel finalProductListModel = productListModel;

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() == true) {

                    Intent intent = new Intent(context, ProductDetailScreen.class);
                    intent.putExtra("product_id", finalProductListModel.getProduct_id());
                    intent.putExtra("category_id", finalProductListModel.getCategory_id());
                    context.overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
                    ((Activity)context).startActivity(intent);

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                }

            }
        });

        return view;
    }

}




