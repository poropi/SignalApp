package jp.ne.poropi.signalapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import jp.ne.poropi.signalapp.R
import jp.ne.poropi.signalapp.activity.fragment.WalkerSignalFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by porop on 2017/12/09.
 */
open class SignalActivity : AppCompatActivity() {

    private lateinit var mSignalFragmentArea: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSignalFragmentArea = activity_main_fragment_area

        val a = supportFragmentManager.beginTransaction()
        a.add(R.id.activity_main_fragment_area, WalkerSignalFragment())
        a.commit()

    }


}