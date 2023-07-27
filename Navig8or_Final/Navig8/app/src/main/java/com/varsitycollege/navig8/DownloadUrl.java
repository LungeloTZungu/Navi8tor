package com.varsitycollege.navig8;
//App creators: Mohamed Rajab-ST10116167, Reeselin Pillay-ST10117187,Terell Rangasamy-ST10117009, Fransua Somers-ST10117162, Lungelo Zungu-ST10116993
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Code attribution
//Links: www.youtube.com/watch?v=wRDLjUK8nyU
//Author: The Code City (2018)
//Accessed: 18 Oct. 2022

public class DownloadUrl {
   public String retrieveUrl(String url) throws IOException{
       String urlData = "";
       HttpURLConnection httpURLConnection = null;
       InputStream inputStream = null;

       try{
           URL getUrl = new URL(url);
           httpURLConnection = (HttpURLConnection) getUrl.openConnection();
           httpURLConnection.connect();


           inputStream = httpURLConnection.getInputStream();
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
           StringBuffer sb = new StringBuffer();

           String line = "";
           while((line = bufferedReader.readLine()) != null){
               sb.append(line);

           }
           urlData = sb.toString();
           bufferedReader.close();

       } catch (Exception exception){
           Log.d("Exception",exception.toString());
       } finally{
           inputStream.close();
           httpURLConnection.disconnect();
       }
       return urlData;
   }


}

