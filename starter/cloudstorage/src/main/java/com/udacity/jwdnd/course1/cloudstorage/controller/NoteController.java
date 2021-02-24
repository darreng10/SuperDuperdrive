package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/notes")
    public ModelAndView createNote(Authentication authentication, Model model, @ModelAttribute Note note) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        note.setUserid(userid);

        if (noteService.doesNoteExist(note.getNotetitle(), note.getNotedescription(),userid)) {

            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "note already exists");
            return new ModelAndView("result");
        }

        try {
            if (note.getNoteid() == null){
                noteService.createNote(note);
            }else {
                noteService.editNote(note);
            }
            model.addAttribute("success", true);
            model.addAttribute("message", "New note added!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error adding note!" + e.getMessage());
        }
        return new ModelAndView("result");
    }

    @PostMapping("/notes/delete")
    public ModelAndView deleteNote(Authentication authentication, Model model, @ModelAttribute Note note) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();
        Integer noteid = note.getNoteid();


        try {
            noteService.deleteNote(note, noteid);
            model.addAttribute("success", true);
            model.addAttribute("message", "Note deleted!");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Error deleting note!" + e.getMessage());
        }
        return new ModelAndView("result");
    }
}
