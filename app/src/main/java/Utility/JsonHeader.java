package Utility;

import com.google.gson.JsonObject;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class JsonHeader {


    public String getString(String filename){
        JsonObject organizationObject = new JsonObject();
        organizationObject.addProperty("path", "/Home/Folder/"+ filename);
        organizationObject.addProperty("mode", "add");
        organizationObject.addProperty("autorename", true);
        organizationObject.addProperty("mute", false);

        return organizationObject.toString();

    }
}
