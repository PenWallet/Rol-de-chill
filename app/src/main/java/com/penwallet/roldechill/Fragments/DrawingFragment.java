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
import com.penwallet.roldechill.Utilities.MyCanvas;
import com.penwallet.roldechill.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment {
    private MainViewModel viewModel;
    private MyCanvas myCanvas;
    private RelativeLayout relativeLayout;

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

        myCanvas = new MyCanvas(requireContext(), viewModel.getStrokeWidth().getValue(), viewModel.getDrawnPaths());

        relativeLayout = requireActivity().findViewById(R.id.drawingLayout);
        relativeLayout.addView(myCanvas);

        final Observer<Float> strokeWidthObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float width) {
                myCanvas.changeStrokeWidth(width);
            }
        };

        viewModel.getStrokeWidth().observe(getViewLifecycleOwner(), strokeWidthObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        viewModel.setDrawnPaths(myCanvas.getPaths());
    }

    public void undoLastAction()
    {
        myCanvas.undoLast();
    }

    public void selectPencil()
    {
        myCanvas.changeToPencil();
    }

    public void selectEraser()
    {
        myCanvas.changeToEraser();
    }

    public void clearCanvas()
    {
        myCanvas.clearCanvas();
    }
}
