package com.penwallet.roldechill.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.penwallet.roldechill.Entities.Ally;
import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Enemy;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.MainViewModel;
import com.penwallet.roldechill.R;

import java.util.Arrays;

public class CreateCharacterDialogFragment extends DialogFragment {
    private View view;
    private MainViewModel viewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.create_character_popup, null);
        this.view = view;
        this.viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //Listener para cambiar la vida a la vez que se cambia la vida máxima
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
                        //No hacer nada
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateCharacterDialogFragment.this.getDialog().cancel();
                    }
                })
                .setCancelable(true);

        CheckBox cEsJugador = view.findViewById(R.id.createEsJugador);
        cEsJugador.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Si es un jugador, desaparece el botón de añadir copias,si no, vuelve a aparecer
                if(isChecked)
                {
                    view.findViewById(R.id.createCopiasLinearLayout).setVisibility(View.GONE);
                    view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.GONE);
                    view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.VISIBLE);
                    ((EditText)view.findViewById(R.id.createCopias)).setText("1");
                }
                else
                {
                    view.findViewById(R.id.createCopiasLinearLayout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.GONE);
                    view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.GONE);
                    ((EditText)view.findViewById(R.id.createDanoRecibido)).setText("0");
                }
            }
        });

        Spinner spinner = view.findViewById(R.id.createEstado);
        String[] estados = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(cEsJugador.isChecked())
        {
            view.findViewById(R.id.createCopiasLinearLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.GONE);
            view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.GONE);
            ((EditText)view.findViewById(R.id.createCopias)).setText("1");
        }
        else
        {
            view.findViewById(R.id.createCopiasLinearLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.GONE);
            view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.GONE);
            ((EditText)view.findViewById(R.id.createDanoRecibido)).setText("0");
        }

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            d.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText eNombre, eIniciativa, eVidaActual, eVidaMaxima, eCopias, eDanoRecibido;
                    CheckBox cEsJugador;
                    Spinner sEstado;
                    String nombre;
                    int iniciativa, vidaActual, vidaMaxima, copias;
                    boolean okay = true, esJugador;
                    Status estado;

                    eNombre = view.findViewById(R.id.createNombre);
                    eIniciativa = view.findViewById(R.id.createIniciativa);
                    eVidaMaxima = view.findViewById(R.id.createVidaMaxima);
                    eVidaActual = view.findViewById(R.id.createVidaActual);
                    cEsJugador = view.findViewById(R.id.createEsJugador);
                    eCopias = view.findViewById(R.id.createCopias);
                    sEstado = view.findViewById(R.id.createEstado);
                    eDanoRecibido = view.findViewById(R.id.createDanoRecibido);

                    nombre = eNombre.getText().toString();
                    iniciativa = eIniciativa.getText().toString().equals("") ? 0 : Integer.valueOf(eIniciativa.getText().toString());
                    esJugador = cEsJugador.isChecked();
                    vidaActual = esJugador ? (eVidaActual.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaActual.getText().toString())) : ((eDanoRecibido.getText().toString().equals("") ? 0 : Integer.valueOf(eDanoRecibido.getText().toString())));
                    vidaMaxima = eVidaMaxima.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaMaxima.getText().toString());
                    copias = eCopias.getText().toString().equals("") ? 0 : Integer.valueOf(eCopias.getText().toString());
                    estado = Status.valueOf((String)sEstado.getSelectedItem());

                    //Comprobar datos
                    if(nombre.isEmpty())
                    {
                        eNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eNombre.getBackground().clearColorFilter();

                    if(iniciativa < 0)
                    {
                        eIniciativa.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "La iniciativa no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eIniciativa.getBackground().clearColorFilter();

                    if(esJugador)
                    {
                        if(vidaMaxima <= 0)
                        {
                            eVidaMaxima.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            Toast.makeText(getContext(), "La vida máxima no puede ser menor o igual a 0", Toast.LENGTH_SHORT).show();
                            okay = false;
                        }
                        else
                            eVidaMaxima.getBackground().clearColorFilter();

                        if(vidaActual < 0)
                        {
                            eVidaActual.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            Toast.makeText(getContext(), "La vida actual no puede ser menor o igual a 0", Toast.LENGTH_SHORT).show();
                            okay = false;
                        }
                        else if(vidaActual > vidaMaxima)
                        {
                            eVidaActual.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            Toast.makeText(getContext(), "La vida actual no puede ser mayor a la vida máxima", Toast.LENGTH_SHORT).show();
                            okay = false;
                        }
                        else
                            eVidaActual.getBackground().clearColorFilter();
                    }
                    else
                    {
                        //Recuerda que la vida máxima es el daño recibido en los mostros
                        if(vidaActual < 0)
                        {
                            eDanoRecibido.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                            Toast.makeText(getContext(), "El daño recibido no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                            okay = false;
                        }
                        else
                            eDanoRecibido.getBackground().clearColorFilter();
                    }

                    if(copias <= 0)
                    {
                        eCopias.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "La cantidad de copias no puede ser menor o igual a 0", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eCopias.getBackground().clearColorFilter();


                    if(okay)
                    {
                        if(esJugador)
                            viewModel.getCreatures().getValue().add(new Ally(nombre, vidaActual, vidaMaxima, iniciativa, estado, 0));
                        else
                        {
                            if(copias == 1)
                                viewModel.getCreatures().getValue().add(new Enemy(nombre, vidaActual, iniciativa, estado, 0));
                            else
                            {
                                for(int i = 1; i <= copias; i++)
                                    viewModel.getCreatures().getValue().add(new Enemy(nombre+" "+i, vidaActual, iniciativa, estado, 0));

                            }
                        }

                        viewModel.getPerformListRefresh().setValue(true);
                        dismiss();
                    }
                }
            });
        }
    }
}
