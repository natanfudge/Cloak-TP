import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.RefSpec
import org.eclipse.jgit.transport.URIish

@Serializable
private data class PullRequest(val title: String, val body: String, val head: String, val base: String)

object Impl {
    private val NormalJson = Json(JsonConfiguration.Stable)


    private class GithubException(message: String) : Exception(message)

    fun createPullRequest(
        repositoryName: String,
        requestingUser: String,
        requestingBranch: String,
        targetBranch: String,
        targetUser: String,
        title: String,
        body: String
    ): String =
        createPullRequestImpl(repositoryName, requestingUser, requestingBranch, targetBranch, targetUser, title, body)

    private fun createPullRequestImpl(
        repositoryName: String,
        requestingUser: String,
        requestingBranch: String,
        targetBranch: String,
        targetUser: String,
        title: String,
        body: String
    ): String {
        val request =
            PullRequest(title = title, base = targetBranch, head = "$requestingUser:$requestingBranch", body = body)
        val postBody = NormalJson.stringify(PullRequest.serializer(), request)


        return createHttpClient().use { client ->
            val httppost = createHttpPost("https://api.github.com/repos/$targetUser/$repositoryName/pulls").apply {
                addHeaderSafe("Authorization", "token 602c85352dbfb648e9c4433c6da4a8d223ad9e68")
                setBody(postBody)
            }
            //Execute and get the response.
            val response = client.send(httppost)
            val entity =
                response.getEntitySafe() ?: throw GithubException("http post request did not return a response")

            entity.getContentString()
        }

    }

    fun getDefaultBranch(repo: String, owner: String): String = getDefaultBranchImpl(repo, owner)

    fun getDefaultBranchImpl(repo: String, owner: String): String {
        return createHttpClient().use { client ->
            val httpget = createHttpGet("https://api.github.com/repos/$owner/$repo")
            httpget.addHeaderSafe("Authorization", "token 602c85352dbfb648e9c4433c6da4a8d223ad9e68")

            //Execute and get the response.
            val response = client.send(httpget)
            val entity = response.entity ?: throw GithubException("http post request did not return a response")

            val responseText = entity.getContentString()
            val serializedResponse = NormalJson.parseJson(responseText) as JsonObject
            serializedResponse["default_branch"]!!.content
        }

    }


}





internal fun Git.deleteBranch(branchName: String) {
    branchDelete().setBranchNames("refs/heads/$branchName").setForce(true).call()
    val refspec = RefSpec().setSource(null).setDestination("refs/heads/$branchName")
    push().setRefSpecs(refspec).setRemote("origin").setValue("Cloak-Bot").call()
}


internal fun Git.push(remoteUrl: String) {
    remoteAdd().setName("origin").setUri(URIish(remoteUrl)).call()
    push().setValue("Cloak-Bot").call()
}



