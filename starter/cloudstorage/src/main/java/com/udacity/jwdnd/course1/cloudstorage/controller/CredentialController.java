package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CredentialController {

    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/credentials")
    public ModelAndView createCredential(Authentication authentication, Model model, @ModelAttribute Credentials credential) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        credential.setUserid(userid);

        try{
            credentialService.createOrUpdateCredential(credential);
            model.addAttribute("success", true);
            model.addAttribute("message",  "credential has been successfully inserted!");
        } catch (Exception e){
            model.addAttribute("error", true);
            model.addAttribute("message", "Error adding credential");
            e.printStackTrace();
        }
        return new ModelAndView("result");
    }

    @PostMapping("/delete/credential")
    public ModelAndView deleteCredential(Authentication authentication, Model model, @ModelAttribute Credentials credentialDelete) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        Integer credentialId = credentialDelete.getCredentialid();


        try {
            credentialService.deleteCredential(credentialId);
            model.addAttribute("success", true);
            model.addAttribute("message", "Credentials deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return new ModelAndView("result");
    }
}