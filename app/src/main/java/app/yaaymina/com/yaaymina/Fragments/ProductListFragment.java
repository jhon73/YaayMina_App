package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import app.yaaymina.com.yaaymina.Adapter.ProductListAdapter;
import app.yaaymina.com.yaaymina.Model.ProductListModel;
import app.yaaymina.com.yaaymina.Model.TagItemModel;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Webservice.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ADMIN on 23-Nov-17.
 */

public class ProductListFragment extends Fragment {

    private GridView gridView_productList;
    private List<ProductListModel> productList;
    private List<TagItemModel> productList1;

    private ProductListAdapter productListAdapter;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private String language_type;
    private String selected_category_title,selected_product_name,selected_product_description;
//    private LinearLayout linearLayout_native;
    private Toolbar linearLayout_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productlist,container,false);

        setUserVisibleHint(true);
        String getArgument = getArguments().getString("data");
        String action = getArguments().getString("action");

        sharedPreferences = getActivity().getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        language_type = sharedPreferences.getString(ConstantData.LANGUAGE_SELECTION,"");

        progressDialog = new ProgressDialog(getActivity());



//        if (!action.equalsIgnoreCase(""))
//        {
//            linearLayout_main.setNavigationIcon(R.drawable.ic_back_arrow);
//        }

//        linearLayout_main.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                linearLayout_main.setNavigationIcon(R.drawable.ic_drawer_icon);
//                getActivity().getFragmentManager().popBackStack();
//            }
//        });

