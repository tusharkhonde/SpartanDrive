package dropbox.actvity;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import Utility.JsonHeader;
/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class ListFiles extends AsyncTask<String, Void, String> {


    StringBuilder response = null;
    StringBuilder urlBuilder = null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;


    public String doInBackground(String... params) {

        String url = params[0];
        String accessToken = params[1];
        String query = params[2];
        String reqBody = new JsonHeader().getListHeader(query);

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
        } catch (Exception e) {
            return String.valueOf(responseCode);
        }
        return String.valueOf(responseCode);
    }

    @Override
    protected void onPostExecute(String data) {

        Log.v("Response Code", data);
    }
}