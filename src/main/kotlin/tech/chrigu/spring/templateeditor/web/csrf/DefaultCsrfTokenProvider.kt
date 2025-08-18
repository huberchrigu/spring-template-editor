package tech.chrigu.spring.templateeditor.web.csrf

import jakarta.servlet.http.HttpServletRequest

class DefaultCsrfTokenProvider : CsrfTokenProvider {
    override fun getCsrfHeader(request: HttpServletRequest): CsrfHeader? {
        return null
    }
}
