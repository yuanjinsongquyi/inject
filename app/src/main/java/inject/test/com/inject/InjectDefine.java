package inject.test.com.inject;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yuanjinsong on 16/10/13.
 */
public class InjectDefine {
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventBase {
        Class<?> listenerType();

        String listenerSetter();

        String methodName();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface injectview {


        int value();


    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ContentView {
        int value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface presenterinject {
        Class<?> value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @EventBase(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener", methodName = "onClick")
    public @interface OnClick
    {
        int[] value();
    }
}
