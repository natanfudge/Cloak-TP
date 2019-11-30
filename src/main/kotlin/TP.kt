import org.eclipse.jgit.api.Git

object TP {

    fun createPullRequest(
        repositoryName: String,
        requestingUser: String,
        requestingBranch: String,
        targetBranch: String,
        targetUser: String,
        title: String,
        body: String
    ): String =
        Impl.createPullRequest(repositoryName, requestingUser, requestingBranch, targetBranch, targetUser, title, body)

    fun getDefaultBranch(repo: String, owner: String): String = Impl.getDefaultBranch(repo, owner)

    fun push(originUrl: String, git: Git) = git.push(originUrl)

    fun deleteBranch(branchName: String, git: Git) = git.deleteBranch(branchName)

}

