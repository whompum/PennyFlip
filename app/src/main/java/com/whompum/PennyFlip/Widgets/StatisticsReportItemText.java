package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

public class StatisticsReportItemText extends StatisticsReportItemView {

    public StatisticsReportItemText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    @Override
    protected int getLayout() {
        return R.layout.statistics_report_data_item_text;
    }

    public void setValue(@StringRes final int value){
        setValue( String.valueOf( value ) );
    }

    public void setValue(final String value){
        getValueView().setText( value );
    }

    private TextView getValueView(){
        return findViewById( R.id.id_local_label );
    }

}
