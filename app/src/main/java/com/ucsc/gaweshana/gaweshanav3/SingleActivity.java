package com.ucsc.gaweshana.gaweshanav3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SingleActivity extends AppCompatActivity  {

    TextView tv;
    TextView tv2;
    ImageView img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        String res=getIntent().getStringExtra("Result");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        tv=(TextView)findViewById(R.id.title);
        tv.setText(res);

        tv2=(TextView)findViewById(R.id.desc);
        tv2.setText(res);

        final Button btnSpeak = (Button) findViewById(R.id.btnSpeak);

        new NetLink().execute("http://gaweshana.pe.hu/",res);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //actionButton = (FloatingActionButton) findViewById(R.id.home_logo);

        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.home_logo);

       /* FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build(); */

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class NetLink extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            return GET(params[0]+params[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);
            try {
                JSONObject jo=new JSONObject(s);
                tv.setText(jo.getString("name"));
                tv2.setText(jo.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient

                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            return result;
        }
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;


            return result;

        }
    }


}
