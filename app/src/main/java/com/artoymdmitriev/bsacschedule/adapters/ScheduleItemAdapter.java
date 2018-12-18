package com.artoymdmitriev.bsacschedule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.artoymdmitriev.bsacschedule.Entity.Pair;
import com.artoymdmitriev.bsacschedule.R;
import com.artoymdmitriev.bsacschedule.parser.NormalItem;

import java.util.ArrayList;

/**
 * Created by adanilenka
 */

public class ScheduleItemAdapter extends ArrayAdapter<Pair> {
    private ArrayList<Pair> pairs;

    public ScheduleItemAdapter(Context context, int textViewResourceId, ArrayList<Pair> pairs) {
        super(context, textViewResourceId, pairs);
        this.pairs = pairs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // assign the view we are converting to a local variable
        View v = convertView;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
        Pair i = pairs.get(position);

        if (i != null) {

            // This is how you obtain a reference to the TextViews.
            // These TextViews are created in the XML files we defined.

            TextView subject = (TextView) v.findViewById(R.id.subject);
            TextView teacher = (TextView) v.findViewById(R.id.teacher);
            TextView time = (TextView) v.findViewById(R.id.time);
            TextView place = (TextView) v.findViewById(R.id.place);

            // check to see if each individual textview is null.
            // if not, assign some text!

            subject.setText(i.getName());
            teacher.setText(i.getProfessor());
            time.setText(i.getTime());
            place.setText(i.getCabinet());
        }

        // the view must be returned to our activity
        return v;
    }
}
