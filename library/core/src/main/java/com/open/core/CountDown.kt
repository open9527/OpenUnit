package com.open.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

object CountDown {
    /**
     * 倒计时
     */
    @OptIn(DelicateCoroutinesApi::class)
    @ExperimentalCoroutinesApi
    fun countDownCoroutines(
        total: Int,
        range: Int = 1,
        onTick: (Int) -> Unit,
        onFinish: () -> Unit,
        scope: CoroutineScope = GlobalScope
    ): Job {
        return flow {
            for (i in total downTo range) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Default) //flowOn只影响该运算符之前的CoroutineContext，对它之后的CoroutineContext没有任何影响
            .onEach { onTick.invoke(it) } //每个值释放的时候可以执行的一段代码
            .onCompletion { onFinish.invoke() } //最后一个值释放完成之后被执行
            .catch { e -> println(e) } //catch函数能够捕获之前产生的异常，之后的异常无法捕获
            .flowOn(Dispatchers.Main)
            .launchIn(scope) //scope.launch { flow.collect() }的缩写, 代表在某个协程上下文环境中去接收释放的值
    }


    /**
     * 协程中实现倒计时
     * @param duration 倒计时时长
     * @param interval 倒计时步长
     * @param range 倒计时结束范围
     * @param scope 协程作用域
     * @param onTick 倒计时变更，回调
     * @param onStart 倒计时开始，回调
     * @param onEnd 倒计时结束，回调
     *
     * @return [Job] 可用于在需要时，取消倒计时
     */
    fun countDownCoroutine(
        duration: Int,
        interval: Int = 1,
        range: Int = 0,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit)? = null,
        onEnd: (() -> Unit)? = null,
        scope: CoroutineScope,
    ): Job {
        if (duration <= 0 || interval <= 0) {
            throw IllegalArgumentException("duration or interval can not less than zero")
        }
        return flow {
            for (i in duration downTo range step interval) {
                emit(i)
                delay((interval * 1000).toLong())
            }
        }
            .onEach { onTick.invoke(it) }
            .onStart { onStart?.invoke() }
            .onCompletion {
                if (it == null) {
                    onEnd?.invoke()
                }
            }
            .flowOn(Dispatchers.Main)
            .launchIn(scope)
    }

}