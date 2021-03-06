package com.devsan.loctionary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devsan.loctionary.R;
import com.devsan.loctionary.WordMeaningActivity;

public class FragmentDefinition extends Fragment {

    // Required Empty Contructor
    public FragmentDefinition() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition, container, false);

        Context context = getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);

        String definition = ((WordMeaningActivity)context).enDefinition;

        text.setText(definition);
        if (definition == null) {
            text.setText("No definition found");
        }

        return view;
    }

}
