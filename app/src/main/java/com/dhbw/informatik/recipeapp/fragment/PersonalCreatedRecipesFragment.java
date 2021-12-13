package com.dhbw.informatik.recipeapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.CreateOwnRecipeActivity;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.model.Meal;

import java.util.List;


public class PersonalCreatedRecipesFragment extends Fragment {
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private FileHandler fileHandler;


    public PersonalCreatedRecipesFragment() {
        fileHandler=FileHandler.getInstance();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_personal_created_recipes, container, false);

        root.findViewById(R.id.btnCreateOwn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), CreateOwnRecipeActivity.class);
                startActivityForResult(i,111);
            }
        });

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateMeals();
    }


    /**
     * befüllt das recycler view
     */
    private void updateMeals() {
        fileHandler.readFiles();

        if(fileHandler.ownRecipes==null)return;
        mealList=fileHandler.ownRecipes.getMeals();

        mealPreviewRecyclerView=getView().findViewById(R.id.recyclerViewLastClicked);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(),RecyclerView.VERTICAL,false));
        mealPreviewAdapter=new MealPreviewAdapter(mealList,getActivity());
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);
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
        if(resultCode==RESULT_OK)
        if(requestCode==111)
        {
            Meal m= (Meal) data.getSerializableExtra("meal");
            if(m!=null) {


                fileHandler.ownRecipes.getMeals().add(m);
                fileHandler.saveFiles();
                updateMeals();
            }
        }
    }
}