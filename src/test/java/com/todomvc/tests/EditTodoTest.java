package com.todomvc.tests;

import com.todomvc.core.BaseTest;
import com.todomvc.data.TodoTexts;
import com.todomvc.pages.TodoPage;
import com.todomvc.pages.components.TodoItemComponent;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.focused;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

@Feature("Edit Todo")
public class EditTodoTest extends BaseTest {

    private final TodoPage page = new TodoPage();

    @Test
    @Story("Enter edit mode")
    @Description("Double-click on the todo label puts it in edit mode: the input is visible, focused and pre-filled.")
    public void doubleClick_activatesEditMode() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        TodoItemComponent item = page.list().itemAt(0);
        item.enterEditMode();

        item.self().shouldHave(cssClass("editing"));
        item.editInput().shouldBe(visible).shouldBe(focused);
        item.editInput().shouldHave(value(TodoTexts.SIMPLE_TEXT_1));
    }

    @Test
    @Story("Save with Enter")
    @Description("Pressing Enter while editing commits the new value and leaves edit mode.")
    public void edit_enter_savesNewText() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        TodoItemComponent item = page.list().itemAt(0);
        item.enterEditMode().replaceEditText(TodoTexts.UPDATED_TITLE).saveWithEnter();

        item.self().shouldHave(not(cssClass("editing")));
        item.label().shouldHave(exactText(TodoTexts.UPDATED_TITLE));
    }

    @Test
    @Story("Save with blur")
    @Description("Clicking outside the edit input commits the new value (blur is treated as save).")
    public void edit_blur_savesNewText() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        TodoItemComponent item = page.list().itemAt(0);
        item.enterEditMode().replaceEditText(TodoTexts.UPDATED_TITLE).saveWithBlur();

        item.self().shouldHave(not(cssClass("editing")));
        item.label().shouldHave(exactText(TodoTexts.UPDATED_TITLE));
    }

    @Test
    @Story("Edge cases")
    @Description("Saving an empty edit deletes the todo.")
    public void edit_toEmpty_deletesTodo() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);

        TodoItemComponent item = page.list().itemAt(0);
        item.enterEditMode().replaceEditText("").saveWithEnter();

        page.list().items().shouldBe(empty);
    }

    @Test
    @Story("Edit-trigger guards")
    @Description("Double-clicking on the toggle checkbox must not enter edit mode.")
    public void doubleClick_onToggleCheckbox_doesNotActivateEdit() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        TodoItemComponent item = page.list().itemAt(0);

        item.toggleCheckbox().doubleClick();

        item.self().shouldHave(not(cssClass("editing")));
    }

    @Test
    @Story("Edit-trigger guards")
    @Description("Double-clicking the destroy button deletes the todo without ever entering edit mode.")
    public void doubleClick_onDestroyButton_doesNotActivateEdit() {
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_1);
        page.header().addTodo(TodoTexts.SIMPLE_TEXT_2);
        TodoItemComponent target = page.list().itemByText(TodoTexts.SIMPLE_TEXT_2);

        target.self().hover();
        target.destroyButton().doubleClick();

        page.list().items().shouldHave(size(1));
        page.list().itemAt(0).self().shouldHave(not(cssClass("editing")));
    }
}
