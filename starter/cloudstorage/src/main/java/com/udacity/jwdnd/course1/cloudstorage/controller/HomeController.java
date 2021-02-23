package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/home")
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, FileService fileService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", this.credentialService.getAllCredentials(userId));
        model.addAttribute("notes", this.noteService.getAllNotesByUserId(userId));
        model.addAttribute("files", this.fileService.getAllFilesByUserId(userId));
        model.addAttribute("noteForm", new Note());
        model.addAttribute("deleteNote", new Note());
        model.addAttribute("credentialForm", new Credentials());
        model.addAttribute("deleteCredential", new Credentials());
        model.addAttribute("deleteFile", new File());
        return "home";
    }

    @PostMapping("/logout")
    public String logout() {
        return "login?logout";
    }

    @GetMapping("/logout")
    public String logoutView() {
        return "redirect:/login?logout";
    }

}
