package com.penwallet.roldechill;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.penwallet.roldechill.Entities.Creature;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Creature>> creatures;
    private MutableLiveData<Integer> selectedCreature;

    public MainViewModel()
    {
        creatures = new MutableLiveData<>();
        selectedCreature = new MutableLiveData<>();

        creatures.setValue(new ArrayList<Creature>());
    }

    public MutableLiveData<ArrayList<Creature>> getCreatures() {
        return creatures;
    }

    public MutableLiveData<Integer> getSelectedCreature() {
        return selectedCreature;
    }

    public void setCreatures(MutableLiveData<ArrayList<Creature>> creatures) {
        this.creatures = creatures;
    }

    public void setSelectedCreature(MutableLiveData<Integer> selectedCreature) {
        this.selectedCreature = selectedCreature;
    }
}
