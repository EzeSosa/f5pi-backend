package com.esosa.f5pi_backend.data.repositories

import com.esosa.f5pi_backend.data.models.Field
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IFieldRepository : JpaRepository<Field, UUID>