package es.developers.achambi.cbfychallenge.presentation.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import es.developers.achambi.cbfychallenge.presentation.BaseActivity

class ProductsActivity: BaseActivity() {
    override fun getFragment(args: Bundle?): Fragment {
        return ProductsFragment.newInstance()
    }
}