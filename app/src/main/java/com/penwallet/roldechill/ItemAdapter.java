package com.penwallet.roldechill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.Utilities.Utils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ItemAdapter extends DragItemAdapter<Creature, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private Context context;

    public ItemAdapter(ArrayList<Creature> list, int layoutId, int grabHandleId, boolean dragOnLongPress, Context context) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.context = context;
        setItemList(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.health.setText(Integer.toString(mItemList.get(position).getVida()));
        holder.maxHealth.setText(Integer.toString(mItemList.get(position).getVidaMaxima()));
        holder.iniciativa.setText(Integer.toString(mItemList.get(position).getIniciativa()));
        holder.name.setText(mItemList.get(position).getNombre());
        holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));

        //Darle foto a la imagen
        if(mItemList.get(position).isEsJugador())
        {
            if(mItemList.get(position).getNombre().toLowerCase().equals("oscar"))
                holder.image.setImageResource(R.drawable.oscar);
            else if(mItemList.get(position).getNombre().toLowerCase().equals("miguel"))
                holder.image.setImageResource(R.drawable.miguel);
            else if(mItemList.get(position).getNombre().toLowerCase().equals("fran"))
                holder.image.setImageResource(R.drawable.fran);
            else if(mItemList.get(position).getNombre().toLowerCase().equals("olga"))
                holder.image.setImageResource(R.drawable.olga2);
            else
                holder.image.setImageResource(R.drawable.player);
        }
        else
            holder.image.setImageResource(R.drawable.creature);

        //Cargar el Spinner
        String[] estados = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinnertext, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(mItemList.get(position).getEstado().ordinal());

        holder.addHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemList.get(position).getVida() >= mItemList.get(position).getVidaMaxima())
                    Utils.animateError(v);
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarVida(1);

                    holder.health.setText(Integer.toString(mItemList.get(position).getVida()));
                }
            }
        });

        holder.subtractHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mItemList.get(position).getVida() == 1 && !mItemList.get(position).isEsJugador())
                    mItemList.remove(position);
                else if(mItemList.get(position).getVida() <= 0)
                {
                    Utils.animateError(v);
                    return;
                }
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarVida(-1);
                }

                holder.health.setText(Integer.toString(mItemList.get(position).getVida()));
            }
        });

        holder.addPifia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemList.get(position).getPifias() >= 99)
                    Utils.animateError(v);
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarPifia(1);

                    holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));
                }
            }
        });

        holder.subtractPifia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mItemList.get(position).getPifias() <= 0)
                    Utils.animateError(v);
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarPifia(-1);

                    holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));
                }
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemList.size() > 0)
                {
                    Utils.animateClick(v);
                    mItemList.remove(position);
                    notifyDataSetChanged();
                }

            }
        });

        holder.whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.selectedCreature = position;
                DialogFragment popup = new EditCharacterDialogFragment();
                popup.show(((AppCompatActivity)context).getSupportFragmentManager(), "popupEdit");
            }
        });

        holder.spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.userInteraction = true;
                return false;
            }
        });

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSpinner, long id) {
                if(holder.userInteraction)
                {
                    Database.creatures.get(position).setEstado(Status.values()[positionSpinner]);
                    holder.userInteraction = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getId();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView health, maxHealth, iniciativa, name, pifias;
        CardView whole;
        ImageView image, close, addHealth, subtractHealth, addPifia, subtractPifia;
        Spinner spinner;
        boolean userInteraction;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            whole = itemView.findViewById(R.id.cardViewCharacter);
            health = itemView.findViewById(R.id.txtHealth);
            maxHealth = itemView.findViewById(R.id.txtMaxHealth);
            iniciativa = itemView.findViewById(R.id.txtIniciativa);
            name = itemView.findViewById(R.id.txtName);
            addHealth = itemView.findViewById(R.id.addHealth);
            subtractHealth = itemView.findViewById(R.id.substractHealth);
            addPifia = itemView.findViewById(R.id.addPifia);
            subtractPifia = itemView.findViewById(R.id.substractPifia);
            image = itemView.findViewById(R.id.imgCharacter);
            close = itemView.findViewById(R.id.closeCharacter);
            pifias = itemView.findViewById(R.id.txtPifias);
            spinner = itemView.findViewById(R.id.statusSpinner);
            userInteraction = false;
        }
    }
}
