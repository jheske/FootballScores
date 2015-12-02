package barqsoft.footballscores.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import barqsoft.footballscores.activities.MainActivityFragment;

/****************************************************************************
 * The MovieRecyclerTouchListener class sets up the RecyclerView's gesture
 * management. It intercepts and handles user clicks on grid items
 */
public class ScoresRVTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector mGestureDetector;
    private MainActivityFragment.ScoresClickListener mClickListener;

    /**
     * Set up Simple listener to detect singleTapUp .  I can add
     * additional gestures later, like onLongPress, if I want.
     */
    public ScoresRVTouchListener(Context context, RecyclerView recyclerView,
                                 MainActivityFragment.ScoresClickListener clickListener) {
        mClickListener = clickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //True = this view has handled the event so
                //it won't be propagated any farther.
                return true;
            }
        });
    }

    /**
     * Required method called only for ViewGroups (like RecyclerView),
     * not for plain Views (like TextViews).
     * Handle the RecyclerView's grid item click event here.
     *
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mClickListener.onClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    /**
     * Required method called on View where very first touch occurs.
     *
     */
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    /**
     * Required method called when a child of RecyclerView does not want
     * RecyclerView and its ancestors to intercept touch events.
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}