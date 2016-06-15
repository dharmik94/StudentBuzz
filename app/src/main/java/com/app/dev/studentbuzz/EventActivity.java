package com.app.dev.studentbuzz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class EventActivity extends AppCompatActivity {
    private ListView lve;
    private List<String> eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        lve = (ListView) findViewById(R.id.listViewevent);
        //store the text in variable
        String c_name = getIntent().getExtras().getString("c_name");
        new EventtActivity(this).execute(c_name);

    }

    public static class JsonEvent {

        public static List<String> Parse(String json){
            try {
                List<String> eventList = new ArrayList<>();
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray cast = jsonResponse.getJSONArray("Event");
                for (int i=0; i<cast.length(); i++) {
                    JSONObject actor = cast.getJSONObject(i);
                    eventList.add(actor.getString("event_name"));
                }
                return eventList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    public void EventAdapter(String json){
        eventList = JsonEvent.Parse(json);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,eventList);
        lve.setAdapter(adapter);
    }

    public class EventtActivity extends AsyncTask<String, Void, String> {

        private Context context;

        public EventtActivity(Context context) {
            this.context = context;
        }

        private ProgressDialog progressDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(EventActivity.this);
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
                link = "http://10.0.3.2/Studentbuzzapp/event.php" + data;
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
            EventAdapter(s);
        }
    }
}
