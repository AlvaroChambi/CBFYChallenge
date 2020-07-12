package es.developers.achambi.cbfychallenge.presentation.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.CartProduct
import es.developers.achambi.cbfychallenge.presentation.BaseFragment
import es.developers.achambi.cbfychallenge.presentation.CartPresenterFactory
import es.developers.achambi.cbfychallenge.presentation.CbfyApplication
import es.developers.achambi.cbfychallenge.presentation.Screen
import es.developers.achambi.cbfychallenge.presentation.products.PresentationBuilder
import es.developers.achambi.cbfychallenge.presentation.products.ProductPresentation
import kotlinx.android.synthetic.main.cart_fragment_layout.*
import kotlinx.android.synthetic.main.cart_item_layout.view.*
import kotlinx.android.synthetic.main.product_item_layout.view.*
import javax.inject.Inject

class CartFragment: BaseFragment(), CartScreen {
    @Inject
    lateinit var  presenterFactory: CartPresenterFactory
    private lateinit var presenter: CartPresenter
    private lateinit var adapter: ItemsAdapter

    companion object {
        fun newInstance(): CartFragment {
            return CartFragment()
        }
    }
    override val layoutResource: Int
        get() = R.layout.cart_fragment_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as CbfyApplication).graph.inject(this)
        presenter = presenterFactory.createPresenter(this, lifecycle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cart_item_recycler.layoutManager = LinearLayoutManager(context)
        presenter.onViewCreated()
    }

    override fun showCartItems(items: List<CartItemPresentation>) {
        adapter = ItemsAdapter(items)
        cart_item_recycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}

interface CartScreen: Screen {
    fun showCartItems(items: List<CartItemPresentation>)
}

class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView)

class ItemsAdapter(private val list: List<CartItemPresentation>): RecyclerView.Adapter<ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.cart_item_layout, parent, false)
        val holder = ItemHolder(rootView)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemView.cart_item_name_text.text = list[position].productPresentation.name
        holder.itemView.cart_item_price_text.text = list[position].productPresentation.price
        holder.itemView.cart_item_quantity_text.text = list[position].quantity
    }

}

//TODO explain why this is better than having the strings of it
class CartItemPresentation(val productPresentation: ProductPresentation,
                           val quantity: String)

class CartItemBuilder@Inject constructor
    (private val context: Context, private val productBuilder: PresentationBuilder) {
    fun build(items: List<CartProduct>): List<CartItemPresentation> {
        val list = ArrayList<CartItemPresentation>()
        items.forEach {
            list.add( CartItemPresentation( productBuilder.build(it.product),
                it.quantity.toString()) )
        }
        return list
    }
}