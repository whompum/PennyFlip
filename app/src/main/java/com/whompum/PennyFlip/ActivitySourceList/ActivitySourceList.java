package com.whompum.PennyFlip.ActivitySourceList;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListFragmentAdapter;
import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.R;


public class  ActivitySourceList extends AppCompatActivity implements View.OnClickListener, IntentReciever {

    private Toolbar toolyBary;

    private ViewPager container;

    private ArgbEvaluator argb = new ArgbEvaluator();

    private PageTitleStrips strips;

    private int sClr;
    private int eClr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_source_list);

        toolyBary = findViewById(R.id.id_source_master_toolbar);
        setSupportActionBar(toolyBary);

        findViewById(R.id.id_source_master_toolbar_up).setOnClickListener(this);

        container = findViewById(R.id.id_source_master_container);
        container.setAdapter(new SourceListFragmentAdapter(getSupportFragmentManager()));
        container.setPageTransformer(false, pageTransformer);

        bindFragmentTitles();

        container.addOnPageChangeListener(pageChangeListener);

        if(Build.VERSION.SDK_INT >= 23) {
            sClr = getColor(R.color.light_green);
            eClr = getColor(R.color.light_red);
        }
        else {
            sClr = getResources().getColor(R.color.light_green);
            eClr = getResources().getColor(R.color.light_red);
        }

    }

    private void bindFragmentTitles(){
        strips = new PageTitleStrips( (ViewGroup)(findViewById(R.id.someId)) );
        strips.bindTitle(this, getString(R.string.string_adding));
        strips.bindTitle(this, getString(R.string.string_spending));
    }


    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {
            strips.setPosition(position);
        }
    };

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


        }
    };

    @Override
    public void onDeliverIntent(Intent intent) {
        startActivity(intent);
    }



}
