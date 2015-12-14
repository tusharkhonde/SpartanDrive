package dropbox.actvity;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import Utility.AsyncInterface;
import Utility.JsonHeader;

/**
 * Created by TUSHAR_SK on 12/13/15.
 */
public class DeleteFileFolder extends AsyncTask<String, Void, List<String>>{

    public AsyncInterface delegate = null;//Call back interface

    public DeleteFileFolder(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }

    StringBuilder response = null;
    StringBuilder urlBuilder = null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;

    @Override
    protected List<String> doInBackground(String... params) {

        String url = params[0];
        String accessToken = params[1];
        String sourceFilePath = params[2];
        String reqBody = new JsonHeader().getCreateFolderHeader(sourceFilePath);
        List<String> list = new ArrayList<String>();

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

        Log.v("Delete Response", data.get(1));
        try {
            String response = data.get(0);
            JSONTokener tokener = new JSONTokener(response);
            JSONObject j;

            j = new JSONObject(tokener);

            System.out.println(j.get(".tag"));
            String tag = j.get(".tag").toString();

            System.out.println(j.get("name"));
            String name = j.get("name").toString();

            delegate.processCreate("Sucess");


        } catch (JSONException e) {
           delegate.processCreate("Error");
        }

    }
}