package edu.csulb.android.photonote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/*
* Display Caption Name and Date in List View
* */
public class CustomAdapter extends ArrayAdapter<PhotoNote> {
    private List<PhotoNote> photoNoteList;
    public CustomAdapter(Context context, int resource, List<PhotoNote> photoNoteList) {
        super(context, resource, photoNoteList);
        this.photoNoteList = photoNoteList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoNote note = photoNoteList.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row, null);
        TextView textView = (TextView) row.findViewById(R.id.rowText);
        TextView textDate = (TextView) row.findViewById(R.id.rowDate);
        textView.setText(note.getCaption());
        textDate.setText(note.getCreatedAt());
        return row;
    }
}
