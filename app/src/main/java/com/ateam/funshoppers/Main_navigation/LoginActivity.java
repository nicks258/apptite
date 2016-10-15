package com.ateam.funshoppers.Main_navigation;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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


import com.ateam.funshoppers.R;

import com.ateam.funshoppers.ui.activity.MainNavigationActivity;

import net.rimoto.intlphoneinput.IntlPhoneInput;


public class LoginActivity extends ActionBarActivity {

    EditText etusername, etpassword;

    String myInternationalNumber;
    LocalDatabase localDatabase;
    NotificationManager manager;
    Notification myNotication;
    IntlPhoneInput phoneInputView;
    private TextInputLayout inputLayoutName, inputLayoutPassword;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        phoneInputView = (IntlPhoneInput) findViewById(R.id.my_phone_input);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
      //  etusername = (EditText) findViewById(R.id.input_phone);
        phoneInputView.hideKeyboard();
        etpassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_login);
        localDatabase = new LocalDatabase(this);
      //  etusername.addTextChangedListener(new MyTextWatcher(etusername));

        etpassword.addTextChangedListener(new MyTextWatcher(etpassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }


    public void onRegisterClick(View view) {
        Intent intent = new Intent(LoginActivity.this, Register.class);
        startActivity(intent);
    }

    private void submitForm() {

       /* if (!validateName()) {
            return;
        }
*/

        if (!validatePassword()) {
            return;
        }
        if(phoneInputView.isValid()) {
            myInternationalNumber = phoneInputView.getNumber();

            String username = phoneInputView.getNumber();
            String password = etpassword.getText().toString();

            Contact contact = new Contact(username, password);
            Log.e("uname = ", contact.username);
            authenticate(contact);
        }
    }




    @Override
    protected void onResume() {
        super.onResume();



    }




   /* private boolean validateName() {
        if (etusername.length() != 10) {
            inputLayoutName.setError(getString(R.string.err_msg_phone));
            requestFocus(etusername);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
*/
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
              //  case R.id.my_phone_input:
              //      validateName();
              //      break;

                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    private void authenticate(Contact contact) {
        if (haveNetworkConnection() == true) {

        ServerRequests serverRequests = new ServerRequests(LoginActivity.this);


        serverRequests.fetchDataInBackground(contact, new GetUserCallback() {

            @Override

            public void done(final Contact returnedContact) {

                if (returnedContact == null) {
                    //show an error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Username & Password don't match!");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                } else {

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(LoginActivity.this, 1, intent, 0);

                    Notification.Builder builderr = new Notification.Builder(LoginActivity.this);

                    builderr.setAutoCancel(false);

                    builderr.setContentTitle("Fun Shopper");
                    builderr.setContentText("Check new Diwali Offers ");
                    builderr.setSmallIcon(R.drawable.applogo);
                    builderr.setContentIntent(pendingIntent);
                    builderr.setSubText("Valid till 31st october");
                    builderr.setNumber(100);
                    builderr.build();

                    myNotication = builderr.getNotification();
                    manager.notify(11, myNotication);

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Welcome Back!!");
                    builder.setMessage("Choose your location");
                    builder.setCancelable(false);

                    builder.setPositiveButton("In Store", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Not In Store", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            localDatabase.storeData(returnedContact);
                            localDatabase.setUserLoggedIn(true);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();



                }

            }
        });

    }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("No Internet");
            builder.setMessage("Click on Setting to connect");
            builder.setPositiveButton("OK", null);

            builder.setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.show();
        }

}
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
