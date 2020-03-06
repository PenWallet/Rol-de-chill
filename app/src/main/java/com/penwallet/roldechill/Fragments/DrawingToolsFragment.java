package com.penwallet.roldechill.Fragments;


import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.penwallet.roldechill.Constants.ToolsConstants;
import com.penwallet.roldechill.MainViewModel;
import com.penwallet.roldechill.R;
import com.penwallet.roldechill.Utilities.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingToolsFragment extends Fragment {

    private MainViewModel viewModel;
    private SeekBar strokeWidthSeekBar;
    private ImageView ivPencil, ivEraser, ivUndo;
    private RecyclerView rvDragCharacters;
    private DrawingToolsFragInterface callbacks;

    public interface DrawingToolsFragInterface
    {
        void undoLastAction();
        void selectPencil();
        void selectEraser();
        void clearCanvas();
    }

    public DrawingToolsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawing_tools, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        callbacks = (DrawingToolsFragInterface)requireActivity();
        strokeWidthSeekBar = requireActivity().findViewById(R.id.sbStrokeWidth);
        ivPencil = requireActivity().findViewById(R.id.btnPencil);
        ivEraser = requireActivity().findViewById(R.id.btnEraser);
        ivUndo = requireActivity().findViewById(R.id.btnUndo);
        rvDragCharacters = requireActivity().findViewById(R.id.rvDragCharacters);

        //Poner el color del botón que está elegido por defecto (lápiz)
        ivPencil.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);

        //Cambiar el progreso de la barra
        strokeWidthSeekBar.setProgress((int)(viewModel.getStrokeWidth().getValue() - ToolsConstants.MINIMUM_STROKE_WIDTH));

        strokeWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.getStrokeWidth().setValue(ToolsConstants.MINIMUM_STROKE_WIDTH + progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.undoLastAction();
                Utils.animateClick(v);
            }
        });

        ivPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.animateClick(v);
                ivPencil.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);
                ivEraser.getBackground().clearColorFilter();
                callbacks.selectPencil();
            }
        });

        ivEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.animateClick(v);
                ivPencil.getBackground().clearColorFilter();
                ivEraser.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);
                callbacks.selectEraser();
            }
        });

        ivEraser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                alertDialog.setTitle("¿Quieres borrar todo?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        callbacks.clearCanvas();
                    }
                });

                alertDialog.setCancelable(true);

                alertDialog.show();

                Utils.animateClick(v);

                return true;
            }
        });
    }
}
