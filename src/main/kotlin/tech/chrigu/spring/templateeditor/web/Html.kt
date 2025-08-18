package tech.chrigu.spring.templateeditor.web

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.WriterOutput
import jakarta.servlet.http.HttpServletRequest
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import tech.chrigu.spring.templateeditor.web.csrf.CsrfHeader
import tech.chrigu.spring.templateeditor.web.csrf.CsrfTokenProvider
import java.io.Writer
import kotlin.io.path.Path

class Html(private val csrfTokenProvider: CsrfTokenProvider) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val renderer = TemplateEngine.createPrecompiled(Path("/src/main/kte"), ContentType.Html)

    fun render(htmlDocument: Document, cssResource: Resource?, cssPath: String?, request: HttpServletRequest, writer: Writer) {
        logger.info("A: ${CsrfHeader::class.java.classLoader}")
        renderer.render(
            "editor", mapOf(
                "htmlDocument" to htmlDocument,
                "cssResource" to cssResource,
                "cssPath" to cssPath,
                "csrfHeader" to csrfTokenProvider.getCsrfHeader(request)
            ), WriterOutput(writer)
        )
    }
}
