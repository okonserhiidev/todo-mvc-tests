package com.todomvc.pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import com.todomvc.data.Filter;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;

public class FooterComponent {

    private final SelenideElement root = $("[data-testid='footer']");
    private final SelenideElement counter = root.$(".todo-count");
    private final ElementsCollection filterLinks = root.$$(".filters a");
    private final SelenideElement clearCompletedButton = root.$(".clear-completed");

    private static WebElementCondition hrefEndsWith(String hash) {
        return Condition.attributeMatching("href", ".*" + Pattern.quote(hash) + "$");
    }

    @Step("Filter by {filter}")
    public FooterComponent filterBy(Filter filter) {
        filterLinks.findBy(hrefEndsWith(filter.hash())).click();
        return this;
    }

    @Step("Click 'Clear completed'")
    public FooterComponent clearCompleted() {
        clearCompletedButton.click();
        return this;
    }

    public SelenideElement counter() {
        return counter;
    }

    public SelenideElement clearCompletedButton() {
        return clearCompletedButton;
    }

    public SelenideElement filterLink(Filter filter) {
        return filterLinks.findBy(hrefEndsWith(filter.hash()));
    }

    public SelenideElement self() {
        return root;
    }
}
