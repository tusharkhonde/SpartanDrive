package Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import tushar_sk.spartandrive.R;

/**
 * Created by TUSHAR_SK on 12/2/15.
 */
public class FolderAdapter extends ArrayAdapter<FolderJSON> {

    private ArrayList<FolderJSON> objects;

    public FolderAdapter(Context context, int resourceId ,ArrayList<FolderJSON> folderModelArrayList) {
        super(context, resourceId , folderModelArrayList);
        this.objects = folderModelArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FolderJSON folderModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.frame_file, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.TextView01);
        TextView tvHome = (TextView) convertView.findViewById(R.id.TextView02);

        ImageView image = (ImageView) convertView.findViewById(R.id.fd_Icon1);

        // Populate the data into the template view using the data object
        tvName.setText(folderModel.getName());


        if(folderModel.getType().equalsIgnoreCase("folder")){
            Picasso.with(getContext()).load(R.drawable.directory_icon).into(image);
            tvHome.setText("Folder");
        }
        else if(folderModel.getType().equalsIgnoreCase("file")){


            if(folderModel.getName().contains(".pdf")) {
                Picasso.with(getContext()).load(R.drawable.pdf_icon).into(image);
                tvHome.setText("PDF File");
            }
            else if(folderModel.getName().contains(".doc")) {
                Picasso.with(getContext()).load(R.drawable.doc_icon).into(image);
                tvHome.setText("DOC File");
            }
            else if(folderModel.getName().contains(".xls")) {
                Picasso.with(getContext()).load(R.drawable.xls_icon).into(image);
                tvHome.setText("XLS File");
            }
            else if(folderModel.getName().contains(".ppt")) {
                Picasso.with(getContext()).load(R.drawable.ppt_icon).into(image);
                tvHome.setText("PPT File");
            }
            else {
                Picasso.with(getContext()).load(R.drawable.file).into(image);
                tvHome.setText("File");
            }

        }

        // Return the completed view to render on screen
        return convertView;
    }
}