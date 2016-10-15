package com.ateam.funshoppers.Main_navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ateam.funshoppers.R;

import net.rimoto.intlphoneinput.IntlPhoneInput;


public class Register extends Activity {
    EditText etname ,etemail , etusername , etpassword, etconfirm_password;
    private TextInputLayout inputLayoutName, inputLayoutEmail,inputLayoutPhone, inputLayoutPassword;
    private Button btnSignUp;
    IntlPhoneInput phoneInputView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
         phoneInputView = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_namer);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_emailr);
       // inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phoner);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_passwordr);
        etname = (EditText) findViewById(R.id.input_namer);
        etemail = (EditText) findViewById(R.id.input_emailr);
       // etusername = (EditText) findViewById(R.id.input_phoner);
        etpassword = (EditText) findViewById(R.id.input_passwordr);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        etname.addTextChangedListener(new MyTextWatcher(etname));
        etemail.addTextChangedListener(new MyTextWatcher(etemail));
       // etusername.addTextChangedListener(new MyTextWatcher(etusername));
        etpassword.addTextChangedListener(new MyTextWatcher(etpassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }

    public void onLoginClick(View view)
    {
        Intent intent = new Intent(Register.this , LoginActivity.class);
        startActivity(intent);
    }

    public void submitForm(){
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }
//        if (!validatePhone()) {
      //      return;
     //   }


        if (!validatePassword()) {
            return;
        }
        if(!phoneInputView.isValid()) {
           return;
        }
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String username = phoneInputView.getNumber();
        String password = etpassword.getText().toString();

        Contact contact = new Contact(name ,email , username,password);
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeDataInBackground(contact , new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {
                Intent intent = new Intent(Register.this , LoginActivity.class);
                startActivity(intent);
            }
        });



    }
    private boolean validateName() {
        if (etname.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(etname);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
   /* private boolean validatePhone() {
        if (etusername.length()!=10) {
            inputLayoutName.setError(getString(R.string.err_msg_phone));
            requestFocus(etusername);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    */
    private boolean validateEmail() {
        String email = etemail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(etemail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePassword() {
        if (etpassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(etpassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_namer:
                    validateName();
                    break;
                case R.id.input_emailr:
                    validateEmail();
                    break;
                //case R.id.input_phoner:
                    //validatePhone();

                  //  break;
                case R.id.input_passwordr:
                    validatePassword();
                    break;
            }
        }
    }



}
