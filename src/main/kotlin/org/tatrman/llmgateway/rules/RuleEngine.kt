package org.tatrman.llmgateway.rules

import org.tatrman.llmgateway.model.Model
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.io.File
import org.springframework.stereotype.Service

data class RequestMetadata(
        val modelName: String? = null,
        val requiredTags: List<String> = emptyList(),
        val user: String? = null
)

@Service
class RuleEngine {

    private var rulesConfig: Config = ConfigFactory.load("rules.conf") // Fallback to classpath

    fun reloadRules(path: String) {
        val file = File(path)
        if (file.exists()) {
            rulesConfig = ConfigFactory.parseFile(file).resolve()
        }
    }

    /**
     * strict: If true, ONLY return models matching all criteria. If false, return best guess (not
     * implemented yet, just first match).
     */
    fun selectModel(metadata: RequestMetadata, availableModels: Iterable<Model>): Model? {
        // 1. Direct Name Match (Highest Priority)
        if (metadata.modelName != null) {
            val direct =
                    availableModels.find { it.name.equals(metadata.modelName, ignoreCase = true) }
            if (direct != null) return direct
        }

        // 2. Tag Matching logic from HOCON or Metadata
        // Simplest Rule: Filter by required tags
        var candidates = availableModels.toList()
        if (metadata.requiredTags.isNotEmpty()) {
            candidates =
                    candidates.filter { model ->
                        metadata.requiredTags.all { reqTag -> model.tags?.contains(reqTag) == true }
                    }
        }

        // 3. Apply HOCON "rules" (Example: alias mapping)
        // rules {
        //    aliases {
        //       "gpt-4-turbo" = "azure-gpt4"
        //    }
        // }
        if (rulesConfig.hasPath("rules.aliases")) {
            val aliases = rulesConfig.getConfig("rules.aliases")
            if (metadata.modelName != null && aliases.hasPath("\"${metadata.modelName}\"")) {
                val targetName = aliases.getString("\"${metadata.modelName}\"")
                return availableModels.find { it.name == targetName }
            }
        }

        return candidates.firstOrNull()
    }
}
