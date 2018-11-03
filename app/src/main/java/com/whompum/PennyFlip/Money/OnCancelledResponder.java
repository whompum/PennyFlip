package com.whompum.PennyFlip.Money;

public interface OnCancelledResponder {
    void onCancelledResponse(final int reason, final String msg);
}
