package com.app.dev.studentbuzz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AnnActivity extends AppCompatActivity {
    private ListView lva;
    private List<String> annList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann);
        lva = (ListView) findViewById(R.id.listViewanc);
        //store the text in variable
        String c_name = getIntent().getExtras().getString("c_name");
        new AnnnActivity(this).execute(c_name);
    }

    public static class JsonAnn {

        public static List<String> Parse(String json){
            try {
                List<String> annList = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray cast = jsonResponse.getJSONArray("Annocunment");
                for (int i=0; i<cast.length(); i++) {
                    JSONObject actor = cast.getJSONObject(i);
                    annList.add(actor.getString("anc_name"));
                }
                return annList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public void AnnAdapter(String json){
        annList = JsonAnn.Parse(json);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,annList);
        lva.setAdapter(adapter);
    }

    public class AnnnActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public AnnnActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(AnnActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... arg0) {
            String c_name = arg0[0];
            String link;
            String data;
            BufferedReader bufferedReader;
            String result;

            try {
                data = "?c_name=" + URLEncoder.encode(c_name, "UTF-8");
                link = "http://10.0.3.2/Studentbuzzapp/anc.php" + data;
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                result = bufferedReader.readLine();
                return result;
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            AnnAdapter(s);
        }
    }

}
