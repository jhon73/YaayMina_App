package app.yaaymina.com.yaaymina.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import app.yaaymina.com.yaaymina.Adapter.HomeGridAdapter;
import app.yaaymina.com.yaaymina.Adapter.HomeSliderAdapter;
import app.yaaymina.com.yaaymina.Model.CategoryModel;
import app.yaaymina.com.yaaymina.Model.SliderModelClass;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 22-Nov-17.
 */

public class HomeFragment extends android.app.Fragment {

    private ArrayList<SliderModelClass> arrayList_image;
    private ArrayList<CategoryModel> arrayList_categorydata;

    private HomeSliderAdapter homeSliderAdapter;
    private HomeGridAdapter homeGridAdapter;

    private TextView[] dots;

    private ViewPager viewPager_home;
    private LinearLayout linearLayout_dots;
    private GridView gridView_home;

    private Handler handler;
    private final int delay = 2000;
    private int page = 0;

    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String language_type;
    String category_title;

    private Toolbar toolbar;

//    private LinearLayout linearLayout_native;
//    private LinearLayout linearLayout_main;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);

        setUserVisibleHint(true);
        sharedPreferences = getActivity().getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        arrayList_image = new ArrayList<>();
        arrayList_categorydata = new ArrayList<>();

        handler = new Handler();

        progressDialog = new ProgressDialog(getActivity());

        viewPager_home = view.findViewById(R.id.homeSlider_pager);
        linearLayout_dots = view.findViewById(R.id.ll_dots);
        gridView_home = view.findViewById(R.id.grid_view_home);

//        linearLayout_main = getActivity().findViewById(R.id.layout_mainlayer);
//        linearLayout_native = getActivity().findViewById(R.id.laayout_secondary_layer);
//        linearLayout_main.setVisibility(View.VISIBLE);
//        linearLayout_native.setVisibility(View.GONE);

        doGetSliderData();
        doGetCategoryList();


        viewPager_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        return view;

    }

    private void doGetCategoryList() {

        progressDialog.setMessage("Please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.CATEGORY_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("CategoryData "+response);

                        try {
                            progressDialog.dismiss();

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("category_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String category_id = jsonObject1.getString("category_id");
                                    String parent_id = jsonObject1.getString("parent_id");
                                    String category_slug = jsonObject1.getString("category_slug");
//                                    String category_title = jsonObject1.getString("category_title");
                                    String category_meta = jsonObject1.getString("category_meta");
                                    String category_description = jsonObject1.getString("category_description");
                                    String category_display_order = jsonObject1.getString("category_display_order");
                                    String category_active = jsonObject1.getString("category_active");
                                    String category_count_item = jsonObject1.getString("category_count_item");
                                    String category_published_article = jsonObject1.getString("category_published_article");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String category_updated = jsonObject1.getString("category_updated");
                                    String category_entered = jsonObject1.getString("category_entered");

                                    if (language_type.equalsIgnoreCase("English"))
                                    {
                                        category_title = jsonObject1.getString("category_title");

                                    }else if (language_type.equalsIgnoreCase("Aerabic"))
                                    {
                                        category_title = jsonObject1.getString("category_title_ar");
                                    }
//                                    String category_title_ar = jsonObject1.getString("category_title_ar");
                                    String image_url = jsonObject1.getString("image_url");


                                    CategoryModel categoryModel = new CategoryModel();
                                    categoryModel.setCategory_id(category_id);
                                    categoryModel.setParent_id(parent_id);
                                    categoryModel.setCategory_title(category_title);
                                    categoryModel.setImage_url(image_url);
                                    categoryModel.setCategory_slug(category_slug);
                                    categoryModel.setCategory_meta(category_meta);
                                    categoryModel.setCategory_description(category_description);
                                    categoryModel.setCategory_display_order(category_display_order);
                                    categoryModel.setCategory_active(category_active);
                                    categoryModel.setCategory_count_item(category_count_item);
                                    categoryModel.setCategory_published_article(category_published_article);
                                    categoryModel.setThumbnail(thumbnail);
                                    categoryModel.setCategory_updated(category_updated);
                                    categoryModel.setCategory_entered(category_entered);
//                                    categoryModel.setCategory_title_ar(category_title_ar);

                                    arrayList_categorydata.add(categoryModel);
                                }

                                homeGridAdapter = new HomeGridAdapter(getActivity(),arrayList_categorydata);
                                gridView_home.setAdapter(homeGridAdapter);

                            }else
                            {                        progressDialog.dismiss();

                                Log.d("message ",message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void doGetSliderData() {

        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Constants.BASE_URL + Constants.SLIDER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("Sliderdata "+response);
                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("slider_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    String slider_id = jsonObject1.getString("slide_id");
                                    String upload_id = jsonObject1.getString("upload_id");
                                    String image_url = jsonObject1.getString("image_url");

                                    SliderModelClass sliderModelClass = new SliderModelClass();

                                    sliderModelClass.setImage_url(image_url);
                                    sliderModelClass.setProduct_id(upload_id);
                                    sliderModelClass.setSlider_id(slider_id);

                                    arrayList_image.add(sliderModelClass);
                                }

                                homeSliderAdapter = new HomeSliderAdapter(getActivity(), arrayList_image);
                                 viewPager_home.setAdapter(homeSliderAdapter);
                                addBottomDots(0);


                            }else
                            {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void addBottomDots(int current_page) {

        dots = new TextView[arrayList_image.size()];

        linearLayout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#9E9E9E"));
            linearLayout_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[current_page].setTextColor(getResources().getColor(R.color.mainColor));
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    Runnable runnable = new Runnable() {
        public void run() {
            if (homeSliderAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager_home.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            Toast.makeText(getActivity(), "visible", Toast.LENGTH_SHORT).show();
//            toolbar = getActivity().findViewById(R.id.toolbar_main);
//
//            toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);
//
//            // called here
////            linearLayout_main = getActivity().findViewById(R.id.toolbar_main);
////            linearLayout_main.setNavigationIcon(R.drawable.ic_back_arrow);
//            toolbar.setBackgroundResource(R.drawable.ic_toolbar_logo_bg);
//
//        }
//        else
//        {
//            Toast.makeText(getActivity(), "invisible", Toast.LENGTH_SHORT).show();
//        }
//    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.your_menu_xml, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

}
