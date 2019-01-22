package com.whompum.PennyFlip.ActivitySourceList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListFragmentAdapter;
import com.whompum.PennyFlip.ActivitySourceList.Dialog.NewSourceDialog;
import com.whompum.PennyFlip.ActivitySourceList.Dialog.OnSourceCreated;
import com.whompum.PennyFlip.ActivitySourceList.Fragments.FragmentSourceList;
import com.whompum.PennyFlip.ListUtils.ListFragment;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Source.SourceSortOrder;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Widgets.TransactionTypeTitleIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class  ActivitySourceList extends AppCompatActivity implements IntentReciever, SourceListControllerClient {

    public static final String SEARCH_VIEW_KY = "search_view.ky";

    public static final int SEARCH_EDIT_TEXT_ID = android.support.v7.appcompat.R.id.search_src_text;

    private SearchView searchView;

    @BindView(R.id.id_global_toolbar) protected Toolbar toolyBary;

    @BindView(R.id.id_source_list_search_toolbar) protected Toolbar searchToolbar;

    @BindView(R.id.id_global_pager) protected ViewPager container;

    @BindView(R.id.local_source_list_toolbar_container) protected ViewGroup toolbarContainer;

    @BindView(R.id.searchBarContainer) protected ViewGroup searchBarContainer;

    @BindView(R.id.id_search_fragment_container) protected FrameLayout searchFragmentContainer;

    @BindView(R.id.id_global_fab) protected FloatingActionButton newSourceFab;

    @BindView(R.id.local_indicator_adding) TransactionTypeTitleIndicator addIndicator;

    @BindView(R.id.local_indicator_spending) TransactionTypeTitleIndicator spendIndicator;

    private NewSourceDialog newSourceDialog;

    private EditText androidSearchEditor;

    private ActivitySourceListConsumer consumer;

    private SourceListFragmentAdapter adapter;

    private ListFragment<Source> searchContract = FragmentSourceList.newInstance(
            R.layout.source_list_search_null_data
    );

    private boolean wasSearchViewExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.source_list);

        ButterKnife.bind(this);

        if(savedInstanceState != null)
            wasSearchViewExpanded = savedInstanceState.getBoolean( SEARCH_VIEW_KY, false );

        setSupportActionBar(toolyBary); //Do we even really need?

        adapter = new SourceListFragmentAdapter( getSupportFragmentManager() );

        container.setAdapter( adapter );

        container.setPageTransformer(false, pageTransformer);

        searchToolbar.inflateMenu(R.menu.menu_search);

        searchView = (SearchView) searchToolbar.getMenu().findItem(R.id.id_global_search).getActionView();

        customizeSearchEditor();

        this.consumer = new ActivitySourceListController(this, this, this );

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean( SEARCH_VIEW_KY,
                getSearchViewItem().isActionViewExpanded()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ActivitySourceListController c = (ActivitySourceListController) consumer;

        if( !c.hasQueried() )
            c.scheduleQueriedCallback();
        else
            handleInitialQuery();

        getSearchViewItem().setOnActionExpandListener( searchExpansionListener );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return consumer.queryWithSearch( newText );
            }
        });


    }

    private MenuItem getSearchViewItem(){
        return searchToolbar.getMenu()
                .findItem( R.id.id_global_search );
    }

    @Override
    public void onDataUpdate(@NonNull List<Source> data, @NonNull final QueryOp op) {

        if( op.equals( QueryOp.DATA_ADD ) )
            setFragmentDisplayData( data, 0 );

        else if( op.equals( QueryOp.DATA_SPEND ) )
            setFragmentDisplayData( data, 1 );

        else if( op.equals( QueryOp.QUERIED_LIKE_TITLE ) && searchContract.isAdded() )
            setFragmentDisplayData( data, searchContract );

    }

    @Override
    public void onSaveResult(boolean successful, @Nullable Integer reason) {
        if( newSourceDialog != null && newSourceDialog.isShowing()  ){

            if( !successful && reason != null && reason == ActivitySourceListController.DATA_ALREADY_EXISTS )
                newSourceDialog.onTitleError( R.string.string_title_error_in_use );
            else if ( successful ){
                newSourceDialog.dismiss();
                Toast.makeText( this, R.string.string_successfully_saved, Toast.LENGTH_SHORT ).show();
            }
        }

    }

    @Override
    public void onDataQueried() {
        handleInitialQuery();
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


    @OnClick(R.id.id_global_nav)
    public void navigateUp(){
        NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.id_global_search)
    public void expandSearchbar(){
        searchToolbar.getMenu().findItem(R.id.id_global_search).expandActionView();
    }


    @OnClick( R.id.id_global_fab )
    public void launchNewSourceDialog(){
        newSourceDialog = new NewSourceDialog(this, new OnSourceCreated() {
            @Override
            public void onSourceCreated(@NonNull Source source) {
                consumer.saveSource( source );
            }
        }, resolveCurrentTransactionType());
        newSourceDialog.show();
    }


    @OnClick(R.id.id_global_sort)
    public void onSortClicked() {
        final AlertDialog.Builder filterBuilder =
                new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);

        filterBuilder.setTitle(R.string.string_sort_title);

        filterBuilder.setSingleChoiceItems(R.array.sortOrderItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consumer.setSourceOrder(getSortOrderFromArray(which));
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = filterBuilder.create();

        Window w;

        if ( (w = dialog.getWindow()) != null)
            w.getAttributes().windowAnimations = R.style.StyleDialogAnimate;

        dialog.show();
    }

    private void customizeSearchEditor(){
        androidSearchEditor = searchView.findViewById(SEARCH_EDIT_TEXT_ID);
        androidSearchEditor.setHint(getString(R.string.string_search_loading));
        androidSearchEditor.setHintTextColor(Color.DKGRAY);
        androidSearchEditor.setTextColor(getResources().getColor(R.color.light_blue));

        setEditorFocus( androidSearchEditor, false );

    }

    private void setEditorFocus(@NonNull final EditText editor, final boolean focus){
        editor.setFocusable( focus );
        editor.setFocusableInTouchMode( focus );

        if( focus ) editor.requestFocus();
    }

    private void handleInitialQuery(){

        final List<Source> addData = consumer.querySourceData( TransactionType.INCOME);
        final List<Source> spendData = consumer.querySourceData( TransactionType.EXPENSE);


        if( addData != null )
            setFragmentDisplayData(addData, 0);

        if( spendData != null )
            setFragmentDisplayData(spendData, 1);

    }

    private void setFragmentDisplayData(@NonNull final List<Source> data, final int fragAdapterPos ){
        setFragmentDisplayData( data, adapter.getFragmentAtPosition( fragAdapterPos ));
    }

    private void setFragmentDisplayData(@NonNull final List<Source> data,
                                        @NonNull final ListFragment<Source> client){

        if( data.size() > 0 )
            client.display( data );
        else client.onNoData();

    }


    /**
     * Launches a new instance of the same fragment that is already being used
     * to handle the search queries
     */
    private void launchSearchFragment(){
        getSupportFragmentManager().beginTransaction()
                .add( R.id.id_search_fragment_container, (Fragment)searchContract )
                .addToBackStack( "SearchFragment" )
                .commit();
        searchFragmentContainer.setVisibility( View.VISIBLE );
    }

    private void removeQueryFragment(){
        searchContract.onNoData();
        getSupportFragmentManager().popBackStack();
        searchFragmentContainer.setVisibility(View.GONE);
    }


    public int resolveCurrentTransactionType(){
        return resolveCurrentTransactionType( container.getCurrentItem() );
    }

    public int resolveCurrentTransactionType( final int pagePosition ){
        return ( pagePosition == 0 ) ? TransactionType.INCOME : TransactionType.EXPENSE;
    }

    private void enterAnimateSearchBar(){

        if(Build.VERSION.SDK_INT >= 21)
            enterAnimateSearchBarPostApi21();

        else
            enterAnimateSearchBarPreApi21();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enterAnimateSearchBarPostApi21(){

        if( wasSearchViewExpanded ) {

            searchBarContainer.setVisibility( View.VISIBLE );
            setEditorFocus( androidSearchEditor, true );

            return;
        }

        final int x = toolyBary.getWidth();
        final int y = toolyBary.getHeight() - (toolyBary.getHeight()/2);


        final Animator circularShowAnimator =
               ViewAnimationUtils.createCircularReveal(searchBarContainer, x, y, 0, searchBarContainer.getWidth());

       circularShowAnimator.addListener(new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {

               setEditorFocus( androidSearchEditor, true );

               final InputMethodManager imm =
                       ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));

               if( imm != null )
                      imm.showSoftInput( androidSearchEditor, 0 );

           }
       });

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

                setEditorFocus( androidSearchEditor, false );

                final InputMethodManager imm =
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));

                if( imm != null )
                    imm.hideSoftInputFromWindow( androidSearchEditor.getWindowToken(), 0 );

                searchBarContainer.setVisibility(View.INVISIBLE);

            }
        });

        circularShowAnimator.start();

    }

    private void exitAnimateSearchBarPreApi21(){
        //TODO implement a fade or something
    }



    //Expand/collapse listener for the SearchView
    private MenuItem.OnActionExpandListener searchExpansionListener = new MenuItem.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {

            final Animation animation = AnimationUtils.loadAnimation(
                    ActivitySourceList.this, R.anim.container_slide_up
            );

            toolbarContainer.startAnimation( animation );
            newSourceFab.startAnimation( animation );
            container.requestLayout();
            enterAnimateSearchBar();

            launchSearchFragment();

            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {

            final Animation animation = AnimationUtils.loadAnimation(
                    ActivitySourceList.this, R.anim.container_slide_down
            );

            toolbarContainer.startAnimation( animation );
            newSourceFab.startAnimation( animation );
            container.requestLayout();

            exitAnimateSearchBar();

            removeQueryFragment();

            return true;
        }
    };


    /*
     * Tightly coupled to the positions of R.arrays.sortOrderItems
     * Returns a sort order from the SortOrder Dialog
     */

    private SourceSortOrder getSortOrderFromArray(final int pos){

        int sortOrder;

        switch( pos ){

            case 0: sortOrder = SourceSortOrder.SORT_TITLE_DESC; break;
            case 1: sortOrder = SourceSortOrder.SORT_TITLE_ASC; break;
            case 2: sortOrder = SourceSortOrder.SORT_LAST_UPDATE_DESC; break;
            case 3: sortOrder = SourceSortOrder.SORT_LAST_UPDATE_ASC; break;
            case 4: sortOrder = SourceSortOrder.SORT_CREATION_DATE_DESC; break;
            case 5: sortOrder = SourceSortOrder.SORT_CREATION_DATE_ASC; break;
            case 6: sortOrder = SourceSortOrder.SORT_TOTAL_DESC; break;
            case 7: sortOrder = SourceSortOrder.SORT_TOTAL_ASC; break;

            default: sortOrder = SourceSortOrder.SORT_CREATION_DATE_DESC;
        }

        return new SourceSortOrder( sortOrder );
    }



    private ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(View page, float position) {

            addIndicator.setPercentage( position );
            spendIndicator.setPercentage( (1.0F - position) );

        }
    };



}
