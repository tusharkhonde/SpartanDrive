package dropbox.actvity;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import Utility.AsyncInterface;
import Utility.JsonHeader;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class UploadFile extends AsyncTask<String, Void, String> {

    public AsyncInterface delegate = null;//Call back interface

    public UploadFile(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }

    StringBuilder response = null;
    StringBuilder urlBuilder =  null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;

    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        String accessToken = params[1];
        String sourceFilePath = params[2];
        String destFileName = params[3];
        String destFolderPath = params[4];

        try {
            response = new StringBuilder();
            urlBuilder = new StringBuilder();
            urlBuilder.append(url);

            URL urlObj = new URL(urlBuilder.toString());
            con = (HttpsURLConnection) urlObj.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);

            con.setRequestProperty("Authorization", accessToken);
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Dropbox-API-Arg", new JsonHeader().getUploadHeader(destFolderPath,destFileName));

            FileBody fileBody = new FileBody(new File(sourceFilePath));
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
            multipartEntity.addPart("file", fileBody);

            OutputStream out = con.getOutputStream();
            multipartEntity.writeTo(out);
            responseCode = con.getResponseCode();


        } catch (Exception e) {
            return String.valueOf(responseCode);
         }
        return String.valueOf(responseCode);
    }

    @Override
    protected void onPostExecute(String data) {

        Log.v("Response Code",data);
        delegate.processCreate("Upload");
    }

}
