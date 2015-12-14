package tushar_sk.spartandrive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import Utility.ApplicationSettings;
import Utility.AsyncInterface;
import Utility.Constants;
import Utility.FolderAdapter;
import Utility.FolderJSON;
import dropbox.actvity.*;


/**
 * Created by TUSHAR_SK on 11/23/15.
 */
public class FolderActivity extends AppCompatActivity{

    public static String query = "";
    public static ArrayList<FolderJSON> arrayList;
    public static ListView listView;
    public static String path;
    final Context context = this;
    private EditText result;
    public static int noOfFiles = 0;
    public static int noOfFolders = 0;
    public static double usage = 0.0;
    public static int size = 0;
    public String TAG = "FolderActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_activity);

        Log.v("email folder", ApplicationSettings.getSharedSettings().getEmail());

        if(ApplicationSettings.getSharedSettings().getPath() == null) {
                path = "/"+ApplicationSettings.getSharedSettings().getEmail();

        }
        else {
            path = ApplicationSettings.getSharedSettings().getPath();
        }

        listView = (ListView) findViewById(R.id.listViewFolder);

     if(ApplicationSettings.getSharedSettings().getSearchMode().equals("FALSE")) {

         final ListFiles listFiles = new ListFiles(new AsyncInterface() {

             @Override
             public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                 try {
                     Log.v("size array", String.valueOf(arrayList.size()));
                 } catch (Exception e) {
                     Log.v("catch", "size 0");
                 } finally {
                     arrayList = folderJSONs;
                     Log.v("size array", String.valueOf(arrayList.size()));
                     FolderAdapter itemsAdapter = new FolderAdapter(getAct().getApplicationContext(), R.layout.fragment_list, arrayList);
                     listView.setAdapter(itemsAdapter);
                 }

             }

             @Override
             public void processCreate(String success) {
                 final CreateFile createFile = new CreateFile(new AsyncInterface() {

                     @Override
                     public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                     }

                     @Override
                     public void processCreate(String path) {
                         ApplicationSettings.getSharedSettings().setNoFolder("FALSE");
                         ApplicationSettings.getSharedSettings().setPath(path);
                         Intent intent = new Intent(getAct(), FolderActivity.class);
                         startActivity(intent);

                     }
                 });

                 createFile.execute(Constants.createFoldersUrl, Constants.accessToken, path);
             }
         });

         listFiles.execute(Constants.listFilesFoldersurl, Constants.accessToken, path);


         listView.setOnItemClickListener(
                 new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

                         Log.v("log path", arrayList.get(pos).getPath());
                         Log.v("log type", arrayList.get(pos).getType());
                         if (arrayList.get(pos).getType().equals("folder")) {

                             ApplicationSettings.getSharedSettings().setPath(arrayList.get(pos).getPath());
                             ApplicationSettings.getSharedSettings().setFlag(true);
                             Log.v("log folder", "folder");
                             arrayList.removeAll(arrayList);
                             Intent intent = new Intent(getAct(), FolderActivity.class);
//                            intent.putExtra("video", arrayList.get(pos).getPath());
                             startActivity(intent);

                         }

                     }

                 });
         Log.v("id", String.valueOf(R.id.listViewFolder));
     }
        else {

         arrayList = ApplicationSettings.getSharedSettings().getSearchFolderJSONs();
         Log.v("size array search", String.valueOf(arrayList.size()));
         FolderAdapter itemsAdapter = new FolderAdapter(getAct().getApplicationContext(), R.layout.fragment_list, arrayList);
         listView.setAdapter(itemsAdapter);
         ApplicationSettings.getSharedSettings().setPath("/"+ApplicationSettings.getSharedSettings().getEmail());
         ApplicationSettings.getSharedSettings().setSearchMode("FALSE");
     }



        handleIntent(getIntent());
    }



    public Activity getAct(){
        return this;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String q) {
                // perform query here
                query = q;
                Log.v("msg:", query);
                Log.v("Search Email", ApplicationSettings.getSharedSettings().getEmail());
                SearchFile searchFile = new SearchFile(
                        new AsyncInterface() {
                            @Override
                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {
                                ApplicationSettings.getSharedSettings().setSearchMode("TRUE");
                                ApplicationSettings.getSharedSettings().setSearchFolderJSONs(folderJSONs);
                                Intent i = new Intent(getApplicationContext(), FolderActivity.class);
                                i.putExtra("query", query);
                                startActivity(i);
                            }

                            @Override
                            public void processCreate(String sucess) {

                                Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
                            }
                        });
                Log.v("query",query);
                searchFile.execute(Constants.searchFilesUrl, Constants.accessToken, ApplicationSettings.getSharedSettings().getEmail(), query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }

    @Override
    public void onBackPressed() {

        path = ApplicationSettings.getSharedSettings().getPath();
        try {
            int charCount = path.length() - path.replaceAll("/", "").length();
            Log.v("Back Pressed", path);


            if (charCount == 1) {
                finish();
            } else {
                int i = path.lastIndexOf("/");
                path = path.subSequence(0, i).toString();
                Log.v("Back Presses", path);
                ApplicationSettings.getSharedSettings().setPath(path);
                Intent intent = new Intent(this, FolderActivity.class);
                startActivity(intent);

            }
        }
        catch (Exception e){
           finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.attach_file) {
            //Create Intent for File VIew Activity
            Intent FileView = new Intent(this, UploadActivity.class);
            //Start File View Activity
            startActivity(FileView);
            return true;
        }


        else if (id == R.id.usagereport){

            noOfFiles =0;
            noOfFolders = 0;

            final ListFiles listFiles = new ListFiles(new AsyncInterface() {


                @Override
                public void processFinish(ArrayList<FolderJSON> folderJSONs) {


                    try {
                        Log.v(TAG, String.valueOf(arrayList.size()));
                    } catch (Exception e) {
                        Log.v(TAG, "size 0");
                    } finally {
                        arrayList = folderJSONs;
                        Log.v("size array", String.valueOf(arrayList.size()));
                        Log.v("t",String.valueOf(arrayList.get(0).getType()));

                        for(int i=0; i<arrayList.size();i++)
                        {
                            if(arrayList.get(i).getType().equals("folder")){
                                noOfFolders = noOfFolders + 1;
                                Log.d("path", arrayList.get(i).getPath());

                                getFolderData(arrayList.get(i).getPath());


                            }
                            else if(arrayList.get(i).getType().contains("file")) {
                                noOfFiles = noOfFiles + 1;


                            }
                        }
                        Log.d("folders", String.valueOf(noOfFolders));
                        Log.d("files,", String.valueOf(noOfFiles));


                    }
// handler to delay...

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            show();
                        }
                    }, 3000);


                }

                @Override
                public void processCreate(String sucess) {


                }

            });
            path = "/"+ApplicationSettings.getSharedSettings().getEmail();
            listFiles.execute(Constants.listFilesFoldersurl, Constants.accessToken, path);



            return true;

        }

        else if (id == R.id.Add_Folder) {
            Toast.makeText(getApplicationContext(), "Add a folder!!", Toast.LENGTH_LONG).show();
            //abcdef
            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.prompts, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    Log.v("Create folder",userInput.getText().toString());
                                    final CreateFile createFile = new CreateFile(new AsyncInterface() {

                                        @Override
                                        public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                        }

                                        @Override
                                        public void processCreate(String path) {
//                                            ApplicationSettings.getSharedSettings().setNoFolder("FALSE");
                                            ApplicationSettings.getSharedSettings().setPath(path);
                                            Intent intent = new Intent(getAct(),FolderActivity.class);
                                            startActivity(intent);

                                        }
                                    });

                                    createFile.execute(Constants.createFoldersUrl, Constants.accessToken, path+"/"+userInput.getText().toString());

                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }


                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void getFolderData(String path1){

        final ListFiles listFiles = new ListFiles(new AsyncInterface() {


            @Override
            public void processFinish(ArrayList<FolderJSON> folderJSONs) {


                try {
                    Log.v(TAG, String.valueOf(arrayList.size()));
                } catch (Exception e) {
                    Log.v(TAG, "size 0");
                } finally {
                    arrayList = folderJSONs;
                    Log.v("size array", String.valueOf(arrayList.size()));
//                    Log.v("t",String.valueOf(arrayList.get(0).getType()));
                    Log.v("g", String.valueOf(folderJSONs));

                    for(int i=0; i<arrayList.size();i++)
                    {
                        if(arrayList.get(i).getType().equals("folder")){
                            noOfFolders = noOfFolders + 1;
                            Log.d("path", arrayList.get(i).getPath());
                            getFolderData(arrayList.get(i).getPath());


                        }
                        else if(arrayList.get(i).getType().contains("file"))
                            noOfFiles = noOfFiles + 1;


                    }
                    Log.d("folders", String.valueOf(noOfFolders));
                    Log.d("files,", String.valueOf(noOfFiles));
                }
//                show();

            }


            @Override
            public void processCreate(String sucess) {
                Log.d("","");

            }
        });


        listFiles.execute(Constants.listFilesFoldersurl, Constants.accessToken,path1);



    }

    public void show(){

        Intent intent = new Intent(getAct(),UsageReport.class);
        startActivity(intent);

    }



    public int getFolder(){
        return noOfFolders;
    }

    public int getNoOfFiles(){
        return noOfFiles;
    }

    public void setSize(int s){
        size =s;
    }
    public int getsize(){
        return size;
    }






}
