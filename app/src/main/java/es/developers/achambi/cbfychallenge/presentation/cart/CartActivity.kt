package es.developers.achambi.cbfychallenge.presentation.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import es.developers.achambi.cbfychallenge.presentation.BaseActivity

class CartActivity: BaseActivity() {
    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, CartActivity::class.java)
        }
    }

    override fun getFragment(args: Bundle?): Fragment {
        return CartFragment.newInstance()
    }
}