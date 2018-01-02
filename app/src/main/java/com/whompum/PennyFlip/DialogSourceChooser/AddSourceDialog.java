package com.whompum.PennyFlip.DialogSourceChooser;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.whompum.PennyFlip.R;

import java.util.Iterator;

/**
 * Created by bryan on 12/21/2017.
 */

public class AddSourceDialog extends SourceDialog {

    {
        HEADER_COLOR = R.color.light_green;
        FAB_SRC = R.drawable.ic_shape_plus_green;
    }

    public static final String TAG = "AddSourceDialog";

    public static SourceDialog newInstance(final Bundle args){
        SourceDialog sourceDialog = new AddSourceDialog();
        sourceDialog.setArguments(args);

    return sourceDialog;
    }


    @Override //Creates the adapter. Normally this guy would comb through a Database or file.
    protected SourceWrapperAdapter manifestAdapter() {
        //this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, new AdapterSelecteable<SourceWrapper>());
        this.sourceListAdapter = new SourceWrapperAdapter(getContext(), HEADER_COLOR, StaticList.get());
    return sourceListAdapter;
    }

    /**
     *
     * Searches through a list of data (most from a content provider)
     * for the specified search query.
     *
     * FILLED WITH DUMMY DATA
     *
     * @param popData search query
     */
    @Override
    protected void populate(@Nullable CharSequence popData ) {

        final AdapterSelecteable<SourceWrapper> data = StaticList.get();
        AdapterSelecteable<SourceWrapper> results = new AdapterSelecteable<>();

        if(popData == null)
            results = data;
        else {

            final Iterator<SourceWrapper> iterator = data.iterator();

            while (iterator.hasNext()) {
                final SourceWrapper wrapper = iterator.next();
                if (wrapper.getTitle().toLowerCase().contains( ((String)popData).toLowerCase() ))
                    results.add(wrapper);
            }
        }
        sourceListAdapter.swapDataSet(results);
    }

    @Override
    protected void onDone() {
        super.onDone();
        //Puts the final Pieces on the TransactionBuilder object, and then sends it on its way to storage.
    }
}
