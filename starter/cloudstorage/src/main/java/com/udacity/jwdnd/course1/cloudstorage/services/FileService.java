package com.udacity.jwdnd.course1.cloudstorage.services;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int createFile(MultipartFile uploadFile, Integer userid) throws IOException {
        File file = new File();
        file.setFilename(uploadFile.getOriginalFilename());
        file.setContenttype(uploadFile.getContentType());
        file.setFiledata(uploadFile.getBytes());
        file.setFilesize(uploadFile.getSize());
        file.setUserid(userid);

        return fileMapper.insert(file);
    }

    public int uploadFile(File file) {
        return fileMapper.insert(file);
    }

    public List<File> getAllFilesByUserId(Integer id) {
        return this.fileMapper.getAllFilesByUserId(id);
    }

    public File getFileById(Integer id) {
        return this.fileMapper.getFileId(id);
    }

    public int deleteFile(File file, Integer userid) {
        file.setUserid(userid);
        return fileMapper.delete(file);
    }

    public boolean doesFilenameExist(String fileName, Integer userId) {
        File f =  this.fileMapper.getFile(fileName, userId);
        if(f != null){
            return(true);
        }else {
            return (false);
        }
    }
}