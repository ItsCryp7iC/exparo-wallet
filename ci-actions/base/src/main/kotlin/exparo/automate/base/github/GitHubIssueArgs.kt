package exparo.automate.base.github

import arrow.core.Either
import arrow.core.raise.either
import exparo.automate.base.Constants
import exparo.automate.base.ensureArgument
import exparo.automate.base.github.model.GitHubIssueNumber
import exparo.automate.base.github.model.GitHubPAT
import exparo.automate.base.parseAsMap

data class GitHubIssueArgs(
    val pat: GitHubPAT,
    val issueNumber: GitHubIssueNumber
)

fun parseArgs(argsList: List<String>): Either<String, GitHubIssueArgs> = either {
    val args = argsList.parseAsMap()

    val gitHubPAT = args.ensureArgument(Constants.ARG_GITHUB_PAT)
    val issueId = args.ensureArgument(Constants.ARG_ISSUE_NUMBER)

    GitHubIssueArgs(
        pat = GitHubPAT.from(gitHubPAT).bind(),
        issueNumber = GitHubIssueNumber.from(issueId).bind(),
    )
}