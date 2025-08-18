package tech.chrigu.spring.templateeditor.web.csrf

import jakarta.servlet.http.HttpServletRequest

interface CsrfTokenProvider {
    fun getCsrfHeader(request: HttpServletRequest): CsrfHeader?
}