//        linearLayout_main.setBackgroundColor(getResources().getColor(R.color.mainColor));
//        linearLayout_native = getActivity().findViewById(R.id.laayout_secondary_layer);
//        linearLayout_main.setVisibility(View.GONE);
//        linearLayout_native.setVisibility(View.VISIBLE);

        if (getArgument.length()!=0)
        {
            doGetProductList(getArgument);
        }else
        {
            Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT).show();
        }



        productList = new ArrayList<>();
        productList1 = new ArrayList<>();

        gridView_productList = view.findViewById(R.id.grid_view_productlist);

        return view;

    }

    private void doGetProductList(final String getArgument) {

        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + Constants.PRODUCT_DETAIL_CATEGORY_ID_BASED_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                    System.out.println("productlistResponse "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONArray jsonArray = jsonObject.getJSONArray("product_info");

                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    System.out.println("jsonObject1Response "+jsonObject1);

                                    String product_id = jsonObject1.getString("product_id");
                                    String category_id = jsonObject1.getString("category_id");
                                    String product_code = jsonObject1.getString("product_code");
                                    String product_tag = jsonObject1.getString("product_tag");
                                    String product_name = jsonObject1.getString("product_name");
                                    String product_rating = jsonObject1.getString("product_rating");
                                    String product_description = jsonObject1.getString("product_description");
                                    String product_overview = jsonObject1.getString("product_overview");
                                    String product_specifications = jsonObject1.getString("product_specifications");
                                    Double product_weight = Double.valueOf(jsonObject1.getString("product_weight"));
                                    String product_minorder = jsonObject1.getString("product_minorder");
                                    String color_option = jsonObject1.getString("color_option");
                                    String size_option = jsonObject1.getString("size_option");
                                    String product_cost = jsonObject1.getString("product_cost");
                                    String product_tax = jsonObject1.getString("product_tax");
                                    String product_price = jsonObject1.getString("product_price");
                                    String product_price_cross = jsonObject1.getString("product_price_cross");
                                    String product_currency = jsonObject1.getString("product_currency");
                                    String product_availability = jsonObject1.getString("product_availability");
                                    String product_status = jsonObject1.getString("product_status");
                                    String thumbnail = jsonObject1.getString("thumbnail");
                                    String product_updated = jsonObject1.getString("product_updated");
                                    String product_entered = jsonObject1.getString("product_entered");
                                    String category_title = jsonObject1.getString("category_title");
                                    String category_title_ar = jsonObject1.getString("category_title_ar");
                                    String product_name_ar = jsonObject1.getString("product_name_ar");
                                    String product_description_ar = jsonObject1.getString("product_description_ar");
                                    String image_url = jsonObject1.getString("image_url");


                                    if (language_type.equalsIgnoreCase("English"))
                                    {
                                         selected_category_title = jsonObject1.getString("category_title");
                                         selected_product_name = jsonObject1.getString("product_name");
                                         selected_product_description = jsonObject1.getString("product_description");

                                    }else if (language_type.equalsIgnoreCase("Aerabic"))
                                    {
                                        selected_category_title = jsonObject1.getString("category_title_ar");
                                         selected_product_name = jsonObject1.getString("product_name_ar");
                                         selected_product_description = jsonObject1.getString("product_description_ar");
                                    }

//                                    linearLayout_main.setTitle(selected_category_title);

                                    ProductListModel productListModel = new ProductListModel();
                                    productListModel.setProduct_id(product_id);
                                    productListModel.setCategory_id(category_id);
                                    productListModel.setProduct_code(product_code);
                                    productListModel.setProduct_tag(product_tag);
                                    productListModel.setProduct_name(selected_product_name);
                                    productListModel.setProduct_rating(product_rating);
                                    productListModel.setProduct_description(selected_product_description);
                                    productListModel.setProduct_overview(product_overview);
                                    productListModel.setProduct_specifications(product_specifications);
                                    productListModel.setProduct_weight(product_weight);
                                    productListModel.setProduct_minorder(product_minorder);
                                    productListModel.setColor_option(color_option);
                                    productListModel.setSize_option(size_option);
                                    productListModel.setProduct_cost(product_cost);
                                    productListModel.setProduct_tax(product_tax);
                                    productListModel.setProduct_price(product_price);
                                    productListModel.setProduct_price_cross(product_price_cross);
                                    productListModel.setProduct_currency(product_currency);
                                    productListModel.setProduct_availability(product_availability);
                                    productListModel.setProduct_status(product_status);
                                    productListModel.setThumbnail(thumbnail);
                                    productListModel.setProduct_updated(product_updated);
                                    productListModel.setProduct_entered(product_entered);
                                    productListModel.setCategory_title(selected_category_title);
                                    productListModel.setCategory_title_ar(category_title_ar);
                                    productListModel.setProduct_name_ar(product_name_ar);
                                    productListModel.setProduct_description_ar(product_description_ar);
                                    productListModel.setImage_url(image_url);

                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("tag");

                                    for (int j = 0; j < jsonArray1.length(); j++)
                                    {
                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(0);

                                        System.out.println("jsonObject2Response "+jsonObject1);

                                        String tag_id = jsonObject2.getString("tag_id");
                                        String tag_name = jsonObject2.getString("tag_name");
                                        String tag_image = jsonObject2.getString("tag_image");
                                        String tag_desc = jsonObject2.getString("tag_desc");
                                        String tag_create_date = jsonObject2.getString("tag_create_date");

                                        TagItemModel productListModel1 = new TagItemModel();

                                        productListModel1.setTag_id(tag_id);
                                        productListModel1.setTag_name(tag_name);
                                        productListModel1.setTag_image(tag_image);
                                        productListModel1.setTag_desc(tag_desc);
                                        productListModel1.setTag_create_date(tag_create_date);

                                        productList1.add(productListModel1);
                                        productListModel.setTagItemModels(productList1);
                                        productListModel.addDataObject(productListModel1);

                                    }

                                        productList.add(productListModel);
                                }



                                productListAdapter = new ProductListAdapter(getActivity(), (ArrayList<ProductListModel>) productList,(ArrayList<TagItemModel>)productList1);
                                gridView_productList.setAdapter(productListAdapter);



                            }
                            else
                            {
                                Log.d("message",message);
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                System.out.println("cat_id "+getArgument);

                HashMap<String,String> params = new HashMap<>();
                params.put(Constants.CATEGORY_ID,getArgument);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser) {
//            Toast.makeText(getActivity(), "visible", Toast.LENGTH_SHORT).show();
//            // called here
//            linearLayout_main = getActivity().findViewById(R.id.toolbar_main);
//            linearLayout_main.setNavigationIcon(R.drawable.ic_back_arrow);
//            linearLayout_main.setBackgroundColor(getResources().getColor(R.color.mainColor));
//
//        }
//        else
//        {
//            Toast.makeText(getActivity(), "invisible", Toast.LENGTH_SHORT).show();
//        }
    }






