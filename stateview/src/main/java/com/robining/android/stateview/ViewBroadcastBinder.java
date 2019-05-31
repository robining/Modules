package com.robining.android.stateview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

public class ViewBroadcastBinder {
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void bind(View view, final IntentFilter filter, final BroadcastResultFilter resultFilter, final Runnable runnable) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (runnable != null) {
                        if (resultFilter == null) {
                            mainHandler.post(runnable);
                        } else {
                            if (resultFilter.filter(context, intent)) {
                                mainHandler.post(runnable);
                            }
                        }
                    }
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                System.out.println(">>>注册:" + this);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(v.getContext());
                localBroadcastManager.registerReceiver(broadcastReceiver, filter);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                System.out.println(">>>取消注册" + this);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(v.getContext());
                localBroadcastManager.unregisterReceiver(broadcastReceiver);
            }
        });
    }

    public static void bindGlobal(View view, final IntentFilter filter, final Runnable runnable) {
        bindGlobal(view, filter, null, runnable);
    }

    public static void bindGlobal(View view, final IntentFilter filter, final BroadcastResultFilter resultFilter, final Runnable runnable) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (runnable != null) {
                        if (resultFilter == null) {
                            mainHandler.post(runnable);
                        } else {
                            if (resultFilter.filter(context, intent)) {
                                mainHandler.post(runnable);
                            }
                        }
                    }
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                System.out.println(">>>注册:" + this);
                ViewContextUtil.getActivity(v).registerReceiver(broadcastReceiver, filter);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                System.out.println(">>>取消注册" + this);
                ViewContextUtil.getActivity(v).unregisterReceiver(broadcastReceiver);
            }
        });
    }

    public static void bind(View view, final IntentFilter filter, final Runnable runnable) {
        bind(view, filter, null, runnable);
    }

    public static void bindNetworkListener(View view, final Runnable runnable) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            bindNetworkListenerN(view, runnable);
//        } else {
            bindGlobal(view, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION), new BroadcastResultFilter() {
                @Override
                public boolean filter(Context context, Intent intent) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    System.out.println(">>>network changed:" + mobileNetworkInfo.isConnected() + "/" + wifiNetworkInfo.isConnected() + "    " + mobileNetworkInfo.isAvailable() + "/" + wifiNetworkInfo.isAvailable());
                    return mobileNetworkInfo.isConnected() || wifiNetworkInfo.isConnected();
                }
            }, runnable);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void bindNetworkListenerN(View view, final Runnable runnable) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            private ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if (runnable != null) {
                        mainHandler.post(runnable);
                    }
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                connectivityManager.registerDefaultNetworkCallback(networkCallback);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                connectivityManager.unregisterNetworkCallback(networkCallback);
            }
        });
    }

    public static void notify(Context context, final Intent intent) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.sendBroadcast(intent);
    }

    public interface BroadcastResultFilter {
        boolean filter(Context context, Intent intent);
    }
}
