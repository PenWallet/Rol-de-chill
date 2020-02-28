package com.penwallet.roldechill;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.penwallet.roldechill.Constants.ToolsConstants;
import com.penwallet.roldechill.Entities.Creature;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Creature>> creatures;
    private MutableLiveData<Integer> selectedCreature;
    private MutableLiveData<Boolean> performListRefresh; //Used only to observe and refresh the list
    private MutableLiveData<Boolean> clearCanvas; //Used only to observe when user wants to clear the canvas

    //Atributos para drawing
    private MutableLiveData<Boolean> isPencilSelected;
    private MutableLiveData<Float> strokeWidth;
    private MutableLiveData<Boolean> undoLastAction;

    public MainViewModel()
    {
        creatures = new MutableLiveData<>();
        selectedCreature = new MutableLiveData<>();
        performListRefresh = new MutableLiveData<>();
        clearCanvas = new MutableLiveData<>();
        undoLastAction = new MutableLiveData<>();

        isPencilSelected = new MutableLiveData<>(); isPencilSelected.setValue(true);
        strokeWidth = new MutableLiveData<>(); strokeWidth.setValue(ToolsConstants.MINIMUM_STROKE_WIDTH);

        creatures.setValue(new ArrayList<Creature>());
    }

    public MutableLiveData<ArrayList<Creature>> getCreatures() {
        return creatures;
    }

    public MutableLiveData<Integer> getSelectedCreature() {
        return selectedCreature;
    }

    public MutableLiveData<Boolean> getPerformListRefresh() {
        return performListRefresh;
    }

    public MutableLiveData<Boolean> getIsPencilSelected() {
        return isPencilSelected;
    }

    public MutableLiveData<Float> getStrokeWidth() {
        return strokeWidth;
    }

    public MutableLiveData<Boolean> getUndoLastAction() {
        return undoLastAction;
    }

    public MutableLiveData<Boolean> getClearCanvas() {
        return clearCanvas;
    }
}
