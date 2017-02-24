package com.alex.heverrest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class ChargeActivity extends AppCompatActivity {


    EditText etUser;
    EditText etPass;
    Button btnLogin;
    ProgressBar progressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        etUser = (EditText) findViewById(R.id.etHeverUser);
        etPass = (EditText) findViewById(R.id.etHeverPass);
        btnLogin = (Button) findViewById(R.id.btnHeverLogin);
        progressLogin = (ProgressBar) findViewById(R.id.progressLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeverLogin task = new HeverLogin(etUser.getText().toString(), etPass.getText().toString());
                task.execute();
            }
        });
    }

    public class HeverLogin extends AsyncTask<Void,Void,Void> {

        private String user;
        private String pass;

        public HeverLogin(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute(){
            btnLogin.setVisibility(View.INVISIBLE);
            progressLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressLogin.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}
