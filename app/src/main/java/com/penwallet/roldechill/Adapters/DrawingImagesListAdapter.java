package com.penwallet.roldechill.Adapters;

import android.content.ClipData;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.R;
import com.penwallet.roldechill.Utilities.Utils;

import java.util.ArrayList;

public class DrawingImagesListAdapter extends RecyclerView.Adapter<DrawingImagesListAdapter.ViewHolder> {

    private ArrayList<Creature> creatures;

    public DrawingImagesListAdapter(ArrayList<Creature> creatures) {
        this.creatures = creatures;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.name = view.findViewById(R.id.txtNameDrag);
            this.image = view.findViewById(R.id.imgCharacterDrag);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawing_drag_character, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Creature creature = creatures.get(position);

        holder.name.setText(creature.getNombre());
        holder.image.setImageResource(Utils.getCreatureImageId(creature));
        holder.view.setTag(creature);

        if(creature.isColocadaEnCanvas())
        {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            holder.image.setColorFilter(new ColorMatrixColorFilter(cm));
        }
        else
        {
            holder.image.clearColorFilter();
        }

        //Permitir a la vista ser arrastrada
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Solo permitimos que coloque a la criatura en el canvas si no ha sido colocada aún
                if(!creature.isColocadaEnCanvas())
                {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            view);
                    view.startDragAndDrop(data, shadowBuilder, view, View.DRAG_FLAG_OPAQUE);

                    creature.setColocadaEnCanvas(true);
                    ColorMatrix cm = new ColorMatrix();
                    cm.setSaturation(0);
                    holder.image.setColorFilter(new ColorMatrixColorFilter(cm));
                }
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return creatures.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return creatures.size();
    }

    public void creatureRemovedFromCanvas(Creature creature)
    {

    }

}
