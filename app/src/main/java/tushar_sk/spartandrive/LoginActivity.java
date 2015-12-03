package tushar_sk.spartandrive;


import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.plus.Plus;
import Utility.AccessTokenUtil;
import Utility.ApplicationSettings;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, AccessTokenUtil.GoogleConnectionUtilProtocol{

    private static final String TAG = "LoginActivity";

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    // [END resolution_variables]

    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [END restore_saved_instance_state]

        // Set up button click listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // Large sign-in
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);

        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addScope(Drive.SCOPE_APPFOLDER)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        // [END create_google_api_client]
    }


    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mGoogleApiClient.disconnect();
    }
    // [END on_start_on_stop]

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        mGoogleApiClient.connect();
        Log.v("Connected","Connected to Google");
    }



    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended:" + i);
//        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
// Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
//        mGoogleApiClient.connect();

//        // Change Activity
//        Intent intent = new Intent(LoginActivity.this, FolderActivity.class);
//        startActivity(intent);

        pickUserAccount();


    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;

                            }
                        }).show();
            } else {
                Log.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);


        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {

            if (resultCode == RESULT_OK) {

                String mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.v("email Result ok:",mEmail);
                ApplicationSettings.getSharedSettings().setEmail(mEmail);
                Log.v("email get", ApplicationSettings.getSharedSettings().getEmail());
                getUsername(mEmail);
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Select an account to proceed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR
                && resultCode == RESULT_OK) {

            String mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.v("email Result ok &&:",mEmail);
            ApplicationSettings.getSharedSettings().setEmail(mEmail);
            getUsername(mEmail);
        }

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            String mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            ApplicationSettings.getSharedSettings().setEmail(mEmail);
            Log.v("email Rc sign in:",mEmail);
            getUsername(mEmail);

            mIsResolving = false;
//            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == RC_PERM_GET_ACCOUNTS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    private void getUsername(String mEmail) {

        if (mEmail == null) {

            pickUserAccount();
        } else {

            if (isDeviceOnline()) {

                Log.v("email:", mEmail);
                new AccessTokenUtil(this, mEmail).execute();
                ApplicationSettings.getSharedSettings().setEmail(mEmail);

            } else {

                Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }
    public boolean isDeviceOnline() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            return true;
        }

        return false;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};

        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }



    @Override
    public void didGenerateAccessToken(String accessToken) {
        ApplicationSettings.getSharedSettings().setAccessToken(accessToken);
        ApplicationSettings.getSharedSettings().setGoogleApiClient(mGoogleApiClient);
         Log.v("dgat",ApplicationSettings.getSharedSettings().getEmail());
        Intent intent = new Intent(this, FolderActivity.class);
        startActivity(intent);
    }

    @Override
    public void didCatchException(final Exception e) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (e instanceof UserRecoverableAuthException) {

                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public GoogleApiClient getmGoogleApiClient(){
        return mGoogleApiClient;
    }


}
