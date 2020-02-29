package com.penwallet.roldechill;

import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;

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
    private ArrayList<Pair<Path, Paint>> drawnPaths;

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
        drawnPaths = new ArrayList<>();

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

    public ArrayList<Pair<Path, Paint>> getDrawnPaths() {
        return drawnPaths;
    }

    public void setDrawnPaths(ArrayList<Pair<Path, Paint>> drawnPaths) {
        this.drawnPaths = drawnPaths;
    }
}
