package com.kotlin.boilerplate.common.exception

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionAdvice {

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(ex: CustomException, request: HttpServletRequest): ErrorResponse {
        log.error(ex) { "CustomException has occurred: ${ex.message}"}
        return generateErrorResponse(request, ex.errorMessage)
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(ex: RuntimeException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        log.error(ex) { "RuntimeException has occurred: ${ex.message}" }
        return generateErrorResponseEntity(request, ex.message!!)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(ex: NoHandlerFoundException, request: HttpServletRequest): ErrorResponse {
        log.error(ex) { "NoHandlerFoundException occurred: ${ex.message}" }
        return generateErrorResponse(request, ErrorMessage.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException, request: HttpServletRequest): ErrorResponse {
        val message = ex.bindingResult.fieldErrors.joinToString(", ") { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }

        log.error(ex) { "MethodArgumentNotValidException occurred: $message" }

        return generateErrorResponse(
            request,
            ErrorMessage.BAD_REQUEST,
            message
        )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException, request: HttpServletRequest): ErrorResponse {
        log.error(ex) { "AccessDeniedException occurred: ${ex.message}" }
        return generateErrorResponse(request, ErrorMessage.FORBIDDEN)
    }

    private fun generateErrorResponse(
        request: HttpServletRequest,
        errorMessage: ErrorMessage,
        customMessage: String? = null,
    ): ErrorResponse {
        return ErrorResponse(
            requestDetails = "[${request.method}] ${request.requestURI}",
            status = errorMessage.status,
            code = errorMessage.code,
            message = customMessage ?: errorMessage.message
        )
    }

    private fun generateErrorResponseEntity(
        request: HttpServletRequest,
        errorMessage: String
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            requestDetails = "[${request.method}] ${request.requestURI}",
            status = ErrorMessage.INTERNAL_SERVER_ERROR.status,
            code = ErrorMessage.INTERNAL_SERVER_ERROR.code,
            message = errorMessage
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }
}