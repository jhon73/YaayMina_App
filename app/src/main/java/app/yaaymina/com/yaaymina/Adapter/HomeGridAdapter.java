package app.yaaymina.com.yaaymina.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.yaaymina.com.yaaymina.Activity.DashBoardActivity;
import app.yaaymina.com.yaaymina.Activity.ProductListScreen;
import app.yaaymina.com.yaaymina.Activity.SplashScreen;
import app.yaaymina.com.yaaymina.Model.CategoryModel;
import app.yaaymina.com.yaaymina.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by ADMIN on 23-Nov-17.
 */

public class HomeGridAdapter extends BaseAdapter {

    private Activity _context;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private static LayoutInflater inflater = null;
//    private FragmentManager fragmentManager;

    public HomeGridAdapter(Activity context, ArrayList<CategoryModel> arrayList_categorydata) {

        this._context = context;
        this.categoryModelArrayList = arrayList_categorydata;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categoryModelArrayList.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v;

        if (view == null) {
            v = inflater.inflate(R.layout.layout_grid_item, null);
//            fragmentManager = ((Activity) _context).getFragmentManager();
        }

        else {
            v = view;
        }
            ImageView imageView_grid = (ImageView) v.findViewById(R.id.grid_item_image);
            TextView textView_grid = (TextView) v.findViewById(R.id.grid_item_text);

            CategoryModel e = new CategoryModel();
            e = categoryModelArrayList.get(i);
            textView_grid.setText(e.getCategory_title());
            System.out.println("cat_name " + e.getCategory_title());

            Glide.with(_context).load(e.getImage_url())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView_grid);



        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ConnectivityManager ConnectionManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() == true) {

                    Intent intent = new Intent(_context,ProductListScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data",categoryModelArrayList.get(i).getCategory_id());
                    intent.putExtra("category_name",categoryModelArrayList.get(i).getCategory_title());
                    (_context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

                    _context.startActivity(intent);
                    ((Activity)_context).finish();

                } else {
                    Toast.makeText(_context, _context.getResources().getString(R.string.no_Intenrnet), Toast.LENGTH_LONG).show();

                }

            }
        });
        return v;
    }


}
