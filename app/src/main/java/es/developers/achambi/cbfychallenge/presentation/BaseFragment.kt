package es.developers.achambi.cbfychallenge.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResource, container, false)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            onRestoreInstanceState(it)
        }
    }

    /**
     * Called whenever the fragment state needs to be recreated, won't be called if there's no
     * available bundle
     */
    open fun onRestoreInstanceState(savedInstanceState: Bundle){}

    abstract val layoutResource: Int
}