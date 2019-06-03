package com.robining.android.modules

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.robining.android.stateview.DefaultViewFactory
import com.robining.android.stateview.StateViewAssist
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appStateViewAssist = StateViewAssist.Builder()
            .configParams(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE,"App我在加载....")
            ))
            .build()

        val stateViewAssist = appStateViewAssist.newBuilder()
            .configParams(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE,"Global我在加载....")
            ))
            .buildWith(fl_content)

        btnNormal.setOnClickListener {
            stateViewAssist.restore()
        }

        btnLoading.setOnClickListener {
            stateViewAssist.show(StateViewAssist.STATE_LOADING, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "正在加载文章")
            ))
        }

        btnError.setOnClickListener {
            stateViewAssist.show(StateViewAssist.STATE_ERROR, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "加载失败啦!!"),
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable {
                    Toast.makeText(this@MainActivity,"加载失败重试",Toast.LENGTH_SHORT).show()
                })
            ))
        }

        btnEmpty.setOnClickListener {
            stateViewAssist.show(
                StateViewAssist.STATE_EMPTY, mapOf(
                    Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "您还没有创建新的文章哟")
                )
            )
        }

        btnNoNetwork.setOnClickListener {
            stateViewAssist.show(StateViewAssist.STATE_NOT_NETWORK, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "检测到您还没有开启网络"),
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable {
                    Toast.makeText(this@MainActivity,"没有网络重试",Toast.LENGTH_SHORT).show()
                })
            ))
        }

        btnNeedLogin.setOnClickListener {
            stateViewAssist.show(StateViewAssist.STATE_NEED_LOGIN, mapOf(
                Pair(DefaultViewFactory.PARAMS_KEY_MESSAGE, "请在登录后查看该内容!"),
                Pair(DefaultViewFactory.PARAMS_KEY_BUTTON_RETRY_TEXT,"我已经登录啦"),
                Pair(DefaultViewFactory.PARAMS_KEY_RETRY_RUNNABLE, Runnable {
                    Toast.makeText(this@MainActivity,"需要登录重试",Toast.LENGTH_SHORT).show()
                })
            ))
        }

        btnSendLogin.setOnClickListener {
            StateViewAssist.notifyLogin(this@MainActivity)
        }
    }
}
