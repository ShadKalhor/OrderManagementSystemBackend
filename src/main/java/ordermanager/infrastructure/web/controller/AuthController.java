package ordermanager.infrastructure.web.controller;

import ordermanager.infrastructure.security.JwtService;
import ordermanager.infrastructure.web.dto.auth.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auths")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService){
        this.authManager = authManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request){
        String phone = request.phone() == null ? "" : request.phone().trim();

        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(phone, request.password())
        );

        String token = jwtService.generateToken(phone, Map.of());
        return ResponseEntity.ok(Map.of("token", token));
    }




}
