package com.whompum.PennyFlip.ActivityDashboard;

import com.whompum.PennyFlip.Source.SourceWrapper;

public interface ActivityDashboardConsumer{
    void saveTransaction(final int type, final long amt, final SourceWrapper source);
}
