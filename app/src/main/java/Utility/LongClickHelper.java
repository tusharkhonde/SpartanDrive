package Utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import dropbox.actvity.DeleteFileFolder;
import dropbox.actvity.DownloadFile;
import dropbox.actvity.GcmNotifyUser;
import dropbox.actvity.ShareFileFolder;
import dropbox.actvity.ViewFileActivity;
import tushar_sk.spartandrive.FolderActivity;
import tushar_sk.spartandrive.R;

/**
 * Created by TUSHAR_SK on 12/12/15.
 */
public class LongClickHelper implements AdapterView.OnItemLongClickListener {

    public String TAG = "Long Click Helper";
    Context context;

    public LongClickHelper( Context context) {
        this.context = context;
        Log.i("Context",this.context.toString());
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, FolderActivity.arrayList.toString());
        String path = FolderActivity.arrayList.get(position).getPath();
        final String finalFromPath = path;
        final String name = FolderActivity.arrayList.get(position).getName();
        String type = FolderActivity.arrayList.get(position).getType();

        Log.i(TAG, "path:" + path + "name:" + name);
        LayoutInflater li = LayoutInflater.from(context);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        CharSequence[] types = {};

        CharSequence[] fileTypes = {"Share","Delete", "Email", "Download", "View"};
        CharSequence[] folderTypes = {"Share", "Delete"};

        if(type.equalsIgnoreCase("folder")){
            types = folderTypes;

            // set dialog message
            alertDialogBuilder.setItems(types,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Handle Share
                            if (id == 0) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.dialog_view, null);
                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder1.setView(promptsView);
                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput2);

