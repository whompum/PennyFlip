package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * MoneyQuery represents the search params to fetch the persistent data we want.
 * It maintains a set of Key-Value pairs in a {@link Map}.
 * Which is populated with a pre-defined set of key constants via a {@link java.util.Set} implementation.
 * {@link QueryBuilder} populates the values of the MoneyQuery.
 *
 * This class relies on two actors to create a QueryParameter.
 * Firstly it uses a QueryKeys object which states all possible Keys it can have.
 * Then, it relies on its Builder object to populate its keys with values.
 *
 */
public final class MoneyQuery {

    public static final int RESERVED_KEY_IS_EMPTY = -1; //RESERVED EMPTY-FLAG VALUE

    private Map<Integer, QueryParameter> queryItems;
    private Set<Integer> keys;

    private MoneyQuery(){
        queryItems = new HashMap<>();

        this.keys = new HashSet<>();
        keys.add(RESERVED_KEY_IS_EMPTY);

        //is empty by default
        queryItems.put(RESERVED_KEY_IS_EMPTY, new QueryParameter<>(true));
    }

    private MoneyQuery(@NonNull final Set<Integer> keys){
        this();
        appendQuerykeys(keys);
    }


    private void setIsQueryEmpty(boolean isQueryEmpty) {
        queryItems.put(RESERVED_KEY_IS_EMPTY, new QueryParameter<>(isQueryEmpty));
    }

    public int getQueryKeysCount(){
        return keys.size();
    }

    public int getQueryParametersCount(){return queryItems.size(); }

    public boolean containsNonNullQueryAtKey(@NonNull final Integer key){
        return (hasQueryKey(key) &&
                queryItems.get(key) != null &&
                queryItems.get(key).get() != null
               );
    }

    public boolean hasQueryKey(@NonNull final Integer key){
        return keys.contains(key);
    }

    public <T> boolean isQueryOfType(final Class cls, final int key){
        return getQueryParameter(key).get().getClass().equals(cls);
    }

    private void setQueryParameter(@NonNull final Integer key, @NonNull final QueryParameter queryParameter){
        if(key == RESERVED_KEY_IS_EMPTY) return; //Possibly throw an Exception?

        queryItems.put(key, queryParameter);
    }

    private void appendQuerykeys(@NonNull final Set<Integer> queryKeys){

        final Iterator<Integer> keysIterator = queryKeys.iterator();

        while(keysIterator.hasNext()) {
            final Integer key = keysIterator.next();

            if( !hasQueryKey(key) ) {
                keys.add(key);
                setQueryParameter(key, new QueryParameter<>(null));
            }
        }
    }

    @Nullable
    public QueryParameter getQueryParameter(@NonNull final Integer key){

        if( !containsNonNullQueryAtKey(key) )
            return null;

    return queryItems.get(key);
    }

    public Set<Integer> getQuerykeys(){
        return keys;
    }

    /**
     * Populate the {@link MoneyQuery}'s indices, as provided by {@link Set}
     * with actual searching values
     */
    public static class QueryBuilder {

        //The MoneyQuery object returned by this actor
        protected MoneyQuery queryObject;

        /**
         * Create a basic QueryBuilder object
         * that could be used to populate values of the {@link MoneyQuery}
         *
         * @param keys All available search parameter for the {@link MoneyQuery}.
         *             {@link Set} don't represent all available data, but rather,
         *             describes all possible search parameters. Regardless of data availability.
         */
        protected QueryBuilder(@NonNull final HashSet<Integer> keys) {
            queryObject = new MoneyQuery(keys);
        }

        /**
         * Determines if the {@link MoneyQuery} is an empty query object.
         * By default it is, so a subclass of this object should override this method
         * to, use its own algorithm to determine if the {@link MoneyQuery} is a basic minimum
         * needs query to read from a DAO capable of supporting {@link MoneyQuery}'s.
         *
         * The default implementation of this object simply checks if the
         * {@link MoneyQuery} contains at least one non-null fields.
         *
         * NOTE: If null values are permissible this method should be overriden to specify that.
         *
         * @return whether an actor on the {@link MoneyQuery} should use a default query or not.
         */
        protected boolean isEmpty(){

            final Iterator<Integer> keys = queryObject.getQuerykeys().iterator();

            while(keys.hasNext()){

                final Integer key = keys.next();

                if(key == RESERVED_KEY_IS_EMPTY)
                    continue;

                if( queryObject.containsNonNullQueryAtKey(key) )
                    return false;

            }

            return true;
        }

        public <T> QueryBuilder setQueryParameter(@NonNull final Integer key,
                                                  @NonNull final T value){

            if(queryObject.hasQueryKey(key))
                queryObject.setQueryParameter(key, new QueryParameter<>(value));

            return this;
        }

        public MoneyQuery getQuery(){
            queryObject.setIsQueryEmpty(isEmpty());
            return queryObject;
        }

    }

}
