package com.github.okbuilds.okbuck.example;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.example.hellojni.HelloJni;
import com.github.okbuilds.okbuck.example.common.Calc;
import com.github.okbuilds.okbuck.example.common.CalcMonitor;
import com.github.okbuilds.okbuck.example.common.IMyAidlInterface;
import com.github.okbuilds.okbuck.example.dummylibrary.DummyActivity;
import com.github.okbuilds.okbuck.example.dummylibrary.DummyAndroidClass;
import com.github.okbuilds.okbuck.example.javalib.DummyJavaClass;
import com.promegu.xlog.base.XLog;
import javax.inject.Inject;

@XLog
public class MainActivity extends AppCompatActivity {
    @Inject
    DummyJavaClass mDummyJavaClass;
    @Inject
    DummyAndroidClass mDummyAndroidClass;
    IMyAidlInterface mIMyAidlInterface;
    private TextView mTextView;
    private TextView mTextView2;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIMyAidlInterface = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();

        DummyComponent component = DaggerDummyComponent.builder().build();
        component.inject(this);

        mTextView.setText(String.format("%s %s, --from %s.", getString(R.string.dummy_library_android_str),
                mDummyAndroidClass.getAndroidWord(this), mDummyJavaClass.getJavaWord()));

        mTextView2.setText(mTextView2.getText() + "\n\n" + HelloJni.stringFromJNI() + "\n\n" + com.github.okbuilds.okbuck.example.FlavorLogger

                .log(this));

        if (BuildConfig.CAN_JUMP) {
            mTextView.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, DummyActivity.class));
            });
        }

        Log.d("test", "1 + 2 = " + new Calc(new CalcMonitor(this)).add(1, 2));

        GithubUser user = GithubUser.create(100, "OkBuilds");
        Toast.makeText(this, user.login(), Toast.LENGTH_SHORT).show();
    }

    private void bind() {
        mTextView = ButterKnife.findById(this, R.id.mTextView);
        mTextView2 = ButterKnife.findById(this, R.id.mTextView2);
    }
}
