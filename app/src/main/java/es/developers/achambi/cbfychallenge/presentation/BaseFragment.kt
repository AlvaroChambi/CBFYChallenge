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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewSetup(view)
        //savedinstancestate will have contents if the fragment has being recreated
        if(savedInstanceState == null) {
            onDataSetup()
        }
    }

    /**
     * Called whenever the view is created, meant to setup any view state that is required, data
     * that is not static wont be accessed at this step
     */
    abstract fun onViewSetup(view: View)

    /**
     * Called whenever the fragment state needs to be recreated, won't be called if there's no
     * available bundle
     */
    open fun onRestoreInstanceState(savedInstanceState: Bundle){}
    /**
     * Called one per fragment creation, won't be called if the fragment is recreated, this step is
     * meant to request data that is going to be needed to display the fragment initial state
     */
    open fun onDataSetup(){}

    abstract val layoutResource: Int
}