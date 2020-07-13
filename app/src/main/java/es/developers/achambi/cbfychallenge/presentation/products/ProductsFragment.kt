package es.developers.achambi.cbfychallenge.presentation.products

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.Product
import es.developers.achambi.cbfychallenge.presentation.BaseFragment
import es.developers.achambi.cbfychallenge.presentation.CbfyApplication
import es.developers.achambi.cbfychallenge.presentation.ProductPresenterFactory
import es.developers.achambi.cbfychallenge.presentation.Screen
import es.developers.achambi.cbfychallenge.presentation.cart.CartActivity
import es.developers.achambi.cbfychallenge.presentation.product.ProductDetailActivity
import kotlinx.android.synthetic.main.product_item_layout.view.*
import kotlinx.android.synthetic.main.products_layout.*
import javax.inject.Inject

class ProductsFragment: BaseFragment(),
    ProductsScreen, ProductsAdapterListener {
    @Inject
    lateinit var presenterFactory: ProductPresenterFactory
    lateinit var presenter: ProductsPresenter
    private lateinit var adapter: ProductsAdapter
    companion object {
        const val PRODUCTS_SAVED_INSTANCE_KEY = "products_saved_instance_key"
        fun newInstance(): Fragment {
            return ProductsFragment()
        }
    }
    override val layoutResource: Int
        get() = R.layout.products_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity?.application as CbfyApplication).graph.inject(this)
        presenter = presenterFactory.createPresenter(this, lifecycle)
        adapter = ProductsAdapter(listener = this)
    }

    override fun onViewSetup(view: View) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        product_recycler_view.layoutManager = LinearLayoutManager(context)
        product_recycler_view.adapter = adapter
    }

    override fun onDataSetup() {
        super.onDataSetup()
        presenter.onDataSetup()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.product_detail_menu, menu)
        //TODO: Fix, it'll only work on the current implementation, adding more toolbar actions will break it
        menu.getItem(0).actionView.setOnClickListener {
            startActivity(activity?.let { it1 -> CartActivity.getStartIntent(it1)})
        }
    }

    override fun showProducts(products: List<ProductPresentation>) {
        adapter.list = ArrayList(products)
        adapter.notifyDataSetChanged()
    }

    override fun onProductSelected(productCode: String) {
        presenter.productSelected(productCode)
    }

    override fun navigateToDetails(product: Product) {
        startActivity(activity?.let { ProductDetailActivity.getStartIntent(it, product) })
    }

    override fun showError() {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(PRODUCTS_SAVED_INSTANCE_KEY, adapter.list)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.list = savedInstanceState.getParcelableArrayList(PRODUCTS_SAVED_INSTANCE_KEY)!!
        adapter.notifyDataSetChanged()
    }
}

interface ProductsScreen: Screen {
    fun showProducts(products: List<ProductPresentation>)
    fun navigateToDetails(product: Product)
    fun showError()
}

interface ProductsAdapterListener {
    fun onProductSelected(productCode: String)
}

class ProductsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ProductsAdapter(var list: ArrayList<ProductPresentation> = ArrayList(),
                      private val listener: ProductsAdapterListener)
    : RecyclerView.Adapter<ProductsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item_layout, parent, false)
        val holder = ProductsHolder(rootView)
        rootView.product_item_image_view.setOnClickListener {
            val position = holder.adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onProductSelected(list[position].code)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.itemView.product_item_name_text.text = list[position].name
        holder.itemView.product_item_price_text.text = list[position].price
        holder.itemView.product_item_discount_image.setImageResource(list[position].discountImage)
    }

}