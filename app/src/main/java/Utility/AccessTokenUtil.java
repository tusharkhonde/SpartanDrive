package Utility;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by TUSHAR_SK on 11/24/15.
 */
public class AccessTokenUtil extends AsyncTask {
    GoogleConnectionUtilProtocol delegate;

    public interface  GoogleConnectionUtilProtocol {

        void didGenerateAccessToken(String accessToken);
        void didCatchException(Exception exc);
        Activity getActivity();
    }


    private final static String DRIVE_API_SCOPE
            = "https://www.googleapis.com/auth/drive.metadata.readonly";
    private final static String mScopes
            = "oauth2:" + DRIVE_API_SCOPE;

    String mEmail;

    public AccessTokenUtil (Activity activity, String name) {

        this.mEmail = name;
        delegate = (GoogleConnectionUtilProtocol) activity;
    }

    private String fetchToken() throws IOException {

        try {

            return GoogleAuthUtil.getToken(delegate.getActivity(), mEmail, mScopes);
        } catch (UserRecoverableAuthException userRecoverableException) {

            delegate.didCatchException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {

            delegate.didCatchException(fatalException);
        }
        return null;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String accessToken = fetchToken();
            if (accessToken != null) {

                System.out.println("Access Token "+accessToken);
                delegate.didGenerateAccessToken(accessToken);
            }
        } catch (IOException e) {

        }
        return null;
    }
}
