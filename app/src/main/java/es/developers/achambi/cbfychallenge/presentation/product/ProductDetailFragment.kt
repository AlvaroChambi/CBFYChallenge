package es.developers.achambi.cbfychallenge.presentation.product

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.presentation.*
import es.developers.achambi.cbfychallenge.presentation.cart.CartActivity
import kotlinx.android.synthetic.main.product_details_layout.*
import javax.inject.Inject

class ProductDetailFragment: BaseFragment(), ProductDetailScreen {
    companion object {
        private val PRODUCT_KEY = "PRODUCT_KEY"
        fun newInstance(bundle: Bundle?): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.arguments =  bundle
            return fragment
        }

        fun buildArguments(product: Product): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_KEY, product)
            return bundle
        }
    }

    override val layoutResource: Int
        get() = R.layout.product_details_layout

    private lateinit var product: Product
    @Inject
    lateinit var presenterFactory: DetailsPresenterFactory
    private lateinit var presenter: ProductDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        //will always be available, there's no other way to invoke this fragment
        product = arguments?.getParcelable(PRODUCT_KEY)!!
        (activity?.application as CbfyApplication).graph.inject(this)
        presenter = presenterFactory.createPresenter(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(products_toolbar)
        products_toolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24)
        products_toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        presenter.onViewCreated(product)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.product_detail_menu, menu)
        menu.getItem(0).actionView.setOnClickListener {
            startActivity(activity?.let { it1 -> CartActivity.getStartIntent(it1) })
        }
    }

    override fun showProduct(presentation: ProductDetailPresentation) {
        product_details_name_text.text = presentation.name
        product_details_price_text.text = presentation.price
        product_details_discount_image.setImageResource(presentation.discountIcon)
        product_detail_description_text.text = presentation.discountText
    }
}

interface ProductDetailScreen: Screen {
    fun showProduct(presentation: ProductDetailPresentation)
}

class ProductDetailPresentation(val name: String,
                                val price: String,
                                val discountIcon: Int,
                                val discountText: String)