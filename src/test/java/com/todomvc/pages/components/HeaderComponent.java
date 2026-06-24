package com.todomvc.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent {

    private final SelenideElement newTodoInput =
            $("[data-testid='header'] [data-testid='text-input']");

    @Step("Add todo: {text}")
    public HeaderComponent addTodo(String text) {
        newTodoInput.setValue(text).pressEnter();
        return this;
    }

    public String inputValue() {
        return newTodoInput.getValue();
    }
}
