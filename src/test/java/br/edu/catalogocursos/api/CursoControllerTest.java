package br.edu.catalogocursos.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(ints = {0, 401})
    void cargaHorariaForaDoIntervaloRetorna400(int cargaHoraria) throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Curso valido", "cargaHoraria": %d, "nivel": "INICIANTE", "instrutorId": 1}
                                """.formatted(cargaHoraria)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("cargaHoraria")))
                .andExpect(jsonPath("$.path").value("/api/cursos"));
    }

    @Test
    void cursoSemCamposObrigatoriosRetorna400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "AB"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("titulo")))
                .andExpect(jsonPath("$.message", containsString("nivel")))
                .andExpect(jsonPath("$.message", containsString("instrutorId")));
    }

    @Test
    void nivelInexistenteRetorna400() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Curso valido", "cargaHoraria": 10, "nivel": "EXPERT", "instrutorId": 1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void cadastroComInstrutorInexistenteRetorna404() throws Exception {
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Curso orfao", "cargaHoraria": 10, "nivel": "INICIANTE", "instrutorId": 999999}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Instrutor não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/cursos"));
    }

    @Test
    void consultarCursoInexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/cursos/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Curso não encontrado"))
                .andExpect(jsonPath("$.path").value("/api/cursos/999999"));
    }

    @Test
    void consultarCursoComIdNaoNumericoRetorna400() throws Exception {
        mockMvc.perform(get("/api/cursos/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cursoComInstrutorValidoApareceNaListagemDoInstrutor() throws Exception {
        String instrutor = mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Marina Costa", "email": "%s"}
                                """.formatted("curso." + UUID.randomUUID() + "@example.com")))
                .andReturn().getResponse().getContentAsString();
        Integer instrutorId = JsonPath.read(instrutor, "$.id");

        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo": "Spring Data JPA", "cargaHoraria": 400, "nivel": "AVANCADO", "instrutorId": %d}
                                """.formatted(instrutorId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/instrutores/{id}/cursos", instrutorId))
                .andExpect(jsonPath("$[0].titulo").value("Spring Data JPA"))
                .andExpect(jsonPath("$[0].nivel").value("AVANCADO"));
    }
}