                                alertDialogBuilder1
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        final ShareFileFolder shareFileFolder = new ShareFileFolder(new AsyncInterface() {

                                                            @Override
                                                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                            }

                                                            @Override
                                                            public void processCreate(String path) {
                                                                Log.v("Share Result", path);

                                                                final GcmNotifyUser gcmNotifyUser = new GcmNotifyUser(new AsyncInterface() {

                                                                    @Override
                                                                    public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                                    }

                                                                    @Override
                                                                    public void processCreate(String path) {
                                                                        Log.v("Notify Result", path);


                                                                    }
                                                                });
                                                                Log.v("email print s", ApplicationSettings.getSharedSettings().getEmail());
                                                                Log.v("email print r", userInput.getText().toString());
                                                                gcmNotifyUser.execute(Constants.gcmNotifyUrl, ApplicationSettings.getSharedSettings().getEmail(), userInput.getText().toString());


                                                            }
                                                        });

                                                        shareFileFolder.execute(Constants.shareFileFolder, Constants.accessToken, finalFromPath,
                                                                "/" + userInput.getText().toString() + "/share/" + ApplicationSettings.getSharedSettings().getEmail() + "/" + name);

                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                // create alert dialog
                                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                // show it
                                alertDialog1.show();
                            }

                            // Handle Delete
                            else if (id == 1) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.dialog_view, null);
                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder1.setView(promptsView);

                                TextView textView = (TextView) promptsView.findViewById(R.id.textView4);
                                textView.setText("Confirm Delete !");
                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput2);
                                userInput.setVisibility(View.INVISIBLE);
                                alertDialogBuilder1
                                        .setCancelable(false)
                                        .setPositiveButton("Delete",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        final DeleteFileFolder deleteFileFolder = new DeleteFileFolder(new AsyncInterface() {

                                                            @Override
                                                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                            }

                                                            @Override
                                                            public void processCreate(String path) {
                                                                Log.v("Delete Result ", path);
                                                                Intent intent = new Intent(context, FolderActivity.class);
                                                                context.startActivity(intent);

                                                            }
                                                        });

                                                        deleteFileFolder.execute(Constants.deleteFileUrl, Constants.accessToken, finalFromPath);

                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                // create alert dialog
                                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                // show it
                                alertDialog1.show();
                            }


                        }
                    }
            );
        }
        else if(type.equalsIgnoreCase("file")){
            types = fileTypes;

            // set dialog message
            alertDialogBuilder.setItems(types,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Handle Share
                            if (id == 0) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.dialog_view, null);
                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder1.setView(promptsView);
                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput2);

                                alertDialogBuilder1
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        final ShareFileFolder shareFileFolder = new ShareFileFolder(new AsyncInterface() {

                                                            @Override
                                                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                            }

                                                            @Override
                                                            public void processCreate(String path) {
                                                                Log.v("Share Result", path);

                                                                final GcmNotifyUser gcmNotifyUser = new GcmNotifyUser(new AsyncInterface() {

                                                                    @Override
                                                                    public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                                    }

                                                                    @Override
                                                                    public void processCreate(String path) {
                                                                        Log.v("Notify Result", path);


                                                                    }
                                                                });
                                                                Log.v("email print s", ApplicationSettings.getSharedSettings().getEmail());
                                                                Log.v("email print r", userInput.getText().toString());
                                                                gcmNotifyUser.execute(Constants.gcmNotifyUrl, ApplicationSettings.getSharedSettings().getEmail(), userInput.getText().toString());


                                                            }
                                                        });

                                                        shareFileFolder.execute(Constants.shareFileFolder, Constants.accessToken, finalFromPath,
                                                                "/" + userInput.getText().toString() + "/share/" + ApplicationSettings.getSharedSettings().getEmail() + "/" + name);

                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                // create alert dialog
                                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                // show it
                                alertDialog1.show();
                            }

                            // Handle Delete
                            else if (id == 1) {
                                LayoutInflater li = LayoutInflater.from(context);
                                View promptsView = li.inflate(R.layout.dialog_view, null);
                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder1.setView(promptsView);

                                TextView textView = (TextView) promptsView.findViewById(R.id.textView4);
                                textView.setText("Confirm Delete !");
                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput2);
                                userInput.setVisibility(View.INVISIBLE);
                                alertDialogBuilder1
                                        .setCancelable(false)
                                        .setPositiveButton("Delete",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        final DeleteFileFolder deleteFileFolder = new DeleteFileFolder(new AsyncInterface() {

                                                            @Override
                                                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {

                                                            }

                                                            @Override
                                                            public void processCreate(String path) {
                                                                Log.v("Delete Result ", path);
                                                                Intent intent = new Intent(context, FolderActivity.class);
                                                                context.startActivity(intent);

                                                            }
                                                        });

                                                        deleteFileFolder.execute(Constants.deleteFileUrl, Constants.accessToken, finalFromPath);

                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                // create alert dialog
                                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                // show it
                                alertDialog1.show();
                            }

                            // Handle Email
                            else if (id == 2) {
                                LayoutInflater li = LayoutInflater.from(context);
                                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                                        context);
                                View promptsView = li.inflate(R.layout.dialog_view, null);
                                alertDialogBuilder1.setView(promptsView);
                                final EditText userInput = (EditText) promptsView
                                        .findViewById(R.id.editTextDialogUserInput2);
                                alertDialogBuilder1
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        final ViewFileActivity emailFile = new ViewFileActivity(new AsyncInterface() {
                                                            @Override
                                                            public void processFinish(ArrayList<FolderJSON> folderJSONs) {
                                                            }

                                                            @Override
                                                            public void processCreate(String path) {
                                                                Log.v(TAG, path);
                                                                try {
                                                                    Log.i(TAG, "path:" + path);
                                                                    final Intent emailIntent = new Intent(
                                                                            android.content.Intent.ACTION_SEND);
                                                                    String subject = "Email from Spartan Drive";
                                                                    String emailtext = "Please check the attached mail";
                                                                    emailIntent.setType("vnd.android.cursor.dir/email");
                                                                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/tmp" + path));
                                                                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                                                            new String[]{userInput.getText().toString()});
                                                                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                                                                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);
                                                                    context.startActivity(Intent
                                                                            .createChooser(emailIntent, "Send mail..."));
                                                                    Log.i(TAG, "processCreate");

                                                                } catch (Throwable t) {
                                                                    t.printStackTrace();
                                                                }

                                                            }

                                                        });
                                                        emailFile.execute(Constants.downloadFileUrl + finalFromPath, Constants.accessToken, finalFromPath, name);
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                // create alert dialog
                                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                                // show it
                                alertDialog1.show();
                            }


                            //Handle Download
                            else if (id == 3) {
                                final DownloadFile downloadFile = new DownloadFile(new AsyncInterface() {
                                    @Override
                                    public void processFinish(ArrayList<FolderJSON> folderJSONs) {
                                    }

                                    @Override
                                    public void processCreate(String path) {
                                        Toast toast = Toast.makeText(context, "File downloaded in path:/Download/" + name, Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                                downloadFile.execute(Constants.downloadFileUrl + finalFromPath, Constants.accessToken, finalFromPath, name);

                            }

                            // Handle View
                            else if (id == 4) {
                                final ViewFileActivity viewFile = new ViewFileActivity(new AsyncInterface() {
                                    @Override
                                    public void processFinish(ArrayList<FolderJSON> folderJSONs) {
                                    }

                                    @Override
                                    public void processCreate(String path) {
                                        String ext = "";
                                        Log.v(TAG, path);
                                        if (path.contains(".pdf"))
                                            ext = ".pdf";
                                        else if (path.contains(".xls"))
                                            ext = ".xls";
                                        else if (path.contains(".doc"))
                                            ext = ".doc";
                                        else if (path.contains(".ppt"))
                                            ext = ".ppt";

                                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/tmp" + ext);
                                        Log.d("file", file.toString());
                                        Intent target = new Intent(Intent.ACTION_VIEW);

                                        if (path.contains(".ppt")) {
                                            target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-powerpoint");
                                        } else if (path.contains(".pdf")) {
                                            target.setDataAndType(Uri.fromFile(file), "application/pdf");

                                        } else if (path.contains(".xls")) {
                                            target.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");

                                        } else if (path.contains(".doc")) {
                                            target.setDataAndType(Uri.fromFile(file), "application/msword");

                                        }
                                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                        Intent intent = Intent.createChooser(target, "Open File");
                                        context.startActivity(intent);
                                    }
                                });
                                viewFile.execute(Constants.downloadFileUrl + finalFromPath, Constants.accessToken, finalFromPath, name);

                            }



                        }
                    }
            );
        }


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }
}