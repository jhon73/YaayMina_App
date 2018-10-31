package app.yaaymina.com.yaaymina.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.yaaymina.com.yaaymina.R;

/**
 * Created by ADMIN on 23-Nov-17.
 */

public class ContactusFragment extends Fragment {

    private TextInputEditText textInputEditText_email, textInputEditText_msg;
    private Button button_send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactus,container,false);


        textInputEditText_email = view.findViewById(R.id.email_textInput);
        textInputEditText_msg = view.findViewById(R.id.message_textInput);

        button_send = view.findViewById(R.id.send_btn);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        button_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String email = textInputEditText_email.getText().toString();
                String message = textInputEditText_msg.getText().toString();

                if (email.length() == 0)
                {
                    textInputEditText_email.setError("Enter email address");
                }else if (!email.matches(emailPattern))
                {
                    textInputEditText_email.setError("Invalid email address");
                }else if (message.length() == 0)
                {
                    textInputEditText_msg.setError("Enter message");
                }else
                {
//                    doContactUs(email,message);
                }
            }
        });

        return view;
    }

//    private void doContactUs(String email, String message) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL + )
//    }

    public static ContactusFragment newInstance() {

        return new ContactusFragment();
    }
}
