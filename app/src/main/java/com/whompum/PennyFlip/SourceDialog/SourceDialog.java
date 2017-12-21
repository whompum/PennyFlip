package com.whompum.PennyFlip.SourceDialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.whompum.PennyFlip.R;

/**
 * Created by bryan on 12/21/2017.
 */

public abstract class SourceDialog extends DialogFragment {

    /**
     * --Is a base SourceDialog that is defines basic behavior for the views.

     --AbstractMethods
     ---populate(): populates its RecyclerView
     ---onDone(): Called when their main Fab is clicked

     --Children set their wanted layouts in a static initilization block
     --Base takes care of changing the first item in a RecyclerView

     */

    @ColorRes
    protected static int HEADER_COLOR = R.color.light_grey;
    @DrawableRes
    protected static int FAB_SRC = R.drawable.ic_checkmark;

    @LayoutRes
    protected int LAYOUT = R.layout.layout_dialog_source;


    protected FloatingActionButton actionFab;

    protected RecyclerView sourceList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext(), R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    return dialog;
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


        ((EditText)layout.findViewById(R.id.id_source_dialog_search_view)).addTextChangedListener(sherlock);


        this.actionFab = layout.findViewById(R.id.id_source_dialog_fab);
             actionFab.setImageResource(FAB_SRC);

        Log.i("test", "HELLO");

    return layout;
    }



    private SearchWatcher sherlock = new SearchWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() == 0)
                populate(null);
            else
                populate(s.toString());
        }
    };


    protected abstract void populate(@Nullable final CharSequence popData);
    protected abstract void onDone();

}
