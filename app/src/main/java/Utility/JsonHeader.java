package Utility;

import com.google.gson.JsonObject;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class JsonHeader {


    public String getUploadHeader(String filename){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", "/Home/Folder/"+ filename);
        jsonObject.addProperty("mode", "add");
        jsonObject.addProperty("autorename", true);
        jsonObject.addProperty("mute", false);

        return jsonObject.toString();
    }

    public String getSearchHeader(String filename){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", "");
        jsonObject.addProperty("query",filename);
        jsonObject.addProperty("start", 0);
        jsonObject.addProperty("max_results", 10);
        jsonObject.addProperty("mode", "filename");

        return jsonObject.toString();
    }

}
