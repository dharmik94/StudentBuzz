package com.app.dev.studentbuzz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText1;
    EditText editText2;
    Button button;
    TextView loginreg;
    String s_email,s_password;
    Context ctx=this;
    String FNAME=null, LNAME=null, PASSWORD=null, EMAIL=null;
    String SID=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText1 = (EditText) findViewById(R.id.user_email);
        editText2 = (EditText) findViewById(R.id.user_password);
        loginreg = (TextView) findViewById(R.id.txtlr);

        button = (Button) findViewById(R.id.butoonlg);
        button.setOnClickListener(this);
        loginreg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.butoonlg:
                String s_email = editText1.getText().toString();
                String s_password = editText2.getText().toString();
                if(!s_email.isEmpty() && !s_password.isEmpty()) {
                    BackGround b = new BackGround();
                    b.execute(s_email, s_password);
                } else {
                    Toast.makeText(this,"Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                break;
            case  R.id.txtlr:
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;
        }
    }


class BackGround extends AsyncTask<String, String, String>
{
    private ProgressDialog progressDialog;
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        String s_email = params[0];
        String s_password = params[1];
        String data="";
        int tmp;

        try {
            URL url = new URL("http://10.0.3.2/Studentbuzzapp/login.php");
            String urlParams = "s_email="+s_email+"&s_password="+s_password;

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
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        String err=null;
        try {
            JSONObject root = new JSONObject(s);
            JSONObject user_data = root.getJSONObject("user_data");
            SID = user_data.getString("s_id");
            FNAME = user_data.getString("s_firstname");
            LNAME = user_data.getString("s_lastname");
            PASSWORD = user_data.getString("s_password");
            EMAIL = user_data.getString("s_email");
            SharedPreferences ui = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor edUi = ui.edit();
            edUi.putString("s_id", SID);
            edUi.putString("s_firstname", FNAME);
            edUi.putString("s_lastname", LNAME);
            edUi.putString("s_email", EMAIL);
            edUi.putString("s_password", PASSWORD);
            edUi.commit();
            Intent i = new Intent(ctx, HomeActivity.class);
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
            err = "Exception: "+e.getMessage();
            Toast.makeText(ctx, "Email or Password are incorrect...", Toast.LENGTH_LONG).show();
        }
        }
    }
}


