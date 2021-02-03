package com.penwallet.roldechill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.penwallet.roldechill.Adapters.CreaturesListAdapter;
import com.penwallet.roldechill.Constants.Constants;
import com.penwallet.roldechill.Entities.Ally;
import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.Fragments.CreateCharacterDialogFragment;
import com.penwallet.roldechill.Fragments.DrawingFragment;
import com.penwallet.roldechill.Fragments.DrawingToolsFragment;
import com.penwallet.roldechill.Fragments.ListFragment;
import com.penwallet.roldechill.Utilities.JsonDeserializerWithInheritance;
import com.penwallet.roldechill.Utilities.MyCanvas;
import com.penwallet.roldechill.Utilities.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements DrawingToolsFragment.DrawingToolsFragInterface, DrawingFragment.DrawingFragInterface, CreaturesListAdapter.CreaturesListAdapterInterface {

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
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);

        viewModel.setPencilColor(preferences.getInt(Constants.SHAREDPREFERENCES_CHOSEN_PENCIL_COLOR, Color.BLACK));

        String creaturesJson = preferences.getString(Constants.SHAREDPREFERENCES_CREATURES_NAME, null);
        if(creaturesJson != null)
        {
            //Try catch por si acaso se tienen datos antiguos. Cuando todos hayan instalado la nueva versión, TODO: Borrar try catch
            try
            {
                Gson gson = new GsonBuilder().registerTypeAdapter(Creature.class, new JsonDeserializerWithInheritance<Creature>()).create();
                Type type = new TypeToken<ArrayList<Creature>>(){}.getType();

                ArrayList<Creature> creaturesList = gson.fromJson(creaturesJson, type);

                //Si no hay ninguna criatura, metemos a los 5 tontos de siempre por defecto (y si acaso al negro)
                if(creaturesList.isEmpty())
                    meterALosMismosDeSiempre();
                else
                    viewModel.getCreatures().setValue(creaturesList);

            }catch(Exception e){
                meterALosMismosDeSiempre();
            }

        }
        else
            meterALosMismosDeSiempre();

        String jsonPaths = preferences.getString(Constants.SHAREDPREFERENCES_PATHS, null);
        if(jsonPaths != null)
        {
            Gson gsonPaths = new GsonBuilder().create();
            Type type = new TypeToken<ArrayList<Pair<Path, Paint>>>(){}.getType();

            ArrayList<Pair<Path, Paint>> paths = gsonPaths.fromJson(jsonPaths, type);

            if(paths.isEmpty())
                viewModel.setDrawnPaths(new ArrayList<Pair<Path, Paint>>());
            else
                viewModel.setDrawnPaths(paths);
        }
        else
            viewModel.setDrawnPaths(new ArrayList<Pair<Path, Paint>>());

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
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();

        Gson gsonCreatures = new GsonBuilder().registerTypeAdapter(Creature.class, new JsonDeserializerWithInheritance<Creature>()).create();
        String jsonCreatures = gsonCreatures.toJson(viewModel.getCreatures().getValue());

        Gson gsonPaths = new Gson();
        String jsonPaths = gsonPaths.toJson(viewModel.getDrawnPaths());

        prefsEditor.putString(Constants.SHAREDPREFERENCES_CREATURES_NAME, jsonCreatures);
        prefsEditor.putString(Constants.SHAREDPREFERENCES_PATHS, jsonPaths);
        prefsEditor.putInt(Constants.SHAREDPREFERENCES_CHOSEN_PENCIL_COLOR, viewModel.getPencilColor());
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

        toolsFrameParams.weight = Constants.TOOLSFRAME_WEIGHT;
        mainFrameParams.weight = Constants.MAINFRAME_WEIGHT;

        mainFrame.setLayoutParams(mainFrameParams);
        toolsFrame.setLayoutParams(toolsFrameParams);
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

    @Override
    public void undoLastAction() {
        drawingFragment.undoLastAction();
    }

    @Override
    public void selectEraser() {
        drawingFragment.selectEraser();
    }

    @Override
    public void selectPencil() {
        drawingFragment.selectPencil();
    }

    @Override
    public void clearCanvas() {
        drawingFragment.clearCanvas();
    }

    @Override
    public void refreshDraggingList() {
        drawingToolsFragment.refreshList();
    }

    @Override
    public void abrirEditar(int position) {
        listFragment.abrirEditar(position);
    }

    @Override
    public void changeColorPencil(int color) {
        drawingFragment.changePencilColor(color);
        viewModel.setPencilColor(color);
    }

    private void meterALosMismosDeSiempre()
    {
        //Metemos a los 5 tontos de siempre por defecto (y si acaso al negro)
        /*viewModel.getCreatures().getValue().add(new Ally("Oscar", 1, 1, 0, Status.NORMAL, 0));
        viewModel.getCreatures().getValue().add(new Ally("Miguel", 1, 1, 0, Status.NORMAL, 0));
        viewModel.getCreatures().getValue().add(new Ally("Olga", 1, 1, 0, Status.NORMAL, 0));
        viewModel.getCreatures().getValue().add(new Ally("Fran", 1, 1, 0, Status.NORMAL, 0));
        viewModel.getCreatures().getValue().add(new Ally("Triana", 1, 1, 0, Status.NORMAL, 0));
        viewModel.getCreatures().getValue().add(new Ally("Jose", 1, 1, 0, Status.NORMAL, 0));*/
    }
}
