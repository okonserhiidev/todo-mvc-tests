package com.todomvc.pages.components;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class TodoItemComponent {

    private final SelenideElement root;

    public TodoItemComponent(SelenideElement root) {
        this.root = root;
    }

    @Step("Toggle todo")
    public TodoItemComponent toggle() {
        toggleCheckbox().click();
        return this;
    }

    @Step("Delete todo")
    public TodoItemComponent delete() {
        root.hover();
        destroyButton().click();
        return this;
    }

    @Step("Enter edit mode")
    public TodoItemComponent enterEditMode() {
        label().doubleClick();
        return this;
    }

    @Step("Replace edit text with: '{newText}'")
    public TodoItemComponent replaceEditText(String newText) {
        editInput().setValue(newText);
        return this;
    }

    @Step("Save edit with Enter")
    public TodoItemComponent saveWithEnter() {
        editInput().pressEnter();
        return this;
    }

    @Step("Save edit with blur (click outside)")
    public TodoItemComponent saveWithBlur() {
        Selenide.$("body").click();
        return this;
    }

    public SelenideElement self() {
        return root;
    }

    public SelenideElement label() {
        return root.$("[data-testid='todo-item-label']");
    }

    public SelenideElement toggleCheckbox() {
        return root.$("[data-testid='todo-item-toggle']");
    }

    public SelenideElement destroyButton() {
        return root.$("[data-testid='todo-item-button']");
    }

    public SelenideElement editInput() {
        return root.$(".edit");
    }
}
