package com.esosa.f5pi_backend.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageMapper {
    companion object {
        fun buildPageRequest(pageNumber: Int, pageSize: Int) =
            PageRequest.of(pageNumber, pageSize, Sort.by("createdAt"))
    }
}