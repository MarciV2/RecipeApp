package com.dhbw.informatik.recipeapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.CreateOwnRecipeActivity;
import com.dhbw.informatik.recipeapp.activity.LastClickedActivity;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ApiTestFragment extends Fragment {

    private MainActivity mainActivity;
private FileHandler fileHandler;
    BottomNavigationView navigationView;


    public ApiTestFragment(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileHandler=FileHandler.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_api_test, container, false);
        root.findViewById(R.id.btnCreateOwn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), CreateOwnRecipeActivity.class);
                startActivityForResult(i,1);
            }
        });
        root.findViewById(R.id.lastClickedActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), LastClickedActivity.class);
                startActivityForResult(i,1);
            }
        });
        return root;
    }

    /**
     * CREATED BY Marcel Vidmar
     * Callback, für wenn die CreateOwnRecipeActivity fertig ist und ein rezept liefert
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK)
        {
            Meal m= (Meal) data.getSerializableExtra("meal");
            if(m!=null) {
                fileHandler.ownRecipes.getMeals().add(m);
                fileHandler.saveFiles();
            }
        }
    }

}