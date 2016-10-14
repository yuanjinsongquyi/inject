package inject.test.com.inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import test.BaseView;

/**
 * Created by yuanjinsong on 16/10/13.
 */

public class InjectUtils
{
    private static final String METHOD_SET_CONTENTVIEW = "setContentView";
    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    public static void inject(Activity activity, BaseView view)
    {
        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);
        injectPresenter(activity,view);
    }

    /**
     * 注入所有的事件
     *
     * @param activity
     */
    private static void injectEvents(Activity activity)
    {

        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods)
        {
            Annotation[] annotations = method.getAnnotations();
            //拿到方法上的所有的注解
            for (Annotation annotation : annotations)
            {
                Class<? extends Annotation> annotationType = annotation
                        .annotationType();
                //拿到注解上的注解
                InjectDefine.EventBase eventBaseAnnotation = annotationType
                        .getAnnotation(InjectDefine.EventBase.class);
                //如果设置为EventBase
                if (eventBaseAnnotation != null)
                {
                    //取出设置监听器的名称，监听器的类型，调用的方法名
                    String listenerSetter = eventBaseAnnotation
                            .listenerSetter();
                    Class<?> listenerType = eventBaseAnnotation.listenerType();
                    String methodName = eventBaseAnnotation.methodName();

                    try
                    {
                        //拿到Onclick注解中的value方法
                        Method aMethod = annotationType
                                .getDeclaredMethod("value");
                        //取出所有的viewId
                        int[] viewIds = (int[]) aMethod
                                .invoke(annotation, null);
                        //通过InvocationHandler设置代理
                        DynamicHandler handler = new DynamicHandler(activity);
                        //往map添加方法
                        handler.addMethod(methodName, method);
                        Object listener = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class<?>[] { listenerType }, handler);
                        //遍历所有的View，设置事件
                        for (int viewId : viewIds)
                        {
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass()
                                    .getMethod(listenerSetter, listenerType);
                            setEventListenerMethod.invoke(view, listener);
                        }

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * 注入所有的控件
     *
     * @param activity
     */
    private static void injectViews(Activity activity)
    {
        try{
            //获取activity上所有的field并且尝试获取field上的InjectView注解
            //如果这里所有的注解view都是public修饰的可以使用getFields方法；
            Field[] fields = activity.getClass().getDeclaredFields();
            for(int i=0;i<fields.length;i++){
                Field field = fields[i];
                InjectDefine.injectview annotation = field.getAnnotation(InjectDefine.injectview.class);
                if(annotation!=null){
                    //如果获取到了注解，那么获取注解上的信息(view对应的id),使用activity找到这个view，并通过反射设置给filed
                    int viewId = annotation.value();
                    View viewObj = activity.findViewById(viewId);
                    field.set(activity,viewObj);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 注入主布局文件
     *
     * @param activity
     */
    private static void injectContentView(Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();
        // 查询类上是否存在ContentView注解
        InjectDefine.ContentView contentView = clazz.getAnnotation(InjectDefine.ContentView.class);
        if (contentView != null)// 存在
        {
            int contentViewLayoutId = contentView.value();
            try
            {
                Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW, int.class);
                method.setAccessible(true);
                method.invoke(activity, contentViewLayoutId);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void injectPresenter(Activity activity,BaseView view) {

        try {
            //获取activity上所有的field并且尝试获取field上的InjectView注解
            //如果这里所有的注解view都是public修饰的可以使用getFields方法；
            Field[] fields = activity.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                InjectDefine.presenterinject annotation = field.getAnnotation(InjectDefine.presenterinject.class);
                if (annotation != null) {
                    Class<?> presenterType = annotation.value();
                    Class<?>[] ptype=new Class[]{Activity.class,BaseView.class};
                    Constructor<?> constructor=presenterType.getConstructor(ptype);
                    Object[] obj=new Object[]{activity,view};
                    Object object=constructor.newInstance(obj);
                    field.set(null, object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
