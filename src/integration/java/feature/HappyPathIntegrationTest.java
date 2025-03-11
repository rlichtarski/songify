package feature;

import com.songify.SongifyApplication;
import com.songify.infrastructure.security.jwt.JwtAuthConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SongifyApplication.class)
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class HappyPathIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    private Jwt createJwtAdmin() {
        return Jwt.withTokenValue("123")
                .claim("email", "rradzzio@gmail.com")
                .header("alg", "none")
                .build();
    }

    private JwtAuthenticationToken createJwtWithAdminRole() {
        Jwt jwt = createJwtAdmin();
        return jwtAuthConverter.convert(jwt);
    }

    private Jwt createJwtUser() {
        return Jwt.withTokenValue("123")
                .claim("email", "john@gmail.com")
                .header("alg", "none")
                .build();
    }

    private JwtAuthenticationToken createJwtWithUserRole() {
        Jwt jwt = createJwtUser();
        return jwtAuthConverter.convert(jwt);
    }

    @Test
    public void happy_path() throws Exception {
        mockMvc.perform(get("/songs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs").isEmpty());

        mockMvc.perform(get("/songs")
                        .with(authentication(createJwtWithAdminRole()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs").isEmpty());

        mockMvc.perform(post("/songs"))
                        .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/songs")
                        .with(authentication(createJwtWithUserRole()))
                ).andExpect(status().isForbidden());

        mockMvc.perform(post("/songs")
//                .with(jwt().authorities(() -> "ROLE_ADMIN"))
                .with(authentication(createJwtWithAdminRole()))
                .content(
                """
                    {
                        "name": "Till I collapse",
                        "releaseDate": "2025-02-18T20:56:47.283Z",
                        "duration": 0,
                        "language": "ENGLISH"
                    }
                """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.song.id", is(1)))
        .andExpect(jsonPath("$.song.name", is("Till I collapse")))
        .andExpect(jsonPath("$.song.genre.id", is(1)))
        .andExpect(jsonPath("$.song.genre.name", is("default")));

        mockMvc.perform(post("/songs")
                .with(jwt().authorities(() -> "ROLE_ADMIN"))
                .content(
                """
                    {
                        "name": "Lose yourself",
                        "releaseDate": "2025-02-18T20:56:47.283Z",
                        "duration": 0,
                        "language": "ENGLISH"
                    }
                """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.song.id", is(2)))
        .andExpect(jsonPath("$.song.name", is("Lose yourself")))
        .andExpect(jsonPath("$.song.genre.id", is(1)))
        .andExpect(jsonPath("$.song.genre.name", is("default")));

        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("default")));

        mockMvc.perform(post("/genres")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .content("""
                                {
                                  "name": "Rap"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("Rap")));

        mockMvc.perform(get("/songs/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.genre.id", is(1)))
                .andExpect(jsonPath("$.song.genre.name", is("default")));

        mockMvc.perform(put("/songs/1/genres/2")
                .with(jwt().authorities(() -> "ROLE_ADMIN"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("updated")));

        mockMvc.perform(get("/songs/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.song.genre.name", is("Rap")));

        mockMvc.perform(get("/albums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.albums").isEmpty());

        mockMvc.perform(post("/albums")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .content("""
                                {
                                  "songsId": [
                                    1
                                  ],
                                  "title": "EminemAlbum1",
                                  "releaseDate": "2025-02-25T21:12:41.943Z"
                                }
                                """.trim())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("EminemAlbum1")))
                .andExpect(jsonPath("$.songIds", is(containsInAnyOrder(1))));

        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Album with id: 1 not found")))
                .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")));

        mockMvc.perform(post("/artists")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .content("""
                                {
                                  "name": "Eminem"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Eminem")));

        mockMvc.perform(put("/artists/1/album/1")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Probably assigned artist to album!")));

        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.artists[*].id", containsInAnyOrder(1)));

        mockMvc.perform(put("/albums/1/songs/2")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("EminemAlbum1")))
                .andExpect(jsonPath("$.songIds[*]", containsInAnyOrder(1, 2)));

        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs[*].id", containsInAnyOrder(1, 2)));
    }

}
