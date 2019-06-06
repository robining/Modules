### 页面状态切换工具
<video src="./videos/stateview.mp4"></video>


#### 一.添加依赖
在项目根目录的build.gradle文件中配置

```Gradle
maven { url 'https://jitpack.io' }
```

在模块目录下的build.gradle中配置
```Gradle
implementation 'com.github.robining.Modules:stateview:XXX'
```
[![Release](https://jitpack.io/v/com.github.robining/Modules.svg?style=flat-square)](https://github.com/robining/Modules/tree/master/params-inject)


#### 二.使用
```kotlin
val appStateViewAssist = StateViewAssist.Builder()
            .configParams(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE,"全局级我在加载....")
            ))
            .build()

val stateViewAssist = appStateViewAssist.newBuilder()
            .configParams(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE,"页面级我在加载....")  //替换配置，其他配置沿用全局定义
            ))
            .buildWith(fl_content)



//恢复页面布局
stateViewAssist.restore()

//显示加载中
stateViewAssist.show(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "正在加载文章") //加载中文字显示
            ))
//显示加载失败
 stateViewAssist.show(StateViewAssist.STATE_ERROR, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "加载失败啦!!"), //加载失败文字显示
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable {  //重试按钮的点击事件
                    Toast.makeText(this@MainActivity,"我是重试点击事件",Toast.LENGTH_SHORT).show()
                })
            ))
//显示空布局
stateViewAssist.show(
                StateViewAssist.STATE_EMPTY, mapOf(
                    Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "您还没有创建新的文章哟") //空布局文字
                )
            )       
//显示没有网络，可以前往设置，也可以点击重试【在网络恢复时，可以自动触发重试】
stateViewAssist.show(StateViewAssist.STATE_NOT_NETWORK, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "检测到您还没有开启网络"),
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable {
                    Toast.makeText(this@MainActivity,"我是重试点击事件",Toast.LENGTH_SHORT).show()
                })
            ))
            
//显示需要登录后使用，可以前往登录（当登录成功后可以自动触发重试）
stateViewAssist.show(StateViewAssist.STATE_NEED_LOGIN, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "请在登录后查看该内容!"),
                Pair(DefaultViewFactory.PARAMS_KEY_BUTTON_RETRY_TEXT,"我已经登录啦"),
                Pair(DefaultViewFactory.PARAMS_KEY_LOGIN_RUNNABLE, Runnable { //登录按钮触发事件
                    Toast.makeText(this@MainActivity,"前往登录",Toast.LENGTH_SHORT).show()
                }),
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable { //重试按钮触发事件
                    Toast.makeText(this@MainActivity,"我是重试点击事件",Toast.LENGTH_SHORT).show()
                })
            ))
            
 //发送登录成功的通知
 StateViewAssist.notifyLogin(this@MainActivity)

/*若不需要更改参数第二个参数可以传null*/
```
#### 三.自定义布局

##### 1.定义ViewFactory
参照[DefaultViewFactory](../stateview/src/main/java/com/robining/android/stateview/DefaultViewFactory.java)(也可以在ViewFactory里面使用自定义参数)
```java
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
    }
```

##### 2.配置StateViewAssist
```kotlin
StateViewAssist.Builder()
            .config(StateViewAssist.STATE_LOADING,xxx)
            .build()
```

#### 四.关于布局绑定广播
##### 1.快速绑定
[ViewBroadcastBinder](../stateview/src/main/java/com/robining/android/stateview/ViewBroadcastBinder.java)
##### 2.自定义绑定
参照[ViewBroadcastBinder](../stateview/src/main/java/com/robining/android/stateview/ViewBroadcastBinder.java)中View.addOnAttachStateChangeListener中进行广播或EventBus等的绑定和解绑
