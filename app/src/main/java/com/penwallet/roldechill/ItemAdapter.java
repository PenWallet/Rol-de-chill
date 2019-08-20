package com.penwallet.roldechill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Utilities.Utils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.health.setText(Integer.toString(mItemList.get(position).getVida()));
        holder.maxHealth.setText(Integer.toString(mItemList.get(position).getVidaMaxima()));
        holder.iniciativa.setText(Integer.toString(mItemList.get(position).getIniciativa()));
        holder.name.setText(mItemList.get(position).getNombre());

        //Darle foto a la imagen
        if(mItemList.get(position).isEsJugador())
        {
            if(mItemList.get(position).getNombre().toLowerCase().equals("oscar"))
                holder.image.setImageResource(R.drawable.oscar);
            else if(mItemList.get(position).getNombre().toLowerCase().equals("miguel"))
            {
                int pifia = new Random().nextInt(20)+1;
                if(pifia == 1)
                    holder.image.setImageResource(R.drawable.pifia);
                else
                    holder.image.setImageResource(R.drawable.miguel);
            }
            else if(mItemList.get(position).getNombre().toLowerCase().equals("fran"))
                holder.image.setImageResource(R.drawable.fran);
            else if(mItemList.get(position).getNombre().toLowerCase().equals("olga"))
                holder.image.setImageResource(R.drawable.olga2);
            else
                holder.image.setImageResource(R.drawable.player);
        }
        else
            holder.image.setImageResource(R.drawable.creature);

        holder.addHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemList.get(position).getVida() >= mItemList.get(position).getVidaMaxima())
                    Utils.animateError(v);
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarVida(1);

                    notifyDataSetChanged();
                }
            }
        });

        holder.subtractHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mItemList.get(position).getVida() == 1 && !mItemList.get(position).isEsJugador())
                    mItemList.remove(position);
                else if(mItemList.get(position).getVida() == 0)
                {
                    Utils.animateError(v);
                }
                else
                {
                    Utils.animateClick(v);
                    mItemList.get(position).cambiarVida(-1);
                }

                notifyDataSetChanged();
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemList.size() > 0)
                {
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
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getId();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView health, maxHealth, iniciativa, name;
        CardView addHealth, subtractHealth, whole;
        ImageView image, close;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            whole = itemView.findViewById(R.id.cardViewCharacter);
            health = itemView.findViewById(R.id.txtHealth);
            maxHealth = itemView.findViewById(R.id.txtMaxHealth);
            iniciativa = itemView.findViewById(R.id.txtIniciativa);
            name = itemView.findViewById(R.id.txtName);
            addHealth = itemView.findViewById(R.id.addHealth);
            subtractHealth = itemView.findViewById(R.id.substractHealth);
            image = itemView.findViewById(R.id.imgCharacter);
            close = itemView.findViewById(R.id.closeCharacter);
        }
    }
}
