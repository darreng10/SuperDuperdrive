package com.udacity.jwdnd.course1.cloudstorage.services;

import org.springframework.stereotype.Service;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void createOrUpdateCredential (Credentials credential) {
        String password = credential.getPassword(); //get password from credential form
        // <start> encrypt the password
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        // <end> encrypt the password
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);

        if (credential.getCredentialid() == null) {
            credentialMapper.insert(credential);
        } else {
            credentialMapper.update(credential);
        }
    }


    public Integer deleteCredential(Integer credentialid) {
        return credentialMapper.delete(credentialid);
    }

    public List<Credentials> getAllCredentials(Integer userId){
        return credentialMapper.getAllCredentialByUserId(userId);
    }
}