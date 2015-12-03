package Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TUSHAR_SK on 12/2/15.
 */
public class FolderJSON {

    public String name;
    public String path;
    public String type;

    public FolderJSON(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FolderJSON() {
    }

    // Constructor to convert JSON object into a Java class instance
        public FolderJSON(Map<String,String> object){
            this.name = object.get("name");
            this.path = object.get("path_lower");
            this.type = object.get("tag");
        }

    public ArrayList<FolderJSON> fromJson(List<Map<String,String>> jsonObjects) {
        ArrayList<FolderJSON> folderModels = new ArrayList<>();
        System.out.println("Inside folder json");
        for (int i = 0; i < jsonObjects.size(); i++) {
            try {
                    folderModels.add(new FolderJSON(jsonObjects.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return folderModels;
    }
    }

