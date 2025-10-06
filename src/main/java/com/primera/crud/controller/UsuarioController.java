package com.primera.crud.controller;

import com.primera.crud.model.Usuario;
import com.primera.crud.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {
        usuarioService.registrar(usuario);
        return "redirect:/registro?success";
    }

    // 👉 Mostrar login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // 👉 Procesar login y guardar sesión
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        return usuarioService.login(email, password)
                .map(usuario -> {
                    session.setAttribute("usuarioLogueado", usuario); // 🔹 Guardar en sesión
                    model.addAttribute("usuario", usuario);
                    return "home";
                })
                .orElse("redirect:/login?error");
    }

    // 👉 Página de inicio (requiere usuario en sesión)
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            return "redirect:/login"; // si no hay sesión, redirige al login
        }
        model.addAttribute("usuario", usuario);
        return "home";
    }

    // 👉 Cerrar sesión
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // elimina la sesión
        return "redirect:/login";
    }
}
