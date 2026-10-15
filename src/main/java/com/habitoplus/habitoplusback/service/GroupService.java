package com.habitoplus.habitoplusback.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.habitoplus.habitoplusback.dto.GroupDTO;
import com.habitoplus.habitoplusback.dto.GroupMemberDTO;
import com.habitoplus.habitoplusback.dto.ProfileDTO;
import com.habitoplus.habitoplusback.dto.UpdateGroupDTO;
import com.habitoplus.habitoplusback.enums.Role;
import com.habitoplus.habitoplusback.model.Group;
import com.habitoplus.habitoplusback.model.GroupMember;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.GroupMemberRepository;
import com.habitoplus.habitoplusback.repository.GroupRepository;
import com.habitoplus.habitoplusback.util.FirebaseCredentialsProvider;

import jakarta.transaction.Transactional;

@Service
public class GroupService {

	@Autowired
	private GroupMemberRepository memberRepository;

	@Autowired
	private GroupRepository repository;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private ModelMapper modelMapper;

	public List<Group> getAll() {
		return repository.findAll();
	}

	public List<Group> getAll(int page, int pageSize) {
		PageRequest pageReq = PageRequest.of(page, pageSize);
		Page<Group> groups = repository.findAll(pageReq);
		return groups.getContent();
	}

	public Group getById(Integer idGroup) {
		if (repository.findById(idGroup).isEmpty())
			throw new NoSuchElementException();

		return repository.findById(idGroup).get();
	}

	public List<Group> getByProfile(Integer idProfile, int page, int pageSize) {
		PageRequest pageReq = PageRequest.of(page, pageSize);

		Page<Group> groups = memberRepository.findGroupsByProfileId(idProfile, pageReq);
		
		if (groups.isEmpty()) {
			throw new NoSuchElementException("No groups found for the given profile ID.");
		}
		
		return groups.getContent();
	}
	

	@Transactional
	public void save(GroupDTO groupDTO) {
		repository.save(convertToEntity(groupDTO));
	}

	@Transactional
	public void save(UpdateGroupDTO groupDTO, Integer idGroup) {

		Group auxGroup = repository.findById(idGroup).orElseThrow(
				() -> new NoSuchElementException());

		auxGroup.setName(groupDTO.getName());
		auxGroup.setDescription(groupDTO.getDescription());
		auxGroup.setGroupType(groupDTO.getGroupType());

		repository.save(auxGroup);
	}

	@Transactional
	public void delete(Integer idGroup) {
		repository.deleteById(idGroup);
	}

	public GroupDTO convertToDTO(Group group) {
		return modelMapper.map(group, GroupDTO.class);
	}

	public ProfileDTO convertToDTO(Profile profile) {
		return modelMapper.map(profile, ProfileDTO.class);
	}

	public Group convertToEntity(GroupDTO groupDTO) {
		List<GroupMemberDTO> groupMembersDTO = groupDTO
				.getGroupMembersDTO();
		List<GroupMember> groupMembers = groupMembersDTO.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());

		Group group = modelMapper.map(groupDTO, Group.class);
		group.setCreationDate(new Date());
		group.setUrl("https://firebasestorage.googleapis.com/v0/b/habitoplus-6d438.appspot.com/o/DefaultGroupImage.png?alt=media");
		group.setMembers(groupMembers);

		Iterator<GroupMemberDTO> groupMemberDTOIterator = groupMembersDTO.iterator();
		Iterator<GroupMember> groupMemberIterator = groupMembers.iterator();
		boolean isFirstElement = true;
		while (groupMemberDTOIterator.hasNext()) {
			GroupMemberDTO groupMemberDTO = groupMemberDTOIterator.next();
			GroupMember groupMember = groupMemberIterator.next();
			Profile managedProfile = profileService.getProfileById(groupMemberDTO.getIdProfile());
			groupMember.setProfile(managedProfile);
			groupMember.setGroup(group);
			if (isFirstElement) {
				groupMember.setRole(Role.ROLE_GROUPADMIN);
				isFirstElement = false;
			} else {
				groupMember.setRole(Role.ROLE_GROUPADMIN);
			}
			groupMember.setUnionDate(new Date());
		}
		return group;
	}

	private GroupMember convertToDTO(GroupMemberDTO groupMemberDTO) {
		return modelMapper.map(groupMemberDTO, GroupMember.class);
	}

	@Transactional
    public String changePhotoGroup(MultipartFile multipartFile, Integer idGroup) {
        try {
            Optional<Group> optionalGroup = repository.findById(idGroup);
            if (!optionalGroup.isPresent()) {
                throw new NoSuchElementException();
            }
            Group group = optionalGroup.get();

            String oldPhotoUrl = group.getUrl();

            if (oldPhotoUrl != null && !oldPhotoUrl.isEmpty() && !oldPhotoUrl.equals("https://firebasestorage.googleapis.com/v0/b/habitoplus-6d438.appspot.com/o/DefaultGroupImage.png?alt=media")) {
                deleteFileFromFirebase(oldPhotoUrl);
            }

  
            String fileName = "photoGroup" + idGroup + getExtension(multipartFile.getOriginalFilename());
            File file = convertToFile(multipartFile, fileName);

    
            String photoUrl = uploadFile(file, fileName);

            if (file.exists()) {
                file.delete();
            }

            group.setUrl(photoUrl);
            repository.save(group);

            return "Photo updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "The image could not be uploaded";
        }
    }

	private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
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
