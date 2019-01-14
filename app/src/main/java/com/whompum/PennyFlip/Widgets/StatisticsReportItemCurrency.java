package com.whompum.PennyFlip.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.whompum.PennyFlip.R;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

public class StatisticsReportItemCurrency extends StatisticsReportItemView {

    public StatisticsReportItemCurrency(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayout() {
        return R.layout.statistics_reported_data_item_currency;
    }

    public void setCurrency(final long pennies){
        setCurrency( String.valueOf( pennies ) );
    }

    public void setCurrency(final String cash){
        getCurrencyView().setText( cash );
    }

    private CurrencyEditText getCurrencyView(){
        return findViewById( R.id.id_global_total_display );
    }

}
