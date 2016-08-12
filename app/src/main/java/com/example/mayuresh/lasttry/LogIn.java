package com.example.mayuresh.lasttry;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView signUp;
    public static final String USER_NAME = "USERNAME";
    private static final String REGISTER_URL = "https://diginotice-thestinson.c9.io/app_user_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        username = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);
        signUp = (TextView) findViewById(R.id.link_signup);
        login.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==login) {

            String unm=username.getText().toString();
            if(unm.equals(""))
            {
                Toast.makeText(getApplicationContext(),"username is must", Toast.LENGTH_LONG).show();
                return;
            }
            String ps=password.getText().toString();
            if(ps.equals(""))
            {
                Toast.makeText(getApplicationContext(), "password is must", Toast.LENGTH_LONG).show();
                return;
            }
            //loginMe(unm, ps);
            Intent intent = new Intent(LogIn.this, MainActivity.class);
            startActivity(intent);
        }
        if(view==signUp)
        {
            Intent i=new Intent(this,SignUp.class);
            startActivity(i);
            finish();
        }
    }

    private void loginMe(final String unm, String ps) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;
            RegisterUserClass registerUserClass = new RegisterUserClass();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LogIn.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("username", params[0]);
                data.put("password", params[1]);
                String result = registerUserClass.sendPostRequest(REGISTER_URL, data);
              return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.contains("@")||s.equals("success")){
                    Toast.makeText(getApplicationContext(), "Welcome "+unm+"!" , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    intent.putExtra(USER_NAME, unm);
                    intent.putExtra("EMAIL",s);
                    SharedPreferences checkObj = getSharedPreferences("LogIn", 0);
                    SharedPreferences.Editor editor = checkObj.edit();
                    editor.putBoolean("LogIn", true);
                    editor.commit();
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(unm, ps);
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
        Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}