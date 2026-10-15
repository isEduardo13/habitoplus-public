package com.habitoplus.habitoplusback.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.habitoplus.habitoplusback.dto.LocationDTO;
import com.habitoplus.habitoplusback.dto.UpdateProfileDTO;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.ProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Page<Profile> getAllProfilesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return profileRepository.findAll(pageable);
    }

    public Profile getProfileById(int id) {
        return profileRepository.findById(id).orElse(null);
    }

    public Profile updateProfile(Integer id, UpdateProfileDTO profile) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Profile not found with"));
        existingProfile.setName(profile.getName());
        ;
        existingProfile.setUsername(profile.getUsername());
        existingProfile.setLastName(profile.getLastName());
        existingProfile.setBirthDate(profile.getBirthDate());
        existingProfile.setAge(profile.getAge());
        existingProfile.setGender(profile.getGender());
        existingProfile.setPreferences(profile.getPreferences());
        existingProfile.setDescription(profile.getDescription());
        existingProfile.setNumberPhone(profile.getNumberPhone());
        return profileRepository.save(existingProfile);
    }

    public Profile updateLocation(Integer id, LocationDTO location) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Profile not found with"));
        existingProfile.setLatitude(location.getLatitude());
        existingProfile.setLongitude(location.getLongitude());
        return profileRepository.save(existingProfile);
    }
    public LocationDTO GetLocation(Integer id) {
        Profile existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Profile not found with"));
        LocationDTO location = new LocationDTO();
        location.setLatitude(existingProfile.getLatitude());
        location.setLongitude(existingProfile.getLongitude());
        return location;
    }

}
