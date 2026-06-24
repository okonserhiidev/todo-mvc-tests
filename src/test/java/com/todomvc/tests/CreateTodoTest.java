package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("Create Todo")
public class CreateTodoTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Smoke")
    @Description("Typing a title and pressing Enter creates a single todo and updates the counter.")
    public void addTodo_singleItem_appearsInList() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        page.list().items().shouldHave(size(1));
        page.list().itemAt(0).self().shouldHave(exactText(TodoTexts.SIMPLE_TEXT_1));
        page.footer().counter().shouldHave(exactText("1 item left!"));
    }

    @Test
    @Story("Input behavior")
    @Description("After submitting a todo, the input is cleared so the next title can be typed straight away.")
    public void addTodo_clearsInputAfterSubmit() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        assertThat(page.header().inputValue()).isEmpty();
    }

    @Test
    @Story("List rendering")
    @Description("Sequential additions preserve insertion order — new todos appear at the bottom of the list.")
    public void addMultipleTodos_appearInOrder() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_3);

        page.list().items().shouldHave(size(3));
        page.list().itemAt(0).self().shouldHave(exactText(TodoTexts.SIMPLE_TEXT_1));
        page.list().itemAt(1).self().shouldHave(exactText(TodoTexts.SIMPLE_TEXT_2));
        page.list().itemAt(2).self().shouldHave(exactText(TodoTexts.SIMPLE_TEXT_3));
    }

    @Test
    @Story("Edge cases")
    @Description("Leading and trailing whitespace is stripped before the todo is saved.")
    public void addTodo_trimsLeadingTrailingWhitespace() {
        page.header().addTodo(TodoTexts.WITH_LEADING_TRAILING_SPACES);

        page.list().items().shouldHave(size(1));
        page.list().itemAt(0).self().shouldHave(exactText(TodoTexts.TRIMMED));
    }

    @Test
    @Story("Edge cases")
    @Description("A whitespace-only entry is rejected by the validator and never reaches the list.")
    public void addTodo_whitespaceOnly_doesNotCreate() {
        page.header().addTodo(TodoTexts.WHITESPACE_ONLY);

        page.list().items().shouldBe(empty);
    }

    @Test
    @Story("Edge cases")
    @Description("Unicode and emoji round-trip unchanged — Cyrillic + emoji + CJK in one title.")
    public void addTodo_unicodeAndEmoji_renderedAsIs() {
        page.header().addTodo(TodoTexts.UNICODE_EMOJI);

        page.list().itemAt(0).self().shouldHave(exactText(TodoTexts.UNICODE_EMOJI));
    }

    @Test
    @Story("Edge cases")
    @Description("A 300-character title is saved verbatim and the layout does not break.")
    public void addTodo_longText_savedVerbatim() {
        page.header().addTodo(TodoTexts.LONG_TEXT);

        page.list().itemAt(0).self().shouldHave(exactText(TodoTexts.LONG_TEXT));
    }
}
