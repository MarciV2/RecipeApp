package com.dhbw.informatik.recipeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.activity.SelectAreaActivity;
import com.dhbw.informatik.recipeapp.activity.SelectCategoryActivity;
import com.dhbw.informatik.recipeapp.activity.SelectMainIngredientActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFilterFragment newInstance(String param1, String param2) {
        SelectFilterFragment fragment = new SelectFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filters, container, false);

        root.findViewById(R.id.toMainIngredients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectMainIngredientActivity.class);
                startActivity(i);
            }
        });

        root.findViewById(R.id.toCategories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectCategoryActivity.class);
                startActivity(i);
            }
        });

        root.findViewById(R.id.toCountries).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectAreaActivity.class);
                startActivity(i);
            }
        });

        return root;
    }
}