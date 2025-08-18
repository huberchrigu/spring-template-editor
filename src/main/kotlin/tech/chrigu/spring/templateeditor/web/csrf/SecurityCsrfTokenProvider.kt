package tech.chrigu.spring.templateeditor.web.csrf

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.csrf.CsrfToken

class SecurityCsrfTokenProvider : CsrfTokenProvider {
    override fun getCsrfHeader(request: HttpServletRequest): CsrfHeader {
        val token = request.getAttribute(CsrfToken::class.java.name) as CsrfToken
        return CsrfHeader(token.headerName, token.token)
    }
}
