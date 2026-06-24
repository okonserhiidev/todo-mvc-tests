package com.todomvc.pages.components;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoListComponent {

    private final ElementsCollection items = $$("[data-testid='todo-item']");
    private final SelenideElement toggleAllLabel = $(".toggle-all-label");
    private final SelenideElement root = $("[data-testid='main']");

    @Step("Toggle all todos")
    public TodoListComponent toggleAll() {
        toggleAllLabel.click(ClickOptions.usingJavaScript());
        return this;
    }

    public TodoItemComponent itemAt(int index) {
        return new TodoItemComponent(items.get(index));
    }

    public TodoItemComponent itemByText(String text) {
        return new TodoItemComponent(items.findBy(Condition.exactText(text)));
    }

    public ElementsCollection items() {
        return items;
    }

    public ElementsCollection visibleItems() {
        return items.filter(Condition.visible);
    }

    public SelenideElement self() {
        return root;
    }
}
