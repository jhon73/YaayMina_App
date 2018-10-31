package app.yaaymina.com.yaaymina.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.yaaymina.com.yaaymina.Model.SliderModelClass;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


/**
 * Created by eminent on 6/24/2017.
 */

public class HomeSliderAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context activity;
    ArrayList<SliderModelClass> image_arraylist;

    public HomeSliderAdapter(Context activity, ArrayList<SliderModelClass> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_slider_item, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.image_viewpager);

        SliderModelClass modelClass = image_arraylist.get(position);
        if (image_arraylist.size()==0)
        {
            im_slider.setVisibility(View.INVISIBLE);
        }
        else {

            Glide.with(activity.getApplicationContext()).load(modelClass.getImage_url())
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
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
