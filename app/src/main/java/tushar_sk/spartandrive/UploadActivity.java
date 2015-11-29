package tushar_sk.spartandrive;



import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import Utility.FileArrayAdapter;
import Utility.Option;

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

    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option>fls = new ArrayList<Option>();
        adapter = new FileArrayAdapter(UploadActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
        try{
            for(File ff: dirs)
            {
                if(ff.isDirectory())
                    dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                else
                {
                    fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                }
            }
        }catch(Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory",f.getParent()));
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
        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }

    //To DO upload the file clicked
    private void onFileClick(Option o)
    {
        Toast.makeText(this, "File Clicked: " + o.getName(), Toast.LENGTH_SHORT).show();
    }

}
