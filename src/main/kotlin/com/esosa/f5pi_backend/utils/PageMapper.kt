package com.esosa.f5pi_backend.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PageMapper {
    companion object {
        fun buildPageRequest(pageNumber: Int, pageSize: Int, sortingAttribute: String) =
            PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, sortingAttribute))
    }
}