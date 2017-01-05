package com.gaok.ui.svgtrace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gaok.ui.R;

public class SVGTraceActivity extends AppCompatActivity {
    private SVGTraceView mSVGTraceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_trace);
        mSVGTraceView = (SVGTraceView) findViewById(R.id.view_svg_trace);
        mSVGTraceView.startAnimate();
    }
}
