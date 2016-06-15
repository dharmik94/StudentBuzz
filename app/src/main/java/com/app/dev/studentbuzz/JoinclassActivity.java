package com.app.dev.studentbuzz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class JoinclassActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnjoin;
    EditText c_ccode;
    Context ctx=this;
    String c_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinclass);

        c_ccode = (EditText) findViewById(R.id.addclass);
        btnjoin = (Button) findViewById(R.id.joinbtn);
        btnjoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        c_code = c_ccode.getText().toString();
        SharedPreferences ui = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String s_id = ui.getString("s_id","s_id");
        Join b = new Join();
        b.execute(c_code,s_id);
    }

    class Join extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String c_code = params[0];
            String s_id = params[1];

            String data="";
            int tmp;

            try {
                URL url = new URL("http://10.0.3.2/Studentbuzzapp/joinclass.php");
                String urlParams = "c_code="+c_code+"&s_id="+s_id;

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
            if(s.equals("success")){
                s="Class Successfully Joined...";
                Intent i = new Intent(ctx, HomeActivity.class);
                startActivity(i);
                Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
            }
            else
            {
                s="Please Enter Valid Code";
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Intent i = new Intent(ctx, JoinclassActivity.class);
                startActivity(i);

            }
        }
    }
}
