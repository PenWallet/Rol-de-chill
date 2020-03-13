package com.penwallet.roldechill.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.MainViewModel;
import com.penwallet.roldechill.Utilities.MyCanvas;
import com.penwallet.roldechill.R;
import com.penwallet.roldechill.Utilities.Utils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment {
    private MainViewModel viewModel;
    private MyCanvas myCanvas;
    private RelativeLayout relativeLayout;
    private DrawingFragInterface callbacks;

    public interface DrawingFragInterface
    {
        void refreshDraggingList();
    }

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
        callbacks = (DrawingFragInterface)requireActivity();

        myCanvas = new MyCanvas(requireContext(), viewModel.getStrokeWidth().getValue(), viewModel.getDrawnPaths());

        relativeLayout = requireActivity().findViewById(R.id.drawingLayout);
        relativeLayout.addView(myCanvas);

        myCanvas.setOnDragListener(new View.OnDragListener() {
            View draggedView;

            @Override
            public boolean onDrag(View v, final DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        draggedView = (View) event.getLocalState();
                        break;
                    case DragEvent.ACTION_DROP:
                        final float eventX = event.getX();
                        final float eventY = event.getY();

                        //Cogemos la criatura y alteramos su estado
                        final Creature creature = (Creature)draggedView.getTag();
                        creature.setColocadaEnCanvas(true);

                        final View view = LayoutInflater.from(requireContext()).inflate(R.layout.drawing_drag_character, relativeLayout, false);
                        view.setTag(draggedView.getTag());
                        ((ImageView)view.findViewById(R.id.imgCharacterDrag)).setImageResource(Utils.getCreatureImageId(creature));
                        ((TextView)view.findViewById(R.id.txtNameDrag)).setText(creature.getNombre());

                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                creature.setCanvasX(eventX-((float)view.getWidth()/2));
                                creature.setCanvasY(eventY-((float)view.getHeight()/2));
                                view.setX(creature.getCanvasX());
                                view.setY(creature.getCanvasY());
                            }
                        });

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((Creature)v.getTag()).setColocadaEnCanvas(false);
                                relativeLayout.removeView(view);
                                callbacks.refreshDraggingList();
                            }
                        });

                        relativeLayout.addView(view);

                        /*String s = ((TextView)draggedView.findViewById(R.id.txtNameDrag)).getText().toString();
                        Toast.makeText(requireContext(), "Las coordenadas son: "+event.getX()+", "+event.getY()+", "+s, Toast.LENGTH_SHORT).show();
                        myCanvas.drawCircleTest(event.getX(), event.getY());*/
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });

        final Observer<Float> strokeWidthObserver = new Observer<Float>() {
            @Override
            public void onChanged(Float width) {
                myCanvas.changeStrokeWidth(width);
            }
        };

        //Añadimos, en caso de que haya, las vistas de los personajes que están añadidos en el canvas
        for(Creature creature : viewModel.getCreatures().getValue())
        {
            if(creature.isColocadaEnCanvas())
            {
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.drawing_drag_character, relativeLayout, false);
                view.setTag(creature);
                ((ImageView)view.findViewById(R.id.imgCharacterDrag)).setImageResource(Utils.getCreatureImageId(creature));
                ((TextView)view.findViewById(R.id.txtNameDrag)).setText(creature.getNombre());
                view.setX(creature.getCanvasX());
                view.setY(creature.getCanvasY());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Creature)v.getTag()).setColocadaEnCanvas(false);
                        relativeLayout.removeView(v);
                        callbacks.refreshDraggingList();
                    }
                });

                relativeLayout.addView(view);
            }
        }

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
        int nCriaturasEnCanvas = 0;

        //Aparte de borrar lo que haya en el canvas dibujado, también quitamos las vistas que haya
        for(Creature c : viewModel.getCreatures().getValue())
        {
            if(c.isColocadaEnCanvas())
            {
                nCriaturasEnCanvas++;
                c.setColocadaEnCanvas(false);
            }
        }

        //Solo iteramos si hay criaturas en canvas
        if(nCriaturasEnCanvas != 0)
        {
            ViewGroup viewGroup = relativeLayout;
            ArrayList<View> viewsABorrar = new ArrayList<>();
            for(int i = 0; i < viewGroup.getChildCount(); i++)
            {
                View child = viewGroup.getChildAt(i);
                if(!child.equals(myCanvas))
                    viewsABorrar.add(child);
            }

            for(View view : viewsABorrar)
                relativeLayout.removeView(view);
        }

        callbacks.refreshDraggingList();
        myCanvas.clearCanvas();
    }
}
