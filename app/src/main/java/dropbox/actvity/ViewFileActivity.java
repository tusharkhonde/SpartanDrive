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
 * Created by TUSHAR_SK on 12/3/15.
 */
public class ViewFileActivity extends AsyncTask<String, Void, List<String>> {

    public String TAG= "ViewActivivty";
    public AsyncInterface delegate = null;//Call back interface

    public ViewFileActivity(AsyncInterface asyncInterface) {
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
        FileOutputStream fos = null;
        String ext="";
        Log.i(TAG,arr_.length+Environment.getExternalStorageDirectory().getAbsolutePath() +" \"/Download\", \"/tmp.pdf\"");
        Log.i(TAG,arr_[arr_.length-1] +"name:"+nm);
        if(nm.contains(".pdf")){
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download", "/tmp.pdf");
            ext = ".pdf";
        }
        else if(nm.contains(".ppt")){
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download", "/tmp.ppt");
            Log.i(TAG,"ppt");
            ext = ".ppt";
        }

        else if(nm.contains(".xls")){
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download", "/tmp.xls");
            Log.i(TAG,"xls");
            ext = ".xls";
        }
        else if(nm.contains(".doc")){
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download", "/tmp.doc");
            Log.i(TAG,"doc");
            ext = ".doc";
        }

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
                list.add(ext);
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
            Log.i(TAG, data.get(0));
        }
        else
            delegate.processCreate("not successfull");
    }
}
