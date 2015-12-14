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
import Utility.AsyncInterface;
import Utility.FolderJSON;
import Utility.JsonHeader;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */

public class SearchFile extends AsyncTask<String, Void, List<String>>{


    public AsyncInterface delegate = null;//Call back interface

    public SearchFile(AsyncInterface asyncInterface) {
        delegate = asyncInterface;//Assigning call back interface through constructor
    }

    StringBuilder response = null;
    StringBuilder urlBuilder =  null;
    BufferedReader in = null;
    HttpsURLConnection con = null;
    int responseCode = -1;
    DataOutputStream wr = null;
    public List<Map<String,String>> list = new ArrayList<>();



    public List<String> doInBackground(String... params){

        String url = params[0];
        String accessToken = params[1];
        String query = params[2];
        String path = params[3];
        String reqBody = new JsonHeader().getSearchHeader(path,query);
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

            list.add(response.toString());
            list.add(String.valueOf(responseCode));

            System.out.println(response);
        }
        catch (Exception e) {
            list.add(null);
            list.add(String.valueOf(responseCode));
            return list;
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<String> data) {

        Log.v("Response Code", data.get(1));
        if(Integer.valueOf(data.get(1)) == -1){

            delegate.processCreate("Create");

        }

        else{

            try {

                String response = data.get(0);
                JSONTokener tokener = new JSONTokener(response);
                JSONObject j;

                j = new JSONObject(tokener);

                if(j.getInt("start") == 0){
                    delegate.processFinish(new ArrayList<FolderJSON>());
                }
                else {
                    JSONArray j1 = (JSONArray) j.get("matches");
                    for (int i = 0; i < j1.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        JSONObject j2 = j1.getJSONObject(i);

                        JSONObject j3 = j2.getJSONObject("metadata");

                        map.put("name", j3.get("name").toString());
                        map.put("path_lower", j3.get("path_lower").toString());

                        System.out.println(j3.get("name"));
                        System.out.println(j3.get("path_lower"));
                        System.out.println(j3.get(".tag"));

                        map.put("tag", j3.get(".tag").toString());

                        list.add(i, map);
                    }
                }
            }
            catch(JSONException e){
                Log.v("Search List","List Empty");

            }

            ArrayList<FolderJSON> newUsers = new FolderJSON().fromJson(list);
            delegate.processFinish(newUsers);

        }
    }
}

