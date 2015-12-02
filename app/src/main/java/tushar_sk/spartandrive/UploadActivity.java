package tushar_sk.spartandrive;



import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import Utility.Constants;
import Utility.FileArrayAdapter;
import Utility.Option;
import dropbox.actvity.UploadFile;

/**
 * Created by TUSHAR_SK on 11/28/15.
 */
public class UploadActivity extends ListActivity{


    private File currentDir;
    FileArrayAdapter adapter;
    Stack<File> dirStack = new Stack<File>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentDir = new File("/sdcard/");
        fill(currentDir);

    }

    //manpreet
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Directory: "+f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option>fls = new ArrayList<Option>();
        adapter = new FileArrayAdapter(UploadActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()) {
                    //manpreet12
                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if (fbuf != null) {
                        buf = fbuf.length;
                    } else buf = 0;
                    String num_item = String.valueOf(buf);
                    if (buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //

                    dir.add(new Option(ff.getName(),num_item, date_modify,ff.getAbsolutePath(),"directory_icon"));
                }
                else
                {
                    if (ff.getName().contains(".xls")) {
                        fls.add(new Option(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "xls_icon"));
                    }
                    else if (ff.getName().contains(".pdf")) {
                        fls.add(new Option(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "pdf_icon"));
                    }
                    else if (ff.getName().contains(".ppt")) {
                        fls.add(new Option(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "ppt_icon"));
                    }
                    else if (ff.getName().contains(".doc")) {
                        fls.add(new Option(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "doc_icon"));
                    }
//                    else {
//                        fls.add(new Option(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "file_icon"));
//                    }
                }
            }
        }catch(Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory","",f.getParent(),"directory_up"));
    }

    @Override
    public void onBackPressed() {
        if (dirStack.size() == 0) {
            finish();
            return;
        }
        currentDir = dirStack.pop();
        fill(currentDir);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }


    //manpreet
    //To DO upload the file clicked
    private void onFileClick(Option o)
    {

        Toast.makeText(this, "File Clicked: " + currentDir+"/"+o.getName(), Toast.LENGTH_SHORT).show();
        String FilePath = currentDir+"/"+o.getName();
        new UploadFile().execute(Constants.fileUplaodUrl, Constants.accessToken, FilePath, o.getName().toString());
        Log.v("path of the file", currentDir.toString());
        Log.v("File clicked",o.getName().toString());
        Intent intent = new Intent(this, FolderActivity.class);
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    "No Application Available to View PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
