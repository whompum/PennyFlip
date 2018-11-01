package com.whompum.PennyFlip.Money.Queries.Query;

import com.whompum.PennyFlip.Money.Transaction.TransactionQueryKeys;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


/**
 * ASSERTIONS:
 *
 * -@ initial creation, a isEmpty query value is not null
 * -@ initial creation, a isEmpty query value is either true or false
 * -@ Initial Creation, the parameters count is equal to the count of provided keys +1 (for isEmpty)
 * -After adding keys, they're in the MoneyRequest with null values
 * -After adding values, they value @ key is populated
 */
public class MoneyRequestTest {

    private MoneyRequest initialSut;
    private int keyCount = TransactionQueryKeys.KEYS.size();


    @Before
    public void setup(){

        initialSut = new MoneyRequest.QueryBuilder(TransactionQueryKeys.KEYS).getQuery();

    }

    @Test
    public void isEmptyQueryInitialization_NotNull_True(){
        //Construct a new MoneyRequest object using the Abstract Query
        assertNotNull( initialSut.getQueryParameter(MoneyRequest.RESERVED_KEY_IS_EMPTY) );
    }

    /**
     *
     * Test done on the {@link MoneyRequest.QueryBuilder} class
     */
    @Test //Tests if
    public void isEmptyQueryInitialization_noParametersIsEmpty_true(){

        final QueryParameter<Boolean> isEmpty =
                new QueryParameter<>( (Boolean)initialSut.getQueryParameter(MoneyRequest.RESERVED_KEY_IS_EMPTY).get() );

        assertTrue( isEmpty.get() );

    }

    @Test
    public void isEmptyQueryInitialization_noKeysIsEmpty_true(){

        final MoneyRequest q = new MoneyRequest.QueryBuilder(new HashSet<Integer>()).getQuery();

        final QueryParameter<Boolean> isEmpty =
                new QueryParameter<>( (Boolean) q.getQueryParameter(MoneyRequest.RESERVED_KEY_IS_EMPTY).get() );

        assertTrue( isEmpty.get() );

    }


    @Test
    public void queryInitialization_parameterCount_keysSizePlusOne(){

        final int paramCount = initialSut.getQueryParametersCount();

        assertThat(paramCount, is((keyCount+1)));
    }

    @Test
    public void queryParameters_setValue_containsNonNullValue(){

       final MoneyRequest query = new MoneyRequest.QueryBuilder(TransactionQueryKeys.KEYS)
                .setQueryParameter(TransactionQueryKeys.SOURCE_ID, new QueryParameter<>("TestTitle"))
               .getQuery();

        assertTrue( query.containsNonNullQueryAtKey(TransactionQueryKeys.SOURCE_ID) );
    }

    @Test
    public void queryParameters_noValueSet_containsNullValue(){

        final MoneyRequest query = new MoneyRequest.QueryBuilder(TransactionQueryKeys.KEYS)
                .getQuery();

        assertFalse( query.containsNonNullQueryAtKey(TransactionQueryKeys.PENNY_VALUE) );
    }

}