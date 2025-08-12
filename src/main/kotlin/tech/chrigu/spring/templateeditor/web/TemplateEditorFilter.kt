package tech.chrigu.spring.templateeditor.web

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

// TODO: Move to own library
internal class TemplateEditorFilter(private val resourceLoader: ResourceLoader) : OncePerRequestFilter() {
    private val html = Html()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return !request.parameterMap.containsKey("edit")
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val responseWrapper = ContentCachingResponseWrapper(response)
        filterChain.doFilter(request, responseWrapper)
        if (responseWrapper.contentType.contains("text/html")) {
            val document = getDocument(responseWrapper)
            val (href, resource) = getLocalCss(document)
            html.render(document, resource, href, request.getAttribute(CsrfToken::class.java.name) as CsrfToken, response.writer)
        } else {
            responseWrapper.copyBodyToResponse()
        }
    }

    private fun getLocalCss(document: Document): Pair<String?, Resource?> {
        val cssLink = document.select("link[rel=stylesheet]")
            .firstOrNull { it.attr("href").startsWith("/css") } // TODO: Make configurable
            ?: return null to null
        val href = cssLink.attr("href")
        val resource = resourceLoader.getResource("classpath:static$href")
        cssLink.remove()
        return Pair(href, resource)
    }

    private fun getDocument(responseWrapper: ContentCachingResponseWrapper): Document {
        val html = responseWrapper.contentAsByteArray.toString(StandardCharsets.UTF_8)
        val document = Jsoup.parse(html)
        return document
    }
}
