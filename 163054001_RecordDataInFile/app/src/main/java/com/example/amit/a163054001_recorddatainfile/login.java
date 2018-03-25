package com.example.amit.a163054001_recorddatainfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.Toast;
/**
 * Created by Amit on 11-02-2018.
 */

public class login extends Fragment {
    private EditText inputFName,inputLName, inputAge, inputEmail, inputMobile;
    private Button loginbtn;
    private RadioButton rb_male,rb_female;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String FName = "FnameKey";
    public static final String LName = "LnameKey";
    public static final String Age = "AgeKey";
    public static final String Mobilenumber = "MobKey";
    public static final String Sex = "SexKey";
    public static final String Email = "emailKey";

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor myeditor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View login_view=inflater.inflate(R.layout.login, container, false);
        inputFName = (EditText) login_view.findViewById(R.id.enterfname);
        inputLName = (EditText) login_view.findViewById(R.id.enterlname);
        inputEmail = (EditText) login_view.findViewById(R.id.enteremail);
        inputAge = (EditText) login_view.findViewById(R.id.enterage);
        inputMobile = (EditText) login_view.findViewById(R.id.entermobile);
        rb_male=(RadioButton) login_view.findViewById(R.id.male) ;
        rb_female=(RadioButton) login_view.findViewById(R.id.female) ;
        loginbtn = (Button) login_view.findViewById(R.id.login_btn);
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         myeditor = sharedpreferences.edit();

        if (sharedpreferences.contains(FName)) {
            inputFName.setText(sharedpreferences.getString(FName, ""));
        }
        if (sharedpreferences.contains(LName)) {
            inputLName.setText(sharedpreferences.getString(LName, ""));
        }
        if (sharedpreferences.contains(Age)) {
            inputAge.setText(sharedpreferences.getString(Age, ""));
        }
        if (sharedpreferences.contains(Email)) {
            inputEmail.setText(sharedpreferences.getString(Email, ""));

        }
        if (sharedpreferences.contains(Mobilenumber)) {
            inputMobile.setText(sharedpreferences.getString(Mobilenumber, ""));

        }
        if (sharedpreferences.contains(Sex)) {
            String status1=sharedpreferences.getString(Sex, "");
            if(status1.contains("Male"))
            {
                rb_male.setChecked(true);
            }
            if(status1.contains("Female"))
            {
                rb_female.setChecked(true);
            }

        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result=submitForm();
                if(result==1) {
                    Toast.makeText(getActivity(), "Login Successfull", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return login_view;
    }



    /**
     * Validating form
     */
    private int submitForm() {
        if (!validateName()) {
            return 0;
        }

        if (!validateMobile()) {
            return 0;
        }

        if (!validateEmail()) {
            return 0;
        }

        if (!validateAge()) {
            return 0;
        }

        if(!validateSex())
        {
            return 0;
        }

        myeditor.commit();
        Toast.makeText(getActivity(), "Login successful!!!", Toast.LENGTH_SHORT).show();
        return 1;
    }

    private boolean validateName() {
        if (inputFName.getText().toString().trim().isEmpty()) {
            inputFName.setError(getString(R.string.err_msg));
            //requestFocus(inputFName);

            Toast.makeText(getActivity(), "Error: Enter first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (inputLName.getText().toString().trim().isEmpty()){
            inputLName.setError(getString(R.string.err_msg));
            //requestFocus(inputFName);
            Toast.makeText(getActivity(), "Error: Enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            // inputFName.setErrorEnabled(false);
        }
        myeditor.putString(FName, inputFName.getText().toString());
        myeditor.putString(LName, inputLName.getText().toString());
        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputEmail.setError(getString(R.string.err_msg));
            Toast.makeText(getActivity(), "Error: Enter correct email", Toast.LENGTH_SHORT).show();
            //requestFocus(inputEmail);
            return false;
        } else {
            //inputEmail.setErrorEnabled(false);
        }

        myeditor.putString(Email, inputEmail.getText().toString());
        return true;
    }

    private boolean validateMobile() {
        String mobile = inputMobile.getText().toString().trim();

        if (mobile.isEmpty() || !isValidMobile(mobile)) {
            inputMobile.setError(getString(R.string.err_msg));
            Toast.makeText(getActivity(), "Error: Enter correct 10 digit mobile", Toast.LENGTH_SHORT).show();
            //requestFocus(inputEmail);
            return false;
        } else {
            //inputEmail.setErrorEnabled(false);
        }
        myeditor.putString(Mobilenumber, inputMobile.getText().toString());
        return true;
    }

    private boolean validateAge() {
        int age = Integer.parseInt(inputAge.getText().toString());
        //String age = inputAge.getText().toString().trim();

        if ( !isValidAge(age)) {
            inputAge.setError(getString(R.string.err_msg));
            Toast.makeText(getActivity(), "Error: Enter correct age between 10 to 99", Toast.LENGTH_SHORT).show();
            //requestFocus(inputEmail);
            return false;
        } else {
            //inputEmail.setErrorEnabled(false);
        }
        myeditor.putString(Age, inputAge.getText().toString());
        return true;
    }

    private boolean validateSex() {
        if (rb_male.isChecked()) {
            myeditor.putString(Sex, "Male");

        } else if (rb_female.isChecked()) {
            myeditor.putString(Sex, "Female");
        }
        else
        {
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private static boolean isValidMobile(String mobile) {
        if(mobile.length() !=10)
            return false;
        else
            return true;
    }

    private static boolean isValidAge(int age) {
        if(age>=100  || age<10)
            return false;
        else
            return true;
    }
}