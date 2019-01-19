package com.whompum.PennyFlip.ActivityStatistics.Data;

import com.whompum.PennyFlip.Money.Statistics.TransactionStatistics;
import com.whompum.PennyFlip.Money.Transaction.Transaction;
import com.whompum.PennyFlip.Money.Transaction.TransactionType;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionStatisticsTest {

    private TransactionStatistics sut;

    @Before
    public void setUp() throws Exception {

        sut = new TransactionStatistics(
                TransactionType.ADD,
                5000L, //5 dollars
                5,
                1000,
                100D,
                null
        );

    }

    private Transaction stubTransaction = new Transaction(1,
            "test", System.currentTimeMillis(),
            2872L, TransactionType.ADD, "SourceTitleTest" );

    @Test
    public void updateWithTransaction_properNetAmount(){

        final long testNetAmount = sut.getNetAmount();
        final long updateNetAmount = stubTransaction.getAmount();

        sut.updateFromTransaction( stubTransaction );

        assertEquals( "Poor Net Amount", sut.getNetAmount(), (testNetAmount+updateNetAmount)  );

    }

    @Test
    public void updateWithTransaction_properTransactionAverage(){

        sut.updateFromTransaction( stubTransaction );

        final long netAmount = sut.getNetAmount();
        final long numTrans = sut.getNumTransactions();

        assertEquals( "Poor transaction average", sut.getTransactionAverage(), netAmount / numTrans );
    }


}