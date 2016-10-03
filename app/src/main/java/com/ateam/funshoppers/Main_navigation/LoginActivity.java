package com.ateam.funshoppers.Main_navigation;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;


import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ateam.funshoppers.R;

import com.ateam.funshoppers.ui.activity.MainNavigationActivity;



public class LoginActivity extends ActionBarActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    EditText etusername , etpassword;



    LocalDatabase localDatabase;

    private TextInputLayout inputLayoutName,  inputLayoutPassword;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        checkConnection();
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        etusername = (EditText) findViewById(R.id.input_phone);

        etpassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_login);
        localDatabase = new LocalDatabase(this);
        etusername.addTextChangedListener(new MyTextWatcher(etusername));

        etpassword.addTextChangedListener(new MyTextWatcher(etpassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }



    public void onRegisterClick(View view)
    {
        Intent intent = new Intent(LoginActivity.this , Register.class);
        startActivity(intent);
    }

    private void submitForm()
    {
        checkConnection();
        if (!validateName()) {
            return;
        }



        if (!validatePassword()) {
            return;
        }
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();

        Contact contact = new Contact(username,  password);
        Log.e("uname = " , contact.username);
        authenticate(contact);

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Welcome To Funshoppers";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private boolean validateName() {
        if (etusername.length()!=10) {
            inputLayoutName.setError(getString(R.string.err_msg_phone));
            requestFocus(etusername);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.input_phone:
                    validateName();
                    break;

                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    private void authenticate(Contact contact)
    {
        ServerRequests serverRequests = new ServerRequests(LoginActivity.this);


        serverRequests.fetchDataInBackground(contact , new GetUserCallback() {

            @Override

            public void done(final Contact returnedContact) {

                if(returnedContact == null)
                {
                   //show an error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username & Password don't match!");
                    builder.setPositiveButton("OK" , null);
                    builder.show();

                }
                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Dialog");
                    builder.setMessage("Hello here is the best example of AppCompatAlertDialog from www.takeoffandroid.com. Lets make use of it");
                    builder.setPositiveButton("In Mall",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Not In Mall", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            localDatabase.storeData(returnedContact);
                            localDatabase.setUserLoggedIn(true);

                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
                    //Log user in






                }

            }
        });

}
}
