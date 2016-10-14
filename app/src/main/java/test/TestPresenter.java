package test;

import android.app.Activity;

/**
 * Created by yuanjinsong on 16/10/13.
 */
public class TestPresenter {

    public Activity activity;
    TestVIew view;

     public TestPresenter(Activity activity, BaseView view) {
        this.activity = activity;
        this.view = (TestVIew) view;

    }

    public void getData() {
        view.show(new bean());

    }

}
