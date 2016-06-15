package com.app.dev.studentbuzz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button bReg;
    EditText sfname, slname, semail, spass;
    String FName, LName, Password, Email;
    Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sfname = (EditText) findViewById(R.id.new_fname);
        slname = (EditText) findViewById(R.id.new_lname);
        semail = (EditText) findViewById(R.id.new_email);
        spass = (EditText) findViewById(R.id.new_pass);

        bReg = (Button) findViewById(R.id.userreg);
        bReg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        FName = sfname.getText().toString();
        LName = slname.getText().toString();
        Password = spass.getText().toString();
        Email = semail.getText().toString();
        BackGround b = new BackGround();
        b.execute(FName, LName, Email, Password);
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String s_firstname = params[0];
            String s_lastname = params[1];
            String s_email = params[2];
            String s_password = params[3];

            String data="";
            int tmp;

            try {
                URL url = new URL("http://10.0.3.2/Studentbuzzapp/register.php");
                String urlParams = "s_firstname="+s_firstname+"&s_lastname="+s_lastname+"&s_email="+s_email+"&s_password="+s_password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("")){
                s="Registration successfully.";
                Intent i = new Intent(ctx, HomeActivity.class);
                startActivity(i);
            }
            Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();

        }
    }
}