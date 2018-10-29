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
 * -After adding keys, they're in the MoneyQuery with null values
 * -After adding values, they value @ key is populated
 */
public class MoneyQueryTest {

    private MoneyQuery initialSut;
    private int keyCount = TransactionQueryKeys.KEYS.size();


    @Before
    public void setup(){

        initialSut = new MoneyQuery.QueryBuilder(TransactionQueryKeys.KEYS).getQuery();

    }

    @Test
    public void isEmptyQueryInitialization_NotNull_True(){
        //Construct a new MoneyQuery object using the Abstract Query
        assertNotNull( initialSut.getQueryParameter(MoneyQuery.RESERVED_KEY_IS_EMPTY) );
    }

    /**
     *
     * Test done on the {@link MoneyQuery.QueryBuilder} class
     */
    @Test //Tests if
    public void isEmptyQueryInitialization_noParametersIsEmpty_true(){

        final QueryParameter<Boolean> isEmpty =
                new QueryParameter<>( (Boolean)initialSut.getQueryParameter(MoneyQuery.RESERVED_KEY_IS_EMPTY).get() );

        assertTrue( isEmpty.get() );

    }

    @Test
    public void isEmptyQueryInitialization_noKeysIsEmpty_true(){

        final MoneyQuery q = new MoneyQuery.QueryBuilder(new HashSet<Integer>()).getQuery();

        final QueryParameter<Boolean> isEmpty =
                new QueryParameter<>( (Boolean) q.getQueryParameter(MoneyQuery.RESERVED_KEY_IS_EMPTY).get() );

        assertTrue( isEmpty.get() );

    }


    @Test
    public void queryInitialization_parameterCount_keysSizePlusOne(){

        final int paramCount = initialSut.getQueryParametersCount();

        assertThat(paramCount, is((keyCount+1)));
    }

    @Test
    public void queryParameters_setValue_containsNonNullValue(){

       final MoneyQuery query = new MoneyQuery.QueryBuilder(TransactionQueryKeys.KEYS)
                .setQueryParameter(TransactionQueryKeys.SOURCE_ID, new QueryParameter<>("TestTitle"))
               .getQuery();

        assertTrue( query.containsNonNullQueryAtKey(TransactionQueryKeys.SOURCE_ID) );
    }

    @Test
    public void queryParameters_noValueSet_containsNullValue(){

        final MoneyQuery query = new MoneyQuery.QueryBuilder(TransactionQueryKeys.KEYS)
                .getQuery();

        assertFalse( query.containsNonNullQueryAtKey(TransactionQueryKeys.PENNY_VALUE) );
    }

}