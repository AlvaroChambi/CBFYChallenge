package es.developers.achambi.cbfychallenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import es.developers.achambi.cbfychallenge.presentation.CbfyApplication
import es.developers.achambi.cbfychallenge.presentation.Executor
import es.developers.achambi.cbfychallenge.presentation.ProductsActivity
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class ProductsUITest: BaseUITest() {
    @Inject
    lateinit var executor: Executor
    @get:Rule
    public val customActivityTestRule: TestRule<ProductsActivity> = TestRule(
        ProductsActivity::class.java, this)

    override fun setup() {
        CbfyApplication.instance.graph.inject(this)
        idlingResource = ExecutorIdlingResource(executor)
        super.setup()
    }

    @Test
    fun test() {
        onView(withId(R.id.fragment_frame)).check(matches(isDisplayed()))
    }

    @Test
    fun test2() {
        onView(withId(R.id.fragment_frame)).check(matches(isDisplayed()))
    }

}