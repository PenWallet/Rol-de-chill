package com.penwallet.roldechill.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.Entities.Status;
import com.penwallet.roldechill.MainViewModel;
import com.penwallet.roldechill.R;
import com.penwallet.roldechill.Utilities.Utils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class DrawingImagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Creature> creatures;
    private Context context;
    private MainViewModel viewModel;

    public DrawingImagesListAdapter(ArrayList<Creature> creatures, Context context, MainViewModel mainViewModel) {
        this.creatures = creatures;
        this.context = context;
        this.viewModel = mainViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return creatures.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return creatures.size();
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.drawing_drag_character, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        Creature creature = creatures.get(position);

        //Darle foto a la imagen
        if(creature.isEsJugador())
        {
            if(creature.getNombre().toLowerCase().equals("oscar"))
                holder.image.setImageResource(R.drawable.oscar);
            else if(creature.getNombre().toLowerCase().equals("miguel"))
                holder.image.setImageResource(R.drawable.miguel);
            else if(creature.getNombre().toLowerCase().equals("fran"))
                holder.image.setImageResource(R.drawable.fran);
            else if(creature.getNombre().toLowerCase().equals("olga"))
                holder.image.setImageResource(R.drawable.olga);
            else if(creature.getNombre().toLowerCase().equals("triana"))
                holder.image.setImageResource(R.drawable.triana);
            else
                holder.image.setImageResource(R.drawable.player);
        }
        else
            holder.image.setImageResource(R.drawable.creature);

        holder.name.setText(creature.getNombre());

        return convertView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView whole;
        ImageView image;

        public ViewHolder(View view) {
            super(view);
            whole = view.findViewById(R.id.cardViewDrawingDragCharacter);
            name = view.findViewById(R.id.txtNameDrag);
            image = view.findViewById(R.id.imgCharacterDrag);
        }
    }*/

}
