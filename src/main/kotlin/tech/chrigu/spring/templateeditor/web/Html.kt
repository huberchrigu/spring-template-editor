package tech.chrigu.spring.templateeditor.web

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.output.WriterOutput
import org.jsoup.nodes.Document
import org.springframework.core.io.Resource
import org.springframework.security.web.csrf.CsrfToken
import java.io.Writer
import kotlin.io.path.Path

class Html { // TODO: Use rendering technology from client, make usage of Csrf Token optional (dependent on classpath)
    private val renderer = TemplateEngine.createPrecompiled(Path("/src/main/kte"), ContentType.Html)

    fun render(htmlDocument: Document, cssResource: Resource?, cssPath: String?, csrfToken: CsrfToken?, writer: Writer) {
        renderer.render(
            "editor", mapOf(
                "htmlDocument" to htmlDocument,
                "cssResource" to cssResource,
                "cssPath" to cssPath,
                "csrfToken" to csrfToken
            ), WriterOutput(writer)
        )
    }
}
