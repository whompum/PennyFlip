package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

public abstract class StatisticsReportItemView extends FrameLayout {

    public StatisticsReportItemView(Context context, AttributeSet attrs) {
        super( context, attrs );

        inflate( context, getLayout(), this );

        final TypedArray array = context.obtainStyledAttributes( attrs, R.styleable.StatisticsReportItemView );

        init( array );
    }

    private void init(@NonNull final TypedArray array){

        String txt = null;

        if( array.getIndexCount() >= 1 && array.getIndex( 0 ) == R.styleable.StatisticsReportItemView_staticText )
            txt = array.getString( array.getIndex( 0 ) );

        if( txt != null )
            setLabel( txt );

        array.recycle();

    }

    public void setLabel(@StringRes final int resId){
        getLabel().setText( resId );
    }

    public void setLabel(@NonNull final String txt){
        getLabel().setText( txt );
    }

    public TextView getLabel(){
        return (TextView) findViewById( R.id.id_global_title );
    }

    @LayoutRes
    protected abstract int getLayout();

}
