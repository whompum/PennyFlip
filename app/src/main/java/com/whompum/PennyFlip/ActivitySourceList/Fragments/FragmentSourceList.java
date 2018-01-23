package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.Data.Loader.SourceLoader;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListClickListener;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;
import com.whompum.PennyFlip.Source.SourceCursorAdapter;
import com.whompum.PennyFlip.Source.SourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;
import com.whompum.PennyFlip.Transaction.Models.TransactionType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public abstract class FragmentSourceList extends Fragment implements SourceListClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener,
        SearchView.OnQueryTextListener{


    /**
     * *************STATE DECLARATIONS*************************
     *
     * @state LAYOUT: the layout file to inflate for this fragment (Same for all implementors of this class)
     * @state loaderId: The id of the loader; Set by children in an instance block initializer
     * @state sourceType: sourceType; Used as a whereArg for the loader; Set by children via initialization block
     * @state WHERE: Where key for the loader bundle query arguments
     * @state WHERE_ARGS_KEY: WhereArgs key for the loader bundle query arguments
     * @state SORT_ORDER_KEY: SortOder key for the loader bundle query arguments
     * @state SORT_ORDER: Main Sort order to use; We always want the last updated sources to display before the oldest updated sources
     *        thus the sort order is col_last_update desc
     * @state list: The recyclerView for the sources
     * @state listAdapter: Special implementaion of Recycler.Adapter for use in this class
     * @state addFab: The fab that allows us to add a new Source Object
     * @state animator: Animates the fab as the recyler view is scrolling (Hiding/showing)
     * @state intent: This class, and its children construct intents to view a source detail activity when "view" is clicked;
     *                Children set the class they want to start via the abstract method createIntent()
     * @state intentReciever: The object that handles the intent; ActivitySourceList
     * @state  loaderArgs: The query arguments for the Loader
     */

    @LayoutRes
    protected static final int LAYOUT = R.layout.layout_source_list_container;

    protected int loaderId = Integer.MIN_VALUE;
    protected int sourceType = Integer.MIN_VALUE;

    public static final String WHERE_KEY = "Where.ky";
    public static final String WHERE_ARGS_KEY = "WhereArgs.ky";
    public static final String SORT_ORDER_KEY = "SortOrder.ky";
    public static final String SORT_ORDER = SourceSchema.SourceTable.COL_LAST_UPDATE + " DESC";


    protected RecyclerView list;
    protected SourceListAdapterBase listAdapter;

    private FloatingActionButton addFab;

    private AnimateScale animator;

    protected Intent intent;

    private IntentReciever intentReciever;

    private Bundle loaderArgs = new Bundle();

    protected SourceCursorAdapter cursorAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listAdapter = manifestAdapter();
        this.cursorAdapter = manifestCursorAdapter();
        generateDefaultLoaderArgs();
        getLoaderManager().initLoader(loaderId, loaderArgs, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            intentReciever = (IntentReciever) context;
        }catch(ClassCastException e){
            Log.i("ActivitySourceList", "The activity must implement the IntentReciever interface");
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(LAYOUT, container, false);

        this.list = view.findViewById(R.id.id_source_master_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.registerSouceListClickListener(this);

        this.list.setAdapter(listAdapter);

        this.list.addOnScrollListener(scrollListener);

        addFab = view.findViewById(R.id.id_fab);
        addFab.setOnClickListener(this);

        animator = new AnimateScale(addFab, true);

    return view;
    }


    int title = 100;

    @Override
    public void onClick(View v) {
        //TODO add source creating logic + Dialog :)


        final ContentValues values = new ContentValues();
        values.put(SourceSchema.SourceTable.COL_TITLE, String.valueOf(title++));
        values.put(SourceSchema.SourceTable.COL_TOTAL, 2782);
        values.put(SourceSchema.SourceTable.COL_TYPE, sourceType);
        values.put(SourceSchema.SourceTable.COL_CREATION_DATE, Timestamp.now().millis());
        values.put(SourceSchema.SourceTable.COL_LAST_UPDATE, Timestamp.now().millis());

        getContext().getContentResolver().insert(SourceSchema.SourceTable.URI, values);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                animator.hide(250L);
            else if(newState == RecyclerView.SCROLL_STATE_IDLE)
                animator.show(250L);
        }
    };


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        populate(newText);
        return true;
    }

    public void onItemSelected(@NonNull SourceMetaData data){
        createIntent(data);

        initIntentArgs(data);

        intentReciever.onDeliverIntent(intent);
    }


    /**
     * Initialize intent arguments when we go to launch a new activity
     * @param data
     */
    @CallSuper
    protected void initIntentArgs(@NonNull final SourceMetaData data){
        this.intent.putExtra(ActivitySourceData.SOURCE_NAME_KEY, data.getSourceName());
        this.intent.putExtra(ActivitySourceData.SOURCE_TIMESTAMP, data.getLastUpdate());
        this.intent.putExtra(ActivitySourceData.SOURCE_TOTAL, data.getPennies());
    }


    /**
     * Called by children when they've recieved new data
     *
     * @param dataList
     */
    protected void swapDataSet(final List<SourceMetaData> dataList){
        if(listAdapter!=null)
            listAdapter.swapDataset(dataList);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        final CursorLoader loader = new SourceLoader(getContext());

       loader.setSelection(args.getString(WHERE_KEY));
       loader.setSelectionArgs(args.getStringArray(WHERE_ARGS_KEY));
       loader.setSortOrder(args.getString(SORT_ORDER_KEY));

       return loader;
    }


    /**
     * Returns a double-ended wildcard like pattern for sqlite querying
     *
     * @param query the text order to search for
     * @return double-ended wildcard query argument
     */
    protected String fetchLikeQuery(String query){
        return "%"+query+"%";
    }


    //CHILD IMPLEMENTED METHODS


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.setCursor(data);
        swapDataSet(cursorAdapter.fromCursor());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    private void generateDefaultLoaderArgs(){


        final String whereClause = SourceSchema.SourceTable.COL_TYPE + " = ?";
        final String[] whereArgs = {String.valueOf(sourceType)};

        loaderArgs.putString(WHERE_KEY, whereClause);
        loaderArgs.putStringArray(WHERE_ARGS_KEY, whereArgs);
        loaderArgs.putString(SORT_ORDER_KEY, SORT_ORDER);

    }

    private void generateLoaderArgs(final CharSequence query){

        if(query == null) {
            generateDefaultLoaderArgs();
            return;
        }

        final String[] queryArg = {fetchLikeQuery((String)query)} ;

        loaderArgs.putString(WHERE_KEY, SourceSchema.SourceTable.COL_TITLE + " LIKE ? ");
        loaderArgs.putStringArray(WHERE_ARGS_KEY, queryArg);
        loaderArgs.putString(SORT_ORDER_KEY, SORT_ORDER);

    }

    /**
     * Each implementation will want to launch a separate Activity to handle its data
     * @param data SourceMetaData is data to give the the SourceData Activity.
     */
    protected abstract void createIntent(@NonNull SourceMetaData data);

    /**
     * @return An Implementation of type SourlceListAdapterBase unique to the child
     */
    protected abstract SourceListAdapterBase manifestAdapter();


    protected abstract SourceCursorAdapter manifestCursorAdapter();


    /**
     * Launches a new Loader with the search query
     * @param query the Selection details for the API
     */
    protected void populate(CharSequence query){
        if(isAdded()) {
            generateLoaderArgs(query);
            getLoaderManager().restartLoader(loaderId, loaderArgs, this);
        }
    }

    /**
     * Filters Results
     *
     * @param filter
     */
    protected abstract void filter(Object filter);

}
