package com.cognizant.pdfuploader;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PDFApplicationTests {

	MockMvc mockMvc;

	@Autowired
	WebApplicationContext appContext;

	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Value("${postAPIURL}")
	private String postAPIURL;

	@Value("${postNCommentsAPIURL}")
	private String postNCommentsAPIURL;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(this.appContext).build();
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void A_fileUpload() throws Exception {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputFile = classloader.getResourceAsStream("maxsize.pdf");

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"maxsize.pdf\" }";

		MockMultipartFile file = new MockMultipartFile("file", "TestFile", "multipart/form-data", inputFile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/uploadandviewpdf").file(file).param("user",
				userReq);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertTrue(200 == result.getResponse().getStatus());

	}

	@Test
	public void A_fileUpload1() throws Exception {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputFile = classloader.getResourceAsStream("dummy.pdf");

		String userReq = "{ \"userID\": \"123456\", \"name\": \"shyam\", \"fileName\": \"dummy.pdf\" }";

		MockMultipartFile file = new MockMultipartFile("file", "TestFile", "multipart/form-data", inputFile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/uploadandviewpdf").file(file).param("user",
				userReq);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		Assert.assertTrue(200 == result.getResponse().getStatus());

	}

	@Test
	public void B_viewfileUpload() throws Exception {

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"maxsize.pdf\" }";

		MvcResult result = mockMvc.perform(post("/viewpdf").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		Assert.assertTrue(200 == result.getResponse().getStatus());

	}

	@Test
	public void C_view_Nofile() throws Exception {

		String userReq = "{ \"userID\": \"1234\", \"name\": \"shyam\", \"fileName\": \"nofile.pdf\" }";

		MvcResult result = mockMvc.perform(post("/viewpdf").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String expectedResponse = "[{\"fileName\":null,\"name\":null,\"fileData\":null,\"errorCode\":\"CTS_ERR_003\",\"errorMessage\":\"Please upload files\"}]";

		Assert.assertTrue(200 == result.getResponse().getStatus());

		Assert.assertTrue(expectedResponse.equals(result.getResponse().getContentAsString()));

	}

	@Test
	public void D_FileRemove() throws Exception {

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"nofile.pdf\" }";

		MvcResult result = mockMvc
				.perform(post("/removeandviewpdf").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		Assert.assertTrue(200 == result.getResponse().getStatus());

	}

	@Test
	public void D_FileRemove_NoFile() throws Exception {

		String userReq = "{ \"userID\": \"123\", \"name\": \"shyam\", \"fileName\": \"nofile.pdf\" }";

		MvcResult result = mockMvc
				.perform(post("/removeandviewpdf").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String expectedResponse = "[{\"fileName\":null,\"name\":null,\"fileData\":null,\"errorCode\":\"CTS_ERR_004\",\"errorMessage\":\"No Files Available to remove\"}]";

		Assert.assertTrue(200 == result.getResponse().getStatus());

		Assert.assertTrue(expectedResponse.equals(result.getResponse().getContentAsString()));

	}

	@Test
	public void E_Invalid_Extension() throws Exception {

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream inputFile = classloader.getResourceAsStream("maxsize.pdf");

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"maxsize.txt\" }";

		MockMultipartFile file = new MockMultipartFile("file", "TestFile", "multipart/form-data", inputFile);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/uploadandviewpdf").file(file).param("user",
				userReq);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expectedResponse = "[{\"fileName\":null,\"name\":null,\"fileData\":null,\"errorCode\":\"CTS_ERR_002\",\"errorMessage\":\"Invalid File Extension\"}]";

		Assert.assertTrue(200 == result.getResponse().getStatus());

		Assert.assertTrue(expectedResponse.equals(result.getResponse().getContentAsString()));

	}

	@Test
	public void F_UserAddPost() throws Exception {

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"maxsize.pdf\", \"postTitle\": \"Test Post\", \"postBody\": \"Adding new Post\" }";

		String addPostMockResponse = "{ \"userId\": 1, \"id\": 101, \"title\": \"Test Post\", \"body\": \"Mocking new Post\" }";

		String getPostnCommentsMockResponse = "[ { \"postId\": 1, \"id\": 1, \"name\": \"Mock Response\", \"email\": \"Eliseo@gardner.biz\", \"body\": \"Mock enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\" }, { \"postId\": 1, \"id\": 2, \"name\": \"quo vero reiciendis velit similique earum\", \"email\": \"Jayne_Kuhic@sydney.com\", \"body\": \"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\" }, { \"postId\": 1, \"id\": 3, \"name\": \"odio adipisci rerum aut animi\", \"email\": \"Nikita@garfield.biz\", \"body\": \"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\" }, { \"postId\": 1, \"id\": 4, \"name\": \"alias odio sit\", \"email\": \"Lew@alysha.tv\", \"body\": \"non et atque\\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\\nquia voluptas consequuntur itaque dolor\\net qui rerum deleniti ut occaecati\" }, { \"postId\": 1, \"id\": 5, \"name\": \"vero eaque aliquid doloribus et culpa\", \"email\": \"Hayden@althea.biz\", \"body\": \"harum non quasi et ratione\\ntempore iure ex voluptates in ratione\\nharum architecto fugit inventore cupiditate\\nvoluptates magni quo et\" } ]";

		String postnCommentsURL = MessageFormat.format(postNCommentsAPIURL, "1");
		mockServer.expect(ExpectedCount.once(), requestTo(new URI(postAPIURL))).andExpect(method(HttpMethod.POST))
				.andRespond(
						withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(addPostMockResponse));

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(postnCommentsURL)))
				.andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(getPostnCommentsMockResponse));

		MvcResult result = mockMvc.perform(post("/addPost").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String expectedResponse = "{\"posts\":{\"postnCommentsResponse\":[{\"postId\":1,\"it\":0,\"body\":\"Mock enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\",\"email\":\"Eliseo@gardner.biz\",\"name\":\"Mock Response\"},{\"postId\":1,\"it\":0,\"body\":\"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\",\"email\":\"Jayne_Kuhic@sydney.com\",\"name\":\"quo vero reiciendis velit similique earum\"},{\"postId\":1,\"it\":0,\"body\":\"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\",\"email\":\"Nikita@garfield.biz\",\"name\":\"odio adipisci rerum aut animi\"},{\"postId\":1,\"it\":0,\"body\":\"non et atque\\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\\nquia voluptas consequuntur itaque dolor\\net qui rerum deleniti ut occaecati\",\"email\":\"Lew@alysha.tv\",\"name\":\"alias odio sit\"},{\"postId\":1,\"it\":0,\"body\":\"harum non quasi et ratione\\ntempore iure ex voluptates in ratione\\nharum architecto fugit inventore cupiditate\\nvoluptates magni quo et\",\"email\":\"Hayden@althea.biz\",\"name\":\"vero eaque aliquid doloribus et culpa\"}]},\"errorCode\":null,\"errorMessage\":null}";

		Assert.assertTrue(200 == result.getResponse().getStatus());

		Assert.assertTrue(expectedResponse.equals(result.getResponse().getContentAsString()));

	}

	@Test
	public void G_UserViewPost() throws Exception {

		String userReq = "{ \"userID\": \"12345\", \"name\": \"shyam\", \"fileName\": \"maxsize.pdf\", \"postTitle\": \"Test Post\", \"postBody\": \"Adding new Post\" }";

		String getPostnCommentsMockResponse = "[ { \"postId\": 1, \"id\": 1, \"name\": \"Mock Response\", \"email\": \"Eliseo@gardner.biz\", \"body\": \"Mock enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\" }, { \"postId\": 1, \"id\": 2, \"name\": \"quo vero reiciendis velit similique earum\", \"email\": \"Jayne_Kuhic@sydney.com\", \"body\": \"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\" }, { \"postId\": 1, \"id\": 3, \"name\": \"odio adipisci rerum aut animi\", \"email\": \"Nikita@garfield.biz\", \"body\": \"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\" }, { \"postId\": 1, \"id\": 4, \"name\": \"alias odio sit\", \"email\": \"Lew@alysha.tv\", \"body\": \"non et atque\\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\\nquia voluptas consequuntur itaque dolor\\net qui rerum deleniti ut occaecati\" }, { \"postId\": 1, \"id\": 5, \"name\": \"vero eaque aliquid doloribus et culpa\", \"email\": \"Hayden@althea.biz\", \"body\": \"harum non quasi et ratione\\ntempore iure ex voluptates in ratione\\nharum architecto fugit inventore cupiditate\\nvoluptates magni quo et\" } ]";

		String postnCommentsURL = MessageFormat.format(postNCommentsAPIURL, "1");
		mockServer.expect(ExpectedCount.once(), requestTo(new URI(postnCommentsURL)))
				.andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(getPostnCommentsMockResponse));
		
		MvcResult result = mockMvc.perform(post("/getPost").content(userReq).contentType(MediaType.APPLICATION_JSON))
				.andReturn();

		String expectedResponse = "{\"posts\":{\"postnCommentsResponse\":[{\"postId\":1,\"it\":0,\"body\":\"Mock enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\",\"email\":\"Eliseo@gardner.biz\",\"name\":\"Mock Response\"},{\"postId\":1,\"it\":0,\"body\":\"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\",\"email\":\"Jayne_Kuhic@sydney.com\",\"name\":\"quo vero reiciendis velit similique earum\"},{\"postId\":1,\"it\":0,\"body\":\"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\",\"email\":\"Nikita@garfield.biz\",\"name\":\"odio adipisci rerum aut animi\"},{\"postId\":1,\"it\":0,\"body\":\"non et atque\\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\\nquia voluptas consequuntur itaque dolor\\net qui rerum deleniti ut occaecati\",\"email\":\"Lew@alysha.tv\",\"name\":\"alias odio sit\"},{\"postId\":1,\"it\":0,\"body\":\"harum non quasi et ratione\\ntempore iure ex voluptates in ratione\\nharum architecto fugit inventore cupiditate\\nvoluptates magni quo et\",\"email\":\"Hayden@althea.biz\",\"name\":\"vero eaque aliquid doloribus et culpa\"}]},\"errorCode\":null,\"errorMessage\":null}";

		Assert.assertTrue(200 == result.getResponse().getStatus());

		Assert.assertTrue(expectedResponse.equals(result.getResponse().getContentAsString()));

	}

}
