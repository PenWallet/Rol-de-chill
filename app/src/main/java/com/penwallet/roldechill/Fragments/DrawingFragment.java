package com.penwallet.roldechill.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.penwallet.roldechill.MainViewModel;
import com.penwallet.roldechill.MyCanvas;
import com.penwallet.roldechill.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment {
    MainViewModel viewModel;
    MyCanvas myCanvas;
    RelativeLayout relativeLayout;

    public DrawingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_drawing, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        myCanvas = new MyCanvas(requireContext(), viewModel.getStrokeWidth().getValue(), viewModel.getIsPencilSelected().getValue());

        relativeLayout = requireActivity().findViewById(R.id.drawingLayout);
        relativeLayout.addView(myCanvas);

        final Observer<Float> strokeWidthObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float width) {
                myCanvas.changeStrokeWidth(width);
            }
        };

        final Observer<Boolean> isPencilObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPencil) {
                if(isPencil)
                    myCanvas.changeToPencil();
                else
                    myCanvas.changeToEraser();
            }
        };

        final Observer<Boolean> clearCanvasObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPencil) {
                myCanvas.clearCanvas();
            }
        };

        final Observer<Boolean> undoLastAction = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean undo) {
                myCanvas.undoLast();
            }
        };

        viewModel.getStrokeWidth().observe(getViewLifecycleOwner(), strokeWidthObserver);
        viewModel.getIsPencilSelected().observe(getViewLifecycleOwner(), isPencilObserver);
        viewModel.getUndoLastAction().observe(getViewLifecycleOwner(), undoLastAction);
        viewModel.getClearCanvas().observe(getViewLifecycleOwner(), clearCanvasObserver);
    }
}
