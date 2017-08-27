package us.ststephens.geonotes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class ExpandItemAnimator extends DefaultItemAnimator {
    private Interpolator interpolator = new DecelerateInterpolator();

    @Override
    public boolean animateChange(@NonNull final RecyclerView.ViewHolder oldHolder, @NonNull final RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {

        final int oldHeight = oldHolder.itemView.getHeight();
        final int newHeight = newHolder.itemView.getHeight();

        if (oldHolder == newHolder || oldHeight == newHeight) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
        }

        final View view = newHolder.itemView;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(oldHeight, newHeight);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(getChangeDuration());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                dispatchAnimationFinished(oldHolder);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.getLayoutParams().height = RecyclerView.LayoutParams.WRAP_CONTENT;
                dispatchAnimationFinished(newHolder);
            }
        });
        valueAnimator.start();
        return true;
    }
}
