package tech.chrigu.spring.templateeditor.web.csrf

data class CsrfHeader(val name: String, val value: String) {
    val jsonObject: String
        get() = """{"$name": "$value"}"""
}
