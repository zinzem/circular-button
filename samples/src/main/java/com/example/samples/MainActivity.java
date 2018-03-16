package com.example.samples;

import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.samples.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.getRoot().findViewById(R.id.circular_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.setActivated(!binding.getActivated());

                ValueAnimator valueAnimator = new ValueAnimator();
                valueAnimator.setIntValues(binding.getActivated() ? 0 : 255, binding.getActivated() ? 255: 0);
                valueAnimator.setDuration(500);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                binding.circularButton.animateText(valueAnimator);
            }
        });
    }
}
