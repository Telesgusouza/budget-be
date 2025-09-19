package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.example.demo.dto.ResponseUrlPhotoDTO;
import com.example.demo.dto.ResultResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.repositories.UserRepository;

@SpringBootTest(properties = { "api.security.token.secret=${JWT_SECRET:my-secret-key}", "aws.acesskey=access_key_aws",
		"aws.secrety=secrety_aws", "aws.bucket=budget-7110" })
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {
	@InjectMocks
	private S3Service s3Service;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AmazonS3 amazonS3;

	@Value("${aws.bucket}")
	private String bucketName;

	private static final String TEST_IMAGE_CONTENT_TYPE = "image/jpeg";
	private static final String TEST_FILE_NAME = "test-image.jpg";
	private static final UUID FILENAME_UUID = UUID.randomUUID();
	private static final UUID USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

	private static final String BUCKET_NAME = "my-bucket";
	private static final UUID VALID_ID = UUID.randomUUID();
	private static final String MOCK_URL = "https://example.com/photo.jpg";

//	// upload
//	@DisplayName("It must upload successfully")
//	@Test
//	public void ItMustUploadSuccessfully() throws IOException {
//		MockMultipartFile file = new MockMultipartFile("file", TEST_FILE_NAME, TEST_IMAGE_CONTENT_TYPE,
//				"arquivo de teste".getBytes());
//
//		User user = new User();
//		user.setId(USER_ID);
//
//		ResultResponseDTO result = s3Service.upload(file, FILENAME_UUID, user);
//
//		assertNotNull(result);
//		assertEquals("200", result.metadataResponse().code());
//		assertEquals("file upload successfully in aws", result.metadataResponse().message());
//		assertEquals("1", result.metadataResponse().noOfRecord());
//		assertEquals(FILENAME_UUID, result.result());
//
//		String urlGerada = "https://budget-7110.s3.amazonaws.com/" + FILENAME_UUID;
//		verify(userRepository).save(user);
//	}
//
//	@Test
//	@DisplayName("ContentType must be Null")
//	public void ContentTypeMustBeNull() {
//
//		MockMultipartFile file = new MockMultipartFile("file", TEST_FILE_NAME, null, "arquivo de teste".getBytes());
//		User user = new User();
//		user.setId(USER_ID);
//		ResultResponseDTO result = s3Service.upload(file, FILENAME_UUID, user);
//
//		assertNotNull(result);
//		assertEquals("400", result.metadataResponse().code());
//		assertEquals("invalid photo", result.metadataResponse().message());
//		assertEquals("0", result.metadataResponse().noOfRecord());
//	}
//
//	// delete
//	@Test
//	@DisplayName("must delete a photo")
//	public void mustDeleteAPhoto() {
//		MockMultipartFile file = new MockMultipartFile("file", TEST_FILE_NAME, TEST_IMAGE_CONTENT_TYPE,
//				"arquivo de teste".getBytes());
//
//		User user = new User();
//		user.setId(USER_ID);
//		ResultResponseDTO result = s3Service.delete(FILENAME_UUID, user);
//
//		assertNotNull(result);
//		assertEquals("200", result.metadataResponse().code());
//		assertEquals("file delete successfully", result.metadataResponse().message());
//		assertEquals("1", result.metadataResponse().noOfRecord());
//		assertEquals(FILENAME_UUID.toString(), result.result()); // Modificado para String
//	}
//
//	@Test
//	@DisplayName("must bring the user's photo")
//	public void mustBringTheUserIsPhoto() throws MalformedURLException {
//		ResponseUrlPhotoDTO resultado = s3Service.getPhoto(VALID_ID);
//
//		System.out.println("Estado do objeto: " + resultado);
//		System.out.println("Valor da URL: " + resultado.photo());
//
//		assertNotNull(resultado, "Resultado deve ser diferente de nulo");
//		assertNotNull(resultado.photo(), "URL da foto deve ser diferente de nula");
//		assertEquals(MOCK_URL, resultado.photo(), "URL da foto deve corresponder à URL esperada");
//	}
//
//	// get photo
//	//
//	// + Surgiu erros durante o teste para trazer a foto
//	// + Errors appeared during the test to bring the photo

}
