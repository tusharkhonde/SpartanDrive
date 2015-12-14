package dropbox.actvity;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import Utility.ApplicationSettings;
import Utility.AsyncInterface;
import Utility.FolderJSON;
import Utility.JsonHeader;

/**
 * Created by TUSHAR_SK on 12/3/15.
 */
public class ShareFileFolder extends AsyncTask<String, Void, List<String>> {


    public AsyncInterface delegate = null;//Call back interface

    public ShareFileFolder(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }


    StringBuilder response = null;
    StringBuilder urlBuilder = null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;
    public List<Map<String, String>> list = new ArrayList<>();


    public List<String> doInBackground(String... params) {

        String url = params[0];
        String accessToken = params[1];
        String from = params[2];
        String to = params[3];
        String reqBody = new JsonHeader().shareFileFolder(from, to);
        System.out.println(reqBody);
        List<String> list = new ArrayList<>();

        try {
            response = new StringBuilder();
            urlBuilder = new StringBuilder();
            urlBuilder.append(url);

            URL urlObj = new URL(urlBuilder.toString());
            con = (HttpsURLConnection) urlObj.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);

            con.setRequestProperty("Authorization", accessToken);
            con.setRequestProperty("Content-Type", "application/json");

            System.out.println(reqBody);
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(reqBody);

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            responseCode = con.getResponseCode();


            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println(response);
            list.add(response.toString());
            list.add(String.valueOf(responseCode));

        } catch (Exception e) {
            list.add(null);
            list.add(String.valueOf(responseCode));
            return list;
        }
        Log.v("List Response", list.get(1));
        return list;
    }

    @Override
    protected void onPostExecute(List<String> data) {

        Log.v("List Response", data.get(1));

        if (Integer.valueOf(data.get(1)) == -1) {

            delegate.processCreate("Error Copying");

        } else {

            delegate.processCreate("Share Sucessfull");

        }

    }
}