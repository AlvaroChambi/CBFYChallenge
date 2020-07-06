package es.developers.achambi.cbfychallenge

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingResource
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import es.developers.achambi.cbfychallenge.presentation.BaseExecutor
import es.developers.achambi.cbfychallenge.presentation.CbfyApplication
import es.developers.achambi.cbfychallenge.presentation.Executor
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
abstract class BaseUITest {
    lateinit var idlingResource : IdlingResource

    @Before
    open fun setup() {

        Espresso.registerIdlingResources(idlingResource)
    }

    open fun beforeActivity(){}
    open fun provideActivityIntent(): Intent{
        return Intent()
    }

    @After
    fun after() {
        Espresso.unregisterIdlingResources(idlingResource)
    }
}

class TestRule<T : Activity?>(activityClass: Class<T>?,
                              private val baseTest: BaseUITest)
    : ActivityTestRule<T>(activityClass, false) {
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        baseTest.beforeActivity()
    }

    override fun getActivityIntent(): Intent {
        return baseTest.provideActivityIntent()
    }
}