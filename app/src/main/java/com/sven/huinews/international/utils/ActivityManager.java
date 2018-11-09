package com.sven.huinews.international.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;

/**
 * Created by sfy. on 2018/3/12 0012.
 */

public class ActivityManager {
        private static ActivityManager instance;
        private Stack<Activity> activityStack;// activity栈

        private ActivityManager() {
        }

        // 单例模式
        public static ActivityManager getInstance() {
            if (instance == null) {
                instance = new ActivityManager();
            }
            return instance;
        }

        // 把一个activity压入栈中
        public void pushOneActivity(Activity actvity) {
            if (activityStack == null) {
                activityStack = new Stack<Activity>();
            }
            if (!activityStack.contains(actvity))activityStack.add(actvity);
            Log.d("MyActivityManager ", "size = " + activityStack.size());
        }

        // 获取栈顶的activity，先进后出原则
        public Activity getLastActivity() {
            if (activityStack != null) {
                return activityStack.lastElement();
            }
            return null;
        }


        public Activity getMainActivity(){
            if(activityStack != null){
                for(Activity activity:activityStack){
                    if("MainActivity".equals(activity.getClass().getSimpleName())){
                        return activity;
                    }
                }
            }
            return null;
        }

        // 移除一个activity
        public void popOneActivity(Activity activity) {
            if (activityStack != null && activityStack.size() > 0) {
                if (activity != null) {
                    activity.finish();
                    activityStack.remove(activity);
                    activity = null;
                }
            }
        }

        // 退出所有activity
        public void finishAllActivity() {
            if (activityStack != null) {
                while (activityStack.size() > 0) {
                    Activity activity = getLastActivity();
                    if (activity == null)
                        break;
                    popOneActivity(activity);
                }
            }
        }

        // 退出所有activity保留栈底的activity
        public void finishActivity() {
            if (activityStack != null) {
                while (activityStack.size() > 0) {
                    Activity activity = getLastActivity();
                    if (activityStack.size() == 1)
                        break;
                    popOneActivity(activity);
                }
            }
        }


        public static boolean isTopActivity(Context context, String className) {
            String rTasks = getRunningTask(context, 1);
            if (rTasks.equals(className)) {
                return true;
            }
            return false;
        }

        public static String getRunningTask(Context context, int num) {
            if (context != null) {
                android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                String rTasks = am.getRunningTasks(1).get(0).topActivity.getClassName();
                return rTasks;
            }
            return null;
        }

        public int getActivitySize(){
            return activityStack.size();
        }


}
