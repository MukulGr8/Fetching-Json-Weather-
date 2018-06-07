package com.example.mukul.demojson;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    String weatherOk;
    public void findWeather(View view){
        //Hides The KeyBoard After Tap On Weather Button
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            //Encoding The Big URL like Sans Fransico Which HAve Space In Between Them
            String encodedCountry = URLEncoder.encode(editText.getText().toString(),"UTF-8");
            result = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCountry+"&APPID=339117dcecbd0090000dfb0acfb3ba24").get();
            //Creating New Json Object And Store The Value Of Result to jsonObject
            JSONObject jsonObject = new JSONObject(result);
            //Find The Weather String in jsonOBject and Store
            String weatherInfo = jsonObject.getString("weather");
            //Creating A Array To Loop Through The All The Json Values Which weatherInfo String has in it
            Log.i("Content Found",weatherInfo);
            JSONArray arr = new JSONArray(weatherInfo);
            //Creating a Loop which Will Loop Through every String Of json Which weatherInfo has in it
            for (int i=0;i<arr.length();i++){
                //Acessing The Json values In Array On The Given Index Arrays
                JSONObject jsonPart = arr.getJSONObject(i);
                //Finally find The Strings From The jsonPart Object
                Log.i("main",jsonPart.getString("main"));
                weatherOk = jsonPart.getString("description");
            }
            textView.setText(weatherOk);//Sowing The Weather
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url ;
            HttpURLConnection urlConnection=null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
    }
}
