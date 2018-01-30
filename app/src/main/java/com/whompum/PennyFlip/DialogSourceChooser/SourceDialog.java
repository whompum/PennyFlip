package com.whompum.PennyFlip.DialogSourceChooser;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.Data.Loader.SourceLoader;
import com.whompum.PennyFlip.Data.Schemas.SourceSchema;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Source.SourceWrapper;
import com.whompum.PennyFlip.Source.SourceWrapperCursorAdapter;

import java.util.List;


public abstract class SourceDialog extends DialogFragment implements OnSourceListItemChange, LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * --Is a base SourceDialog that is defines basic behavior for the views.

     --AbstractMethods
     ---populate(): populates its RecyclerView
     ---onDone(): Called when their main Fab is clicked

     --Children set their wanted layouts in a static initilization block
     --Base takes care of changing the first item in a RecyclerView

     */

    @ColorRes
    protected int HEADER_COLOR = R.color.light_grey;
    @DrawableRes
    protected int FAB_SRC = R.drawable.ic_checkmark;

    @LayoutRes
    protected int LAYOUT = R.layout.layout_dialog_source;

    private static final String WHERE_KEY = "where.ky";
    private static final String WHERE_ARGS_KEY = "whereArgs.ky";
    private static final String SORT_ORDER_KEY = "sortOrder.ky";

    private static final int LOADER_ID = 100;

    protected FloatingActionButton actionFab;

    protected RecyclerView sourceList;

    protected SourceWrapperAdapter sourceListAdapter;

    protected SourceWrapper item;

    private AnimateScale animator;

    protected OnSourceItemSelected sourceItemSelectedListener;

    protected int transactionType = -1;

    private Bundle loaderArgs = new Bundle();

    private SourceWrapperCursorAdapter wrapperAdapter = new SourceWrapperCursorAdapter(null);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populate(null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext(), R.style.SourceDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        if(params != null)
            params.windowAnimations = R.style.StyleDialogAnimate;

    return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Rect displaySize = new Rect();



        final WindowManager windowManager = getDialog().getWindow().getWindowManager(); //Kotlin would be nice right here...
        if(windowManager != null)
            windowManager.getDefaultDisplay().getRectSize(displaySize);

        final int offsetHor = getResources().getDimensionPixelSize(R.dimen.dimen_source_dialog_offset_hor);
        final int offsetVer = getResources().getDimensionPixelSize(R.dimen.dimen_source_dialog_offset_ver);

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);

        getDialog().getWindow().setLayout(displaySize.width()-offsetHor, displaySize.height()-offsetVer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, container, false);

        int headerColor = 0;

        if(Build.VERSION.SDK_INT >= 23)
            headerColor = getContext().getColor(HEADER_COLOR);
        else
            headerColor = getContext().getResources().getColor(HEADER_COLOR);

        layout.findViewById(R.id.id_source_dialog_header).setBackgroundColor(headerColor);

        sourceList = layout.findViewById(R.id.id_source_dialog_source_list);
        sourceList.addOnScrollListener(scrollListener);

        final SourceWrapperAdapter adapter = manifestAdapter();
        adapter.registerOnClickListener(this);

        sourceList.setAdapter(adapter);

        ((EditText)layout.findViewById(R.id.id_source_dialog_search_view)).addTextChangedListener(sherlock);


        this.actionFab = layout.findViewById(R.id.id_fab);
             actionFab.setImageResource(FAB_SRC);
             actionFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     onDone();
                 }
             });

        animator = new AnimateScale(actionFab, false);

    return layout;
    }


    private void insertIntoFirst(final CharSequence title){
        if(title!=null)
        sourceListAdapter.insertToFirst(new SourceWrapper(title.toString(), SourceWrapper.TAG.NEW, transactionType));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final CursorLoader loader = new SourceLoader(getContext());

        final String[] columns = {SourceSchema.SourceTable._ID,
                                  SourceSchema.SourceTable.COL_TITLE,
                                  SourceSchema.SourceTable.COL_TYPE,
                                  SourceSchema.SourceTable.COL_TOTAL};

        loader.setProjection(columns);

        loader.setSelection(args.getString(WHERE_KEY));
        loader.setSelectionArgs(args.getStringArray(WHERE_ARGS_KEY));

    return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        wrapperAdapter.setData(data);

        final AdapterSelecteable<SourceWrapper> listOData = (AdapterSelecteable<SourceWrapper>) wrapperAdapter.fromCursor();

        for(SourceWrapper w: listOData){
            Log.i("SourceDialog", "TOTAL: " + w.getOriginalAmount());
        }

       sourceListAdapter.swapDataSet(listOData);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            //If the item has been set, then we can do animations; Otherwise the fab is already hidden so no animations are needed
            if(item !=null) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    hideFab();

                else if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    showFab();
            }

        }

    };


    private SearchWatcher sherlock = new SearchWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() == 0)
                populate(null);
            else
                populate(s.toString());

            /**
             * FUTURE note to myself:
             * This code below was causing a bug where i'd insert into the first index
             * , but it would quickly be overwritten by the dataset fetched from the Database;
             * The problem was because i'm using concurency to fetch the Source meta data
             * so insertToFirst() would be called *after* populate (which restarts a loader)
             * but would run before the data would return; The solution is going to be
             */
          insertIntoFirst(s.toString());
        }
    };

    private void hideFab() {
        animator.hide(250L);
    }

    private void showFab(){
        animator.show(250L);
    }

    @Override
    public void onSourceListItemChange(SourceWrapper sourceWrapper) {

        /**
         * If the first element is selected, but has been changed, and the fab is showing, hide.
         * Else if any element has been selected, and the fab isn't show, show.
         */

        if(sourceWrapper == null & animator.isShowing())
            animator.hide(250L);

        else if(sourceWrapper != null & !animator.isShowing())
            animator.show(250L);

        this.item = sourceWrapper;
    }

    @CallSuper
    protected void onDone(){
        dismiss();
        notifyListener();
    }

    private void populate(@Nullable final CharSequence popData){

        generateLoaderArgs(popData);

        getLoaderManager().restartLoader(LOADER_ID, loaderArgs, this);
    }

    private void generateDefaultLoaderArgs(){
        loaderArgs.putString(WHERE_KEY, SourceSchema.SourceTable.COL_TYPE + "=?");
        loaderArgs.putStringArray(WHERE_ARGS_KEY, new String[]{String.valueOf(transactionType)});
        loaderArgs.putString(SORT_ORDER_KEY, SourceSchema.SourceTable.COL_TITLE + " ASC");
    }

    /**
     * Fetches Sources where the Title is LIKE the query, and the type is our type
     *
     * SELECT * FROM 'SourceTable'
     * WHERE 'title' LIKE 'query'
     * AND 'sourceType' = 'transactionType'
     *
     * @param query The name query to search for with da' loader
     */
    private void generateLoaderArgs(@Nullable final CharSequence query){

        if(query == null){
            generateDefaultLoaderArgs();
            return;
        }
        else if(query.length() == 0){
            generateDefaultLoaderArgs();
            return;
        }

        final String queryArgs = "%"+query+"%";

        loaderArgs.putString(WHERE_KEY, SourceSchema.SourceTable.COL_TITLE + " LIKE? AND " +
                                        SourceSchema.SourceTable.COL_TYPE + " =? ");
        loaderArgs.putStringArray(WHERE_ARGS_KEY, new String[]{queryArgs, String.valueOf(transactionType)});
    }



    @CallSuper
    protected void notifyListener(){
        if(sourceItemSelectedListener != null)
            sourceItemSelectedListener.onSourceItemSelected(item);
    }

    public void registerItemSelectedListener(final OnSourceItemSelected l){
        this.sourceItemSelectedListener = l;
    }


    @NonNull
    protected abstract SourceWrapperAdapter manifestAdapter();


    public interface OnSourceItemSelected{
        void onSourceItemSelected(final SourceWrapper wrapper);
    }




}
