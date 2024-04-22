package com.example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
/*
 *	@AutoConfigureMockMvc - эта аннотация используется для автоматической конфигурации модуля MockMvc в тесте.
 *  Она настраивает MockMvc с помощью конфигурации по умолчанию, включая фильтры,
 *  слушатели и интерцепторы, которые могут быть определены в приложении.
 *  Это позволяет нам легко выполнять HTTP-запросы к контроллерам в тестах,
 *  не запуская полноценное приложение Spring.
 */
@AutoConfigureMockMvc
/*
 *  @WithMockUser - эта аннотация используется для установки заранее определенного пользователя в контексте безопасности Spring.
 *  Она позволяет нам легко имитировать аутентифицированного пользователя в тестах, не запуская полноценную систему аутентификации и авторизации.
 *  Аннотация принимает различные параметры, такие как username, password, roles и authorities, которые могут быть использованы для определения пользователя.
 */
@WithMockUser(username = "hiss")
class CashcardApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    //	Проверка на наличие карты, совпадение её данных и её владельца
    @Test
    void shouldReturnACashCardWhenDataIsSaved() throws Exception {
        this.mockMvc.perform(get("/cashcards/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.owner").value("hiss"));
    }

    //	Проверка на сохранение карты
    @Test
//	@DirtiesContext - аннотация указывает, что контекст Spring будет "грязным" после выполнения теста,
//	т.е. он не может быть повторно использован для других тестов.
//	Это необходимо, чтобы убедиться, что тест не загрязняет состояние базы данных и других ресурсов.
    @DirtiesContext
    void shouldCreateANewCashCard() throws Exception {
//		здесь мы выполняем HTTP-запрос типа POST на корень ресурса /cashcards.
//		В теле запроса содержится JSON-представление новой кэш-карты с идентификатором hiss и суммой 250.00.
        String location = this.mockMvc.perform(post("/cashcards")
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                                {
                                	"amount": 250.00,
                                	"owner": "hiss"
                                }
                                """))
//				Проверяем, что статус ответа равен 201 Created
                .andExpect(status().isCreated())
//				Проверяем, что заголовок Location присутствует в ответе HTTP-запроса
                .andExpect(header().exists("Location"))
//				мы извлекаем значение заголовка Location и сохраняем его в переменную location.
                .andReturn().getResponse().getHeader("Location");

//		здесь мы выполняем HTTP-запрос типа GET на URI локации созданной кэш-карты.
        this.mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(250.00))
                .andReturn().getResponse().getHeader("Location");
        this.mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(250.00))
                .andExpect(jsonPath("$.owner").value("hiss"));

    }

    //	Тест для проверки метода поиска всех карт
    @Test
    void shouldReturnAllCashCardsWhenListIsRequested() throws Exception {
        this.mockMvc.perform(get("/cashcards"))
                .andExpect(status().isOk())
//                Проверяем, что у нашего принципала из jwt токена приходит нужное количество карт и в приницпе тот самый принципал
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$..owner").value(everyItem(equalTo("hiss"))));
    }

}
