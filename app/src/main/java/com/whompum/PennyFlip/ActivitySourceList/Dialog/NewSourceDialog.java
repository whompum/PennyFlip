package com.whompum.PennyFlip.ActivitySourceList.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.whompum.PennyFlip.Money.Source.Source;
import com.whompum.PennyFlip.R;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

public class NewSourceDialog extends Dialog implements View.OnClickListener {

    @LayoutRes
    public static final int DIALOG_LAYOUT = R.layout.layout_new_source_dialog;

    private TextView counterDisplay;
    private EditText sourceEditor;
    private TextView errorDisplay;

    private View content;

    private OnSourceCreated client;

    private int transactionType;


    public NewSourceDialog(@NonNull Context context,
                           @Nullable final OnSourceCreated client,
                           final int transactionType) {
        super(context, R.style.StyleDialogAnimate);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams windowParams = null;

        if(getWindow() != null)
            if((windowParams = getWindow().getAttributes()) != null )
                windowParams.windowAnimations = R.style.StyleDialogAnimate;

        final View content = LayoutInflater.from(context)
                .inflate(DIALOG_LAYOUT, null, false);

        //Sets the icon of the Dialogs content

        int icon = -1;

        if(transactionType == TransactionType.ADD) icon = R.drawable.graphic_source_plus;
        if(transactionType == TransactionType.SPEND) icon = R.drawable.graphic_source_minus;

        ((ImageView) content.findViewById(R.id.id_source_type_display)).setImageResource(icon);

        content.findViewById(R.id.id_dialog_done).setOnClickListener(this);

        content.findViewById(R.id.id_dialog_cancel).setOnClickListener(this);

        this.counterDisplay = content.findViewById(R.id.id_text_limit_display);
             counterDisplay.setText("0/20");

        this.sourceEditor = content.findViewById(R.id.id_source_name_editor);
             sourceEditor.addTextChangedListener(new TextLimitWatcher(counterDisplay));

        this.errorDisplay = content.findViewById(R.id.id_title_error);

        setContentView(content); // Must be called before i set the dialog width

        if(windowParams != null)
        {

            final DisplayMetrics m = new DisplayMetrics();

            getWindow().getWindowManager().getDefaultDisplay().getMetrics(m);

            final int w = m.widthPixels; // Subtract padding

            windowParams.width = w;
            windowParams.dimAmount = .7F;
        }

        if(windowParams!= null)
            windowParams.windowAnimations = R.style.StyleDialogAnimate;

        if(client != null)
            this.client = client;

        this.content = content;
        this.transactionType = transactionType;

    }

    @Override
    public void onClick(View v) {

        if( v.getId() == R.id.id_dialog_cancel ) dismiss();

        final String title = sourceEditor.getText().toString();

        if( v.getId() == R.id.id_dialog_done )
            if( canUseTitle( title ) && client != null )
                 client.onSourceCreated( new Source( title, transactionType  ) );
    }


    public boolean canUseTitle(@NonNull String potentialTitle) {
        if( potentialTitle.replaceAll( "\\s", "" ).length() == 0 ) {
            onTitleError( R.string.string_title_error_empty );
            return false;
        }

        if( potentialTitle.contains("%") ){
            onTitleError( R.string.string_title_error_poor_format );
            return false;
        }

        return true;
    }

    public void onTitleError(@StringRes final int titleErrorRes){
        errorDisplay.setText( titleErrorRes );

        errorDisplay.setAlpha( 1F );//IDK why but this is needed
        errorDisplay.startAnimation( AnimationUtils.loadAnimation( getContext(), R.anim.fade_in ) );

        content.startAnimation( AnimationUtils.loadAnimation( getContext(), R.anim.shake ) );
    }

}
