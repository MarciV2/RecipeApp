package com.dhbw.informatik.recipeapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalActionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalActionsFragment extends Fragment {


    public PersonalActionsFragment() {
        // Required empty public constructor
    }

    public static PersonalActionsFragment newInstance(String param1, String param2) {
        PersonalActionsFragment fragment = new PersonalActionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_actions, container, false);
    }
}
