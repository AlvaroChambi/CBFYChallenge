package es.developers.achambi.cbfychallenge.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment

class ProductsActivity: BaseActivity() {
    override fun getFragment(args: Bundle?): Fragment {
        return ProductsFragment.newInstance()
    }
}