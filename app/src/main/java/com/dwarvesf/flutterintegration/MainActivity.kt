package com.dwarvesf.flutterintegration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dwarvesf.flutterintegration.const.MY_CHAT_CHANNEL
import com.dwarvesf.flutterintegration.const.MY_CHAT_ENGINE_ID
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var flutterEngineCache: FlutterEngineCache


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())

        flutterEngineCache = FlutterEngineCache.getInstance()
        flutterEngineCache.put(MY_CHAT_ENGINE_ID, flutterEngine)

        initClickEvent()
        initMethodChannel()
    }

    private fun initClickEvent() {
        btnFlutter.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine(MY_CHAT_ENGINE_ID)
                    .build(this)
            )
        }
    }

    private fun initMethodChannel() {
        MethodChannel(
            flutterEngine.dartExecutor,
            MY_CHAT_CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "get_init_info") {
                val initCount = etInitCount.text.toString().toIntOrNull() ?: 0
                val yourName = etYourName.text.toString()

                result.success(hashMapOf(
                    "initCount" to initCount,
                    "userName" to yourName
                ))
            } else {
                result.notImplemented()
            }
        }
    }
}