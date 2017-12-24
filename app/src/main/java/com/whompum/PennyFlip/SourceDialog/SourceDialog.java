package com.whompum.PennyFlip.SourceDialog;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.R;


public abstract class SourceDialog extends DialogFragment implements OnSourceListItemCliked{

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

    protected SourceWrapperAdapter sourceListAdapter;

    protected SourceWrapper item;

    private AnimateScale animator;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getContext(), R.style.SourceDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        final Rect displaySize = new Rect();

        getDialog().getWindow()
                .getWindowManager()
                .getDefaultDisplay()
                .getRectSize(displaySize);

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


        this.actionFab = layout.findViewById(R.id.id_source_dialog_fab);
             actionFab.setImageResource(FAB_SRC);
             actionFab.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     onDone();
                 }
             });

        animator = new AnimateScale(actionFab);

    return layout;
    }


    private void insertIntoFirst(final CharSequence title){
        if(title!=null)
        sourceListAdapter.insertToFirst(new SourceWrapper(title.toString(), SourceWrapper.TAG.NEW));
    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                hideFab();
            else if(newState == RecyclerView.SCROLL_STATE_IDLE)
                showFab();


        }

    };

    private SearchWatcher sherlock = new SearchWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() == 0)
                populate(null);
            else
                populate(s.toString());


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
    public void onSourceItemClicked(SourceWrapper sourceWrapper) {
        this.item = sourceWrapper;
    }

    protected  void onDone(){
        getDialog().getWindow().getDecorView().animate().x(-1000).setInterpolator(new AnticipateInterpolator()).setDuration(500L).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 500L);
    }


    protected abstract SourceWrapperAdapter manifestAdapter();
    protected abstract void populate(@Nullable final CharSequence popData);


}
