package com.penwallet.roldechill;

import com.penwallet.roldechill.Entities.Creature;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class Database {
    public static ArrayList<Creature> creatures = new ArrayList<>();
    public static DragListView listView;
    public static int selectedCreature;
}
