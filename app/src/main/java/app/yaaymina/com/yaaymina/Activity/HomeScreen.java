package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import app.yaaymina.com.yaaymina.Fragments.CartFragment;
import app.yaaymina.com.yaaymina.Fragments.ContactusFragment;
import app.yaaymina.com.yaaymina.Fragments.HomeFragment;
import app.yaaymina.com.yaaymina.Fragments.MyAccountFragment;
import app.yaaymina.com.yaaymina.Fragments.ProductListFragment;
import app.yaaymina.com.yaaymina.Fragments.SearchFragment;
import app.yaaymina.com.yaaymina.Model.BottomNavigationViewHelper;
import app.yaaymina.com.yaaymina.R;
import app.yaaymina.com.yaaymina.Storage.ConstantData;
import app.yaaymina.com.yaaymina.Storage.SharedPref;

public class HomeScreen extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    public ActionBarDrawerToggle toggle;

    private Toolbar toolbar;

    private SharedPref session;

    private BottomNavigationView bottomNavigationView;
    private ProductListFragment productListFragment;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_);



        sharedPreferences = getSharedPreferences(ConstantData.PREF_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        session = new SharedPref(HomeScreen.this);

        Intent intent = getIntent();

        if (intent.hasExtra("action"))
        {
            Log.d("tag","doNothing");
        }else
        {
            session.checkLogin();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(false);

        toolbar = findViewById(R.id.toolbar_main);

        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        setUpNavigationView();
        setupBottomNavigation();


        if (savedInstanceState == null) {

            loadHomeFragment();
        }

//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//
//                    Toast.makeText(HomeScreen.this, "1", Toast.LENGTH_SHORT).show();
////                    productListFragment = new ProductListFragment();
////                    toolbar.setNavigationIcon(null);
////                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
////
////                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////
////                        @Override
////                        public void onClick(View v) {
////                            onBackPressed();
////                            toolbar.setNavigationIcon(null);
////                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
////
////                        }
////                    });
//                } else {
//
//                    Toast.makeText(HomeScreen.this, "2", Toast.LENGTH_SHORT).show();
////                    toolbar.setNavigationIcon(null);
////                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
////
////                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////
////                        @Override
////                        public void onClick(View v) {
////                            onBackPressed();
////                        }
////                    });
//                    //show hamburger
////                    drawerFragment.mDrawerToggle.setDrawerIndicatorEnabled(true);
////                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
////                    drawerFragment.mDrawerToggle.syncState();
////                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            onBackPressed();
////                        }
////                    });
//                }
//            }
//        });

    }

    private void setupBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        loadHomeFragment();
                        return true;
                    case R.id.navigation_cart:
                        loadCartFragment();
                        return true;
                    case R.id.navigation_search:
                        loadSearchFragment();
                        return true;
                    case R.id.navigation_account:
                        loadAccountFragment();
                        return true;
                }
                return false;
            }
        });
    }

    private void loadAccountFragment() {

        MyAccountFragment fragment = MyAccountFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadSearchFragment() {

        SearchFragment fragment = SearchFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadCartFragment() {

        CartFragment fragment = CartFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadHomeFragment() {

        HomeFragment fragment = HomeFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadContactUsFragment() {

        ContactusFragment fragment = ContactusFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_home:
                        loadHomeFragment();
                        break;

                    case R.id.navigation_account:
                        loadAccountFragment();
                        break;

                    case R.id.navigation_cart:
                        loadCartFragment();
                        break;

                    case R.id.navigation_contact:
                        loadContactUsFragment();
                        break;

                    case R.id.navigation_search:
                        loadSearchFragment();
                        break;

                    case R.id.navigation_signout:

                        editor.clear();
                        editor.commit();

                        session.logoutUser();
                        finish();
                        break;


                    default:
                        break;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                menuItem.setChecked(true);
                // Set action bar title
                setTitle(menuItem.getTitle());
                // Close the navigation drawer
                drawer.closeDrawers();

                return true;
            }
        });


        toggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        drawer.addDrawerListener(toggle);
    }

    public ActionBarDrawerToggle setupDrawerToggle() {

        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }else
        {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_localization_option, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_eng:
                showPopup();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("RestrictedApi")
    public void showPopup(){

        View menuItemView = findViewById(R.id.navigation_eng);
        PopupMenu popup = new PopupMenu(this, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_select_language, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getTitle().equals("English"))
                {
//                    Toast.makeText(HomeScreen.this, "English", Toast.LENGTH_SHORT).show();
                    recreate();
                    editor.putString(ConstantData.LANGUAGE_SELECTION,"English").commit();
                }
                else if (item.getTitle().equals("Aerabic"))
                {
                    recreate();
                    editor.putString(ConstantData.LANGUAGE_SELECTION,"Aerabic").commit();
                }

                return false;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(this, (MenuBuilder) popup.getMenu(), menuItemView);
        menuHelper.setForceShowIcon(true);
        menuHelper.show();

    }

}
