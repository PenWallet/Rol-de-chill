package com.penwallet.roldechill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.penwallet.roldechill.Constants.SharedPreferencesConstants;
import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.Fragments.CreateCharacterDialogFragment;
import com.penwallet.roldechill.Fragments.DrawingFragment;
import com.penwallet.roldechill.Fragments.DrawingToolsFragment;
import com.penwallet.roldechill.Fragments.ListFragment;
import com.penwallet.roldechill.Utilities.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    ListFragment listFragment;
    DrawingFragment drawingFragment;
    DrawingToolsFragment drawingToolsFragment;
    FrameLayout toolsFrame;
    FrameLayout mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        //Cogemos los datos del SharedPreferences si existen
        SharedPreferences preferences = this.getSharedPreferences(SharedPreferencesConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String creaturesJson = preferences.getString(SharedPreferencesConstants.CREATURES_SHAREDPREFERENCES_NAME, null);
        if(creaturesJson != null)
        {
            Type type = new TypeToken<ArrayList<Creature>>(){}.getType();
            ArrayList<Creature> creaturesList = new Gson().fromJson(creaturesJson, type);

            //Si no hay ninguna criatura, metemos a los 4 tontos de siempre por defecto
            if(creaturesList.size() == 0)
            {
                //Metemos a Miguel, Olga y a mi por defecto
                viewModel.getCreatures().getValue().add(new Creature("Oscar", 1, 1, 0, true, Status.NORMAL, 0));
                viewModel.getCreatures().getValue().add(new Creature("Miguel", 1, 1, 0, true, Status.NORMAL, 0));
                viewModel.getCreatures().getValue().add(new Creature("Olga", 1, 1, 0, true, Status.NORMAL, 0));
                viewModel.getCreatures().getValue().add(new Creature("Triana", 1, 1, 0, true, Status.NORMAL, 0));
            }
            else
                viewModel.getCreatures().setValue(creaturesList);
        }
        else
        {
            //Si no hay ninguna criatura, metemos a los 4 tontos de siempre por defecto
            viewModel.getCreatures().getValue().add(new Creature("Oscar", 1, 1, 0, true, Status.NORMAL, 0));
            viewModel.getCreatures().getValue().add(new Creature("Miguel", 1, 1, 0, true, Status.NORMAL, 0));
            viewModel.getCreatures().getValue().add(new Creature("Olga", 1, 1, 0, true, Status.NORMAL, 0));
            viewModel.getCreatures().getValue().add(new Creature("Triana", 1, 1, 0, true, Status.NORMAL, 0));
        }

        listFragment = new ListFragment();
        drawingFragment = new DrawingFragment();
        drawingToolsFragment = new DrawingToolsFragment();
        toolsFrame = findViewById(R.id.drawingToolsFrame);
        mainFrame = findViewById(R.id.mainFrame);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, listFragment).commit();


    }

    @Override
    protected void onStop() {
        super.onStop();

        //Guardar a SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences(SharedPreferencesConstants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(viewModel.getCreatures().getValue());
        prefsEditor.putString(SharedPreferencesConstants.CREATURES_SHAREDPREFERENCES_NAME, json);
        prefsEditor.apply();
    }

    //Ordenar por iniciativa
    public void orderByIniciativa(View view) {
        Utils.animateClick(view);
        Collections.sort(viewModel.getCreatures().getValue(), new Comparator<Creature>() {
            @Override
            public int compare(Creature o1, Creature o2)
            {
                if(o1.getIniciativa() < o2.getIniciativa())
                    return 1;
                else if(o1.getIniciativa() > o2.getIniciativa())
                    return -1;
                else
                    return 0;
            }
        });

        viewModel.getPerformListRefresh().setValue(true);
    }

    public void addCreature(View view) {
        Utils.animateClick(view);
        DialogFragment popup = new CreateCharacterDialogFragment();
        popup.show(getSupportFragmentManager(), "popup");
    }

    public void changeToDrawingFragment(View view) {
        Utils.animateClick(view);

        //Cambios de fragmentos
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFrame, drawingFragment);
        ft.replace(R.id.drawingToolsFrame, drawingToolsFragment);
        ft.addToBackStack("Drawing");
        ft.commit();

        //Cambios de weight en el linear layout
        LinearLayout.LayoutParams toolsFrameParams = (LinearLayout.LayoutParams)toolsFrame.getLayoutParams();
        LinearLayout.LayoutParams mainFrameParams = (LinearLayout.LayoutParams)mainFrame.getLayoutParams();

        toolsFrameParams.weight = 1.3f;
        mainFrameParams.weight = 8.7f;

        mainFrame.setLayoutParams(mainFrameParams);
        toolsFrame.setLayoutParams(toolsFrameParams);
    }

    public void choosePencil(View view) {
        viewModel.getIsPencilSelected().setValue(true);
        Utils.animateClick(view);
    }

    public void chooseEraser(View view) {
        viewModel.getIsPencilSelected().setValue(false);
        Utils.animateClick(view);
    }

    public void undoLastAction(View view) {
        viewModel.getUndoLastAction().setValue(true);
        Utils.animateClick(view);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0)
            super.onBackPressed();
        else
        {
            getSupportFragmentManager().popBackStack();

            //Cambios de weight en el linear layout
            LinearLayout.LayoutParams toolsFrameParams = (LinearLayout.LayoutParams)toolsFrame.getLayoutParams();
            LinearLayout.LayoutParams mainFrameParams = (LinearLayout.LayoutParams)mainFrame.getLayoutParams();

            toolsFrameParams.weight = 0f;
            mainFrameParams.weight = 10f;

            mainFrame.setLayoutParams(mainFrameParams);
            toolsFrame.setLayoutParams(toolsFrameParams);
        }
    }
}
