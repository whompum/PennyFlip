package com.whompum.PennyFlip.ActivitySourceList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListFragmentAdapter;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceList;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceListAdd;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceListSpend;
import com.whompum.PennyFlip.Animations.PageTitleStrips;
import com.whompum.PennyFlip.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;


public class  ActivitySourceList extends AppCompatActivity implements IntentReciever {

    public static final int SEARCH_EDIT_TEXT_ID = android.support.v7.appcompat.R.id.search_src_text;


    private ArgbEvaluator argb = new ArgbEvaluator();

    private PageTitleStrips strips;

    private int toolyBaryBottomExpanded;

    private SearchView searchView;

    @BindColor(R.color.light_green) protected int sClr;

    @BindColor(R.color.light_red) protected int eClr;

    @BindView(R.id.id_toolbar) protected Toolbar toolyBary;

    @BindView(R.id.id_search_toolbar) protected Toolbar searchToolbar;

    @BindView(R.id.id_source_list_container) protected ViewPager container;

    @BindView(R.id.id_toolbar_container) protected ViewGroup toolbarContainer;

    @BindView(R.id.searchBarContainer) protected ViewGroup searchBarContainer;

    @BindView(R.id.id_fragment_container) protected FrameLayout searchFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolyBary); //Do we even really need?

        container.setAdapter(new SourceListFragmentAdapter(getSupportFragmentManager()));
        container.setPageTransformer(false, pageTransformer);

        //Binds the ViewPager fragments, with the title indicator
        bindFragmentTitles();

        searchToolbar.inflateMenu(R.menu.menu_search);

        searchView = (SearchView) searchToolbar.getMenu().findItem(R.id.id_action_search).getActionView();

        //Sets the SearchView expansion/collapsing listener
        searchToolbar.getMenu().findItem(R.id.id_action_search).setOnActionExpandListener(searchExpansionListener);


        //Fetch the bottom X of Toolybary (used for translation purposes)
        toolyBary.post(new Runnable() {
            @Override
            public void run() {
                toolyBaryBottomExpanded = toolyBary.getHeight();
            }
        });

        customizeSearchEditor();
    }

    private void customizeSearchEditor(){
        EditText txtSearch = searchView.findViewById(SEARCH_EDIT_TEXT_ID);
        txtSearch.setHint(getString(R.string.string_search_loading));
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.light_green));
    }

    private void bindFragmentTitles(){

        strips = new PageTitleStrips((ViewGroup) (findViewById(R.id.source_list_strips)), new PageTitleStrips.StripClick() {
            @Override
            public void onStripClicked(int position) {
                container.setCurrentItem(position);
            }
        });
        strips.bindTitle(this, getString(R.string.string_adding));
        strips.bindTitle(this, getString(R.string.string_spending));
    }

    /**
     * Called by the fragments to launch the appropriate Activity
     *
     * @param intent An intent containing SourceMetaData, and a Activity.class to launch
     */
    @Override
    public void onDeliverIntent(Intent intent) {
        startActivity(intent);
    }

    @OnPageChange(R.id.id_source_list_container)
    public void onPageChange(final int position){
        strips.setPosition(position);
    }

    @OnClick(R.id.id_up_navigation)
    public void navigateUp(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.id_source_master_toolbar_search)
    public void expandSearchbar(){
        searchToolbar.getMenu().findItem(R.id.id_action_search).expandActionView();
    }

    @OnClick(R.id.id_source_list_toolbar_sort)
    public void launchSortDialog(){
        ((OnSortButtonClicked)((SourceListFragmentAdapter)container.getAdapter()).getItem(container.getCurrentItem())).onSortClicked();
    }

    private void enterAnimateSearchBar(){

        if(Build.VERSION.SDK_INT >= 21)
            enterAnimateSearchBarPostApi21();

        else
            enterAnimateSearchBarPreApi21();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enterAnimateSearchBarPostApi21(){

        final int x = toolyBary.getWidth();
        final int y = toolyBary.getHeight() - (toolyBary.getHeight()/2);

       final Animator circularShowAnimator =
               ViewAnimationUtils.createCircularReveal(searchBarContainer, x, y, 0, searchBarContainer .getWidth());

       circularShowAnimator.setDuration(350L);
       searchBarContainer.setVisibility(View.VISIBLE);
       circularShowAnimator.start();

    }

    private void enterAnimateSearchBarPreApi21(){
        //TODO: Implement a fade maybe?
    }

    private void exitAnimateSearchBar(){

        if(Build.VERSION.SDK_INT >= 21)
            exitAnimateSearchBarPostApi21();

        else
            exitAnimateSearchBarPreApi21();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void exitAnimateSearchBarPostApi21(){

        final int x = toolyBary.getWidth();
        final int y = 0;

        final Animator circularShowAnimator =
                ViewAnimationUtils.createCircularReveal(searchBarContainer, x, y, searchBarContainer.getWidth(), 0);

        circularShowAnimator.setDuration(350L);

        circularShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchBarContainer.setVisibility(View.INVISIBLE);
            }
        });

        circularShowAnimator.start();

    }

    private void exitAnimateSearchBarPreApi21(){
        //TODO implement a fade or somethign
    }

    /**
     * Launches a new instance of the same fragment that is already being used
     * to handle the search queries
     */
    private void launchSearchFragment(){

        final FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();

        FragmentSourceList fragmentSourceList = null;

        final Fragment currFragmentType = ((SourceListFragmentAdapter)container.getAdapter()).getItem(container.getCurrentItem());

        if(currFragmentType instanceof FragmentSourceListAdd)
            fragmentSourceList = FragmentSourceListAdd.newInstance(null);

        else if(currFragmentType instanceof FragmentSourceListSpend)
            fragmentSourceList = FragmentSourceListSpend.newInstance(null);

        fragTrans.add(R.id.id_fragment_container, fragmentSourceList, "backstackTag");

        searchFragmentContainer.setVisibility(View.VISIBLE);

        this.searchView.setOnQueryTextListener(fragmentSourceList);

        fragTrans.addToBackStack("backstackTag").commit();
    }

    private void removeQueryFragment(){
        getSupportFragmentManager().popBackStack();
        searchFragmentContainer.setVisibility(View.GONE);
    }



    //Expand/collapse listener for the SearchView
    private MenuItem.OnActionExpandListener searchExpansionListener = new MenuItem.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            enterAnimateSearchBar();
            toolbarContainer.animate().y(-toolyBaryBottomExpanded).setDuration(350L).start();
            launchSearchFragment();
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            exitAnimateSearchBar();
            toolbarContainer.animate().y(0).setDuration(350L).start();
            removeQueryFragment();
            return true;
        }
    };


    private ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            /**
             * Animates the toolbar color, and whatnot
             */
            final int color = (Integer) argb.evaluate(position, eClr, sClr);

            toolyBary.setBackgroundColor(color);
            strips.getContainer().setBackgroundColor(color);

        }
    };

}
