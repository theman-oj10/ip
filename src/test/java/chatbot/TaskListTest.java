package chatbot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void testAddTodoWithCorrectFormat() {
        TaskList taskList = new TaskList();
        String result = taskList.addTaskFromInput("todo read book");
        assertEquals("Got it. I've added this task:\n  [T][ ] read book\nNow you have 1 task in the list.", result);
    }
    @Test
    public void testAddTodoWithEmptyDescription() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce. The description of a todo cannot be empty.",
                taskList.addTaskFromInput("todo"));
    }
    @Test
    public void testAddDeadlineWithCorrectFormat() {
        TaskList taskList = new TaskList();
        String result = taskList.addTaskFromInput("deadline return book /by 22/12/2021 1800");
        assertEquals("Got it. I've added this task:\n  [D][ ] return book (by: 22/12/2021 18:00)\n"
                + "Now you have 1 task in the list.", result);
    }
    @Test
    public void testAddDeadlineWithEmptyDescription() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce. The description of a deadline cannot be empty.",
                taskList.addTaskFromInput("deadline /by 22/12/2021 1800"));
    }
    @Test
    public void testAddDeadlineWithEmptyBy() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce. Please specify the due date or time by including /by.",
                taskList.addTaskFromInput("deadline return book /by"));
    }
    @Test
    public void testAddEventWithCorrectFormat() {
        TaskList taskList = new TaskList();
        String result = taskList.addTaskFromInput("event Project meeting /from 12/12/2023 0900 /to 12/12/2023 1200");
        assertEquals("Got it. I've added this task:\n  [E][ ] Project meeting (from: 12/12/2023 09:00 to: 12/12/2023"
                + " 12:00)\nNow you have 1 task in the list.", result);
    }
    @Test
    public void testAddEventWithEmptyDescription() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce. The description of an event cannot be empty.",
                taskList.addTaskFromInput("event /from 12/12/2023 0900 /to 12/12/2023 1200"));
    }
    @Test
    public void testAddEventWithEmptyFrom() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce.Please specify both description, start time, and end time.",
                taskList.addTaskFromInput("event Project meeting /from /to 12/12/2023 1200"));
    }
    @Test
    public void testAddEventWithEmptyTo() {
        TaskList taskList = new TaskList();
        assertEquals("Sorry Master Bruce.Please specify both description, start time, and end time.",
                taskList.addTaskFromInput("event Project meeting /from 12/12/2023 0900 /to"));
    }
    @Test
    public void testMarkTaskAsDone() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        assertDoesNotThrow(() -> {
            String result = taskList.markList(0);
            assertEquals("Nice! I've marked this task as done:\n  [T][X] read book", result);
        });
    }
    @Test
    public void testMarkTaskAsDoneWithInvalidIndex() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        try {
            taskList.markList(1);
        } catch (AlfredException e) {
            assertEquals("Sorry Master Bruce. The task number you have entered is not in the list."
                    + " There is only one item in the list.", e.getMessage());
        }
    }
    @Test
    public void testListTasks() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        taskList.addTaskFromInput("deadline return book /by 22/12/2021 1800");
        taskList.addTaskFromInput("event Project meeting /from 12/12/2023 0900 /to 12/12/2023 1200");
        assertEquals("1. [T][ ] read book\n"
                + "2. [D][ ] return book (by: 22/12/2021 18:00)\n"
                + "3. [E][ ] Project meeting (from: 12/12/2023 09:00 to: 12/12/2023 12:00)\n", taskList.toString());
    }
    @Test
    public void testDeleteTask() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        assertDoesNotThrow(() -> {
            String result = taskList.deleteList(0);
            assertEquals("Noted. I've removed this task:\n  "
                    + "[T][ ] read book\nNow you have 0 tasks in the list.", result);
        });
    }
    @Test
    public void testDeleteTaskWithInvalidIndex() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        try {
            taskList.deleteList(1);
        } catch (AlfredException e) {
            assertEquals("Sorry Master Bruce. The task number you have entered is not in the list."
                    + " There is only one item in the list.", e.getMessage());
        }
    }
    @Test
    public void testFindByDate() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("deadline Return book /by 22/12/2021 1800");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        LocalDateTime dateTime = LocalDateTime.parse("22/12/2021 1800", formatter);
        TaskList result = taskList.findByDate(dateTime);
        assertEquals(1, result.taskList.size());
    }

    @Test
    public void testFindByKeyword() {
        TaskList taskList = new TaskList();
        taskList.addTaskFromInput("todo read book");
        TaskList result = taskList.findByKeyword("read");
        assertEquals(1, result.taskList.size());
    }
}

