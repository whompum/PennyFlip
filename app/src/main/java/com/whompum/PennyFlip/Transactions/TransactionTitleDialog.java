package com.whompum.PennyFlip.Transactions;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.whompum.PennyFlip.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;

public class TransactionTitleDialog extends Dialog {

    @LayoutRes
    public static final int LAYOUT = R.layout.transaction_name_dialog;

    protected String defaultName;

    protected EditText titleEditor;

    private OnTitleListener client;

    public TransactionTitleDialog(@NonNull Context context, @Nullable final OnTitleListener titleListener) {
        super(context, R.style.StyleDialogAnimate);
        if(titleListener != null) client = titleListener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCancelable(false);

        defaultName = context.getString(R.string.string_default_transaction_name);

        final View view = LayoutInflater.from(context).inflate(LAYOUT, null);

        this.titleEditor = view.findViewById(R.id.transaction_title_editor);

        view.findViewById(R.id.id_done).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                done();
            }
        });

        view.findViewById(R.id.id_skip).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                skip();
            }
        });

        setContentView(view);

        if(getWindow() != null)
            if(getWindow().getAttributes() != null)
                getWindow().getAttributes().windowAnimations = R.style.StyleDialogAnimate;

        show();
    }

    public void skip(){
        finish(defaultName);
    }

    public void done(){ //TODO set a name character limit for  the transaction titles.
        if(titleEditor.getText() != null)
            if(!titleEditor.getText().toString().isEmpty())
                finish(titleEditor.getText().toString());
        else
            finish(defaultName);

    }

    private void finish(@NonNull final String name){
        client.onTitleSet(name);
        dismiss();
    }


}
