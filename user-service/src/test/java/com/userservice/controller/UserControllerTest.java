package com.userservice.controller;

import com.jayway.jsonpath.JsonPath;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Container
    static MySQLContainer<?> mySql=
            new MySQLContainer<>("mysql:8.0.34")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registery) {
        registery.add("spring.datasource.url",mySql::getJdbcUrl);
        registery.add("spring.datasource.username",mySql::getUsername);
        registery.add("spring.datasource.password",mySql::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User existingUser;

    @BeforeEach
    void setUp() {
        User user=new User();
        user.setPhone("1234567890");
        user.setRole(Role.USER);
        user.setAge(20);
        user.setEmail("pramod@gmail.com");
        user.setPassword(passwordEncoder.encode("password@123"));
        user.setFullName("Pramod Masanakatti");
       existingUser=userRepository.save(user);
   }

   @Test
    public void testRegisterUserSuccess() throws Exception{
        String payload= """
                {
                 "fullName":"Abc",
                 "email":"abc@gmail.com",
                 "password":"password@12234",
                 "age":"20",
                 "role":"ADMIN",
                 "phone":"12345676543"
                 }""";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists());
        assertNotNull(userRepository.findByEmail("abc@gmail.com"));
   }

   @Test
    public void testRegisterUserFail() throws Exception{
        String payload= """
                {
                "fullName":"Abc",
                 "email":"pramod@gmail.com",
                 "password":"password@12234",
                 "age":"20",
                 "role":"ADMIN",
                 "phone":"12345676543"}""";
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isConflict());
   }

   @Test
    public void testGetuserSuccess() throws Exception{

        String payLoad= """
                {
                "email":"pramod@gmail.com",
                "password":"password@123"}
                """;
        String response=mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payLoad))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token= JsonPath.read(response,"$.token");
        Integer userId=existingUser.getUserId();
        String uri="/api/users/"+userId;
        mockMvc.perform(get(uri)
                        .header("Authorization","Bearer " +token))
               .andExpect(status().isOk());
   }


   @Test
    public void testGetUserFail() throws Exception{
       String payLoad= """
                {
                "email":"pramod@gmail.com",
                "password":"password@123"}
                """;
      String response=mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payLoad))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

      String token=JsonPath.read(response,"$.token");

      mockMvc.perform(get("/api/users/10000")
                      .header("Authorization","Bearer " +token))
              .andExpect(status().isNotFound());
   }
}