package app.yaaymina.com.yaaymina.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.yaaymina.com.yaaymina.Model.ProductDetailTag;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 25-Nov-17.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private Context context;
    private List<ProductDetailTag> productDetailTagList;
    private ProductDetailTag productDetailTag;
    public TagAdapter(Context applicationContext, ArrayList<ProductDetailTag> productDetailTagArrayList) {

        this.context = applicationContext;
        this.productDetailTagList = productDetailTagArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        productDetailTag = productDetailTagList.get(position);

        Glide.with(context).load(productDetailTag.getTag_image())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView_tagItem);

    }

    @Override
    public int getItemCount() {
        return productDetailTagList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_tagItem;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_tagItem = itemView.findViewById(R.id.imgTag);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int i = getAdapterPosition();

                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                    final View dialogView = inflater.inflate(R.layout.layout_tag_desc, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);
                    final AlertDialog alertbox = dialogBuilder.create();

                    ImageView imageview_close = dialogView.findViewById(R.id.close_imageview);
                    ImageView imageview_tag = dialogView.findViewById(R.id.tag_image);
                    TextView textView_desc = dialogView.findViewById(R.id.tag_desc_textvw);
                    TextView textView_title = dialogView.findViewById(R.id.text_title);

                    imageview_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertbox.dismiss();
                        }
                    });

                    textView_desc.setText("-"+ " " +productDetailTagList.get(i).getTag_desc());

                    Glide.with(context).load(productDetailTagList.get(i).getTag_image())
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.mipmap.ic_launcher)
                            .into(imageview_tag);



                    alertbox.show();
                }
            });
                }

        }

}
