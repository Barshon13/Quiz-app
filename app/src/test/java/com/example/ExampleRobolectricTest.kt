package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.LifelineType
import com.example.data.model.QuizCategory
import com.example.data.repository.QuizRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Quiz Master", appName)
  }

  @Test
  fun `verify quiz repository question bank contains domestic and international questions`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = QuizRepository(context)

    assertTrue("Question bank should not be empty", repository.questionBank.isNotEmpty())

    val bdLiberationQuestions = repository.questionBank.filter { it.category == QuizCategory.BD_LIBERATION }
    assertTrue("Should contain Liberation War questions", bdLiberationQuestions.isNotEmpty())

    val bdQuestions = repository.questionBank.filter { it.category.isDomestic }
    val worldQuestions = repository.questionBank.filter { !it.category.isDomestic }

    assertTrue("Should have domestic Bangladesh questions", bdQuestions.isNotEmpty())
    assertTrue("Should have international world questions", worldQuestions.isNotEmpty())

    bdLiberationQuestions.forEach { q ->
      assertEquals("Each question must have 4 options", 4, q.options.size)
      assertTrue("Correct index must be within 0..3", q.correctIndex in 0..3)
      assertTrue("Explanation must not be blank", q.explanation.isNotBlank())
    }
  }

  @Test
  fun `verify lifeline purchase and heart deduction logic`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = QuizRepository(context)

    val initialHearts = repository.userProfile.value.hearts
    val successDeduct = repository.deductHeart()
    assertTrue(successDeduct)
    assertEquals(initialHearts - 1, repository.userProfile.value.hearts)

    repository.refillHearts()
    assertEquals(repository.userProfile.value.maxHearts, repository.userProfile.value.hearts)
  }
}
