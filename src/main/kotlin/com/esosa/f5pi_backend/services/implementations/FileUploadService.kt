package com.esosa.f5pi_backend.services.implementations

import com.cloudinary.Cloudinary
import com.esosa.f5pi_backend.services.interfaces.IFileUploadService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileUploadService(private val cloudinary: Cloudinary) : IFileUploadService {
    override fun uploadFile(multipartFile: MultipartFile): String =
        cloudinary.uploader()
            .upload( multipartFile.bytes, emptyMap<String, Any>() )["url"]
            .toString()
}