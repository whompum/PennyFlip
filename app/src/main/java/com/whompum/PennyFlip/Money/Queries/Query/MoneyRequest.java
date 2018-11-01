package com.whompum.PennyFlip.Money.Queries.Query;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * MoneyRequest represents the search params to fetch the persistent data we want.
 * It maintains a set of Key-Value pairs in a {@link Map}.
 * Which is populated with a pre-defined set of key constants via a {@link java.util.Set} implementation.
 * {@link QueryBuilder} populates the values of the MoneyRequest.
 *
 * This class relies on two actors to create a QueryParameter.
 * Firstly it uses a QueryKeys object which states all possible Keys it can have.
 * Then, it relies on its Builder object to populate its keys with values.
 *
 */
public final class MoneyRequest {

    public static final int RESERVED_KEY_IS_EMPTY = -1; //RESERVED EMPTY-FLAG VALUE
    public static final int RESERVED_KEY_IS_OBSERVABLE = -2;

    private Map<Integer, QueryParameter> queryItems;
    private Set<Integer> keys;

    private MoneyRequest(){
        queryItems = new HashMap<>();

        this.keys = new HashSet<>();
        keys.add(RESERVED_KEY_IS_EMPTY);
        keys.add(RESERVED_KEY_IS_OBSERVABLE);

        //is empty by default
        setIsQueryEmpty(true);
        //Not observable by default
        setIsQueryObservable(false);
    }

    private MoneyRequest(@NonNull final Set<Integer> keys){
        this();
        this.keys.addAll(keys);
    }


    private void setIsQueryEmpty(boolean isQueryEmpty) {
        queryItems.put(RESERVED_KEY_IS_EMPTY, new QueryParameter<>(isQueryEmpty));
    }

    private void setIsQueryObservable(final boolean isQueryObservable){
        queryItems.put(RESERVED_KEY_IS_OBSERVABLE, new QueryParameter<>(isQueryObservable));
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
        if(key == RESERVED_KEY_IS_EMPTY || key == RESERVED_KEY_IS_OBSERVABLE) return; //Possibly throw an Exception?

        queryItems.put(key, queryParameter);
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
     * Populate the {@link MoneyRequest}'s indices, as provided by {@link Set}
     * with actual searching values
     */
    public static class QueryBuilder {

        //The MoneyRequest object returned by this actor
        protected MoneyRequest queryObject;

        /**
         * Create a basic QueryBuilder object
         * that could be used to populate values of the {@link MoneyRequest}
         *
         * @param keys All available search parameter for the {@link MoneyRequest}.
         *             {@link Set} don't represent all available data, but rather,
         *             describes all possible search parameters. Regardless of data availability.
         */
        public QueryBuilder(@NonNull final HashSet<Integer> keys) {
            queryObject = new MoneyRequest(keys);
        }

        /**
         * Determines if the {@link MoneyRequest} is an empty query object.
         * By default it is, so a subclass of this object should override this method
         * to, use its own algorithm to determine if the {@link MoneyRequest} is a basic minimum
         * needs query to read from a DAO capable of supporting {@link MoneyRequest}'s.
         *
         * The default implementation of this object simply checks if the
         * {@link MoneyRequest} contains at least one non-null fields.
         *
         * NOTE: If null values are permissible this method should be overriden to specify that.
         *
         * @return whether an actor on the {@link MoneyRequest} should use a default query or not.
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

        public QueryBuilder toggleObservable(){
            queryObject.setIsQueryObservable( true );

            return this;
        }

        public <T> QueryBuilder setQueryParameter(@NonNull final Integer key,
                                                  @NonNull final T value){

            if(queryObject.hasQueryKey(key) &&
                    key != RESERVED_KEY_IS_EMPTY &&
                    key != RESERVED_KEY_IS_OBSERVABLE)
                queryObject.setQueryParameter(key, new QueryParameter<>(value));

            return this;
        }

        public MoneyRequest getQuery(){
            queryObject.setIsQueryEmpty(isEmpty());
            return queryObject;
        }

    }

}
