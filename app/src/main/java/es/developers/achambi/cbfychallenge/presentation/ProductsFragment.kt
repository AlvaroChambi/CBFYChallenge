package es.developers.achambi.cbfychallenge.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import es.developers.achambi.cbfychallenge.R
import javax.inject.Inject

class ProductsFragment: BaseFragment(),
    ProductsScreen {
    @Inject
    lateinit var presenterFactory: PresenterFactory
    lateinit var presenter: ProductsPresenter
    companion object {
        fun newInstance(): Fragment {
            return ProductsFragment()
        }
    }
    override val layoutResource: Int
        get() = R.layout.products_layout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as CbfyApplication).graph.inject(this)
        presenter = presenterFactory.createPresenter(this, lifecycle)

        presenter.onViewCreated()
    }

    override fun doSomething(text: String) {
        Log.i("PRODUCTS", text)
    }
}

interface ProductsScreen: Screen {
    fun doSomething(text: String)
}