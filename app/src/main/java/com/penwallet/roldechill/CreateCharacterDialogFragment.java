package com.penwallet.roldechill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;

import java.util.Arrays;

public class CreateCharacterDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.create_character_popup, null);

        ((EditText)view.findViewById(R.id.createVidaMaxima)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((EditText)view.findViewById(R.id.createVidaActual)).setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Crear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText eNombre, eIniciativa, eVidaActual, eVidaMaxima;
                        CheckBox cEsJugador;
                        Spinner sEstado;
                        String nombre;
                        int iniciativa, vidaActual, vidaMaxima;
                        boolean okay = true, esJugador;
                        Status estado;

                        eNombre = view.findViewById(R.id.createNombre);
                        eIniciativa = view.findViewById(R.id.createIniciativa);
                        eVidaMaxima = view.findViewById(R.id.createVidaMaxima);
                        eVidaActual = view.findViewById(R.id.createVidaActual);
                        cEsJugador = view.findViewById(R.id.createEsJugador);
                        sEstado = view.findViewById(R.id.createEstado);

                        nombre = eNombre.getText().toString();
                        iniciativa = eIniciativa.getText().toString().equals("") ? 0 : Integer.valueOf(eIniciativa.getText().toString());
                        vidaActual = eVidaActual.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaActual.getText().toString());
                        vidaMaxima = eVidaMaxima.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaMaxima.getText().toString());
                        esJugador = cEsJugador.isChecked();
                        estado = Status.valueOf((String)sEstado.getSelectedItem());


                        //Comprobar datos
                        if(nombre.isEmpty() || iniciativa <= 0 || vidaActual <= 0 || vidaMaxima <= 0 || vidaActual > vidaMaxima)
                            okay = false;

                        if(okay)
                        {
                            Creature c = new Creature(nombre, vidaActual, vidaMaxima, iniciativa, esJugador, estado);
                            Database.creatures.add(c);
                            Database.listView.getAdapter().notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(getContext(), "¿Crees que soy gilipollas o qué?", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateCharacterDialogFragment.this.getDialog().cancel();
                    }
                })
                .setCancelable(true);

        Spinner spinner = view.findViewById(R.id.createEstado);
        String[] estados = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return builder.create();
    }
}
