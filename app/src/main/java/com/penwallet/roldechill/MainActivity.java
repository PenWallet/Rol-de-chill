package com.penwallet.roldechill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

public class MainActivity extends AppCompatActivity {

    DragListView dragListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dragListView = findViewById(R.id.dragList);
        dragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(MainActivity.this, "Empiezas a arrastrar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {
                Toast.makeText(MainActivity.this, "Arrastrando", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(MainActivity.this, "Terminas de arrastrar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dragListView.setLayoutManager(new LinearLayoutManager(this));
        //ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.list_item, R.id.image, false);
        //dragListView.setAdapter(listAdapter);
        dragListView.setCanDragHorizontally(false);
    }
}
