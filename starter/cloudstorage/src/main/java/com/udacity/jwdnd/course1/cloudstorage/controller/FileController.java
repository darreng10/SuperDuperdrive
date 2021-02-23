package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/file-upload")
    public ModelAndView postFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication,
                                 Model model) {
        if (multipartFile.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "No file selected to upload");
            return new ModelAndView("result");
        }

        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserId();

        if (fileService.doesFilenameExist(multipartFile.getOriginalFilename(), userId)) {

            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "file name already exists");
            return new ModelAndView("result");
        }
        try {
            fileService.createFile(multipartFile, userId);
            model.addAttribute("success", true);
            model.addAttribute("message", "New File added successfully");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return new ModelAndView("result");
    }

    @PostMapping("/files/delete")
    public ModelAndView deleteFile(@ModelAttribute File fileDelete, Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userid = user.getUserId();

        try {
            fileService.deleteFile(fileDelete, userid);
            model.addAttribute("success", true);
            model.addAttribute("message", "file Deleted");
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "System error!" + e.getMessage());
        }
        return new ModelAndView("result");
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId){
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(  file.getContenttype() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }
}
