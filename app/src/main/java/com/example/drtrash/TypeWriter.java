package com.example.drtrash;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import android.os.Handler;

public class TypeWriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long delay = 100;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private Handler mHandler = new Handler();

    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {

            setText(mText.subSequence(0, mIndex++));

            if (mIndex <= mText.length()) {

                mHandler.postDelayed(characterAdder, delay);

            }


        }
    };

    public void animateText(CharSequence txt) {

        mText = txt;
        mIndex = 0;
        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, delay);

    }

    public void setCharacterDelay(long m) {
        delay = m;

    }

}
