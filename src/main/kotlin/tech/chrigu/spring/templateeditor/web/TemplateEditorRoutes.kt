package tech.chrigu.spring.templateeditor.web

import org.springframework.web.servlet.function.ServerResponse
import tech.chrigu.spring.templateeditor.css.CssService
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isRegularFile

internal class TemplateEditorRoutes() {
    private val staticPath =  // TODO: Error handling, make configurable
        Paths.get(javaClass.getResource("/static").toURI())
            .resolve("../../../../src/main/resources/static")
            .normalize()

    fun saveStyles(request: SaveCssRequest): ServerResponse {
        if (request.path.isBlank()) {
            return ServerResponse.badRequest().body(mapOf("message" to "CSS path is missing."))
        }
        val filePath = staticPath.resolve("." + request.path).normalize()
        return if (filePath.toFile().exists() && filePath.isRegularFile() && filePath.startsWith(staticPath)) {
            val originalCss = Files.readString(filePath, StandardCharsets.UTF_8)
            val mergedCss = CssService.mergeCss(originalCss, request.content)
            Files.writeString(filePath, mergedCss)
            ServerResponse.ok().body(mapOf("message" to "Styles saved successfully to ${request.path}"))
        } else {
            ServerResponse.badRequest().body(mapOf("message" to "Invalid CSS path: $filePath"))
        }
    }

    data class SaveCssRequest(val path: String, val content: String)
}
