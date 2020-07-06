package es.developers.achambi.cbfychallenge.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import dagger.*
import es.developers.achambi.cbfychallenge.BaseUITest
import es.developers.achambi.cbfychallenge.ProductsUITest
import es.developers.achambi.cbfychallenge.data.MockRepository
import es.developers.achambi.cbfychallenge.data.ProductsService
import es.developers.achambi.cbfychallenge.data.Repository
import es.developers.achambi.cbfychallenge.domain.ProductsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(modules = [ServiceModule::class, RepositoryModule::class])
interface TestComponent {
    fun inject(uiTest: ProductsUITest)
}

class PresenterFactory @Inject constructor(private val executor: Executor,
                                           private val useCase: ProductsUseCase,
                                           private val builder: PresentationBuilder) {
    fun createPresenter(screen: ProductsScreen, lifecycle: Lifecycle): ProductsPresenter {
        return ProductsPresenter(screen, lifecycle, executor, useCase, builder)
    }
}

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(repository: MockRepository): Repository
}

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideExecutor(): Executor {
        return Executor.buildExecutor()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(): ProductsService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ProductsService::class.java)
    }
}

class CbfyApplication: Application() {
    companion object {
        lateinit var instance: CbfyApplication
    }

    val graph: TestComponent by lazy {
        TestComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}