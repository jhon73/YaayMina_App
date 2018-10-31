package app.yaaymina.com.yaaymina.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.yaaymina.com.yaaymina.Fragments.ConfirmationFragment;
import app.yaaymina.com.yaaymina.Fragments.PaymentFragment;
import app.yaaymina.com.yaaymina.Fragments.ShipingFragment;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.SlidingTabLayout;

public class CheckOutScreen extends AppCompatActivity  {


    Toolbar toolbar;
    ViewPager view_pager;
    TabLayout tabLayout;
    TextView textView_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar_native);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view_pager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(view_pager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(view_pager);

        textView_toolbar = findViewById(R.id.title_checkout);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShipingFragment(), "Shipping");
        adapter.addFragment(new PaymentFragment(), "Payment");
        adapter.addFragment(new ConfirmationFragment(), "Confirmation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
