package com.penwallet.roldechill.Utilities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.penwallet.roldechill.Entities.Ally;
import com.penwallet.roldechill.Entities.Creature;
import com.penwallet.roldechill.R;

public class Utils {
    public static void animateClick(View v)
    {
        ScaleAnimation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new CycleInterpolator(1));
        animation.setDuration(200);

        v.startAnimation(animation);
    }

    public static void animateError(View v)
    {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new CycleInterpolator(2));
        animation.setDuration(200);

        v.startAnimation(animation);
    }

    public static int getCreatureImageId(Creature creature)
    {
        if(creature instanceof Ally)
        {
            String nombre = creature.getNombre().toLowerCase();
            switch (nombre)
            {
                case "oscar":
                    return R.drawable.oscar;
                case "miguel":
                    return R.drawable.miguel;
                case "fran":
                    return R.drawable.fran;
                case "olga":
                    return R.drawable.olga;
                case "triana":
                    return R.drawable.triana;
                case "jose":
                case "negro":
                    return R.drawable.joseniggar;
                default:
                    return R.drawable.player;

            }
        }
        else
            return R.drawable.enemy;
    }
}
