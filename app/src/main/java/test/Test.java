package test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import inject.test.com.inject.InjectDefine;
import inject.test.com.inject.InjectUtils;
import inject.test.com.inject.R;
/**
 * Created by yuanjinsong on 16/10/13.
 */
public class Test extends AppCompatActivity implements TestVIew{
    @InjectDefine.injectview(R.id.bt_inject)
    public Button bt;
    @InjectDefine.injectview(R.id.tv_bean)
    public TextView tv;
    @InjectDefine.presenterinject(TestPresenter.class)
    static public  TestPresenter prese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        InjectUtils.inject(this,this);
        tv.setText("fsdf");

        prese.getData();
    }
    @InjectDefine.OnClick({R.id.bt_inject})
    public void clicklis() {
        Toast.makeText(this,"dsfsdf",Toast.LENGTH_LONG).show();
    }

    @Override
    public void show(bean bean) {
        tv.setText(bean.text);

    }
}
