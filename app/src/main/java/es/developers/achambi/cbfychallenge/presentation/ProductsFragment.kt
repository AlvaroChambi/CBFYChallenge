package es.developers.achambi.cbfychallenge.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.developers.achambi.cbfychallenge.R
import kotlinx.android.synthetic.main.product_item_layout.view.*
import kotlinx.android.synthetic.main.products_layout.*
import javax.inject.Inject

class ProductsFragment: BaseFragment(),
    ProductsScreen {
    @Inject
    lateinit var presenterFactory: PresenterFactory
    lateinit var presenter: ProductsPresenter
    private lateinit var adapter: ProductsAdapter
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

        product_recycler_view.layoutManager = LinearLayoutManager(context)
        presenter.onViewCreated()
    }

    override fun doSomething(products: List<ProductPresentation>) {
        adapter = ProductsAdapter(products)
        product_recycler_view.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}

interface ProductsScreen: Screen {
    fun doSomething(products: List<ProductPresentation>)
}

class ProductsHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ProductsAdapter(private val list: List<ProductPresentation>): RecyclerView.Adapter<ProductsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_item_layout, parent, false)
        return ProductsHolder(rootView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) {
        holder.itemView.product_item_name_text.text = list[position].name
        holder.itemView.product_item_price_text.text = list[position].price
    }

}