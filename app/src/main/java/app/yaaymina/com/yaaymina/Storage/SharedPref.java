package app.yaaymina.com.yaaymina.Storage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import app.yaaymina.com.yaaymina.Activity.LoginScreen;
import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 22-Nov-17.
 */

public class SharedPref {

    public static final String PREF_NAME = "user_preference";
    int PRIVATE_MODE = 0;

    public static final boolean IS_LOGIN = false;
    public static final boolean IS_GUEST_LOGIN = false;
    public static final String USER_ID = "user_id";
    public static final String FULL_NAME = "full_name";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    SharedPreferences sharedPreferences_common;
    SharedPreferences.Editor editor_common;

    public SharedPref(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String email, String login_type){

        if (login_type.equalsIgnoreCase("guest"))
        {
            editor.putBoolean(String.valueOf(IS_GUEST_LOGIN), true);
        }
        else
        {
            editor.putBoolean(String.valueOf(IS_LOGIN), true);
        }

        editor.putString(EMAIL, name);
        editor.putString(PASSWORD, email);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin() {


        // Check login status
        if (!this.isLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            ((Activity)_context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);
            ((Activity)_context).finish();
        }
        else
        {
        }
    }

    public boolean isLoggedIn(){

        sharedPreferences_common = _context.getSharedPreferences(ConstantData.PREF_NAME,Context.MODE_PRIVATE);

        String type = sharedPreferences_common.getString(ConstantData.GUEST_USER,"");

        if (type.equalsIgnoreCase("1"))
        {
            return pref.getBoolean(String.valueOf(IS_GUEST_LOGIN), false);
        }
        else
        {
            return pref.getBoolean(String.valueOf(IS_LOGIN), false);
        }

    }

    public void logoutUser()
    {
        // Clearing all data from Shared Preferences


        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginScreen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)_context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void guestLogoutUser()
    {
        // Clearing all data from Shared Preferences
        editor.remove(String.valueOf(IS_GUEST_LOGIN));
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginScreen.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)_context).overridePendingTransition(R.anim.translate_right_to_left, R.anim.stable);

        // Staring Login Activity
        _context.startActivity(i);
    }

}
