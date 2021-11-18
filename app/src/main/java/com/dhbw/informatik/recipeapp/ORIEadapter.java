package com.dhbw.informatik.recipeapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.informatik.recipeapp.activity.CreateOwnRecipeActivity;
import com.dhbw.informatik.recipeapp.model.OwnRecipeIngredientElement;

import java.util.List;

/**
 * Created by Marcel Vidmar
 * Adapter für die Darstellung und Verarbeitung der Felder für Zutaten und Mengen, dem Button (neue zutat)
 * Ebenfalls wird hier die AutoCompletion eingefügt
 */
public class ORIEadapter extends RecyclerView.Adapter<ORIEadapter.ORIEViewHolder> {

    private List<OwnRecipeIngredientElement> orieList;
    private CreateOwnRecipeActivity createOwnRecipeActivity;
    private String[] availableIngredients;
    private ArrayAdapter<String> arrayAdapter;

    public ORIEadapter(List<OwnRecipeIngredientElement> orieList, CreateOwnRecipeActivity createOwnRecipeActivity) {
        this.orieList = orieList;
        this.createOwnRecipeActivity = createOwnRecipeActivity;

    }

    /**
     * Setzt die Liste der bereits in api verwendeten zutaten und aktualisiert diese in allen auto-complete-feldern
     * @param availableIngredients Liste aller bereits in api verwendeten zutaten
     */
    public void setAvailableIngredients(String[] availableIngredients) {
        this.availableIngredients = availableIngredients;

        //Autocomplete für ingredient einfügen
        arrayAdapter=new ArrayAdapter<String>(createOwnRecipeActivity,
                android.R.layout.simple_selectable_list_item,
                availableIngredients);
                this.notifyDataSetChanged();


    }

    @NonNull
    @Override
    public ORIEadapter.ORIEViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        //Unterscheiden in "normales element" oder button am ende
        if (viewType == R.layout.ownrecipeingredientelement)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ownrecipeingredientelement, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.newingredientbtn, parent, false);

        return new ORIEViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ORIEadapter.ORIEViewHolder holder, int position) {
        if (position == orieList.size()) {
            //am ende -> button
            holder.btnAddIngr.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            createOwnRecipeActivity.addIngredient();
                        }
                    }
            );
        } else {
            //nicht am ende -> ingredientElement
            OwnRecipeIngredientElement orie = orieList.get(position);


            //text setzen, dient, um beim scrollen werte von recycelten elementen nicht zu verlieren
            holder.actvIngredient.setText(orie.getIngredient());
            holder.etMeasurement.setText(orie.getMeasurement());

            //hint anhand von locale setzen mit fortlaufender nummerierung
            holder.actvIngredient.setHint( createOwnRecipeActivity.getString(R.string.ingredient)+String.valueOf(position+1));
            holder.etMeasurement.setHint( createOwnRecipeActivity.getString(R.string.measurement)+String.valueOf(position+1));


            //Funktionalität für auto-complete
            holder.actvIngredient.setThreshold(1);
            holder.actvIngredient.setAdapter(arrayAdapter);




            //TextWatcher, um eingegebene zutat zu übernehmen, würde sonst beim scrollen verloren gehen! + keine end-speicherung notwendig
            holder.actvIngredient.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    orie.setIngredient(editable.toString());
                }
            });

            //TextWatcher, um eingegebene Menge zu übernehmen, würde sonst beim scrollen verloren gehen! + keine end-speicherung notwendig
            holder.etMeasurement.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    orie.setMeasurement(editable.toString());
                }
            });

        }
    }

    /**
     *  anzahl der elemente
     * @return  Anzahl der zutaten +1, da button am ende
     */
    @Override
    public int getItemCount() {
        return orieList.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        return (position == orieList.size()) ? R.layout.newingredientbtn : R.layout.ownrecipeingredientelement;
    }

    class ORIEViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView actvIngredient;
        EditText etMeasurement;
        Button btnAddIngr;

        public ORIEViewHolder(@NonNull View itemView) {
            super(itemView);
            actvIngredient = itemView.findViewById(R.id.actvIngredient);
            etMeasurement = itemView.findViewById(R.id.etMeasurement);
            btnAddIngr = itemView.findViewById(R.id.btnNewIngr);
        }
    }


}
