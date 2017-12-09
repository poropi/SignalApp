package jp.ne.poropi.signalapp.activity.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jp.ne.poropi.signalapp.R
import kotlinx.android.synthetic.main.fragment_signal_walker.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by porop on 2017/12/10.
 */
class WalkerSignalFragment: Fragment() {

    private lateinit var mMainRedImageOn: ImageView

    private lateinit var mMainBlueImageOn: ImageView

    var mBlinkSignal: Disposable? = null

    var mIntervalTimerDisposable: Disposable? = null

    var mSignalId = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_signal_walker, null)

        mMainRedImageOn = view.activity_main_red_on_image
        mMainBlueImageOn = view.activity_main_blue_on_image

        // 青にする
        setSignal(1)

        mIntervalTimerDisposable = Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map {
                    ++mSignalId
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    signalIdObserver(it)
                }
        return view
    }

    private fun signalIdObserver(it: Int?) {
        when (it) {
            1 -> {
                // 青点滅状態にする
                mBlinkSignal = doBlinkBlueSignal()
            }
            2 -> {
                // 青点滅状態解除
                mBlinkSignal?.dispose()
                // 赤にする
                setSignal(2)
            }
            else -> {
                mSignalId = 0
                // 青にする
                setSignal(1)
            }
        }
    }

    override fun onDestroyView() {
        mIntervalTimerDisposable?.dispose()
        mBlinkSignal?.dispose()

        super.onDestroyView()
    }

    private fun doBlinkBlueSignal(): Disposable? {
        var isSignal = true
        // 300ミリ秒ごとに青信号の点滅をする
        return Observable.interval(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map { !isSignal }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isSignal = it
                    setSignal(if (it) 1 else 0)
                }
    }

    private fun setSignal(signal: Int) {
        when (signal) {
            0 -> {
                // 赤、青ともに未点灯
                mMainRedImageOn.visibility = View.INVISIBLE
                mMainBlueImageOn.visibility = View.INVISIBLE
            }
            1 -> {
                // 青を点灯
                mMainRedImageOn.visibility = View.INVISIBLE
                mMainBlueImageOn.visibility = View.VISIBLE
            }
            2 -> {
                // 赤を点灯
                mMainRedImageOn.visibility = View.VISIBLE
                mMainBlueImageOn.visibility = View.INVISIBLE
            }
            3 -> {
                // 赤、青ともに点灯（通常ありえない）
                mMainRedImageOn.visibility = View.VISIBLE
                mMainBlueImageOn.visibility = View.VISIBLE
            }
        }
    }
}