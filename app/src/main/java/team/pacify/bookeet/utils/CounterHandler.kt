package team.pacify.bookeet.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.view.MotionEvent
import android.view.View

class CounterHandler private constructor(builder: Builder) {
    val handler = Handler()
    private val incrementalView: View?
    private val decrementalView: View?
    private var minRange = -1
    private var maxRange = -1
    private var startNumber = 0
    private var counterStep = 1
    private var counterDelay = 50
    private var isCycle = false
    private var autoIncrement = false
    private var autoDecrement = false
    private val listener: CounterListener?

    private val counterRunnable: Runnable = object : Runnable {
        override fun run() {
            if (autoIncrement) {
                increment()
                handler.postDelayed(this, counterDelay.toLong())
            } else if (autoDecrement) {
                decrement()
                handler.postDelayed(this, counterDelay.toLong())
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initIncrementalView() {
        incrementalView!!.setOnClickListener { increment() }
        incrementalView.setOnLongClickListener {
            autoIncrement = true
            handler.postDelayed(counterRunnable, counterDelay.toLong())
            false
        }
        incrementalView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initDecrementalView() {
        decrementalView!!.setOnClickListener { decrement() }
        decrementalView.setOnLongClickListener {
            autoDecrement = true
            handler.postDelayed(counterRunnable, counterDelay.toLong())
            false
        }
        decrementalView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false
            }
            false
        }
    }

    private fun increment() {
        var number = startNumber
        if (maxRange != -1) {
            if (number + counterStep <= maxRange) {
                number += counterStep
            } else if (isCycle) {
                number = if (minRange == -1) 0 else minRange
            }
        } else {
            number += counterStep
        }
        if (number != startNumber && listener != null) {
            startNumber = number
            listener.onIncrement(incrementalView, startNumber)
        }
    }

    private fun decrement() {
        var number = startNumber
        if (minRange != -1) {
            if (number - counterStep >= minRange) {
                number -= counterStep
            } else if (isCycle) {
                number = if (maxRange == -1) 0 else maxRange
            }
        } else {
            number -= counterStep
        }
        if (number != startNumber && listener != null) {
            startNumber = number
            listener.onDecrement(decrementalView, startNumber)
        }
    }

    interface CounterListener {
        fun onIncrement(view: View?, number: Int)
        fun onDecrement(view: View?, number: Int)
    }

    class Builder {
        var incrementalView: View? = null
        var decrementalView: View? = null
        var minRange = -1
        var maxRange = -1
        var startNumber = 0
        var counterStep = 1
        var counterDelay = 50
        var isCycle = false
        var listener: CounterListener? = null
        fun incrementalView(`val`: View?): Builder {
            incrementalView = `val`
            return this
        }

        fun decrementalView(`val`: View?): Builder {
            decrementalView = `val`
            return this
        }

        fun minRange(`val`: Int): Builder {
            minRange = `val`
            return this
        }

        fun maxRange(`val`: Int): Builder {
            maxRange = `val`
            return this
        }

        fun startNumber(`val`: Int): Builder {
            startNumber = `val`
            return this
        }

        fun listener(`val`: CounterListener?): Builder {
            listener = `val`
            return this
        }

        fun build(): CounterHandler {
            return CounterHandler(this)
        }
    }

    fun updateCounter(start: Int) {
        startNumber = start
    }

    init {
        incrementalView = builder.incrementalView
        decrementalView = builder.decrementalView
        minRange = builder.minRange
        maxRange = builder.maxRange
        startNumber = builder.startNumber
        counterStep = builder.counterStep
        counterDelay = builder.counterDelay
        isCycle = builder.isCycle
        listener = builder.listener
        initDecrementalView()
        initIncrementalView()
        if (listener != null) {
            listener.onIncrement(incrementalView, startNumber)
            listener.onDecrement(decrementalView, startNumber)
        }
    }
}