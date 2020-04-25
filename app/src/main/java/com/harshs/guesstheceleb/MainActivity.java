package com.harshs.guesstheceleb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class celeb extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {

         String result="";

            URL url=null;

            try {

                url = new URL(urls[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data=reader.read();

                while(data != -1){

                    char cuurent = (char)data;

                    result +=cuurent;

                    data = reader.read();

                }
                return result;

                }
            catch (Exception e) {

                e.printStackTrace();

            }


            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        celeb task = new celeb();
        String result =null;

        try {

            result=task.execute("http://www.posh24.se/kandisar").get();

            Log.i("Content of URL", result);

        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }
    }
}
