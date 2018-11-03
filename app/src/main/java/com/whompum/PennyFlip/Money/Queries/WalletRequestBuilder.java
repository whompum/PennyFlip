package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;

import java.util.HashSet;

public class WalletRequestBuilder extends MoneyRequest.QueryBuilder {

    public WalletRequestBuilder() {
        super(new HashSet<Integer>());
    }



}
