package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 22-Nov-17.
 */

public class MyAccountFragment extends Fragment implements View.OnClickListener {

    private LinearLayout  linearLayout_order, linearLayout_address;
    private TextView linearLayout_profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        linearLayout_profile = view.findViewById(R.id.profile_layout);
        linearLayout_order = view.findViewById(R.id.order_layout);
        linearLayout_address = view.findViewById(R.id.address_layout);


        linearLayout_profile.setOnClickListener(this);
        linearLayout_order.setOnClickListener(this);
        linearLayout_address.setOnClickListener(this);


        return view;
    }

    public static MyAccountFragment newInstance() {

        return new MyAccountFragment();
    }

    @Override
    public void onClick(View view) {

        if (view == linearLayout_profile)
        {
            MyProfileFragment fragment = MyProfileFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_frame, fragment);
            ft.addToBackStack(String.valueOf(new MyAccountFragment()));
            ft.commit();
        }
        else if (view == linearLayout_order)
        {
            Toast.makeText(getActivity(), "my order", Toast.LENGTH_SHORT).show();
        }else if (view == linearLayout_address)
        {
            MyAddressFragment fragment = MyAddressFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_frame, fragment);
            ft.addToBackStack(String.valueOf(new MyAccountFragment()));
            ft.commit();
        }
    }
}