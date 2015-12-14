package dropbox.actvity;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Utility.AsyncInterface;
import Utility.JsonHeader;

/**
 * Created by TUSHAR_SK on 12/11/15.
 */
public class GcmNotifyUser extends AsyncTask<String, Void, List<String>> {


    StringBuilder response = null;
    StringBuilder urlBuilder = null;
    BufferedReader in = null;
    HttpURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;

    public AsyncInterface delegate = null;//Call back interface

    public GcmNotifyUser(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }

    public List<String> doInBackground(String... params) {

        String url = params[0];
        String s_email = params[1];
        String r_email = params[2];
        String reqBody = new JsonHeader().notifyGcmUser(s_email,r_email);
        List<String> list = new ArrayList<String>();

        try {
            response = new StringBuilder();
            urlBuilder = new StringBuilder();
            urlBuilder.append(url);

            URL urlObj = new URL(urlBuilder.toString());
            con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);

            con.setRequestProperty("Content-Type", "application/json");

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
        return list;
    }

    @Override
    protected void onPostExecute(List<String> data) {

        Log.v("Notify Response", data.get(1));
        try {

            String response = data.get(0);
            JSONTokener tokener = new JSONTokener(response);
            JSONObject j;

            j = new JSONObject(tokener);
            System.out.println(j.get("msg"));
            String path = j.get("msg").toString();
            delegate.processCreate(path);


        } catch (JSONException e) {
           delegate.processCreate("Error");
        }

    }
}