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


/**
 * Created by nikhilakambham on 12/12/15.
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
        String nm = params[3];

        Log.i(TAG,"url:"+url+"\taccesstoken"+accessToken+"\tfilename:"+file_name+"\tnm:"+nm);
        String[] arr_ = file_name.split("/");
        FileOutputStream fos = null;
        String ext="";
       /* Log.i(TAG,arr_.length+Environment.getExternalStorageDirectory().getAbsolutePath() +" \"/Download\", \"/tmp.pdf\"");
        Log.i(TAG,arr_[arr_.length-1] +"name:"+nm);*/
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
        list.add(ext);
        try {
            Log.i(TAG,"in try block");
            ucon = new URL(url).openConnection();
            ucon.addRequestProperty("Authorization", accessToken);
            ucon.setRequestProperty("Content-Type", "application/octet-stream");
            HttpURLConnection httpConnection = (HttpURLConnection) ucon;
            int responseCode = 0;

            fos = new FileOutputStream(file);
            responseCode = httpConnection.getResponseCode();
            Log.i(TAG, "Response code:"+ String.valueOf(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedInputStream bis = null;

                bis = new BufferedInputStream(ucon.getInputStream());
                Log.d(TAG, bis.toString());
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                Log.i(TAG,"downloading");
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
        if(!data.isEmpty()) {
            delegate.processCreate(data.get(0));
            Log.i(TAG, "extension type"+data.get(0));
        }
        else
            delegate.processCreate("not successful");
    }
}
