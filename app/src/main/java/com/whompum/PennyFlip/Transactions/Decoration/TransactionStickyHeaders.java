package com.whompum.PennyFlip.Transactions.Decoration;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whompum.PennyFlip.R;

public class TransactionStickyHeaders extends RecyclerView.ItemDecoration {

    private View header;
    private RectF staticHeaderBounds = new RectF();

    private StickyData stickyData;

    private Subject zeroIndexHeaderSubject = new Subject(){

        @Override
        protected boolean collided(@NonNull RectF header, RectF ourBounds) {
            return header.contains( 0, ourBounds.bottom );
        }

        @Override
        public float getHeaderTop(@NonNull final RectF header, @NonNull final RectF ourBounds) {
            return ourBounds.bottom;
        }

        @Override
        protected boolean hasTraveledPastDragSlop(float dragSlop,
                                                  @NonNull final RectF header,
                                                  @NonNull final RectF ourBounds) {
            return ( header.top - ourBounds.bottom ) > dragSlop;
        }
    };

    private Subject firstIndexHeaderSubject = new Subject(){

        { isBlocked = false; }

        @Override
        protected boolean collided(@NonNull RectF header, RectF ourBounds) {
            return header.contains( 0, ourBounds.top );
        }

        @Override
        public float getHeaderTop(@NonNull final RectF header, @NonNull final RectF ourBounds) {
            return ourBounds.top - header.height();
        }

        @Override
        protected boolean hasTraveledPastDragSlop(float dragSlop,
                                                  @NonNull final RectF header,
                                                  @NonNull final RectF ourBounds) {
            return ( header.bottom + dragSlop ) < ourBounds.top;
        }

    };

    private boolean firstDrawPass = true;

    public TransactionStickyHeaders(final StickyData stickyData, @NonNull final ViewGroup container){
        this.stickyData = stickyData;

        makeHeader( container );

        header.setBackgroundResource( R.drawable.background_rounded_rect_right_dark );

    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        if( parent.getChildCount() < 2 ) //We can't use headers if there's no data to head...
            return;

        final View tempZeroChild = parent.getChildAt( 0 );
        final View tempFirstChild = parent.getChildAt( 1 );

        bind(parent, tempZeroChild);  //Fix redundant draw calls

        if( zeroIndexHeaderSubject.v != tempZeroChild && isHeader( parent, tempZeroChild ) ) {

            if( tempZeroChild.equals( firstIndexHeaderSubject.v ) )
                zeroIndexHeaderSubject.setReleasePoint();

            zeroIndexHeaderSubject.set( tempZeroChild );
        }

        if( firstIndexHeaderSubject.v != tempFirstChild && isHeader( parent, tempFirstChild ) ) {

            if( tempFirstChild.equals( zeroIndexHeaderSubject.v ) )
                firstIndexHeaderSubject.setReleasePoint();

            firstIndexHeaderSubject.set(tempFirstChild);
        }

        if( firstDrawPass ){
            firstDrawPass = false;
            tempZeroChild.setAlpha( 0F );
        }

        final int zeroIndexHeaderAdapterPos = getAdapterPosition( parent, zeroIndexHeaderSubject.v );

        if( zeroIndexHeaderAdapterPos == 0 )
            zeroIndexHeaderSubject.v.setAlpha( 0F );

        final boolean zeroIndexCollision =
                zeroIndexHeaderSubject.collision() && ( zeroIndexHeaderAdapterPos > 0 );

        final boolean firstIndexCollision =
                firstIndexHeaderSubject.collision();

        if( zeroIndexCollision || firstIndexCollision ){

            if( zeroIndexCollision )
                drawHeader( zeroIndexHeaderSubject.getHeaderTop() );

            else {
                zeroIndexHeaderSubject.isBlocked = false;
                drawHeader(firstIndexHeaderSubject.getHeaderTop());
            }

        } else
            resetHeader();

    }

    private void resetHeader(){
        drawHeader( staticHeaderBounds.top );
    }

    /**
     * Checks if the item is a header item or not
     * @return whether item is a header or not.
     */
    private boolean isHeader(final RecyclerView parent, final View child){
        return stickyData.isItemAHeader( getAdapterPosition( parent, child ) );
    }

    private void bind(@NonNull final RecyclerView parent, final View child){
        stickyData.bindHeader(header, getAdapterPosition(parent, child));
    }

    //Returns the adapter position of the on-screen item
    private int getAdapterPosition(@NonNull final RecyclerView parent, final View child){
        return parent.getChildAdapterPosition(child);
    }

    private void makeHeader(@NonNull final ViewGroup container){

        final View header = LayoutInflater
                .from( container.getContext() )
                .inflate( R.layout.transaction_list_dynamic_header, container, false );

        final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final int headerMarinTop = container
                .getContext()
                .getResources()
                .getDimensionPixelOffset( R.dimen.dimen_padding_ver_large ) / 2 ;

        params.setMargins(
            0,
            headerMarinTop,
            0,
            0
        );

        header.setLayoutParams( params );

        container.addView( header );

        header.post(new Runnable() {
            @Override
            public void run() {

                staticHeaderBounds.set(
                        header.getLeft(),
                        header.getTop(),
                        header.getRight(),
                        header.getBottom()
                );

            }
        });

        this.header = header;
    }

    /**
     * Draw a header, with an optional translation.
     * The deltaY is the new Top value of the new Header.
     *
     * @param headerTop The Y of the new Header
     */
    private void drawHeader(final float headerTop ){
        header.setY( headerTop );
        header.invalidate();
    }


    public void setHeaderClickListener(@NonNull final View.OnClickListener listener){
        header.findViewById( R.id.id_global_timestamp ).setOnClickListener( listener );
    }

    private abstract class Subject{

        //Distance dragged before it can acquire focus again
        public static final float DRAG_SLOP = 5F;

        private View v;
        private RectF staticBounds = new RectF();
        private RectF movableBounds = new RectF();

        protected boolean isBlocked = true;   //Disallows collision detection until conditions are meet.

        private boolean releasedAndWaiting = false;

        private void set(@NonNull final View v){

            this.v = v;

            staticBounds.set(
                    v.getLeft(),
                    v.getTop(),
                    v.getRight(),
                    v.getBottom()
            );

        }

        private boolean collision(){
            if( v == null || isBlocked )
                return false;

            movableBounds.set(
                    0,
                    v.getTop(),
                    0,
                    v.getBottom()
            );

            if( releasedAndWaiting ){

                if( !hasTraveledPastDragSlop( DRAG_SLOP, staticHeaderBounds, movableBounds ) )
                    return false;
                else
                    releasedAndWaiting = false;
            }

            return collided( staticHeaderBounds, movableBounds );
        }

        public float getHeaderTop(){
            return getHeaderTop( staticHeaderBounds, movableBounds );
        }


        protected abstract boolean collided(@NonNull final RectF header, @NonNull final RectF ourBounds);

        public abstract float getHeaderTop(@NonNull final RectF header, @NonNull final RectF ourBounds);

        protected abstract boolean hasTraveledPastDragSlop(final float dragSlop, @NonNull final RectF header,
                                                           @NonNull final RectF ourBounds);

        public void setReleasePoint(){
            releasedAndWaiting = true;
        }

    }

    public interface StickyData {
        boolean isItemAHeader(final int position);
        void bindHeader(final View header, final int adapterPos);
    }


}