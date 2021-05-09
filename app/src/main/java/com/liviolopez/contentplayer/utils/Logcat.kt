package com.liviolopez.contentplayer.utils


import android.util.Log
import com.google.gson.Gson

/**
 * Created by Livio Lopez on 1/17/21.
 */

private const val GLOBAL_TAG = "MY-APP"
private const val FILTER_PACKAGE = "com.liviolopez"

data class LogcatDebug(val level:Int = Log.DEBUG, val link:Boolean = true)

private fun tagLog(TAG: String): String{
    return if(TAG.isNotEmpty()){ "$GLOBAL_TAG:$TAG" } else GLOBAL_TAG
}

private fun lastTraceElement(): StackTraceElement? = Thread.currentThread().stackTrace.filter { it.className.startsWith(
        FILTER_PACKAGE
) }.firstOrNull{ it.fileName != "Logcat.kt" }

private fun classPath(trace: StackTraceElement?): String? {
    return (trace?.className?.split('$')?.firstOrNull() ?: trace?.className)
            ?.replace('.', '/')
            ?.replace(FILTER_PACKAGE.replace(".","/"),"")
            ?.split("/")?.dropLast(1)?.joinToString("/")
}

private fun methodFlow(trace: StackTraceElement?): String? {
    var methodFlow = trace?.className?.split(".")?.last()?.replace("$1", "")?.split('$')?.joinToString(" ❱ ")
    if(methodFlow != null) methodFlow += " ❱ "; methodFlow += trace?.methodName
    return methodFlow
}

private fun logcatLink(trace: StackTraceElement?) = ("${trace?.fileName}:${trace?.lineNumber}").let { "(${it})" }

private fun headerLog(): String {
    val trace = lastTraceElement()

    val headerInfo = "${classPath(trace)}/${logcatLink(trace)} ❪$${methodFlow(trace)}❫"

    return "$headerInfo ⟹ "
}

fun Any._log(TAG:String = "", priority: Int = Log.DEBUG) {
    //The logging tag can be at most 23 characters
    val tagLog = tagLog(TAG).let { if (it.length > 23) { it.substring(0,22) } else it }
    val header = headerLog()
    val logLines = formatLog(this)


        if (logLines.size == 1) {
            Log.println(priority, tagLog, header + logLines[0])
        } else {
            Log.println(priority, tagLog, header + "\n" + logLines[0])
            logLines.forEachIndexed { index, logLine ->
                if (index > 0) Log.println(priority, tagLog, logLine)
            }
        }

}

fun String._logline(){
    val trace = lastTraceElement()
    println("${tagLog("")} ${logcatLink(trace)}:${trace?.methodName} ${this.repeat(100)}")
}

fun _printTraceLog(TAG:String = "", onlyApp:Boolean = true){

        val startWith = if(onlyApp) FILTER_PACKAGE else ""

        Thread.currentThread().stackTrace
                .filter { it.className.startsWith(startWith) && !it.className.endsWith("LogcatKt") }
                .toList().asReversed()
                .forEachIndexed { index, it ->
                    if (index == 0) println("${tagLog(TAG)} :: START TRACE HERE")
                    println("${tagLog(TAG)} :: ⟶ ${classPath(it)}/${it.fileName} :: ${methodFlow(it)} :: ${logcatLink(it)}")
                }
                .let { println("${tagLog(TAG)} :: END TRACE HERE") }

}

fun Any._output(line: String = "-"): String{
    return line.repeat(10) + "\n" + getString(this) + "\n"
}

private fun formatLog(any: Any): List<String>{
    return splitLogcat(getString(any))
}

private fun getString(any: Any): String {
    return when(any){
        is String, Int, Double -> any.toString()
        else -> {
            try { Gson().toJson(any) ?: "" }
            catch (e: Exception) { "LocalDebug error to convert Json: ${e.message}" }
        }
    }
}

private fun splitLogcat(logString: String): List<String> {
    val logLines = mutableListOf<String>()
    val maxLogSize = 2000
    val stringLength = logString.length
    for (i in 0..stringLength / maxLogSize) {
        val start = i * maxLogSize
        var end = (i + 1) * maxLogSize
        end = if (end > logString.length) logString.length else end
        logLines += logString.substring(start, end)
    }

    return logLines
}