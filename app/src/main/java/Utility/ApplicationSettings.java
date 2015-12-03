package Utility;

import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by TUSHAR_SK on 11/24/15.
 */
public class ApplicationSettings {
    private static ApplicationSettings sharedSettings = null;

    private String accessToken;

    private String email;

    private boolean flag = false;

    private String path = null;

    private String noFolder = "FALSE";

    private String searchMode = "FALSE";

    private ArrayList<FolderJSON> folderJSONs;

    private ArrayList<FolderJSON> searchFolderJSONs;

    private GoogleApiClient googleApiClient;

    private ApplicationSettings() {
         accessToken = "";

    }

    public ArrayList<FolderJSON> getSearchFolderJSONs() {
        return searchFolderJSONs;
    }

    public void setSearchFolderJSONs(ArrayList<FolderJSON> searchFolderJSONs) {
        this.searchFolderJSONs = searchFolderJSONs;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public String getNoFolder() {
        return noFolder;
    }

    public void setNoFolder(String noFolder) {
        this.noFolder = noFolder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public ArrayList<FolderJSON> getFolderJSONs() {
        return folderJSONs;
    }

    public void setFolderJSONs(ArrayList<FolderJSON> folderJSONs) {
        this.folderJSONs = folderJSONs;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public static ApplicationSettings getSharedSettings() {

        if (sharedSettings == null) {

            sharedSettings = new ApplicationSettings();
        }

        return sharedSettings;
    }


    public String getAccessToken() {

        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {

        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

