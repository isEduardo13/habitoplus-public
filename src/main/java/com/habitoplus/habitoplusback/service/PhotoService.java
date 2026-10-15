package com.habitoplus.habitoplusback.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.habitoplus.habitoplusback.exception.PhotoUploadException;
import com.habitoplus.habitoplusback.exception.UserNotFoundException;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.ProfileRepository;
import com.habitoplus.habitoplusback.util.FirebaseCredentialsProvider;

@Service
public class PhotoService {

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public String changePhoto(MultipartFile multipartFile, Integer profileId) {
        try {
            Optional<Profile> optionalProfile = profileRepository.findById(profileId);
            if (!optionalProfile.isPresent()) {
                throw new UserNotFoundException("Profile not found");
            }
            Profile profile = optionalProfile.get();

            String oldPhotoUrl = profile.getPhoto().getUrl();

            if (oldPhotoUrl != null && !oldPhotoUrl.isEmpty() && !oldPhotoUrl.equals("https://firebasestorage.googleapis.com/v0/b/habitoplus-6d438.appspot.com/o/DefaultImage.jpg?alt=media")) {
                deleteFileFromFirebase(oldPhotoUrl);
            }

  
            String fileName = "photoUser"+ profileId + getExtension(multipartFile.getOriginalFilename());
            File file = convertToFile(multipartFile, fileName);

    
            String photoUrl = uploadFile(file, fileName);

            if (file.exists()) {
                file.delete();
            }

            profile.getPhoto().setUrl(photoUrl);
            profileRepository.save(profile);

            return "Photo updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
             throw new PhotoUploadException("Could not upload the image", e);
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File("/tmp/"+fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("habitoplus-6d438.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = FirebaseCredentialsProvider.getCredentials();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String downloadURL = "https://firebasestorage.googleapis.com/v0/b/habitoplus-6d438.appspot.com/o/%s?alt=media";
        return String.format(downloadURL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private void deleteFileFromFirebase(String fileUrl) throws IOException {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.indexOf("?alt=media"));
        BlobId blobId = BlobId.of("habitoplus-6d438.appspot.com", fileName);
        Credentials credentials = FirebaseCredentialsProvider.getCredentials();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
    }
}