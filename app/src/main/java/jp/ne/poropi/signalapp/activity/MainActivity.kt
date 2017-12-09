package jp.ne.poropi.signalapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.ne.poropi.signalapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * Created by porop on 2017/12/09.
 */
open class MainActivity: AppCompatActivity() {

    var mMainRedImageOn: ImageView? = null

    var mMainBlueImageOn: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainRedImageOn = activity_main_red_on_image
        mMainBlueImageOn = activity_main_blue_on_image

        mMainRedImageOn!!.visibility = View.INVISIBLE
        mMainBlueImageOn!!.visibility = View.INVISIBLE

        setBlueBlinkToRedSignal()

    }

    fun setBlueToBlincBlueSignal() {
        // 青にする
        mMainRedImageOn!!.visibility = View.INVISIBLE
        mMainBlueImageOn!!.visibility = View.VISIBLE

        // 5秒後に青を点滅させる
        Observable.timer(5, TimeUnit.SECONDS).subscribe {
            setBlueBlinkToRedSignal()
        }
    }

    fun setBlueBlinkToRedSignal() {
        var isSignal = true
        // 300ミリ秒ごとに青信号の点滅をする
        val signal = Observable.interval(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map { !isSignal }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isSignal = it
                    if (it) {
                        mMainRedImageOn!!.visibility = View.INVISIBLE
                        mMainBlueImageOn!!.visibility = View.VISIBLE
                    } else {
                        mMainRedImageOn!!.visibility = View.INVISIBLE
                        mMainBlueImageOn!!.visibility = View.INVISIBLE
                    }
                }
        // 5秒後に赤に切り替える
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    signal.dispose()
                    mMainRedImageOn!!.visibility = View.VISIBLE
                    mMainBlueImageOn!!.visibility = View.INVISIBLE

                    // 5秒後に青に切り替える
                    Observable.timer(5, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe{setBlueToBlincBlueSignal()}
                }
        return
    }


}