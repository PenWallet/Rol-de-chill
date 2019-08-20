package com.penwallet.roldechill;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Database.creatures.add(new Creature("Oscar", 2, 21, 8, true, Status.ENVENENADO));
        Database.creatures.add(new Creature("Miguel", 20, 21, 5, true, Status.ATURDIDO));
        Database.creatures.add(new Creature("Fran", 20, 21, 21, true, Status.SANGRANDO));
        Database.creatures.add(new Creature("Olga", 20, 21, 22, true, Status.NORMAL));
        Database.creatures.add(new Creature("Bicho 1", 20, 21, 8, false, Status.NORMAL));
        Database.creatures.add(new Creature("Personaje 1", 20, 21, 1, true, Status.NORMAL));
        Database.creatures.add(new Creature("Bicho 2", 20, 21, 2, false, Status.NORMAL));
        Database.creatures.add(new Creature("Olga", 20, 21, 19, false, Status.NORMAL));*/

        Database.listView = findViewById(R.id.dragList);

        Database.listView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter listAdapter = new ItemAdapter(Database.creatures, R.layout.character_layout, R.id.cardViewCharacter, true, this);
        Database.listView.setAdapter(listAdapter, false);
        Database.listView.setCanDragHorizontally(false);
        Database.listView.setCustomDragItem(null);
    }

    public void orderByIniciativa(View view) {
        Collections.sort(Database.creatures, new Comparator<Creature>() {
            @Override
            public int compare(Creature o1, Creature o2)
            {
                if(o1.getIniciativa() < o2.getIniciativa())
                    return 1;
                else if(o1.getIniciativa() > o2.getIniciativa())
                    return -1;
                else
                {
                    if(o1.getVida() < o2.getVida())
                        return 1;
                    else if(o1.getVida() > o2.getVida())
                        return -1;
                    else
                    {
                        if(o1.getVidaMaxima() < o2.getVidaMaxima())
                            return 1;
                        else if(o1.getVidaMaxima() > o2.getVidaMaxima())
                            return -1;
                        else
                            return o1.getNombre().compareTo(o2.getNombre());
                    }
                }
            }
        });

        Database.listView.getAdapter().notifyDataSetChanged();
    }

    public void addCreature(View view) {
        DialogFragment popup = new CreateCharacterDialogFragment();
        popup.show(getSupportFragmentManager(), "popup");
    }
}
