package com.login.spring_security.controller;

import com.login.spring_security.entity.Usuario;
import com.login.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "loginpage";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registerpage";
    }

    @PostMapping("/registro")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        // Obtener autenticación
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // El username es el nombre de usuario autenticado

        // Buscar en la base de datos
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario != null) {
            model.addAttribute("nombre", usuario.getUsername()); // O usuario.getNombre() si lo tienes
        } else {
            model.addAttribute("nombre", "Invitado");
        }

        return "dashboard";
    }
}
