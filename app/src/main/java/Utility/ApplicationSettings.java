package Utility;

import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by TUSHAR_SK on 11/24/15.
 */
public class ApplicationSettings {
    private static ApplicationSettings sharedSettings = null;

    private String accessToken;

    private GoogleApiClient googleApiClient;

    private ApplicationSettings() {
         accessToken = "";

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


}

