package com.whompum.PennyFlip.ActivitySourceList.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.ListUtils.OnItemSelected;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.ActivitySourceData.ActivitySourceData;
import com.whompum.PennyFlip.ActivitySourceList.Adapter.SourceListAdapter;
import com.whompum.PennyFlip.ActivitySourceList.IntentReciever;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentSourceList extends Fragment implements OnItemSelected<Source>, SourceListClientContract{

    @LayoutRes
    protected static final int LAYOUT = R.layout.source_list_content;

    @ColorRes
    protected int highlight = -1;

    private IntentReciever intentReciever; //Callback impl to recieve the startActivity intent

    protected SourceListAdapter listAdapter;

    private Unbinder unbinder;

    @BindView(R.id.id_global_list) protected RecyclerView list;

    public FragmentSourceList newInstance(@NonNull final Integer highlight){
        final FragmentSourceList fragment = new FragmentSourceList();

        fragment.highlight = highlight;

    return fragment;
    }

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

        list.addItemDecoration( new SourceListMarginDecorator(
                getContext().getResources().getDimensionPixelSize( R.dimen.dimen_padding_ver_base )
        ) );

    return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void display(@NonNull List<Source> data) {
        swapAdapterData( data );
    }

    @Override
    public void clear() {
        swapAdapterData( new ArrayList<Source>() );
    }

    private void swapAdapterData(@NonNull final List<Source> data){

        if( listAdapter != null ){

            listAdapter.swapDataset( data );

            if( list != null )
                listAdapter.notifyDataSetChanged();
        }

    }

    public void onItemSelected(@NonNull Source data){
        final Intent intent = new Intent(getActivity(), ActivitySourceData.class);
        intent.putExtra(ActivitySourceData.DATA, data);

        intentReciever.onDeliverIntent(intent);
    }


    /*
    @Override
    public void onSourceCreated(@NonNull final Source source) {

        //First check if the Source is usable
        final MoneyRequest request = new MoneyRequest.QueryBuilder( SourceQueryKeys.KEYS )
                .setQueryParameter( SourceQueryKeys.TITLE, source.getTitle() ).getQuery();

        final Deliverable<Source> deliverable = queries.queryById( request, database );

        deliverable.attachCancelledResponder(new OnCancelledResponder() {
            @Override
            public void onCancelledResponse(int reason, String msg) {
                if( reason == QueryHandler.NULL_DATA_QUERY ) { //Title doesn't exist
                    new RoomMoneyWriter( database ).saveSource( source );
                    newSourceDialog.dismiss();
                }
            }
        });

        deliverable.attachResponder(new Responder<Source>() {
            @Override
            public void onActionResponse(@NonNull Source data) {
                //Title exists.
                newSourceDialog.onTitleError( R.string.string_title_error_in_use );
            }
        });

    }
    */

    /**
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



    protected void populate(CharSequence query){


        if(query != null && query.length() > 0) {

            searchLikeQueryBuilder.setQueryParameter( SourceQueryKeys.LIKE_TITLE, "%" + query + "%")
                    .setQueryParameter( SourceQueryKeys.TRANSACTION_TYPE, transactionType);

            final Deliverable<List<Source>> deliverable =
                    queries.queryGroup( searchLikeQueryBuilder.getQuery(), database );

            deliverable.attachResponder(new Responder<List<Source>>() {
                @Override
                public void onActionResponse(@NonNull List<Source> data) {
                    listAdapter.swapDataset( data );
                }
            });
        }

    }
     */

    /**
     * Applies a sort order to the search query
     *
     * @param order
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
     */

    /**
     * Tightly coupled to the positions of R.arrays.sortOrderItems
     * Returns a sort order from the SortOrder Dialog
     * @return The sort order (Given from the selected item)

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
    */
}
