package com.whompum.PennyFlip.ActivityDashboard;

import com.whompum.PennyFlip.DialogSourceChooser.SourceWrapper;

public interface ActivityDashboardConsumer{
    void saveTransaction(final int type, final long amt, final SourceWrapper source);
}
