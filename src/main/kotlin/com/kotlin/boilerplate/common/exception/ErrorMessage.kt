package com.kotlin.boilerplate.common.exception

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorMessage(
    val status: Int,
    val code: String,
    val message: String,
) {

    BAD_REQUEST(400, "ERR001", "잘못된 요청 형식입니다."),
    UNAUTHORIZED(401, "ERR002", "유효하지 않은 인가 접근입니다."),
    FORBIDDEN(403, "ERR003", "유효하지 않은 접근 권한입니다."),
    NOT_FOUND(404, "ERR004", "리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "ERR005", "유효하지 않은 메소드입니다."),
    INTERNAL_SERVER_ERROR(500, "ERR006", "내부 서버 오류입니다."),
    ;
}