package com.robining.android.stateview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

public class DefaultViewFactory {
    public static final String PARAMS_KEY_MESSAGE = "PARAMS_KEY_MESSAGE";
    public static final String PARAMS_KEY_BUTTON_RETRY_TEXT = "PARAMS_KEY_BUTTON_RETRY_TEXT";
    public static final String PARAMS_KEY_BUTTON_LOGIN_TEXT = "PARAMS_KEY_BUTTON_LOGIN_TEXT";
    public static final String PARAMS_KEY_BUTTON_SETTING_TEXT = "PARAMS_KEY_BUTTON_SETTING_TEXT";
    public static final String PARAMS_KEY_RETRY_RUNNABLE = "PARAMS_KEY_RETRY_RUNNABLE";
    public static final String PARAMS_KEY_LOGIN_RUNNABLE = "PARAMS_KEY_LOGIN_RUNNABLE";

    static final StateViewAssist.ViewFactory LOADING = new StateViewAssist.ViewFactory() {
        @Override
        public View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
            View view = LayoutInflater.from(target.getContext()).inflate(R.layout.com_robining_android_stateview_default_layout_loading, null);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            String message = getParam(PARAMS_KEY_MESSAGE, null, args, defaultArgs);
            tvMessage.setText(message);
            tvMessage.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
            return view;
        }
    };

    static final StateViewAssist.ViewFactory ERROR = new StateViewAssist.ViewFactory() {
        @Override
        public View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
            View view = LayoutInflater.from(target.getContext()).inflate(R.layout.com_robining_android_stateview_default_layout_error, null);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setText((CharSequence) getParam(PARAMS_KEY_MESSAGE, null, args, defaultArgs));

            TextView btnRetry = view.findViewById(R.id.btn_retry);
            btnRetry.setText((CharSequence) getParam(PARAMS_KEY_BUTTON_RETRY_TEXT, null, args, defaultArgs));

            final Runnable retryRunnable = getParam(PARAMS_KEY_RETRY_RUNNABLE, null, args, defaultArgs);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (retryRunnable != null) {
                        retryRunnable.run();
                    }
                }
            });
            return view;
        }
    };

    static final StateViewAssist.ViewFactory EMPTY = new StateViewAssist.ViewFactory() {
        @Override
        public View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
            View view = LayoutInflater.from(target.getContext()).inflate(R.layout.com_robining_android_stateview_default_layout_empty, null);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setText((CharSequence) getParam(PARAMS_KEY_MESSAGE, null, args, defaultArgs));

            final Runnable retryRunnable = getParam(PARAMS_KEY_RETRY_RUNNABLE, null, args, defaultArgs);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (retryRunnable != null) {
                        retryRunnable.run();
                    }
                }
            });
            return view;
        }
    };

    static final StateViewAssist.ViewFactory NOT_NETWORK = new StateViewAssist.ViewFactory() {
        @Override
        public View provideView(final View target, final StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
            View view = LayoutInflater.from(target.getContext()).inflate(R.layout.com_robining_android_stateview_default_layout_not_network, null);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setText((CharSequence) getParam(PARAMS_KEY_MESSAGE, null, args, defaultArgs));

            TextView btnSetting = view.findViewById(R.id.btn_setting);
            btnSetting.setText((CharSequence) getParam(PARAMS_KEY_BUTTON_SETTING_TEXT, null, args, defaultArgs));
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context activityContext = target.getContext() instanceof Activity ? target.getContext() : ViewContextUtil.getActivity(v);
                    activityContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });

            final TextView btnRetry = view.findViewById(R.id.btn_retry);
            btnRetry.setText((CharSequence) getParam(PARAMS_KEY_BUTTON_RETRY_TEXT, null, args, defaultArgs));

            final Runnable retryRunnable = getParam(PARAMS_KEY_RETRY_RUNNABLE, null, args, defaultArgs);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (retryRunnable != null) {
                        retryRunnable.run();
                    }
                }
            });


            ViewBroadcastBinder.bindNetworkListener(view, new Runnable() {
                @Override
                public void run() {
                    btnRetry.performClick();
                }
            });
            return view;
        }
    };

    static final StateViewAssist.ViewFactory NEED_LOGIN = new StateViewAssist.ViewFactory() {
        @Override
        public View provideView(View target, StateViewAssist stateViewAssist, Map<String, Object> args, Map<String, Object> defaultArgs) {
            View view = LayoutInflater.from(target.getContext()).inflate(R.layout.com_robining_android_stateview_default_layout_need_login, null);
            TextView tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setText((CharSequence) getParam(PARAMS_KEY_MESSAGE, null, args, defaultArgs));

            TextView btnLogin = view.findViewById(R.id.btn_login);
            btnLogin.setText((CharSequence) getParam(PARAMS_KEY_BUTTON_LOGIN_TEXT, null, args, defaultArgs));

            final Runnable loginRunnable = getParam(PARAMS_KEY_LOGIN_RUNNABLE, null, args, defaultArgs);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loginRunnable != null) {
                        loginRunnable.run();
                    }
                }
            });

            final TextView btnRetry = view.findViewById(R.id.btn_retry);
            btnRetry.setText((CharSequence) getParam(PARAMS_KEY_BUTTON_RETRY_TEXT, null, args, defaultArgs));

            final Runnable retryRunnable = getParam(PARAMS_KEY_RETRY_RUNNABLE, null, args, defaultArgs);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (retryRunnable != null) {
                        retryRunnable.run();
                    }
                }
            });

            ViewBroadcastBinder.bind(view, new IntentFilter(Config.ACTION_LOGIN), new Runnable() {
                @Override
                public void run() {
                    btnRetry.performClick();
                }
            });
            return view;
        }
    };
}
