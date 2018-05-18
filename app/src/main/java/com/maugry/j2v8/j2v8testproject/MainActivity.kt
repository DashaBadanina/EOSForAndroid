package com.maugry.j2v8.j2v8testproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Array
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import android.webkit.ValueCallback




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webViewTest()
    }


//    private fun dupcate() {
//      //  val duktape = Duktape.create()
//       // duktape.evaluate(readFileToString("eos.js"))
//        //val utf8 = duktape.get("Utf8",)
//    }
//
//    private fun evaluatorForAndroid() {
//        val jsEvaluator = JsEvaluator(this)
//        jsEvaluator.evaluate("2 * 17", object : JsCallback {
//            override fun onResult(result: String) {
//                tv_hello_world.text = result
//            }
//
//            override fun onError(errorMessage: String) {
//                Log.i("", errorMessage)
//            }
//        })
//    }

    private fun webViewTest() {
        val webView = WebView(this)
        val eos = readFileToString("html.txt")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.settings.domStorageEnabled = true
        webView.loadData(eos, "text/html", "UTF-8")
        webView.evaluateJavascript("getBlock()",  { s ->
            Log.d("LogName", s) // Log is written, but s is always null
        })

        setContentView(webView)
    }


    private fun executeScript() {
        val v8: V8 = V8.createV8Runtime()
        val result: Int = v8.executeIntegerScript(
                "var hello = 'hello';\n" +
                        "var world = 'world';\n" +
                        "hello.concat(world).length;\n")
        tv_hello_world.text = result.toString()
        v8.release()
    }

    private fun executeFunction() {
        val v8: V8 = V8.createV8Runtime()
        var function = "var foo = function(x) {return 42 + x;}"
        v8.executeVoidScript(function)
        var parameters = V8Array(v8).push(6)
        var result: Int = v8.executeIntegerFunction("foo", parameters)
        tv_hello_world.text = result.toString()
        parameters.release()
        v8.release()
    }

    private fun executeFunctionEosScript() {
        val v8: V8 = V8.createV8Runtime()
        val eos = readFileToString("eos.js")
        v8.executeVoidScript(eos)
        v8.executeStringScript(eos)
    }

    private fun readFileToString(path: String): String {
        val buf = StringBuilder()
        val json = assets.open(path)
        val reader = BufferedReader(InputStreamReader(json, "UTF-8"))
        var str: String? = reader.readLine()
        while (str != null) {
            buf.append(str)
            str = reader.readLine()
        }
        return buf.toString()
    }
}
