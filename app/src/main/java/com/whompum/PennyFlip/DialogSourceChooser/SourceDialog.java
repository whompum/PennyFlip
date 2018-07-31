package com.whompum.PennyFlip.DialogSourceChooser;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whompum.PennyFlip.Animations.AnimateScale;
import com.whompum.PennyFlip.Money.MoneyController;
import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Time.PennyFlipTimeFormatter;
import com.whompum.PennyFlip.Time.Timestamp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public abstract class SourceDialog extends DialogFragment implements OnSourceListItemChange, Handler.Callback{


    @ColorRes
    protected int HEADER_COLOR = R.color.light_grey;

    @DrawableRes
    protected int FAB_SRC = R.drawable.ic_checkmark;

    @LayoutRes
    protected int LAYOUT = R.layout.dashboard_save_transaction_layout;

    public static final String TIMESTAMP_KEY = "timestamp.ky";
    public static final String TOTAL_KEY = "total.ky";

    private long timestamp;
    private long pennies;

    @BindView(R.id.id_fab)
    protected FloatingActionButton actionFab;

    @BindView(R.id.id_source_dialog_source_list)
    protected RecyclerView sourceList;

    @BindView(R.id.id_source_dialog_total_display)
    protected CurrencyEditText totalDisplay;

    @BindView(R.id.id_source_dialog_time_display)
    protected TextView timeDisplay;

    @BindView(R.id.id_source_dialog_transaction_name_editor)
    protected EditText transactionNameEditor;


    protected int transactionType = -1;

    private Unbinder unbinder;

    private Handler dataReceiver = new Handler(this);


    /**
     * Is passed into the MoneyController fetchSources method
     * to recieve a message containing a single Source. We use this source
     * to see if the source the user wants to make, is already in use.
     */
    private Handler sourceChecker = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if(msg.obj == null) {
                dismiss();
                notifyListener();
            }else{
                Toast.makeText(getContext(), R.string.string_title_error_in_use, Toast.LENGTH_SHORT)
                .show();
            }
            return true;
        }
    });

    protected SourceWrapper item;

    protected SourceWrapperAdapter sourceListAdapter;

    protected OnSourceItemSelected sourceItemSelectedListener;

    private AnimateScale animator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            timestamp = getArguments().getLong(TIMESTAMP_KEY);
            pennies = getArguments().getLong(TOTAL_KEY);
        }

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout = inflater.inflate(LAYOUT, container, false);

        this.unbinder = ButterKnife.bind(this, layout);

        int headerColor;

        if(Build.VERSION.SDK_INT >= 23)
            headerColor = getContext().getColor(HEADER_COLOR);
        else
            headerColor = getContext().getResources().getColor(HEADER_COLOR);

        layout.findViewById(R.id.id_source_dialog_header).setBackgroundColor(headerColor);

        totalDisplay.setText(String.valueOf(pennies));
        timeDisplay.setText(String.valueOf(PennyFlipTimeFormatter.simpleTime(Timestamp.from(timestamp))));

        sourceList.addOnScrollListener(scrollListener);

        final SourceWrapperAdapter adapter = manifestAdapter();
        adapter.registerOnClickListener(this);

        sourceList.setAdapter(adapter);

        actionFab.setImageResource(FAB_SRC);
        animator = new AnimateScale(actionFab, false);

        return layout;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        if(args.getLong(TIMESTAMP_KEY, -1L) == -1L)
            throw new IllegalArgumentException("Timestamp must be provided to the Source Dialog");

        if(args.getLong(TOTAL_KEY, -1L) == -1L)
            throw new IllegalArgumentException("A Total must be provided to the Source Dialog");
    }

    @Override //Handles the data fromm the backend
    public boolean handleMessage(Message msg) {

        //Convert msg.obj to usable data
        //Check if null
        //Convert List<Source> to AdapterSelecteable
        //Swap Data set of sourceListAdapter

        final List<Source> data = (List<Source>) msg.obj;

        if(data == null) return true;

        Log.i("SOURCE_DIALOG", "NUMBER OF RESULTS: " + data.size());

        final AdapterSelecteable<SourceWrapper> wrappers = new AdapterSelecteable<>();

        for(Source s: data)
            wrappers.add(new SourceWrapper(s, SourceWrapper.TAG.REGULAR));

        sourceListAdapter.swapDataSet(wrappers);

        return true;
    }

    @Override
    public void onSourceListItemChange(SourceWrapper sourceWrapper) {

        if(sourceWrapper == null & animator.isShowing())
            animator.hide(250L);

        else if(sourceWrapper != null & !animator.isShowing())
            animator.show(250L);

        this.item = sourceWrapper;
    }

    private void insertIntoFirst(final Source source){
        if(source != null)
            sourceListAdapter.insertToFirst(new SourceWrapper(source, SourceWrapper.TAG.NEW));
    }

    private void hideFab() {
        animator.hide(250L);
    }

    private void showFab(){
        animator.show(250L);
    }

    @OnClick(R.id.id_fab)
    protected void onDone(){

        if(item.getTag().equals(SourceWrapper.TAG.NEW))
             MoneyController.obtain(getContext()) //Attempt to fetch this source object to see if it exists
            .fetchSources(sourceChecker, item.getSourceId(), null, false);
        else {
            dismiss();
            notifyListener();
        }
    }

    private void populate(@Nullable final CharSequence popData){
        //Fetch Source data that matches the popData like clause

        //Null by default. If popData is null, we want everything for our transaction type
        String query = null;
        boolean searchLike = false;

        if(popData != null && popData.length() > 0) {
            query = "%" + popData.toString() + "%";
            searchLike = true;
        }

        //Will resolve either a list of Sources of our TransactionType only, or our TransactionType plus similar namings.
        MoneyController.obtain(getContext()).fetchSources(dataReceiver, query, transactionType, searchLike);
    }


    @CallSuper
    protected void notifyListener(){

        final Transaction transaction = new Transaction(item.getSourceId(), transactionType, pennies);

        String transName = transactionNameEditor.getText().toString();

        if( TextUtils.isEmpty(transName) )
            transName = getString(R.string.string_default_transaction_name);

        transaction.setTitle(transName);

        transaction.setTimestamp(timestamp);

        if(item.getTag().equals(SourceWrapper.TAG.NEW))
            item.getSource().setPennies(transaction.getAmount()); //Setting the initial amount

        if(sourceItemSelectedListener != null)
            sourceItemSelectedListener.onSourceItemSelected(item, transaction);
    }

    public void registerItemSelectedListener(final OnSourceItemSelected l){
        this.sourceItemSelectedListener = l;
    }

    @OnTextChanged(value = R.id.id_source_dialog_search_view, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onSearchEntered(final Editable editable){

        String s = editable.toString();

        if (s.length() == 0)
            populate(null);
        else
            populate(s);

        //Check if text is usable. If not, set S to equal ugly.

        if( s.replaceAll("\\s", "").length() == 0 || s.contains("%") )
            s = SourceWrapper.FLAG_NON_USABLE; //We don't allow sources to be identified by whitespaces, or %.

        //Make sure that insertIntoFirst isn't being overwritten by the loaded data from the backend.
        insertIntoFirst(new Source(s, transactionType));
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener(){

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            //If the item has been set, then we can do animations; Otherwise the fab is already hidden so no animations are needed
            if(item !=null)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) hideFab();
                else if (newState == RecyclerView.SCROLL_STATE_IDLE) showFab();
        }
    };


    @NonNull
    protected abstract SourceWrapperAdapter manifestAdapter();

}
