package com.example.cashcard.controllers;

import com.example.cashcard.domain.CashCard;
import com.example.cashcard.services.CashCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cashcards")
@RequiredArgsConstructor
public class CashCardController {

    private final CashCardService cashCardService;

    //    Поиск карты по id
    @GetMapping("{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId) {
//        Вызываем метод из сервиса
        return this.cashCardService.findById(requestedId)
//                .map(ResponseEntity::ok) - метод map - это операция последовательностей в Java 8,
//                которая применяет функцию к элементам последовательности.
//                В данном случае, если Optional содержит значение, оно будет "распаковано" в ResponseEntity
//                с помощью статического метода ResponseEntity::ok.
//                В результате, мы получим ResponseEntity с телом, содержащим кэш-карту, и статус-кодом HTTP 200 OK.
                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build()) - если Optional не содержит значения
//                (например, кэш-карты с указанным идентификатором не найдена),
//                то мы создаем ResponseEntity с HTTP-статусом 404 Not Found.
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //    Создание карты
    @PostMapping
    private ResponseEntity<CashCard> createCashCard(@RequestBody CashCard newCashCard, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardService.saveCashCard(newCashCard);
//         здесь мы создаем URI, который будет содержать локацию созданной кэш-карты.
//         Мы используем UriComponentsBuilder для построения URI с помощью шаблона /cashcards/{id},
//         где {id} - это идентификатор созданной кэш-карты.
        URI localtionOfNewCashCard = ucb
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.getId())
                .toUri();
//        в конце, мы создаем и возвращаем ResponseEntity с HTTP-статусом 201 Created и телом,
//        содержащим созданную кэш-карту. Кроме того, мы устанавливаем заголовок Location в заголовки HTTP-ответа,
//        содержащий URI локации созданной кэш-карты.
//        Это позволяет клиенту легко получить доступ к созданному ресурсу.
        return ResponseEntity.created(localtionOfNewCashCard).body(savedCashCard);
    }

    //	Ищем все карты
//    Для метода findAll добавляем обязательный параметр аунтификации
//    Далее сравниваем данные, которые пришли от аунтифицированного принципала с данными карты owner
    @GetMapping
//    @CurrentSecurityContext(expression = "authentication") - аннотация которая позволяет вводить текущий контекст безопасности в параметр метода
    /*
     *   Используя @CurrentSecurityContext в параметре аутентификации, мы указываем Spring Security внедрить в метод текущий объект аутентификации.
     *  Это позволяет нам получить доступ к данным аутентифицированного пользователя, таким как его имя пользователя, роли и другие атрибуты.
     */
    private ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext(expression = "authentication.name") String owner) {
        var result =
                this.cashCardService.findByOwner(owner);
        return ResponseEntity.ok(result);
    }

}
