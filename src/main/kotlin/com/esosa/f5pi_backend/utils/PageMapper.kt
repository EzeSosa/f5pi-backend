package com.esosa.f5pi_backend.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PageMapper {
    companion object {
        fun buildPageRequest(pageNumber: Int, pageSize: Int, sortingAttribute: String, sortOrder: String = "desc") =
            PageRequest.of( pageNumber, pageSize, Sort.by( buildSortOrder(sortOrder), sortingAttribute ) )

        private fun buildSortOrder(sortOrder: String): Direction =
            when (sortOrder) {
                "desc" -> Direction.DESC
                "asc" -> Direction.ASC
                else -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort order is not valid")
            }
    }
}