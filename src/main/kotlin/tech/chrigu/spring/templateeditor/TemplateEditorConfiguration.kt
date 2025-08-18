package tech.chrigu.spring.templateeditor

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.router
import tech.chrigu.spring.templateeditor.web.csrf.CsrfTokenProvider
import tech.chrigu.spring.templateeditor.web.csrf.DefaultCsrfTokenProvider
import tech.chrigu.spring.templateeditor.web.csrf.SecurityCsrfTokenProvider
import tech.chrigu.spring.templateeditor.web.TemplateEditorFilter
import tech.chrigu.spring.templateeditor.web.TemplateEditorProperties
import tech.chrigu.spring.templateeditor.web.TemplateEditorRoutes

@TestConfiguration
@EnableConfigurationProperties(TemplateEditorProperties::class)
class TemplateEditorConfiguration(private val resourceLoader: ResourceLoader, private val templateEditorProperties: TemplateEditorProperties) {
    private val logger = LoggerFactory.getLogger(TemplateEditorConfiguration::class.java)
    private val styleController = TemplateEditorRoutes(templateEditorProperties)

    @Bean
    internal fun editStylingFilter(csrfTokenProvider: CsrfTokenProvider): TemplateEditorFilter {
        logger.info("Configure spring-template-editor... You can pass the editor query parameter to enable the editor. ${this::class.java.classLoader}")
        return TemplateEditorFilter(resourceLoader, csrfTokenProvider, templateEditorProperties)
    }

    @ConditionalOnMissingBean
    @Bean
    fun csrfTokenProvider(): CsrfTokenProvider = DefaultCsrfTokenProvider()

    @Bean
    fun templateEditRoutes() = router {
        POST("/save-styles") {
            styleController.saveStyles(it.body<TemplateEditorRoutes.SaveCssRequest>())
        }
    }

    @ConditionalOnClass(CsrfToken::class)
    @Configuration
    class TemplateEditorCsrfConfiguration {
        @Bean
        fun csrfTokenProvider(): CsrfTokenProvider = SecurityCsrfTokenProvider()
    }
}
