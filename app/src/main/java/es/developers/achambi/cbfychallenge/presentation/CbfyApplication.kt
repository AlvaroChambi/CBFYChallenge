package es.developers.achambi.cbfychallenge.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import dagger.*
import es.developers.achambi.cbfychallenge.data.CbfyRepository
import es.developers.achambi.cbfychallenge.data.ProductsService
import es.developers.achambi.cbfychallenge.data.Repository
import es.developers.achambi.cbfychallenge.data.database.AppDatabase
import es.developers.achambi.cbfychallenge.data.database.DiscountEntity
import es.developers.achambi.cbfychallenge.domain.CartUseCase
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import es.developers.achambi.cbfychallenge.presentation.cart.CartFragment
import es.developers.achambi.cbfychallenge.presentation.cart.CartItemBuilder
import es.developers.achambi.cbfychallenge.presentation.cart.CartPresenter
import es.developers.achambi.cbfychallenge.presentation.cart.CartScreen
import es.developers.achambi.cbfychallenge.presentation.product.DetailsPresentationBuilder
import es.developers.achambi.cbfychallenge.presentation.product.ProductDetailFragment
import es.developers.achambi.cbfychallenge.presentation.product.ProductDetailPresenter
import es.developers.achambi.cbfychallenge.presentation.product.ProductDetailScreen
import es.developers.achambi.cbfychallenge.presentation.products.PresentationBuilder
import es.developers.achambi.cbfychallenge.presentation.products.ProductsFragment
import es.developers.achambi.cbfychallenge.presentation.products.ProductsPresenter
import es.developers.achambi.cbfychallenge.presentation.products.ProductsScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, RepositoryModule::class])
interface ComponentGraph {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ComponentGraph
    }
    fun inject(fragment: ProductsFragment)
    fun inject(fragment: ProductDetailFragment)
    fun inject(fragment: CartFragment)
}

class ProductPresenterFactory @Inject constructor(private val executor: Executor,
                                                  private val useCase: ProductsUseCase,
                                                  private val builder: PresentationBuilder
) {
    fun createPresenter(screen: ProductsScreen, lifecycle: Lifecycle): ProductsPresenter {
        return ProductsPresenter(
            screen,
            lifecycle,
            executor,
            useCase,
            builder
        )
    }
}

class DetailsPresenterFactory@Inject constructor(private val executor: Executor,
                                                 private val builder: DetailsPresentationBuilder) {
    fun createPresenter(screen: ProductDetailScreen, lifecycle: Lifecycle): ProductDetailPresenter {
        return ProductDetailPresenter(screen, lifecycle, executor, builder)
    }
}

class CartPresenterFactory@Inject constructor(private val executor: Executor,
                           private val useCase: CartUseCase, private val builder: CartItemBuilder) {
    fun createPresenter(screen: CartScreen, lifecycle: Lifecycle): CartPresenter {
        return CartPresenter(screen, lifecycle, executor, useCase, builder)
    }
}

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(repository: CbfyRepository): Repository
}

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideExecutor(): Executor {
        return Executor.buildExecutor()
    }

    //TODO move to gradle or resources
    @Provides
    @Singleton
    fun provideRetrofitService(): ProductsService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ProductsService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        //TODO just be done just on first run of the app
        val database = Room.databaseBuilder(context, AppDatabase::class.java, "cbfydatabase")
            .build()
        database.discountsDao().insert(DiscountEntity("TWO_FOR_ONE", "VOUCHER"))
        database.discountsDao().insert(DiscountEntity("THREE_MORE", "TSHIRT"))
        return database
    }
}

class CbfyApplication: Application() {
    val graph: ComponentGraph by lazy {
        DaggerComponentGraph.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}