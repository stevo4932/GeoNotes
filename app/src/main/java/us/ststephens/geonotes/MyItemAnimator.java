package us.ststephens.geonotes;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

/**
 * Created by stevo4932 on 8/20/17.
 */

public class MyItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Log.d("Notes", "Holder removed");
        return super.animateRemove(holder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.d("Notes", "Holder add");
        return super.animateAdd(holder);
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.d("Notes", "Holder move");
        return super.animateMove(holder, fromX, fromY, toX, toY);
    }

    @Override
    public boolean animateChange(final RecyclerView.ViewHolder oldHolder, final RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        int oldViewHeight = oldHolder.itemView.getHeight();
        int newViewHeight = newHolder.itemView.getHeight();
        final View view = newHolder.itemView;
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(oldViewHeight, newViewHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                view.getLayoutParams().height = val;
            }
        });
        valueAnimator.setDuration(getChangeDuration());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                dispatchAnimationFinished(oldHolder);
                dispatchChangeStarting(newHolder, false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                dispatchChangeFinished(newHolder,false);
                dispatchAnimationFinished(newHolder);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
//        final View view = oldHolder == null ? null : oldHolder.itemView;
//        final View newView = newHolder != null ? newHolder.itemView : null;
//        Log.d("Notes", "From Y: " + fromY + " To Y: " + toY);
//        if (view != null) {
//            final ViewPropertyAnimatorCompat oldViewAnim = ViewCompat.animate(view).setDuration(getChangeDuration());
//            oldViewAnim.translationY(toY - fromY);
//            oldViewAnim.setListener(new ViewPropertyAnimatorListener() {
//                @Override
//                public void onAnimationStart(View view) {
//                    dispatchChangeStarting(oldHolder, true);
//                }
//
//                @Override
//                public void onAnimationEnd(View view) {
//                    oldViewAnim.setListener(null);
//                    ViewCompat.setTranslationY(view, 0);
//                    dispatchChangeFinished(oldHolder, true);
//                }
//
//                @Override
//                public void onAnimationCancel(View view) {
//
//                }
//            }).start();
//        }
//        if (newView != null) {
//            final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(newView);
//            newViewAnimation.translationY(0).setDuration(getChangeDuration())
//                    .setListener(new ViewPropertyAnimatorListener() {
//                         @Override
//                         public void onAnimationStart(View view) {
//                             dispatchChangeStarting(newHolder, false);
//                         }
//
//                         @Override
//                         public void onAnimationEnd(View view) {
//                             newViewAnimation.setListener(null);
//                             ViewCompat.setTranslationY(newView, 0);
//                             dispatchChangeFinished(newHolder, false);
//                         }
//
//                         @Override
//                         public void onAnimationCancel(View view) {
//
//                         }
//                     }).start();
//        }
        return true;
    }
}
