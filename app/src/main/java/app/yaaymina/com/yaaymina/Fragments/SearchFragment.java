package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 22-Nov-17.
 */

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,container,false);

        return view;
    }

    public static SearchFragment newInstance() {

        return new SearchFragment();
    }
}
