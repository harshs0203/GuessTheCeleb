package com.harshs.guesstheceleb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> celebUrls = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    int choosenCeleb=0;

    ImageView imageView;

    public class celebImg extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection Connection = (HttpURLConnection) url.openConnection();

                Connection.connect();

                InputStream in = Connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return myBitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

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

        imageView=(ImageView)findViewById(R.id.imageView);


        try {

            result=task.execute("http://www.posh24.se/kandisar").get();

            String[] splitResult = result.split("<div class=\"sidebarInnerContainer\">");

            Pattern p = Pattern.compile("img=\"(.*?)\"");
            Matcher m =p.matcher(splitResult[0]);

            while (m.find()){

                celebUrls.add(m.group(1));
            }

             p = Pattern.compile("alt=\"(.*?)\"");
             m =p.matcher(splitResult[0]);

            while (m.find()){

                celebNames.add(m.group(1));;
            }

            Random random = new Random();
            choosenCeleb= random.nextInt(celebUrls.size());

            celebImg imgTask= new celebImg();
            Bitmap myBitmap;

            myBitmap=imgTask.execute(celebUrls.get(choosenCeleb)).get();
            imageView.setImageBitmap(myBitmap);


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }
    }
}
