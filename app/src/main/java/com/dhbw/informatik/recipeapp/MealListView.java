package com.dhbw.informatik.recipeapp;
/**
 * Created by TutorialsPoint7 on 9/14/2016.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class MealListView extends androidx.appcompat.widget.AppCompatTextView {
    private String textToBeDisplayed;

    public MealListView(Context context) {
        super(context);
    }

    public MealListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // retrieved values correspond to the positions of the attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MealListView);
        int count = typedArray.getIndexCount();
        try{

            for (int i = 0; i < count; ++i) {

                int attr = typedArray.getIndex(i);
                // the attr corresponds to the title attribute
                if(attr == R.styleable.MealListView_entireText) {

                    // set the text from the layout
                    textToBeDisplayed = typedArray.getString(attr);

                    updateView();
                }
            }
        }

        // the recycle() will be executed obligatorily
        finally {
            // for reuse
            typedArray.recycle();
        }
    }

    public MealListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void updateView(){
        setText(textToBeDisplayed);
    }

}