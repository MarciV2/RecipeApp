package com.dhbw.informatik.recipeapp.fragment;

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

import com.dhbw.informatik.recipeapp.activity.MealDetailActivity;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private MainActivity mainActivity;
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;

    public HomeFragment(MainActivity mainActivity) {

        this.mainActivity=mainActivity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealList=new ArrayList<>();
        updateMeals();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealPreviewRecyclerView=view.findViewById(R.id.recyclerViewOfMeals);

        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        mealPreviewAdapter=new MealPreviewAdapter(mealList,mainActivity);
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);


        getActivity().findViewById(R.id.toMeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), MealDetailActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        mainActivity.queryFunctionality();
        super.onViewStateRestored(savedInstanceState);
    }


    /**
     * befüllt das recycler view
     */
    private void updateMeals() {

        //API-Aufrufe starten

                for(int i=0; i<10; i++){
                    Call<MealList> call = MainActivity.apiService.getRandomRecipe();
                    call.enqueue(new Callback<MealList>() {
                        @Override
                        public void onResponse(@NonNull Call<MealList> call, @NonNull Response<MealList> response) {

                            //Abfangen/Ausgeben Fehlercode Bsp. 404
                            if (!response.isSuccessful()) {
                                Log.d("ERROR", "Code: " + response.code());
                                return;
                            }
                            List<Meal> tmp2MealList = response.body().getMeals();
                            Meal m=tmp2MealList.get(0);
                            m.fillArrays();
                            Log.d("dev","Rezept geholt: "+tmp2MealList.get(0).getStrMeal()+" insgesamt: "+mealList.size());

                            mealPreviewAdapter.update(m);

                        }

                        @Override
                        public void onFailure(Call<MealList> call, Throwable t) {
                            Snackbar.make(mealPreviewRecyclerView, "Network error!", BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });

                }




    }



}