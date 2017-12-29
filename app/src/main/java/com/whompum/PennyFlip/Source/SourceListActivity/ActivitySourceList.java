package com.whompum.PennyFlip.Source.SourceListActivity;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.whompum.PennyFlip.R;


public class ActivitySourceList extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolyBary;

    private ViewPager container;

    private TextView addingLabel;
    private TextView spendingLabel;

    private ArgbEvaluator argb = new ArgbEvaluator();
    private ViewPagerTitleAnimator titleAnimator;

    private int sClr;
    private int eClr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_source_master);

        toolyBary = findViewById(R.id.id_source_master_toolbar);
        setSupportActionBar(toolyBary);

        findViewById(R.id.id_source_master_toolbar_up).setOnClickListener(this);

        container = findViewById(R.id.id_source_master_container);
        container.setAdapter(new SourceListFragmentAdapter(getSupportFragmentManager()));
        container.setPageTransformer(false, pageTransformer);

        addingLabel = findViewById(R.id.id_source_master_toolbar_adding_label);
        spendingLabel = findViewById(R.id.id_source_master_toolbar_spending_label);

        titleAnimator = new ViewPagerTitleAnimator(addingLabel, spendingLabel);


        if(Build.VERSION.SDK_INT >= 23) {
            sClr = getColor(R.color.light_green);
            eClr = getColor(R.color.light_red);
        }
        else {
            sClr = getResources().getColor(R.color.light_green);
            eClr = getResources().getColor(R.color.light_red);
        }

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.id_source_master_toolbar_up)
            NavUtils.navigateUpFromSameTask(this);

    }


    private ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            /**
             * Animates the toolbar color, and whatnot
             */
            final int color = (Integer) argb.evaluate(position, eClr, sClr);
            toolyBary.setBackgroundColor(color);
            titleAnimator.animateStateChange(position);

            if(position < 0.5F){
                addingLabel.setTypeface(null, Typeface.NORMAL);
                spendingLabel.setTypeface(null, Typeface.BOLD);
            }else if(position > 0.5F){
                addingLabel.setTypeface(null, Typeface.BOLD);
                spendingLabel.setTypeface(null, Typeface.NORMAL);
            }

        }
    };


    private class ViewPagerTitleAnimator{


        private TextView object, subject;
        private float ciel, floor;

        private FloatEvaluator evaluator = new FloatEvaluator();

        private ViewPagerTitleAnimator(final TextView object, final TextView subject){
            this.object = object; this.subject = subject;
            ciel = 14; floor = 12;
        }

        /*
         * Code works i do not entirely know why. Please don't touch.
         */
        private void animateStateChange(final float delta){
                grow(object, delta);
                shrink(subject, delta);
        }

        private void grow(final TextView textView, final float delta){
             textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, evaluator.evaluate(delta, floor, ciel));
        }

        private void shrink(final TextView textView, final float delta){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,evaluator.evaluate(delta, ciel, floor));
        }

    }


}
