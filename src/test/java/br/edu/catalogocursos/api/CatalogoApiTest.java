package br.edu.catalogocursos.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogoApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cadastrarInstrutorRetorna201ComLocationESemColecaoDeCursos() throws Exception {
        String email = emailUnico();
        MockHttpServletResponse resposta = mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(instrutorJson("Marina Costa", email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Marina Costa"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.cursos").doesNotExist())
                .andReturn().getResponse();

        Integer id = JsonPath.read(resposta.getContentAsString(), "$.id");
        assertThat(resposta.getHeader("Location")).endsWith("/api/instrutores/" + id);
    }

    @Test
    void cadastrarCursoRetorna201ComLocationEResumoDoInstrutor() throws Exception {
        Integer instrutorId = cadastrarInstrutor("Marina Costa");

        MockHttpServletResponse resposta = mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson("APIs REST com Spring Boot", instrutorId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("APIs REST com Spring Boot"))
                .andExpect(jsonPath("$.cargaHoraria").value(40))
                .andExpect(jsonPath("$.nivel").value("INTERMEDIARIO"))
                .andExpect(jsonPath("$.instrutor.id").value(instrutorId))
                .andExpect(jsonPath("$.instrutor.nome").value("Marina Costa"))
                .andReturn().getResponse();

        Integer id = JsonPath.read(resposta.getContentAsString(), "$.id");
        assertThat(resposta.getHeader("Location")).endsWith("/api/cursos/" + id);
    }

    @Test
    void consultarCursoRetorna200SemRecursaoNoJson() throws Exception {
        Integer instrutorId = cadastrarInstrutor("Marina Costa");
        Integer cursoId = cadastrarCurso("APIs REST com Spring Boot", instrutorId);

        mockMvc.perform(get("/api/cursos/{id}", cursoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cursoId))
                .andExpect(jsonPath("$.instrutor.id").value(instrutorId))
                .andExpect(jsonPath("$.instrutor.nome").value("Marina Costa"))
                .andExpect(jsonPath("$.instrutor.email").doesNotExist())
                .andExpect(jsonPath("$.instrutor.cursos").doesNotExist());
    }

    @Test
    void consultarInstrutorRetorna200() throws Exception {
        Integer instrutorId = cadastrarInstrutor("Marina Costa");

        mockMvc.perform(get("/api/instrutores/{id}", instrutorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(instrutorId))
                .andExpect(jsonPath("$.nome").value("Marina Costa"))
                .andExpect(jsonPath("$.cursos").doesNotExist());
    }

    @Test
    void listarCursosDoInstrutorRetornaSomenteOsDeleOrdenadosPorTitulo() throws Exception {
        Integer marina = cadastrarInstrutor("Marina Costa");
        Integer paulo = cadastrarInstrutor("Paulo Lima");
        cadastrarCurso("Java orientado a objetos", marina);
        cadastrarCurso("Banco de dados", paulo);
        cadastrarCurso("APIs REST com Spring Boot", marina);

        mockMvc.perform(get("/api/instrutores/{id}/cursos", marina))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo").value("APIs REST com Spring Boot"))
                .andExpect(jsonPath("$[1].titulo").value("Java orientado a objetos"))
                .andExpect(jsonPath("$[0].instrutor").doesNotExist());
    }

    @Test
    void listarCursosDeInstrutorSemCursosRetornaListaVazia() throws Exception {
        Integer instrutorId = cadastrarInstrutor("Marina Costa");

        mockMvc.perform(get("/api/instrutores/{id}/cursos", instrutorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private Integer cadastrarInstrutor(String nome) throws Exception {
        String corpo = mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(instrutorJson(nome, emailUnico())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(corpo, "$.id");
    }

    private Integer cadastrarCurso(String titulo, Integer instrutorId) throws Exception {
        String corpo = mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cursoJson(titulo, instrutorId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(corpo, "$.id");
    }

    private static String emailUnico() {
        return "instrutor." + UUID.randomUUID() + "@example.com";
    }

    private static String instrutorJson(String nome, String email) {
        return """
                {"nome": "%s", "email": "%s"}
                """.formatted(nome, email);
    }

    private static String cursoJson(String titulo, Integer instrutorId) {
        return """
                {"titulo": "%s", "cargaHoraria": 40, "nivel": "INTERMEDIARIO", "instrutorId": %d}
                """.formatted(titulo, instrutorId);
    }
}
