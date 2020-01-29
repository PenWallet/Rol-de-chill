package com.penwallet.roldechill;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Utilities.Utils;

import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private MainViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        ImageView cv = getActivity().findViewById(R.id.btnAddCreature);
        cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                alertDialog.setTitle("¿Borrar todas las criaturas?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.getCreatures().getValue().clear();
                        Database.listView.getAdapter().notifyDataSetChanged();
                    }
                });

                alertDialog.setCancelable(true);

                alertDialog.show();

                return true;
            }
        });

        Database.listView = getActivity().findViewById(R.id.dragList);

        Database.listView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ItemAdapter listAdapter = new ItemAdapter(viewModel.getCreatures().getValue(), R.layout.character_layout, R.id.cardViewCharacter, true, requireContext(), viewModel);
        Database.listView.setAdapter(listAdapter, false);
        Database.listView.setCanDragHorizontally(false);
        Database.listView.setCustomDragItem(null);

        //Observer para la posición elegida para abrir el popup de editar
        final Observer<Integer> selectedCreatureObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                DialogFragment popup = new EditCharacterDialogFragment();
                popup.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "popupEdit");
            }
        };

        viewModel.getSelectedCreature().observe(getViewLifecycleOwner(), selectedCreatureObserver);
    }
}
