package com.penwallet.roldechill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.Utilities.Utils;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    ListFragment listFragment;
    DrawingFragment drawingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        //Cogemos los datos del SharedPreferences si existen
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String creaturesJson = preferences.getString(Constants.CREATURES_SHAREDPREFERENCES_NAME, null);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, listFragment).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Guardar a SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(viewModel.getCreatures().getValue());
        prefsEditor.putString(Constants.CREATURES_SHAREDPREFERENCES_NAME, json);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, drawingFragment).addToBackStack(null).commit();
    }
}
