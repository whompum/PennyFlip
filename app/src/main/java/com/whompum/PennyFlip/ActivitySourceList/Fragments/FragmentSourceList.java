package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.whompum.PennyFlip.ActivitySourceList.Dialog.NewSourceDialog;
import com.whompum.PennyFlip.ActivitySourceList.Dialog.OnSourceCreated;
import com.whompum.PennyFlip.ActivitySourceList.OnSortButtonClicked;
import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.Money.Source.SourceSortOrder;
import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.OnItemSelected;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by bryan on 12/27/2017.
 */

public abstract class FragmentSourceList extends Fragment implements OnItemSelected<Source>,
        SearchView.OnQueryTextListener,
        OnSortButtonClicked, Handler.Callback, Observer<List<Source>>, OnSourceCreated {

    @LayoutRes
    protected static final int LAYOUT = R.layout.source_list_content;

    protected int highlight = -1;

    private int selectedSourceItem = -1;

    protected int transactionType = -1;

    private AnimateScale animator;

    protected Intent intent;

    private IntentReciever intentReciever; //Callback impl to recieve the startActivity intent

    protected SourceListAdapter listAdapter;

    private Unbinder unbinder;

    @BindView(R.id.id_list) protected RecyclerView list;

    @BindView(R.id.id_fab) protected FloatingActionButton addFab;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create early so we can populate ASAP

        int color;

        if(Build.VERSION.SDK_INT >= 23)
            color = getContext().getColor(highlight);
        else
            color = getContext().getResources().getColor(highlight);

        this.listAdapter = new SourceListAdapter(getContext(), color);

        MoneyController.obtain(getContext())
                .fetchObservableSources(new Handler(this), null, transactionType, false);
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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(LAYOUT, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listAdapter.registerSouceListClickListener(this);

        this.list.setAdapter(listAdapter);

        this.list.addOnScrollListener(scrollListener);

        animator = new AnimateScale(addFab, true);

    return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false; //We populate progressively not on request
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        if(!isAdded()) return false;

        populate(newText);
        return true;
    }


    @Override
    public boolean handleMessage(Message msg) {

        if(msg.obj == null)
            return true;

        if(msg.obj instanceof LiveData)
            ((LiveData<List<Source>>)msg.obj).observe(this, this);

        if(msg.obj instanceof List) {
            Log.i("SEARCHING_FOR_SOURCE", "DATA SIZE: " + (((List) msg.obj).size()));
            listAdapter.swapDataset((List<Source>) msg.obj);
        }

        return true;
    }

    @Override
    public void onChanged(@Nullable List<Source> sources) {
        if(listAdapter != null)
            listAdapter.swapDataset(sources);
    }


    protected void createIntent(@NonNull Source data){
        this.intent = new Intent(getActivity(), ActivitySourceData.class);
    }

    @OnClick(R.id.id_fab)
    public void launchNewSourceDialog(){
        new NewSourceDialog(getContext(), this, transactionType).show();
    }

    @Override
    public void onSourceCreated(@NonNull Source source) {
        MoneyController.obtain(getContext()).insertNewSource(source);
    }

    public void onItemSelected(@NonNull Source data){
        createIntent(data);
        initIntentArgs(data);
        intentReciever.onDeliverIntent(intent);
    }

    /**
     * Initialize intent arguments when we go to launch a new activity
     * @param data
     */
    @CallSuper
    protected void initIntentArgs(@NonNull final Source data){
        intent.putExtra(ActivitySourceData.DATA, data);
    }


    protected void populate(CharSequence query){

        String sourceName = null;
        boolean searchLike = false;

        if(query != null && query.length() > 0) {
            sourceName = "%" + query + "%";
            searchLike = true;
        }

        MoneyController.obtain(getContext()).fetchSources(new Handler(this), sourceName, transactionType, searchLike);
    }

    @Override
    public void onSortClicked() {
        final AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog);

        filterBuilder.setTitle(R.string.string_sort_title);
        filterBuilder.setSingleChoiceItems(R.array.sortOrderItems, selectedSourceItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSourceItem = which;
                sortData(getSortOrderFromArray(selectedSourceItem));
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
    protected final void sortData(SourceSortOrder order){
        //Resolve the sort comparator form SourceSortOrder
        //Fetch data from SourceListAdapter
        //Sort, and put back in.

        final List<Source> data = listAdapter.getDataSet();

        if(data == null || data.size() == 0)
            return;

        Collections.sort(data, order.resolveSorter());
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Tightly coupled to the positions of R.arrays.sortOrderItems
     * Returns a sort order from the SortOrder Dialog
     * @return The sort order (Given from the selected item)
     */
    private SourceSortOrder getSortOrderFromArray(final int pos){

        int sortOrder;

        switch(pos){

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

        return new SourceSortOrder(sortOrder);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                animator.hide(250L);
            else if(newState == RecyclerView.SCROLL_STATE_IDLE)
                animator.show(250L);
        }
    };

}
