package ai.advance.liveness.sdk.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import ai.advance.liveness.sdk.R;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
    }
}
