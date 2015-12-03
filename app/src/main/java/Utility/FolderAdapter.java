package Utility;

import android.content.Context;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.folder_title);
        TextView tvHome = (TextView) convertView.findViewById(R.id.folder_path);
        // Populate the data into the template view using the data object
        tvName.setText(folderModel.getName());
        tvHome.setText(folderModel.getPath());
        // Return the completed view to render on screen
        return convertView;
    }
}