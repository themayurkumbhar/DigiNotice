package com.example.mayuresh.lasttry;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private TextView login;
    private Button signup;

    private static final String REGISTER_URL = "https://diginotice-thestinson.c9.io/regap.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText) findViewById(R.id.input_name);
        password = (EditText) findViewById(R.id.input_password);
        email = (EditText) findViewById(R.id.input_email);
        login = (TextView) findViewById(R.id.link_login);
        signup = (Button) findViewById(R.id.btn_signup);
        name = (EditText) findViewById(R.id.name);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {


        if (view == signup) {
            String nm = name.getText().toString();
            String em = email.getText().toString();
            String ps = password.getText().toString();
            String unm = username.getText().toString();

            if (nm.equals("")) {
                Toast.makeText(getBaseContext(), "name is must", Toast.LENGTH_LONG).show();
                return;
            }
            if (em.equals("")) {
                Toast.makeText(getBaseContext(), "email is must", Toast.LENGTH_LONG).show();
                return;
            }
            if (ps.equals("")) {
                Toast.makeText(getBaseContext(), "password is must", Toast.LENGTH_LONG).show();
                return;
            }
            if (unm.equals("")) {
                Toast.makeText(getBaseContext(), "username is must", Toast.LENGTH_LONG).show();
                return;
            }

            registerUser(nm, em, ps, unm);

        }
        if (view == login) {
            Intent i = new Intent(this, LogIn.class);
            startActivity(i);
            finish();
        }
    }

    private void registerUser(String nm, String em, String ps, String unm) {

        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass registerUserClass = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUp.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               Intent i=new Intent(SignUp.this,LogIn.class);
                startActivity(i);
                finish();
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("name", params[0]);
                data.put("username", params[1]);
                data.put("password", params[2]);
                data.put("email", params[3]);

                String result = registerUserClass.sendPostRequest(REGISTER_URL, data);

                return result;
            }

        }

        RegisterUser ru = new RegisterUser();
        ru.execute(nm, unm, ps, em);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(SignUp.this,LogIn.class);
        startActivity(i);
    }
}
