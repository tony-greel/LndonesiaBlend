package com.example.lndonesiablend.activity.submission;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lndonesiablend.R;
import com.example.lndonesiablend.service.UpAppService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubmissionLoanActivity extends AppCompatActivity {

    @BindView(R.id.tv_id)
    TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_loan);
        ButterKnife.bind(this);

        startService(new Intent(this, UpAppService.class));
    }
}
