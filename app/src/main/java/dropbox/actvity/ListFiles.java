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
import Utility.Constants;
import Utility.FolderJSON;
import Utility.JsonHeader;


/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class ListFiles extends AsyncTask<String, Void, List<String>> {


    public AsyncInterface delegate = null;//Call back interface

    public ListFiles(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }


    StringBuilder response = null;
    StringBuilder urlBuilder = null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;
    public List<Map<String,String>> list = new ArrayList<>();


    public List<String> doInBackground(String... params) {

        String url = params[0];
        String accessToken = params[1];
        String query = params[2];
        String reqBody = new JsonHeader().getListHeader(query);
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

        if(Integer.valueOf(data.get(1)) == -1){
//            new CreateFile().execute(Constants.createFoldersUrl, Constants.accessToken, "/"+ApplicationSettings.getSharedSettings().getEmail());
              ApplicationSettings.getSharedSettings().setNoFolder("TRUE");
              delegate.processCreate("Create");

        }

        else{
        String response = data.get(0);
        JSONTokener tokener = new JSONTokener(response);
        JSONObject j;

        try {

            j = new JSONObject(tokener);

            JSONArray j1 = (JSONArray) j.get("entries");
            for (int i = 0; i < j1.length(); i++) {
                Map<String, String> map = new HashMap<>();
                JSONObject j2 = j1.getJSONObject(i);

                map.put("name", j2.get("name").toString());
                map.put("path_lower", j2.get("path_lower").toString());
                Log.v("file", j2.get("name").toString());
                System.out.println(j2.get("name"));
                System.out.println(j2.get("path_lower"));
                map.put("tag", j2.get(".tag").toString());
                System.out.println(j2.get(".tag"));
                if (!j2.get(".tag").equals("folder")) {
                    map.put("size", j2.get("size").toString());
                    map.put("date", j2.get("client_modified").toString());
                    System.out.println(j2.get("size"));
                    System.out.println(j2.get("client_modified"));
                }
                list.add(i, map);
            }
        }
            catch(JSONException e){
                Log.v("MSG","list empty");
            }

            ArrayList<FolderJSON> newUsers = new FolderJSON().fromJson(list);
            delegate.processFinish(newUsers);

        }

    }
}