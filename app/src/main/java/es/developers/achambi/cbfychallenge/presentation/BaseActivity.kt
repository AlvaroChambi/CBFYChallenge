package es.developers.achambi.cbfychallenge.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.developers.achambi.cbfychallenge.R

abstract class BaseActivity: AppCompatActivity() {
    companion object {
        val BASE_ARGUMENTS = "base_arguments"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        //TODO why?
        if(savedInstanceState == null) {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()

            transaction.add(
                R.id.fragment_frame, getFragment(intent.getBundleExtra(
                    BASE_ARGUMENTS
                ))).commit()
        }
    }

    abstract fun getFragment(args: Bundle?): Fragment
}