package app.yaaymina.com.yaaymina.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.yaaymina.com.yaaymina.CommonInterface.OnCompleteListener;
import app.yaaymina.com.yaaymina.R;


/**
 * Created by hp on 29-03-2017.
 */

public class DialogPickup extends DialogFragment {

    Button buttonOk;
    Spinner spinner;
    TimePicker timePicker;
    String time,textBranch;
    String second = "00";
    TextView textViewValidate;
    int selection;
    private DateFormat dateFormat;
    private int minHour = 9,minMinute = 0,maxHour = 21,maxMinute=0;
    private int currentHour,currentMinute;
    Calendar datetime;
    private OnCompleteListener mListener;
    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.pickfrom_layout, null);
        dateFormat = new SimpleDateFormat("hh:mm aa");
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        textViewValidate = (TextView) view.findViewById(R.id.validText);
        buttonOk = (Button) view.findViewById(R.id.ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mListener.onComplete(currentHour+":"+ currentMinute+":"+"00");
                getDialog().dismiss();
            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);

        if (time!=null)
        {
            buttonOk.setVisibility(View.VISIBLE);

        }else
        {
            int minute = timePicker.getCurrentMinute();
            int hour = timePicker.getCurrentHour();
            datetime = Calendar.getInstance();
            Calendar c = Calendar.getInstance();

            if (datetime.getTimeInMillis() >= c.getTimeInMillis()+1800000) {
                buttonOk.setVisibility(View.VISIBLE);
            }
            else {
                //it's before current'
                textViewValidate.setVisibility(View.VISIBLE);
                buttonOk.setVisibility(View.GONE);
            }
            time = String.valueOf(hour)+":"+String.valueOf(minute)+":"+second;
            Log.d("time", time);
        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                boolean validTime;
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, i);
                datetime.set(Calendar.MINUTE, i1);

                System.out.println("minText " + i + " " + i1);

                time = dateFormat.format(datetime.getTime());
                if (datetime.getTimeInMillis() >= c.getTimeInMillis()+1800000)
                {
                    /*buttonOk.setVisibility(View.VISIBLE);*/

                    if(i >= minHour && i<= maxHour)
                    {
                        if(i<21)
                        {
                            Log.d("ZeroStatus","in if");
                            validTime = true;
                            buttonOk.setVisibility(View.VISIBLE);
                        }
                        else if(i == 21 && i1 == 0)
                        {
                            Log.d("ZeroStatus","in else..if");
                            validTime = true;
                            buttonOk.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            validTime = false;
                            buttonOk.setVisibility(View.GONE);
                            textViewValidate.setVisibility(View.VISIBLE);
                            //if (datetime.getTimeInMillis() >= c.getTimeInMillis()+1800000) {
                            if ((i >= 21 && i1 > 0) || (i > 21 || i<9))
                            {
                                textViewValidate.setText(R.string.select_btwn_9_to_9);
                    /*buttonOk.setVisibility(View.VISIBLE);*/
                            }
                            else
                            {
                                textViewValidate.setText(getString(R.string.please_select_time_after_30_minutes));
                            }
                            Log.d("ZeroStatus","in else");
                        }
                    }
                    else
                    {
                        validTime = false;
                        buttonOk.setVisibility(View.GONE);
                        textViewValidate.setVisibility(View.VISIBLE);
                        textViewValidate.setText(R.string.select_btwn_9_to_9);
                    }
                }
                else {
                    if ((i >= 21 && i1 > 0) || (i > 21 || i<9)){
                        textViewValidate.setText(R.string.select_btwn_9_to_9);
                    /*buttonOk.setVisibility(View.VISIBLE);*/
                    }
                    else
                    {
                        Log.d("ZeroHour", String.valueOf(i));
                        Log.d("ZeroMinute", String.valueOf(i1));
                        //it's before current'
                        buttonOk.setVisibility(View.GONE);
                        textViewValidate.setVisibility(View.VISIBLE);
                        textViewValidate.setText(getString(R.string.please_select_time_after_30_minutes));
                    }


                }

                currentHour = i;
                currentMinute = i1;

//                /*time = String.valueOf(hour)+":"+String.valueOf(minute)+":"+second;
//                Log.d("hour", String.valueOf(+hour)+" "+minute);*/
//                String status = "AM";
//
//                if(hour > 11)
//                {
//                    // If the hour is greater than or equal to 12
//                    // Then the current AM PM status is PM
//                    status = "PM";
//                }
//
//                // Initialize a new variable to hold 12 hour format hour value
//                int hour_of_12_hour_format;
//
//                if(hour > 11){
//
//                    // If the hour is greater than or equal to 12
//                    // Then we subtract 12 from the hour to make it 12 hour format time
//                    hour_of_12_hour_format = hour - 12;
//                }
//                else {
//
//                    hour_of_12_hour_format = hour;
//                }
//                time = hour_of_12_hour_format+":"+minute+" "+status;
            }

        });


        return view;


    }

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    private void updateTime(int currentHour, int currentMinute) {
        datetime.set(Calendar.HOUR_OF_DAY, currentHour);
        datetime.set(Calendar.MINUTE, currentMinute);
        //String title = dateFormat.format(calendar.getTime());
        //setTitle(title);
    }
    private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        //String title = dateFormat.format(calendar.getTime());
        //setTitle(title);
    }

}