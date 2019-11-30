package com.weiyun.liveness;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class WyVerticalTextView extends TextView {
    final boolean topDown;

    public WyVerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int gravity = this.getGravity();
        if (Gravity.isVertical(gravity) && (gravity & 112) == 80) {
            this.setGravity(gravity & 7 | 48);
            this.topDown = false;
        } else {
            this.topDown = true;
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        this.setMeasuredDimension(this.getMeasuredHeight(), this.getMeasuredWidth());
    }

    protected boolean setFrame(int l, int t, int r, int b) {
        return super.setFrame(l, t, l + (b - t), t + (r - l));
    }

    public void draw(Canvas canvas) {
        if (this.topDown) {
            canvas.translate((float) this.getHeight(), 0.0F);
            canvas.rotate(90.0F);
        } else {
            canvas.translate(0.0F, (float) this.getWidth());
            canvas.rotate(-90.0F);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipRect(0.0F, 0.0F, (float) this.getWidth(), (float) this.getHeight());
        } else {
            canvas.clipRect(0.0F, 0.0F, (float) this.getWidth(), (float) this.getHeight(), Region.Op.REPLACE);
        }
        super.draw(canvas);
    }
}
