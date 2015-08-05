package com.ucsc.gaweshana.gaweshanav3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


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

        new NetLink().execute("http://gaweshana.pe.hu/api/getArtifact/",res);

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
           // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                JSONObject jo=new JSONObject(s);
                tv.setText(jo.getString("name"));
                tv2.setText(jo.getString("description"));
                String k=jo.getString("image");
                k=k.replaceFirst(".","");
                new FetchImageAsyncTask().execute("http://gaweshana.pe.hu/"+k,jo.getString("id"));
               // Toast.makeText(getApplicationContext(),"http://gaweshana.pe.hu/"+k,Toast.LENGTH_LONG).show();

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

    class FetchImageAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ImageView p=(ImageView)findViewById(R.id.img);
            //p.setMaxWidth(l.getWidth());
            File dir=new File("sdcard/gaweshana/");
            if(!dir.exists()){
                dir.mkdirs();
            }
            File img=new File("sdcard/gaweshana/" + s + ".jpeg");
            if(img.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile("sdcard/gaweshana/" + s + ".jpeg");
                p.setImageBitmap(bitmap);
            }else{
                Toast.makeText(getApplicationContext(), "Sorry No Image available", Toast.LENGTH_LONG).show();
                p.setImageResource(R.drawable.home_logo);
            }







            //p.setIma
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            String u;
            HttpURLConnection connection = null;
            try {
                Log.d("Image", sUrl[0]);
                if (!sUrl[0].startsWith("http")) {
                    u = "http://" + sUrl[0];
                }
                else{
                    u= sUrl[0];
                }

                Log.d("Image", u);
                URL url = new URL(u);
                //Log.d("Image", "thumbs1.ebaystatic.com//m//mOR0AuMSi9LLnSjryx8wE3Q//140.jpg");
                Log.d("Image", "con");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                Log.d("Image", "if");
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                Log.d("Image", "save");
                if (sUrl[1]!=null) {
                    output = new FileOutputStream("sdcard/gaweshana/"+sUrl[1]+".jpeg");
                }
                Log.d("Image", "byte");
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    //if (fileLength > 0) // only if total length is known
                    //publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.d("Image", e.toString());
                e.getStackTrace();
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return sUrl[1];

        }
    }
}
