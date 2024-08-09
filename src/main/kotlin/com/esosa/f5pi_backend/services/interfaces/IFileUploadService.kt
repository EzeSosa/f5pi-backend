package com.esosa.f5pi_backend.services.interfaces

import org.springframework.web.multipart.MultipartFile

interface IFileUploadService {
    fun uploadFile(multipartFile: MultipartFile): String
}