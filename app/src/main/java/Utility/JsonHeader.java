package Utility;

import com.google.gson.JsonObject;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class JsonHeader {


    public String getUploadHeader(String path, String filename){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", path +"/"+ filename);
        jsonObject.addProperty("mode", "add");
        jsonObject.addProperty("autorename", true);
        jsonObject.addProperty("mute", false);

        return jsonObject.toString();
    }

    public String getSearchHeader(String path,String filename){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", "/"+filename);
        jsonObject.addProperty("query",path);
        jsonObject.addProperty("start", 0);
        jsonObject.addProperty("max_results", 10);
        jsonObject.addProperty("mode", "filename");

        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }

    public String getListHeader(String folder){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", folder);
        jsonObject.addProperty("recursive",false);
        jsonObject.addProperty("include_media_info", false);
        jsonObject.addProperty("include_deleted", false);

        return jsonObject.toString();
    }

    public String getCreateFolderHeader(String folder){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("path", folder);
        return jsonObject.toString();
    }

    public String shareFileFolder(String from, String to){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_path", from);
        jsonObject.addProperty("to_path",to);
        return jsonObject.toString();
    }

    public String insertGcmUser(String email, String regId){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_id", email);
        jsonObject.addProperty("reg_id",regId);
        return jsonObject.toString();
    }

    public String notifyGcmUser(String sender, String receiver){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("s_email_id", sender);
        jsonObject.addProperty("r_email_id",receiver);
        return jsonObject.toString();
    }

}
