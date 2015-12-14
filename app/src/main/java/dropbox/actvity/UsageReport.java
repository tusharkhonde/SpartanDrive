package dropbox.actvity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import tushar_sk.spartandrive.FolderActivity;
import tushar_sk.spartandrive.R;

/**
 * Created by nikhilakambham on 12/12/15.
 */
public class UsageReport extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usagereport);

        FolderActivity c = new FolderActivity();
        int folder = c.getFolder();



        int file = c.getNoOfFiles();
        int size = c.getsize();

       String folders = String.valueOf(folder);
       String files = String.valueOf(file);
        TextView f = (TextView) findViewById(R.id.noOfFiles);
       f.setText(files);

        TextView f1 = (TextView) findViewById(R.id.noofFolders);
        f1.setText(folders);




    }




    }
