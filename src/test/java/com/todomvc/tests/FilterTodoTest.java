package com.todomvc.tests;

import com.codeborne.selenide.Selenide;
import com.todomvc.core.BaseTest;
import com.todomvc.data.Filter;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Filters & routing")
public class FilterTodoTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Active filter")
    @Description("'Active' shows only non-completed todos and updates the URL and the selected link.")
    public void activeFilter_showsOnlyActiveTodos() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.list().itemByText(TodoTexts.SIMPLE_TEXT_2).toggle();

        page.footer().filterBy(Filter.ACTIVE);

        assertThat(Selenide.webdriver().driver().url()).endsWith(Filter.ACTIVE.hash());
        page.footer().filterLink(Filter.ACTIVE)
                .shouldHave(exactText(Filter.ACTIVE.label()))
                .shouldHave(cssClass("selected"));
        page.list().visibleItems().shouldHave(exactTexts(TodoTexts.SIMPLE_TEXT_1));
    }

    @Test
    @Story("Completed filter")
    @Description("'Completed' shows only completed todos, updates the URL and marks its link as selected.")
    public void completedFilter_showsOnlyCompletedTodos() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.list().itemByText(TodoTexts.SIMPLE_TEXT_1).toggle();

        page.footer().filterBy(Filter.COMPLETED);

        assertThat(Selenide.webdriver().driver().url()).endsWith(Filter.COMPLETED.hash());
        page.footer().filterLink(Filter.COMPLETED)
                .shouldHave(exactText(Filter.COMPLETED.label()))
                .shouldHave(cssClass("selected"));
        page.list().visibleItems().shouldHave(exactTexts(TodoTexts.SIMPLE_TEXT_1));
    }

    @Test
    @Story("Reactive filtering")
    @Description("While on the Active filter, completing a todo removes it from view and decrements the counter.")
    public void toggleItem_onActiveFilter_disappearsImmediately() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);

        page.footer().filterBy(Filter.ACTIVE);
        page.list().visibleItems().shouldHave(size(2));

        page.list().itemByText(TodoTexts.SIMPLE_TEXT_1).toggle();

        page.list().visibleItems().shouldHave(exactTexts(TodoTexts.SIMPLE_TEXT_2));
        page.footer().counter().shouldHave(exactText("1 item left!"));
    }
}
