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


public class  ActivitySourceList extends AppCompatActivity implements View.OnClickListener, IntentReciever {

    /**
     *
     *****************STATE DECLARATIONS**********************************************************
     *
     * @state SEARCH_EDIT_TEXT_ID: The id of the SearchViews search edit text (Used to customize)
     *
     * @state toolyBary: The main toolbar of our Activity (contains up arrow, search button, filter button)
     *
     * @state searchToolbar: The search Toolbar that animates ontop of toolyBary
     *
     * @state searchView: The searchView action view of the searchToolbar: onQueryChange is implemented by container fragments
     *
     * @state container: The ViewPager of our sourcelist fragments
     *
     * @state argb: used sClr / eClr to change toolbary color as user swipes pages
     *
     * @state strips: Title indicator of which page we're on; Bounded to R.id.source_list_strips in method bindFragmentTitles()
     *
     * @state sClr: starting color (R.color.light_green) of Toolybary (for ADDING fragment)
     *
     * @state eClr: ending color (R.color.light_red) of ToolyBary (for the SPENDING fragment)
     *
     * @state toolyBaryBottomExpanded: When displaying searchToolbar, we hide toolbary by animating it so its Y is
     *        0 - toolyBary.getHeight(); In order to restore it to its correct position, that is to translate it to its real-Y
     *        we cache its value, and fetch it in a posted runnable. toolyBaryBottomExpanded is the real-Y of toolyBary
     *        NOTE: This value is also the botto for toolbarContainer
     *
     * @state toolbarContainer: The entire container for toolybary and the strips; We use this object to animate toolyBary and the title strips
     *
     * @state searchBarContainer: we do the searchToolbar circular reveal on this reference
     *
     * @state searchFragmentContainer: The Container for the Fragment launched to handle the user Queries
     *
     */


    public static final int SEARCH_EDIT_TEXT_ID = android.support.v7.appcompat.R.id.search_src_text;

    private Toolbar toolyBary;
    private Toolbar searchToolbar;
    private SearchView searchView;
    private ViewPager container;
    private ArgbEvaluator argb = new ArgbEvaluator();
    private PageTitleStrips strips;
    private int sClr;
    private int eClr;
    private int toolyBaryBottomExpanded;
    private ViewGroup toolbarContainer;
    private ViewGroup searchBarContainer;
    private FrameLayout searchFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_source_list);

        toolyBary = findViewById(R.id.id_source_master_toolbar);
        setSupportActionBar(toolyBary); //Do we even really need?

        //Registers the Toolbar Up action
        findViewById(R.id.id_up_navigation).setOnClickListener(this);

        //Initialize the ViewPager, set its adapter, and the pageTransformer / pageChangeListener
        container = findViewById(R.id.id_source_list_container);
        container.setAdapter(new SourceListFragmentAdapter(getSupportFragmentManager()));
        container.setPageTransformer(false, pageTransformer);
        container.addOnPageChangeListener(pageChangeListener);


        //Binds the ViewPager fragments, with the title indicator
        bindFragmentTitles();


        //Fetches toolybary colors in a backwards-compatible fashion
        if(Build.VERSION.SDK_INT >= 23) {
            sClr = getColor(R.color.light_green);
            eClr = getColor(R.color.light_red);
        }
        else {
            sClr = getResources().getColor(R.color.light_green);
            eClr = getResources().getColor(R.color.light_red);
        }


        // reegister the search icon click listener
        findViewById(R.id.id_source_master_toolbar_search).setOnClickListener(this);



        searchToolbar = findViewById(R.id.id_search_toolbar);
        searchToolbar.inflateMenu(R.menu.menu_search);

        //SearchView
        searchView = (SearchView) searchToolbar.getMenu().findItem(R.id.id_action_search).getActionView();

        //Sets the SearchView expansion/collapsing listener
        searchToolbar.getMenu().findItem(R.id.id_action_search).setOnActionExpandListener(searchExpansionListener);


        //Fetch the bottom for a translation animation when search toolbar is shown
        toolyBary.post(new Runnable() {
            @Override
            public void run() {
                toolyBaryBottomExpanded = toolyBary.getHeight();
            }
        });

        //Init the containers for Animation purposes
        toolbarContainer = findViewById(R.id.source_list_toolbar_container);
        searchBarContainer = findViewById(R.id.searchBarContainer);


        //Customize the Search Edit Text
        EditText txtSearch = searchView.findViewById(SEARCH_EDIT_TEXT_ID);
        txtSearch.setHint(getString(R.string.string_search_loading));
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.light_green));

        searchFragmentContainer = findViewById(R.id.id_fragment_container);

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


    ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {
            strips.setPosition(position);
        }
    };

    @Override
    public void onClick(View v) {

        //Navigate to parent activity
        if(v.getId() == R.id.id_up_navigation)
            NavUtils.navigateUpFromSameTask(this);

        //Show the searchgit add
        else if(v.getId() == R.id.id_source_master_toolbar_search)
            searchToolbar.getMenu().findItem(R.id.id_action_search).expandActionView();
    }

    private void showSearchBar(){

        if(Build.VERSION.SDK_INT >= 21)
            showSearchBarPostLollipop();

        else
            showSearchBarPreLollipop();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showSearchBarPostLollipop(){

        final int x = toolyBary.getWidth();
        final int y = toolyBary.getHeight() - (toolyBary.getHeight()/2);

       final Animator circularShowAnimator =
               ViewAnimationUtils.createCircularReveal(searchBarContainer, x, y, 0, searchBarContainer .getWidth());

       circularShowAnimator.setDuration(350L);
       searchBarContainer.setVisibility(View.VISIBLE);
       circularShowAnimator.start();

    }

    private void showSearchBarPreLollipop(){
        //Fade?
    }


    private void hideSearchBar(){

        if(Build.VERSION.SDK_INT >= 21)
            hideSearchBarPostLollipop();

        else
            hideSearchBarPreLollipop();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hideSearchBarPostLollipop(){

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

    private void hideSearchBarPreLollipop(){
        //fade
    }


    /**
     * Launches a new instance of the same fragment that is already being used;
     * The benefits of launching a new fragment to handle the Search Queries
     * is better decoupling from the UI
     */
    private void launchQueryFragment(){

        final FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();

        FragmentSourceList fragmentSourceList = null;

        final Fragment currFragmentType = ((SourceListFragmentAdapter)container.getAdapter()).getItem(container.getCurrentItem());

        if(currFragmentType instanceof FragmentSourceListAdd)
            fragmentSourceList = FragmentSourceListAdd.newInstance(null);

        else if(currFragmentType instanceof FragmentSourceListSpend)
            fragmentSourceList = FragmentSourceListSpend.newInstance(null);

        fragTrans.add(
                R.id.id_fragment_container,
                fragmentSourceList,
                "backstackTag"
        );

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
            showSearchBar();
            toolbarContainer.animate().y(-toolyBaryBottomExpanded).setDuration(350L).start();
            launchQueryFragment();
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            hideSearchBar();
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

    /**
     * Called by the fragments to launch the appropriate Activity
     *
     * @param intent An intent containing SourceMetaData, and a Activity.class to launch
     */
    @Override
    public void onDeliverIntent(Intent intent) {
        startActivity(intent);
    }



}
