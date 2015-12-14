package dropbox.actvity;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import org.apache.http.util.ByteArrayBuffer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import Utility.AsyncInterface;
import Utility.Constants;

/**
 * Created by TUSHAR_SK on 12/3/15.
 */
public class DownloadFile extends AsyncTask<String, Void, List<String>> {

    public String TAG= "Down Activity";
    public AsyncInterface delegate = null;//Call back interface

    public DownloadFile(AsyncInterface asyncInterface) {
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
        Log.i(TAG,"after split");
        for(String r :arr_)
        {
            Log.i(TAG,r);
        }
        FileOutputStream fos = null;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/"+arr_[arr_.length-1] );
        try {
            ucon = new URL(url).openConnection();
            ucon.addRequestProperty("Authorization", accessToken);
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
        if (!data.isEmpty()){
            delegate.processCreate(data.get(0));
            Log.i(TAG,"data:"+ data.get(0));
        }
        else
            delegate.processCreate("not successful");
    }
}