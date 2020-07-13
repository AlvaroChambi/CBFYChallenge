package es.developers.achambi.cbfychallenge.presentation.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.developers.achambi.cbfychallenge.R
import es.developers.achambi.cbfychallenge.domain.CartProduct
import es.developers.achambi.cbfychallenge.domain.CartProducts
import es.developers.achambi.cbfychallenge.presentation.BaseFragment
import es.developers.achambi.cbfychallenge.presentation.CartPresenterFactory
import es.developers.achambi.cbfychallenge.presentation.CbfyApplication
import es.developers.achambi.cbfychallenge.presentation.Screen
import es.developers.achambi.cbfychallenge.presentation.products.PresentationBuilder
import es.developers.achambi.cbfychallenge.presentation.products.ProductPresentation
import kotlinx.android.synthetic.main.cart_fragment_layout.*
import kotlinx.android.synthetic.main.cart_item_layout.view.*
import javax.inject.Inject

class CartFragment: BaseFragment(), CartScreen, CartItemListener {
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

    override fun onViewSetup(view: View) {

    }

    override fun showCartItems(cartPresentation: CartPresentation) {
        adapter = ItemsAdapter(cartPresentation.items, this)
        cart_item_recycler.adapter = adapter
        adapter.notifyDataSetChanged()

        cart_total_price_text.text = cartPresentation.total
        cart_subtotal_price_text.text = cartPresentation.subtotal
        twoforone_price_group.visibility = cartPresentation.showTwoForOneValue
        twoforone_discount_group.visibility = cartPresentation.showTwoforOneInfo
        threeormore_price_group.visibility = cartPresentation.showThreeOrMoreValue
        threeormore_discount_group.visibility = cartPresentation.showThreeOrMoreInfo
        cart_discount_two_text.text = cartPresentation.twoForOneDiscount
        cart_discount_three_text.text = cartPresentation.threeOrMoreDiscount
    }

    override fun showUpdateError() {
        Toast.makeText(activity, "Something failed :(. Please try again.", Toast.LENGTH_LONG)
            .show()
    }

    override fun onDeleteSelected(id: Long) {
        presenter.removeItem(id)
    }

    override fun onDecreaseSelected(id: Long) {
        presenter.decreaseSelected(id)
    }

    override fun onIncreaseSelected(id: Long) {
        presenter.increaseSelected(id)
    }
}

interface CartScreen: Screen {
    fun showCartItems(cartPresentation: CartPresentation)
    fun showUpdateError()
}

class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView)

interface CartItemListener {
    fun onDeleteSelected(id: Long)
    fun onDecreaseSelected(id: Long)
    fun onIncreaseSelected(id: Long)
}

class ItemsAdapter(private val list: List<CartItemPresentation>,
                   private val listener: CartItemListener): RecyclerView.Adapter<ItemHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(
            R.layout.cart_item_layout, parent, false)
        val holder = ItemHolder(rootView)
        rootView.cart_item_delete_image.setOnClickListener {
            val position = holder.adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onDeleteSelected(list[position].id)
            }
        }
        rootView.cart_item_decrease_image.setOnClickListener {
            val position = holder.adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onDecreaseSelected(list[position].id)
            }
        }
        rootView.cart_item_increase_image.setOnClickListener {
            val position = holder.adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onIncreaseSelected(list[position].id)
            }
        }
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.itemView.cart_item_name_text.text = list[position].productPresentation.name
        holder.itemView.cart_item_price_text.text = list[position].totalPrice
        holder.itemView.cart_item_quantity_text.text = list[position].quantity
    }

}

//TODO explain why this is better than having the strings of it
class CartItemPresentation(val  id: Long,
                            val productPresentation: ProductPresentation,
                           val quantity: String, val totalPrice: String)

class CartPresentation(val items: List<CartItemPresentation>, val subtotal: String,
val total: String, val showTwoforOneInfo: Int, val showThreeOrMoreInfo: Int,
val showTwoForOneValue: Int, val showThreeOrMoreValue: Int,
val twoForOneDiscount: String, val threeOrMoreDiscount: String)

class CartItemBuilder@Inject constructor( private val productBuilder: PresentationBuilder) {
    fun build(items: List<CartProduct>): List<CartItemPresentation> {
        val list = ArrayList<CartItemPresentation>()
        items.forEach {
            list.add( CartItemPresentation( it.id, productBuilder.build(it.product),
                it.quantity.toString(), it.totalPrice.toString()) )
        }
        return list
    }
}

class CartPresentationBuilder@Inject constructor(private val itemBuilder: CartItemBuilder) {
    fun build(cartProducts: CartProducts): CartPresentation {
        val items = itemBuilder.build(cartProducts.cartProducts)
        val total = cartProducts.total.toString()
        val subtotal = cartProducts.baseTotal.toString()
        var showTwoforOneInfo = View.GONE
        var showTwoForOneValue = View.GONE
        var showThreeOrMoreInfo = View.GONE
        var showThreeOrMoreValue = View.GONE

        if(cartProducts.canApplyTwoForOne) {
            if(cartProducts.twoForOnePrice.toInt() > 0) {
                showTwoForOneValue = View.VISIBLE
            } else {
                showTwoforOneInfo = View.VISIBLE
            }
        }

        if(cartProducts.canApplyThreeOrMore) {
            if(cartProducts.threeOrMorePrice.toInt() > 0) {
                showThreeOrMoreValue = View.VISIBLE
            } else {
                showThreeOrMoreInfo = View.VISIBLE
            }
        }

        return CartPresentation(items, subtotal, total, showTwoforOneInfo, showThreeOrMoreInfo,
            showTwoForOneValue, showThreeOrMoreValue, cartProducts.twoForOnePrice.toString(),
            cartProducts.threeOrMorePrice.toString())
    }

}