package com.budworm.smallutils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity管理栈
 */
public class AtyContainer {

    private AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<>();

    public static AtyContainer getInstance() {
        if(instance == null) {
            synchronized (AtyContainer.class) {
                if (instance == null) {
                    instance = new AtyContainer();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAll() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        System.exit(0);
    }

    /**
     * 结束指定类型Activity
     */
    public void finish(Class cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)
                    && activityStack.get(i).getClass().getSimpleName().equalsIgnoreCase(cls.getClass().getSimpleName())) {
                activityStack.get(i).finish();
            }
        }
    }

}
