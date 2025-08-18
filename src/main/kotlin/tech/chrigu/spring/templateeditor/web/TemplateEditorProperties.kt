package tech.chrigu.spring.templateeditor.web

import org.springframework.boot.context.properties.ConfigurationProperties
import java.nio.file.Path
import kotlin.io.path.Path

@ConfigurationProperties(prefix = "template-editor")
data class TemplateEditorProperties(
    /**
     * The local css file is detected by matching its path with this prefix.
     */
    val localCssPrefix: String = "/css",
    /**
     * The local directory that contains the css files.
     */
    val staticPath: Path = Path("src/main/resources/static")
)
