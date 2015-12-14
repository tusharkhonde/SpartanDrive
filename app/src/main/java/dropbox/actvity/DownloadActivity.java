package dropbox.actvity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import Utility.ApplicationSettings;
import Utility.AsyncInterface;
import Utility.Constants;
import Utility.FolderJSON;
import Utility.JsonHeader;

/**
 * Created by nikhilakambham
 */
public class DownloadActivity extends AsyncTask<String, Void, List<String>> {

    public String TAG= "DownloadActivivty";
    public AsyncInterface delegate = null;//Call back interface

    public DownloadActivity(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }

    public List<String> list = new ArrayList<>();

    URLConnection ucon = null;

    public List<String> doInBackground(String... params) {
       File file = null;
        String url = params[0];
        String accessToken = params[1];
        String file_name = params[2];
        String nm= params[3];
        String[] arr_ = file_name.split("/");
        Log.i(TAG,"file name:"+file_name);
        Log.i(TAG,"aftrer split");
        for(String r :arr_)
        {
            Log.i(TAG,r);
        }
        FileOutputStream fos = null;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/"+arr_[arr_.length-1] );
        try {
            ucon = new URL(url).openConnection();
            ucon.addRequestProperty("Authorization", Constants.accessToken);
            ucon.setRequestProperty("Content-Type", "application/octet-stream");
            HttpURLConnection httpConnection = (HttpURLConnection) ucon;
            int responseCode = 0;

            fos = new FileOutputStream(file);
            responseCode = httpConnection.getResponseCode();
            Log.d(TAG, String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {
                list.add(arr_[arr_.length-1]);
                BufferedInputStream bis = null;

                bis = new BufferedInputStream(ucon.getInputStream());
                Log.d(TAG, bis.toString());
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                fos.write(baf.toByteArray());
                fos.close();
                bis.close();

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<String> data) {
        if(data!=null) {
            delegate.processCreate(data.get(0));
            Log.i(TAG,"data:"+ data.get(0).toString());
        }
        else
            delegate.processCreate("not successfull");
    }
}
