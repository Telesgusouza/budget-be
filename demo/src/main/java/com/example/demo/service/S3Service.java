package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.example.demo.dto.MetadataResponseDTO;
import com.example.demo.dto.ResponseUrlPhotoDTO;
import com.example.demo.dto.ResultResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.exception.ResourceNotFoundException;

@Service
public class S3Service {

	@Autowired
	private UserRepository userRepository;

	@Value("${aws.bucket}")
	private String bucketName;

	private final AmazonS3 s3Cliente;

	public S3Service(AmazonS3 s3Client) {
		this.s3Cliente = s3Client;
	}

	public ResultResponseDTO upload(MultipartFile file, UUID filename, User user) {

		String contentType = file.getContentType();

		if (contentType == null || !isImage(contentType)) {

			MetadataResponseDTO metadataResponse = new MetadataResponseDTO("400", "invalid photo", "0");
			ResultResponseDTO response = new ResultResponseDTO(metadataResponse, null);

			return response;
		}

		if (file == null || file.isEmpty()) {

			MetadataResponseDTO metdaMetadataResponse = new MetadataResponseDTO("400", "invalid file", "0");
			ResultResponseDTO response = new ResultResponseDTO(metdaMetadataResponse, null);

			return response;
		}

		try {

			File fileSave = convertMultiPartToFile(file);
			s3Cliente.putObject(bucketName, "" + filename, fileSave);

			MetadataResponseDTO metadataResponse = new MetadataResponseDTO("200", "file upload successfully in aws",
					"1");
			ResultResponseDTO response = new ResultResponseDTO(metadataResponse, filename);

			user.setImg("https://budget-7110.s3.amazonaws.com/" + response.result());
			userRepository.save(user);

			return response;
		} catch (Exception e) {

			e.printStackTrace();

			MetadataResponseDTO metdataMetadataResponse = new MetadataResponseDTO("400",
					"failed to upload file in s3 bucket", "0");
			ResultResponseDTO response = new ResultResponseDTO(metdataMetadataResponse, null);

			return response;
		}

	}

	public ResultResponseDTO delete(UUID id, User user) {
		try {

			s3Cliente.deleteObject(bucketName, "" + id);

			MetadataResponseDTO metadataResponse = new MetadataResponseDTO("200", "file delete successfully", "1");
			ResultResponseDTO response = new ResultResponseDTO(metadataResponse, "" + id);

			user.setImg("");
			userRepository.save(user);

			return response;

		} catch (Exception e) {
			MetadataResponseDTO metadataResponse = new MetadataResponseDTO("400", "failed to delete file", "0");
			ResultResponseDTO response = new ResultResponseDTO(metadataResponse, null);
			return response;
		}
	}

	@Cacheable("get photo s3")
	public ResponseUrlPhotoDTO getPhoto(UUID id) {

		if (id == null) {
			throw new ResourceNotFoundException("ID não pode ser nulo");
		}

		try {
			return new ResponseUrlPhotoDTO("" + s3Cliente.getUrl(bucketName, "" + id));
		} catch (Exception e) {
			throw new ResourceNotFoundException("Error get photo");
		}
	}

	private boolean isImage(String contentType) {
		return contentType.equals(MediaType.IMAGE_JPEG_VALUE) || contentType.equals(MediaType.IMAGE_PNG_VALUE);
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {

		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);

		fos.write(file.getBytes());
		fos.close();

		return convFile;
	}

}
