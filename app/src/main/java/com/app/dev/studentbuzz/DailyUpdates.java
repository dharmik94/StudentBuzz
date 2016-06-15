package com.app.dev.studentbuzz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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



public class DailyUpdates extends AppCompatActivity {
TextView tv;
    private ListView lv;
    private List<String> dailyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_updates);
        lv = (ListView) findViewById(R.id.listViewdaily);
        //store the text in variable
        String c_name = getIntent().getExtras().getString("c_name");
        new DailyActivity(this).execute(c_name);

    }

    public static class JsonParser {

        public static List<String> Parse(String json){
            try {
                List<String> classList = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray cast = jsonResponse.getJSONArray("Daily updates");
                for (int i=0; i<cast.length(); i++) {
                    JSONObject actor = cast.getJSONObject(i);
                    classList.add(actor.getString("topic_name"));
                }
                return classList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public void ClassAdapter(String json){
        dailyList = JsonParser.Parse(json);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dailyList);
        lv.setAdapter(adapter);
    }

    public class DailyActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public DailyActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(DailyUpdates.this);
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
                link = "http://10.0.3.2/Studentbuzzapp/dailyupdates.php" + data;
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
            ClassAdapter(s);
        }
    }
}
