package com.llmgateway.model

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository

@Table("models")
data class Model(
        @Id val id: Int? = null,
        val name: String,
        val description: String?,
        val modelType: String,
        val tags: Array<String>?,
        val createdAt: Instant? = null
)

interface ModelRepository : CrudRepository<Model, Int> {
    fun findByName(name: String): Model?
}
