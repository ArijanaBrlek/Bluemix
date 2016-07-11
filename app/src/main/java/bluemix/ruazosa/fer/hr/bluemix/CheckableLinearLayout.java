package bluemix.ruazosa.fer.hr.bluemix;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by arijana on 7/3/16.
 */
public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private boolean mChecked = false;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean b) {
        if (b != mChecked) {
            mChecked = b;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

}
