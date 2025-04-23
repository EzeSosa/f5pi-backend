package com.esosa.f5pi_backend.services.interfaces

import com.esosa.f5pi_backend.controllers.requests.MemberRequest
import com.esosa.f5pi_backend.controllers.responses.MemberResponse

interface IMemberService {
    fun saveMember(memberRequest: MemberRequest): MemberResponse
}