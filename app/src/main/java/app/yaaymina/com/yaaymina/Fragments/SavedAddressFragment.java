package app.yaaymina.com.yaaymina.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 01-Dec-17.
 */

public class SavedAddressFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_oldaddress, container,false);

        return view;
    }

    public static SavedAddressFragment newInstance() {

        return new SavedAddressFragment();
    }
}
