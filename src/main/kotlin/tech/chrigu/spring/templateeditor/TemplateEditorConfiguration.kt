package tech.chrigu.spring.templateeditor

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import tech.chrigu.spring.templateeditor.web.TemplateEditorFilter
import tech.chrigu.spring.templateeditor.web.TemplateEditorRoutes

@TestConfiguration
class TemplateEditorConfiguration(private val resourceLoader: ResourceLoader) {
    private val styleController = TemplateEditorRoutes()

    @Bean
    internal fun editStylingFilter() = TemplateEditorFilter(resourceLoader)

    @Bean
    fun templateEditRoutes() = router {
        POST("/save-styles") {
            styleController.saveStyles(it.body<TemplateEditorRoutes.SaveCssRequest>())
        }
    }
}
