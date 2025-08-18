package tech.chrigu.spring.templateeditor.css

import com.helger.css.ECSSVersion
import com.helger.css.decl.CascadingStyleSheet
import com.helger.css.reader.CSSReader
import com.helger.css.writer.CSSWriter
import com.helger.css.writer.CSSWriterSettings
import java.nio.charset.StandardCharsets

internal object CssService {
    fun mergeCss(original: String, modified: String): String {
        val charset = StandardCharsets.UTF_8

        val originalSheet = CSSReader.readFromString(original, charset, ECSSVersion.CSS30) ?: return modified
        val modifiedSheet = CSSReader.readFromString(modified, charset, ECSSVersion.CSS30) ?: return original

        val originalRules = originalSheet.allStyleRules.toMutableList()

        // Replace or append modified rules
        for (modRule in modifiedSheet.allStyleRules) {
            val idx = originalRules.indexOfFirst { it.getSelectorsAsCSSString(CSSWriterSettings(), 2) == modRule.getSelectorsAsCSSString(CSSWriterSettings(), 2) }
            if (idx >= 0) {
                // Replace existing
                originalRules[idx] = modRule
            } else {
                // Append new
                originalRules.add(modRule)
            }
        }

        // Write back into a new stylesheet
        val resultSheet = CascadingStyleSheet()
        originalRules.forEach { resultSheet.addRule(it) }

        val writerSettings = CSSWriterSettings(ECSSVersion.CSS30, false)
        val writer = CSSWriter(writerSettings)
        writer.setWriteHeaderText(false)
        return writer.getCSSAsString(resultSheet)
    }
}
