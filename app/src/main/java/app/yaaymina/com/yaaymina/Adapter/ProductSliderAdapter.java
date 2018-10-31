package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.yaaymina.com.yaaymina.Activity.ProductDetailScreen;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by ADMIN on 25-Nov-17.
 */

public class ProductSliderAdapter extends PagerAdapter {


    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<String> image_arraylist;


    public ProductSliderAdapter(ProductDetailScreen productDetailScreen, ArrayList<String> sliderList) {
        this.activity = productDetailScreen;
        this.image_arraylist = sliderList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_product_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.image_viewpager);

        if (image_arraylist.size()==0)
        {
            im_slider.setVisibility(View.INVISIBLE);
        }
        else {

            Glide.with(activity.getApplicationContext()).load(image_arraylist.get(position))
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(im_slider);

        }

        container.addView(view);

        return view;

    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;
        container.removeView(view);
    }
}
