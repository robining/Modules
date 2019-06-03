package com.robining.android.stateview;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;

import java.util.Map;

public class StateViewAssist {
    public static final int STATE_NORMAL = -1;
    public static final int STATE_LOADING = -2;
    public static final int STATE_ERROR = -3;
    public static final int STATE_EMPTY = -4;
    public static final int STATE_NOT_NETWORK = -5;
    public static final int STATE_NEED_LOGIN = -6;

    public static final int MODE_REPLACE = -1;
    public static final int MODE_OVERRIDE = -2;

    private SparseArray<ViewFactory> factoryMap;
    private int mode;
    private SparseArray<Map<String, Object>> defaultParamsMap;
    private View target;

    private StateViewAssist(Builder builder) {
        this.factoryMap = builder.factoryMap;
        this.mode = builder.mode;
        this.defaultParamsMap = builder.defaultParamsMap;
    }

    public void show(View target, int state, Map<String, Object> args) {
        assertNotNull(target, "the target view cannot be null");
        IVaryViewHelper varyViewHelper = getVaryViewHelper(target);
        varyViewHelper.showLayout(getReplaceView(target, state, args));
        target.setTag(R.id.com_robining_android_stateview_tag_state, state);
    }

    public void show(int state, Map<String, Object> args) {
        show(target, state, args);
    }

    public void restore(View target) {
        assertNotNull(target, "the target view cannot be null");
        IVaryViewHelper varyViewHelper = getVaryViewHelper(target);
        varyViewHelper.restoreView();
        target.setTag(R.id.com_robining_android_stateview_tag_state, StateViewAssist.STATE_NORMAL);
    }

    public void restore() {
        restore(target);
    }

    public int getCurrentState(View target) {
        assertNotNull(target, "the target view cannot be null");
        Object state = target.getTag(R.id.com_robining_android_stateview_tag_state);
        if (state == null) {
            return StateViewAssist.STATE_NORMAL;
        } else {
            return (int) state;
        }
    }

    public int getCurrentState() {
        return getCurrentState(target);
    }

    public View getTarget() {
        return target;
    }

    public StateViewAssist setTarget(View target) {
        this.target = target;
        return this;
    }

    public static void notifyLogin(Context context) {
        ViewBroadcastBinder.notify(context, new Intent(Config.ACTION_LOGIN));
    }

    private View getReplaceView(View target, int state, Map<String, Object> args) {
        ViewFactory viewFactory = factoryMap.get(state);
        if (viewFactory == null) {
            throw new IllegalStateException("cannot get ViewFactory for state:" + state + ", please config it before using");
        }

        View view = viewFactory.provideView(target, this, args, defaultParamsMap.get(state));
        if (view == null) {
            throw new NullPointerException("the view factory's return must not be null");
        }

        return view;
    }

    private IVaryViewHelper getVaryViewHelper(final View target) {
        synchronized (target) {
            IVaryViewHelper varyViewHelper = (IVaryViewHelper) target.getTag(R.id.com_robining_android_stateview_tag_varyViewHelper);
            if (varyViewHelper == null) {
                switch (mode) {
                    case MODE_OVERRIDE:
                        varyViewHelper = new VaryViewHelperX(target);
                        break;
                    case MODE_REPLACE:
                        varyViewHelper = new VaryViewHelper(target);
                        break;
                    default:
                        throw new IllegalArgumentException("not support this mode:\"" + mode + "\", to see StateViewAssist.MODE_REPLACE or StateViewAssist.MODE_OVERRIDE");
                }

                target.setTag(R.id.com_robining_android_stateview_tag_varyViewHelper, varyViewHelper);
            }
            return varyViewHelper;
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    private void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static class Builder {
        private SparseArray<ViewFactory> factoryMap;
        private SparseArray<Map<String, Object>> defaultParamsMap;
        private int mode;

        public Builder() {
            this.factoryMap = new SparseArray<>();
            this.mode = MODE_REPLACE;
            this.defaultParamsMap = new SparseArray<>();

            config(STATE_LOADING, DefaultViewFactory.LOADING);
            config(STATE_EMPTY, DefaultViewFactory.EMPTY);
            config(STATE_ERROR, DefaultViewFactory.ERROR);
            config(STATE_NOT_NETWORK, DefaultViewFactory.NOT_NETWORK);
            config(STATE_NEED_LOGIN, DefaultViewFactory.NEED_LOGIN);
        }

        public Builder(StateViewAssist assist) {
            this.factoryMap = assist.factoryMap.clone();
            this.mode = assist.mode;
            this.defaultParamsMap = assist.defaultParamsMap.clone();
        }

        public Builder config(int state, ViewFactory factory) {
            factoryMap.put(state, factory);
            return this;
        }

        public Builder configParams(int state, Map<String, Object> defaultParams) {
            defaultParamsMap.put(state, defaultParams);
            return this;
        }

        public int getMode() {
            return mode;
        }

        public Builder setMode(int mode) {
            if (mode != MODE_REPLACE && mode != MODE_OVERRIDE) {
                throw new IllegalArgumentException("not support this mode:\"" + mode + "\", to see StateViewAssist.MODE_REPLACE or StateViewAssist.MODE_OVERRIDE");
            }
            this.mode = mode;
            return this;
        }

        public StateViewAssist build() {
            return new StateViewAssist(this);
        }

        public StateViewAssist buildWith(View target) {
            return new StateViewAssist(this).setTarget(target);
        }
    }

    public abstract static class ViewFactory {
        public abstract View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs);

        public <T> T getParam(String key, T defaultValue, Map<String, Object>... args) {
            for (Map<String, Object> map : args) {
                if (map != null && map.containsKey(key)) {
                    Object obj = map.get(key);
                    return (T) obj;
                }
            }

            return defaultValue;
        }
    }
}
