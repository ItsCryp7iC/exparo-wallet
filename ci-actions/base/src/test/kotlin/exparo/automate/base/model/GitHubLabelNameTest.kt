package exparo.automate.base.model

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.shouldBe
import exparo.automate.base.github.model.GitHubLabelName
import org.junit.Test

class GitHubLabelNameTest {
    @Test
    fun `invalid username`() {
        GitHubLabelName.from("").shouldBeLeft()
        GitHubLabelName.from(" ").shouldBeLeft()
        GitHubLabelName.from("  ").shouldBeLeft()
    }

    @Test
    fun `valid username`() {
        // given
        val rawLabel = " devexp "

        // when
        val res = GitHubLabelName.from(rawLabel)

        // then
        res.shouldBeRight().value shouldBe "devexp"
    }
}
