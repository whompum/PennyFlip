package com.whompum.PennyFlip.Money.Queries;

import android.support.annotation.NonNull;

import com.whompum.PennyFlip.Money.Queries.Query.MoneyRequest;


/**
 * Client-facing interface to the underlying money data.
 * This interface will be implemented by parties interested in
 * speaking to, or, literally providing the data requested.
 * Typically, this interface is implemented by an object that can
 * map {@link MoneyRequest} objects to literal method invocations providing that data.
 *
 * Note that this data is also for one-use data only meaning no observable functionality
 * is provided.
 */
public interface MoneyReader {

    void query(@NonNull final MoneyRequest query);

}
