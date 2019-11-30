import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

internal fun createHttpPost(url: String) = HttpPost(url)
internal fun createHttpGet(url: String) = HttpGet(url)
internal fun createHttpClient(): CloseableHttpClient = HttpClients.createDefault()
internal fun HttpPost.addHeaderSafe(key: String, value: String) = addHeader(key, value)
internal fun HttpGet.addHeaderSafe(key: String, value: String) = addHeader(key, value)
internal fun HttpPost.setBody(content: String) {
    entity = StringEntity(content)
}

internal fun HttpClient.send(post: HttpPost): HttpResponse = execute(post)
internal fun HttpClient.send(get: HttpGet): HttpResponse = execute(get)
internal fun HttpEntity.getContentString(): String = EntityUtils.toString(this)
internal fun HttpResponse.getEntitySafe(): HttpEntity? = entity

internal fun PushCommand.setValue(username: String) =
    setCredentialsProvider(UsernamePasswordCredentialsProvider(username, "602c85352dbfb648e9c4433c6da4a8d223ad9e68"))