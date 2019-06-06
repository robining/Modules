### 参数注入工具

在Android开发过程中页面间传值和保持恢复传值，利用Java反射和注解，将指定对象中包含@AutoParam注解的字段自动赋值

#### 一.添加依赖
在项目根目录的build.gradle文件中配置

```Gradle
maven { url 'https://jitpack.io' }
```

在模块目录下的build.gradle中配置
```Gradle
implementation 'com.github.robining.Modules:params-inject:XXX'
```
[![Release](https://jitpack.io/v/com.github.robining/Modules.svg?style=flat-square)](https://github.com/robining/Modules/tree/master/params-inject)


#### 二.使用
```kotlin
class MainActivity : AppCompatActivity() {
    @AutoParam("key1")
    var title:String = "Default Title"
    
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        AutoParamCompat.injectValue(this,savedInstanceState,intent.extras)
    }
    
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        AutoParamCompat.saveValues(this,outState) //可选
    }
}

var intent = Intent(context,MainActivity::class.java)
intent.putExtra("key1","This is Title Value")
startActivity(intent)
```