package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.whompum.PennyFlip.ActivitySourceList.OnSortButtonClicked;
import com.whompum.PennyFlip.ActivitySourceList.TextLimitWatcher;
import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.Data.Loader.SourceLoader;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.Data.Schemas.SourceSortOrder;
import com.whompum.PennyFlip.PennyFlipCursorAdapter;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListClickListener;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapterBase;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;
import com.whompum.PennyFlip.Sources.SourceCursorAdapter;
import com.whompum.PennyFlip.Sources.SourceMetaData;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.List;

/**
 * Created by bryan on 12/27/2017.
 */

public abstract class FragmentSourceList extends Fragment implements SourceListClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener,
        SearchView.OnQueryTextListener,
        OnSortButtonClicked{


    /**
     * *************STATE DECLARATIONS*************************
     *
     * @state LAYOUT: the layout file to inflate for this fragment (Same for all implementors of this class)
     * @state DIALOG_LAYOUT: The layout resource file for the Add Source Dialog
     * @state sourceNameLimit: The limit for the SourceName length
     * @state loaderId: The id of the loader; Set by children in an instance block initializer
     * @state sourceType: sourceType; Used as a whereArg for the loader; Set by children via initialization block
     * @state newSourceDialogImage: Image reference set by children, to say which drawable item to use for the new source dialog
     * @state WHERE: Where key for the loader bundle query arguments
     * @state WHERE_ARGS_KEY: WhereArgs key for the loader bundle query arguments
     * @state SORT_ORDER_KEY: SortOder key for the loader bundle query arguments
     * @state DEFAULT_SORT_ORDER: Main Sort order to use; We always want the last updated sources to display before the oldest updated sources
     *        thus the sort order is col_last_update desc
     * @state list: The recyclerView for the sources
     * @state listAdapter: Special implementaion of Recycler.Adapter for use in this class
     * @state addFab: The fab that allows us to add a new Source Object
     * @state animator: Animates the fab as the recyler view is scrolling (Hiding/showing)
     * @state intent: This class, and its children construct intents to view a source detail activity when "view" is clicked;
     *                Children set the class they want to start via the abstract method createIntent()
     * @state intentReciever: The object that handles the intent; ActivitySourceList
     * @state  loaderArgs: The query arguments for the Loader
     * @state sourceNameEditor: EditText in the SourceDialog that users input the Sources name into
     * @state sourceNameCounter: A source's name can only have 20 characters, and this object displays  (n / 20) characters left as
     *                           the user changes the source name
     * @state addSourceDialog: The dialog that users can use to add a source
     * @state titleErrorDisplay: Displays an error to the user
     * @state sortOrder: The user defined (or default) Sort Order to apply on the queryies
     * @state selectedSourceItem: Cache the variable selected in the SortOrder dialog
     */

    @LayoutRes
    protected static final int LAYOUT = R.layout.layout_source_list_container;
    @LayoutRes
    public static final int DIALOG_LAYOUT = R.layout.layout_add_source_dialog;

    public static final int SOURCE_NAME_LIMIT = 20;

    protected int loaderId = Integer.MIN_VALUE;
    protected int sourceType = Integer.MIN_VALUE;

    @DrawableRes
    protected int newSourceDialogImage = -1;

    public static final String WHERE_KEY = "Where.ky";
    public static final String WHERE_ARGS_KEY = "WhereArgs.ky";
    public static final String SORT_ORDER_KEY = "SortOrder.ky";
    public static final SourceSortOrder DEFAULT_SORT_ORDER = SourceSortOrder.TOTAL_HIGH_TO_LOW;

    protected RecyclerView list;
    protected SourceListAdapterBase listAdapter;

    private FloatingActionButton addFab;

    private AnimateScale animator;

    protected Intent intent;

    private IntentReciever intentReciever;

    private Bundle loaderArgs = new Bundle();

    protected PennyFlipCursorAdapter cursorAdapter;

    private EditText sourceNameEditor;
    private TextView sourceNameCounter;

    private Dialog addSourceDialog;

    private TextView titleError;

    private View dialogContentView;

    private SourceSortOrder sortOrder = DEFAULT_SORT_ORDER;

    private int selectedSourceItem = -1;

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

        this.list = view.findViewById(R.id.id_list);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.registerSouceListClickListener(this);

        this.list.setAdapter(listAdapter);

        this.list.addOnScrollListener(scrollListener);

        addFab = view.findViewById(R.id.id_fab);
        addFab.setOnClickListener(this);

        animator = new AnimateScale(addFab, true);

    return view;
    }


    /**
     * Makes the Add Source Dialog, and shows it;
     * @param v unused (The view that was clicked)
     */
    @Override
    public void onClick(View v) {
        makeDialog().show();
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
     * Makes the SourceDialog, and sets up all of its the view objects
     *
     * @return A dialog to add a new source
     */
    protected final Dialog makeDialog(){

        final int DIALOG_STYLE = R.style.StyleDialogAnimate;

        addSourceDialog = new Dialog(getContext(), DIALOG_STYLE);

        addSourceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogContentView = inflateAddDialog();

        ((ImageView)dialogContentView.findViewById(R.id.id_source_type_display))
                .setImageResource(newSourceDialogImage);

        dialogContentView.findViewById(R.id.id_dialog_done)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSourceAdded();
                    }
                });

        dialogContentView.findViewById(R.id.id_dialog_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addSourceDialog.dismiss();
                    }
                });


        this.sourceNameCounter = dialogContentView.findViewById(R.id.id_text_limit_display);
             sourceNameCounter.setText("0/20");

        this.sourceNameEditor = dialogContentView.findViewById(R.id.id_source_name_editor);
        sourceNameEditor.addTextChangedListener(new TextLimitWatcher(sourceNameCounter));

        this.titleError = dialogContentView.findViewById(R.id.id_title_error);

        addSourceDialog.setContentView(dialogContentView);
        setDialogWidth();

        final WindowManager.LayoutParams windowAttrs = addSourceDialog.getWindow().getAttributes();

        if(windowAttrs!= null)
            windowAttrs.windowAnimations = DIALOG_STYLE;

        return addSourceDialog;
    }

    /**
     * Returns a View layout to use for the Dialog
     * @return the Dialogs Layout
     */
    private View inflateAddDialog(){

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        return inflater.inflate(DIALOG_LAYOUT, null, false);
    }


    /**
     * Override Dialogs default WRAP_CONTENT width
     * and sets to MATCH_PARENT,  Then we use View padding to control re
     */
    private void setDialogWidth(){

        if(addSourceDialog.getWindow().getAttributes() != null) {

            final DisplayMetrics metrics = new DisplayMetrics();
            addSourceDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //DP value to inset each side of the Dialog by

            //Screen width MINUS (twice the size of the offset (to account for each side))
            final int screenWidth = (metrics.widthPixels);

            addSourceDialog.getWindow().getAttributes().width = screenWidth;

        }
    }



    private void onSourceAdded(){

        final String newTitle = sourceNameEditor.getText().toString();

        //If title is null
        if(TextUtils.isEmpty(newTitle)) {
            onTitleError(R.string.string_title_error_null);
            return;
        }


        //If title is already in use
        if(!canUseTitle(newTitle)) {
            onTitleError(R.string.string_title_error_in_use);
            return;
        }

        //Yay we made it this far
        final boolean saved = saveNewSource(newTitle);

       //Notify users
        if(saved) {
            addSourceDialog.dismiss();
            notifySourceSaved();
        }
        else
            onTitleError(R.string.string_some_wrong_error);
    }


    /**
     * Should this be done off-UI thread? It's a small quick query so IDK
     * Checks if the database has the current Title already in use
     * @param title The requested Title from the user
     * @return True or false if we can use the title
     */
    public boolean canUseTitle(final CharSequence title){


        final Cursor data = getContext().getContentResolver().query(
                SourceSchema.SourceTable.URI,
                SourceSchema.SourceTable.COLUMNS_WITHOUT_ID,
                SourceSchema.SourceTable.COL_TITLE + "=?",
                new String[]{(String)title},
                null
        );


        final boolean dataNull = (data == null);
        final boolean dataIsEmpty = (data.getCount() == 0);

        data.close();

       if(dataNull)
           return false;

       return dataIsEmpty;
    }

    /**
     * Tries to insert and save the new Source Object
     * @return Whether or not the Source was successfully saved
     */
    private boolean saveNewSource(@NonNull final CharSequence title){

        final ContentValues sourceData = new ContentValues();

        sourceData.put(SourceSchema.SourceTable.COL_TITLE, (String)title);
        sourceData.put(SourceSchema.SourceTable.COL_TOTAL, 0L);
        sourceData.put(SourceSchema.SourceTable.COL_TYPE, sourceType);
        sourceData.put(SourceSchema.SourceTable.COL_CREATION_DATE, Timestamp.now().millis());
        sourceData.put(SourceSchema.SourceTable.COL_LAST_UPDATE, Timestamp.now().millis());

        final Uri newUri = getContext().getContentResolver().insert(SourceSchema.SourceTable.URI, sourceData);

        return newUri != null;
    }

    private void notifySourceSaved(){
        Toast.makeText(getContext(), R.string.string_successfully_saved, Toast.LENGTH_SHORT).show();
    }


    /**
     * Displays a shake animation if Text is null, and animates the text
     * @param titleErrorRes
     */
    private void onTitleError(@StringRes final int titleErrorRes){

        titleError.setText(titleErrorRes);

        titleError.setAlpha(1F);//IDK why but this is needed
        titleError.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        dialogContentView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));

    }


    /**
     * Initialize intent arguments when we go to launch a new activity
     * @param data
     */
    @CallSuper
    protected void initIntentArgs(@NonNull final SourceMetaData data){
        intent.putExtra(ActivitySourceData.SOURCE_KEY, data);
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


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.setData(data);
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
        loaderArgs.putString(SORT_ORDER_KEY, sortOrder.getSortOrder());

    }

    private void generateLoaderArgs(final CharSequence query){

        if(query == null) {
            generateDefaultLoaderArgs();
            return;
        }

        final String[] queryArg = {fetchLikeQuery((String)query), String.valueOf(sourceType)} ;

        loaderArgs.putString(WHERE_KEY, SourceSchema.SourceTable.COL_TITLE + " LIKE ? AND "
        + SourceSchema.SourceTable.COL_TYPE + " =?");
        loaderArgs.putStringArray(WHERE_ARGS_KEY, queryArg);
        loaderArgs.putString(SORT_ORDER_KEY, sortOrder.getSortOrder());
    }

    private SourceCursorAdapter manifestCursorAdapter(){
        return new SourceCursorAdapter(null);
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


    @Override
    public void onSortClicked() {
        showFilterDialog();
    }

    private void showFilterDialog(){

        final AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog);


        filterBuilder.setTitle(R.string.string_sort_title);
        filterBuilder.setSingleChoiceItems(R.array.sortOrderItems, selectedSourceItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSourceItem = which;
                sort(getSortOrderFromArray(selectedSourceItem));
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = filterBuilder.create();

        final WindowManager.LayoutParams attrs = dialog.getWindow().getAttributes();

        if(attrs != null)
            attrs.windowAnimations = R.style.StyleDialogAnimate;

    dialog.show();
    }

    /**
     * Applies a sort order to the search query
     *
     * @param order
     */
    protected final void sort(SourceSortOrder order){

        this.sortOrder = order;
        loaderArgs.putString(SORT_ORDER_KEY, sortOrder.getSortOrder());

        //Restart the loader with the sort param
        getLoaderManager().restartLoader(loaderId, loaderArgs, this);
    }

    /**
     * Tightly coupled to the positions of R.arrays.sortOrderItems
     * Returns a sort order from the SortOrder Dialog
     * @return The sort order (Given from the selected item)
     */
    private SourceSortOrder getSortOrderFromArray(final int pos){

        switch(pos){

            case 0: return SourceSortOrder.TITLE_HIGH_TO_LOW;
            case 1: return SourceSortOrder.TITLE_LOW_TO_HIGH;
            case 2: return SourceSortOrder.LAST_UPDATE_HIGH_TO_LOW;
            case 3: return SourceSortOrder.LAST_UPDATE_LOW_TO_HIGH;
            case 4: return SourceSortOrder.CREATION_DATE_LOW_TO_HIGH;
            case 5: return SourceSortOrder.CREATION_DATE_HIGH_TO_LOW;
            case 6: return SourceSortOrder.TOTAL_HIGH_TO_LOW;
            case 7: return SourceSortOrder.TOTAL_LOW_TO_HIGH;

            default: return null;
        }

    }

}
