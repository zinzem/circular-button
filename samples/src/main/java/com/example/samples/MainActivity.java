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

                if (!binding.getActivated()) {
                    ValueAnimator valueAnimator = new ValueAnimator();
                    valueAnimator.setIntValues(255, 0);
                    valueAnimator.setDuration(500);
                    valueAnimator.setInterpolator(new DecelerateInterpolator());
                    binding.circularButton.setIconAlpha(0);
                    ValueAnimator valueAnimator2 = new ValueAnimator();
                    valueAnimator2.setIntValues(0, 255);
                    valueAnimator2.setDuration(500);
                    valueAnimator2.setInterpolator(new DecelerateInterpolator());
                    binding.circularButton.setText("lol", valueAnimator2);
                } else {
                    ValueAnimator valueAnimator = new ValueAnimator();
                    valueAnimator.setIntValues(0, 255);
                    valueAnimator.setDuration(500);
                    valueAnimator.setInterpolator(new DecelerateInterpolator());
                    binding.circularButton.setIconAlpha(255);
                    ValueAnimator valueAnimator2 = new ValueAnimator();
                    valueAnimator2.setIntValues(255, 0);
                    valueAnimator2.setDuration(500);
                    valueAnimator2.setInterpolator(new DecelerateInterpolator());
                    binding.circularButton.animateTextAlpha(valueAnimator2);
                }

                binding.setActivated(!binding.getActivated());
            }
        });
    }
}
