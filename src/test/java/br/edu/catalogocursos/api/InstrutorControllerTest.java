package br.edu.catalogocursos.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InstrutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cadastroDeInstrutorInvalidoRetorna400() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "A", "email": "email-invalido"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("nome")))
                .andExpect(jsonPath("$.message", containsString("email")))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void cadastroDeInstrutorSemCamposRetorna400() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void cadastroDeInstrutorComNomeAcimaDe120CaracteresRetorna400() throws Exception {
        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "%s", "email": "longo@example.com"}
                                """.formatted("a".repeat(121))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("nome")));
    }

    @Test
    void cadastroComEmailDuplicadoRetorna409() throws Exception {
        String corpo = """
                {"nome": "Marina Costa", "email": "%s"}
                """.formatted("dup." + UUID.randomUUID() + "@example.com");
        mockMvc.perform(post("/api/instrutores").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/instrutores").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message", containsString("Email já cadastrado")))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void consultarInstrutorInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/instrutores/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Instrutor não encontrado"));
    }

    @Test
    void listarCursosDeInstrutorInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/instrutores/999999/cursos"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Instrutor não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/instrutores/999999/cursos"));
    }
}
