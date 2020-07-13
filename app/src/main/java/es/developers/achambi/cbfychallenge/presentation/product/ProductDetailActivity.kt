package es.developers.achambi.cbfychallenge.presentation.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.presentation.BaseActivity

class ProductDetailActivity: BaseActivity() {
    companion object {
        fun getStartIntent(context: Context, productCode: String): Intent {
            val intent = Intent(context, ProductDetailActivity::class.java)
            val args = ProductDetailFragment.buildArguments(productCode)
            intent.putExtra(BASE_ARGUMENTS, args)
            return intent
        }
    }

    override fun getFragment(args: Bundle?): Fragment {
        return ProductDetailFragment.newInstance(args)
    }
}