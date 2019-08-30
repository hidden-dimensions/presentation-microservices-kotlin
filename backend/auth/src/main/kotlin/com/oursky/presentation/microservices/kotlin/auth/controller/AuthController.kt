package com.oursky.presentation.microservices.kotlin.auth.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import com.oursky.presentation.microservices.kotlin.auth.service.AuthService
import com.oursky.presentation.microservices.kotlin.auth.service.JwtService

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    lateinit var authService: AuthService
    @Autowired
    lateinit var jwtService: JwtService

    data class SignupRequest(
        val user: String,
        val pass: String
    )
    data class SignupResponse(
        val accessToken: String
    )
    // curl -X POST http://127.0.0.1:8080/auth/signup -H "Content-Type: application/json" -d '{"user": "test", "pass": "1234"}'
    @PostMapping("/signup")
    fun login(
        @RequestBody body: SignupRequest
    ): ResponseEntity<SignupResponse> {
        val userId = authService.signup(body.user, body.pass)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        return ResponseEntity.ok(SignupResponse(
            accessToken = jwtService.sign(userId, "access")
        ))
    }

    data class LoginRequest(
        val user: String,
        val pass: String
    )
    data class LoginResponse(
        val accessToken: String
    )
    // curl -X POST http://127.0.0.1:8080/auth/login -H "Content-Type: application/json" -d '{"user": "test", "pass": "1234"}'
    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest
    ): ResponseEntity<LoginResponse> {
        val userId = authService.login(body.user, body.pass)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)
        return ResponseEntity.ok(LoginResponse(
            accessToken = jwtService.sign(userId, "access")
        ))
    }

    // curl -X GET http://127.0.0.1:8080/auth/logout
    @GetMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }
}
