package com.penwallet.roldechill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cv = findViewById(R.id.btnAddCreature);
        cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("¿Borrar todas las criaturas?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Database.creatures.clear();
                        Database.listView.getAdapter().notifyDataSetChanged();
                    }
                });

                alertDialog.setCancelable(true);

                alertDialog.show();

                return true;
            }
        });

        //Cogemos los datos del SharedPreferences si existen
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        String creaturesJson = preferences.getString(Constants.CREATURES_SHAREDPREFERENCES_NAME, null);
        if(creaturesJson != null)
        {
            Type type = new TypeToken<ArrayList<Creature>>(){}.getType();
            ArrayList<Creature> creaturesList = new Gson().fromJson(creaturesJson, type);

            //Si no hay ninguna criatura, metemos a los 3 tontos de siempre por defecto
            if(creaturesList.size() == 0)
            {
                //Metemos a Miguel, Olga y a mi por defecto
                Database.creatures.add(new Creature("Oscar", 1, 1, 0, true, Status.NORMAL, 0));
                Database.creatures.add(new Creature("Miguel", 1, 1, 0, true, Status.NORMAL, 0));
                Database.creatures.add(new Creature("Olga", 1, 1, 0, true, Status.NORMAL, 0));
            }
            else
                Database.creatures = creaturesList;
        }
        else
        {
            //Si no hay ninguna criatura, metemos a los 3 tontos de siempre por defecto
            Database.creatures.add(new Creature("Oscar", 1, 1, 0, true, Status.NORMAL, 0));
            Database.creatures.add(new Creature("Miguel", 1, 1, 0, true, Status.NORMAL, 0));
            Database.creatures.add(new Creature("Olga", 1, 1, 0, true, Status.NORMAL, 0));
        }

        Database.listView = findViewById(R.id.dragList);

        Database.listView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter listAdapter = new ItemAdapter(Database.creatures, R.layout.character_layout, R.id.cardViewCharacter, true, this);
        Database.listView.setAdapter(listAdapter, false);
        Database.listView.setCanDragHorizontally(false);
        Database.listView.setCustomDragItem(null);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Guardar a SharedPreferences
        SharedPreferences preferences = this.getSharedPreferences(Constants.SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Database.creatures); // myObject - instance of MyObject
        prefsEditor.putString(Constants.CREATURES_SHAREDPREFERENCES_NAME, json);
        prefsEditor.apply();
    }

    //Ordenar por iniciativa. Si ambas iniciativas son iguales, entonces se ordena por el nombre.
    public void orderByIniciativa(View view) {
        Utils.animateClick(view);
        Collections.sort(Database.creatures, new Comparator<Creature>() {
            @Override
            public int compare(Creature o1, Creature o2)
            {
                if(o1.getIniciativa() < o2.getIniciativa())
                    return 1;
                else if(o1.getIniciativa() > o2.getIniciativa())
                    return -1;
                else
                    return o1.getNombre().compareTo(o2.getNombre());
            }
        });

        Database.listView.getAdapter().notifyDataSetChanged();
    }

    public void addCreature(View view) {
        Utils.animateClick(view);
        DialogFragment popup = new CreateCharacterDialogFragment();
        popup.show(getSupportFragmentManager(), "popup");
    }
}
