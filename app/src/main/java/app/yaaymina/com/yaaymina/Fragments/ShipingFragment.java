package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Webservice.BadgeDrawable;
import co.ceryle.segmentedbutton.SegmentedButton;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;

/**
 * Created by ADMIN on 01-Dec-17.
 */

public class ShipingFragment extends android.support.v4.app.Fragment {

    private SegmentedButtonGroup group;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shipping,container,false);

        group = (SegmentedButtonGroup) view.findViewById(R.id.dynamic_drawable_group);

        if (savedInstanceState == null)
        {
            NewAddressFragment fragment = NewAddressFragment.newInstance();
            android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.segment_container, fragment);
            ft.commit();
        }

        group.setOnClickedButtonListener(new SegmentedButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(int position) {

                if (position==0)
                {
                    NewAddressFragment fragment = NewAddressFragment.newInstance();
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.segment_container, fragment);
                    ft.commit();
                }else
                {
                    SavedAddressFragment fragment = SavedAddressFragment.newInstance();
                    android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.segment_container, fragment);
                    ft.commit();
                }
            }
        });


        return view;
    }



    public ShipingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ShipingFragment newInstance() {

        return new ShipingFragment();
    }


}
