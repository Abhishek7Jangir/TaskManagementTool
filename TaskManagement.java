
// //Task Management tool
// //Allow user to create , update and delete task(completed)
// //Organize tasks into catagories
// //set task deadline and reminder
// //Provide filtering and sorting options for tasks
import java.io.*;
import java.util.*;
import java.util.Map;

class Task {
    String title;
    String description;
    String category;
    String deadline;
    boolean reminder;

    public Task(String title, String description, String category, String deadline, boolean reminder) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.deadline = deadline;
        this.reminder = reminder;
    }

    public String toString() {
        return "Title       : " + title + "\n" +
                "Description : " + description + "\n" +
                "Category    : " + category + "\n" +
                "Deadline    : " + deadline + "\n" +
                "Reminder    : " + (reminder ? "Set" : "Not set");
    }
}

class TaskManager {
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> deleTasks = new ArrayList<>();
    private final String dataFileName = "project.txt";
    private final String deletedDataFile = "deletedTask.txt";

    public TaskManager() {
        loadFromFile();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveInFile();
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            Task dTask = tasks.remove(index);
            deleTasks.add(dTask);
            saveInFile();
            saveDeletedTasksInFile();
            System.out.println("Task deleted Successfully!");
        } else {
            System.out.println("Invalid Task No.!");
            System.out.println("Task Not Deleted!");
        }
    }

    public void updateTask(int index, Scanner sc) {
        if (index >= 0 && index < tasks.size()) {
            Task task = tasks.get(index);
            System.out.println("1. Change Title");
            System.out.println("2. Change Description");
            System.out.println("3. Change Category");
            System.out.println("4. Change Deadline");
            System.out.println("5. Change Reminder");
            System.out.println("6. Change Complete Task");
            System.out.print("Enter your Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    task.title = sc.nextLine();
                    break;
                case 2:
                    System.out.print("Enter Description: ");
                    task.description = sc.nextLine();
                    break;
                case 3:
                    System.out.print("Enter Category: ");
                    task.category = sc.nextLine();
                    break;
                case 4:
                    System.out.print("Enter Deadline(yyyy-mm-dd): ");
                    task.deadline = sc.nextLine();
                    break;
                case 5:
                    System.out.print("Set reminder? (true/false): ");
                    task.reminder = sc.nextBoolean();
                    break;
                case 6:
                    System.out.print("Enter task title: ");
                    task.title = sc.nextLine();
                    System.out.print("Enter task description: ");
                    task.description = sc.nextLine();
                    System.out.print("Enter task category: ");
                    task.category = sc.nextLine();
                    System.out.print("Enter task deadline(yyyy-mm-dd): ");
                    task.deadline = sc.nextLine();
                    System.out.print("Set reminder? (true/false): ");
                    task.reminder = sc.nextBoolean();
                    break;
                default:
                    System.out.println("Invalid Choice: ");
                    break;
            }
            tasks.set(index, task);
            saveInFile();
            System.out.println("Task updated successfully!");
        } else {
            System.out.println("Invalid Task No.");
        }
    }

    public void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No task Found!");
        } else {
            for (Task task : tasks) {
                System.out.println("Task: " + (tasks.indexOf(task) + 1));
                System.out.println("_______________________________");
                System.out.println(task);
                System.out.println("_______________________________");
            }
        }
    }

    public void displayTasksByCategory() {
        HashMap<String, ArrayList<Task>> tasksByCategory = new HashMap<>();
        for (Task task : tasks) {
            if (!tasksByCategory.containsKey(task.category)) {
                tasksByCategory.put(task.category, new ArrayList<>());
            }
            tasksByCategory.get(task.category).add(task);
        }

        for (Map.Entry<String, ArrayList<Task>> entry : tasksByCategory.entrySet()) {
            System.out.println("Category: " + entry.getKey());
            System.out.println("_______________________________");
            for (Task task : entry.getValue()) {
                System.out.println(task);
                System.out.println();
            }
            System.out.println("_______________________________");
        }
    }

    public void displayDeletedTasks()
    {
        if(deleTasks.isEmpty())
        {
            System.out.println("No Task Found!");
        }
        else
        {
            for(Task task : deleTasks)
            {
                System.out.println("Task: " + (deleTasks.indexOf(task) + 1));
                System.out.println("_______________________________");
                System.out.println(task);
                System.out.println("_______________________________");
            }
        }
    }

    public void saveInFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFileName));
            for (Task task : tasks) {
                writer.write(task.title + "," + task.description + "," + task.category + "," + task.deadline + ","
                        + task.reminder);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void saveDeletedTasksInFile() {
        try {
            BufferedWriter dWriter = new BufferedWriter(new FileWriter(deletedDataFile));
            for (Task task : deleTasks) {
                dWriter.write(task.title + "," + task.description + "," + task.category + "," + task.deadline + ","
                        + task.reminder);
                dWriter.newLine();
            }
            dWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void organizeTasksByCategory() {
        HashMap<String, ArrayList<Task>> tasksByCategory = new HashMap<>();
        for (Task task : tasks) {
            if (!tasksByCategory.containsKey(task.category)) {
                tasksByCategory.put(task.category, new ArrayList<>());
            }
            tasksByCategory.get(task.category).add(task);
        }

        tasks.clear();

        for (Map.Entry<String, ArrayList<Task>> entry : tasksByCategory.entrySet()) {
            tasks.addAll(entry.getValue());
        }

        saveInFile();
    }

    public void loadFromFile() {
        File file = new File(dataFileName);
        File dFile = new File(deletedDataFile);
        if (file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String data = sc.nextLine();
                    String[] parts = data.split(",");
                    if (parts.length >= 5) {
                        Task task = new Task(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                        addTask(task);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        if (dFile.exists()) {
            try (Scanner sc = new Scanner(dFile)) {
                while (sc.hasNextLine()) {
                    String data = sc.nextLine();
                    String[] parts = data.split(",");
                    if (parts.length >= 5) {
                        Task task = new Task(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                        deleTasks.add(task);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void sort(Scanner sc) {
        System.out.println("Sort by-");
        System.out.println("1. Title");
        System.out.println("2. Category");
        System.out.println("3. Deadline");
        System.out.print("Enter your Choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                Collections.sort(tasks, Comparator.comparing(task -> task.title.toLowerCase()));
                saveInFile();
                break;
            case 2:
                Collections.sort(tasks, Comparator.comparing(task -> task.category.toLowerCase()));
                saveInFile();
                break;
            case 3:
                Collections.sort(tasks, Comparator.comparing(task -> task.deadline));
                saveInFile();
                break;
            default:
                System.out.println("Invalid Choice!");
                break;
        }
        System.out.println("Task Sorted Successfully!");
    }

    public void filter(Scanner sc) {
        System.out.println("Filter by-");
        System.out.println("1. Title");
        System.out.println("2. Category");
        System.out.println("3. Deadline");
        System.out.print("Enter your Choice: ");

        int count = 0;

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Title: ");
                String title = sc.nextLine();
                System.out.println("________________________");
                for (Task task : tasks) {
                    if (task.title.equalsIgnoreCase(title)) {
                        System.out.println(task + "\n");
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("Invalid Title!");
                }
                System.out.println("________________________");
                break;
            case 2:
                System.out.print("Enter Category: ");
                String category = sc.nextLine();
                System.out.println("________________________");
                for (Task task : tasks) {
                    if (task.category.equalsIgnoreCase(category)) {
                        System.out.println(task + "\n");
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("Invalid Category!");
                }
                System.out.println("________________________");
                break;
            case 3:
                System.out.print("Enter Deadline: ");
                String deadline = sc.nextLine();
                System.out.println("________________________");
                for (Task task : tasks) {
                    if (task.deadline.equalsIgnoreCase(deadline)) {
                        System.out.println(task + "\n");
                        count++;
                    }
                }
                if (count == 0) {
                    System.out.println("Invalid Deadline!");
                }
                System.out.println("________________________");
                break;
            default:
                System.out.println("Invalid Choice!");
                break;
        }
    }
}

public class TaskManagement {

    public static void main(String[] args) {
        TaskManager tasks = new TaskManager();
        Scanner sc = new Scanner(System.in);
        Boolean exit = false;

        while (!exit) {
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task");
            System.out.println("3. Update Task");
            System.out.println("4. Organize Tasks into Categories");
            System.out.println("5. Sort");
            System.out.println("6. Filter");
            System.out.println("7. Display Tasks");
            System.out.println("8. Display Category Wise");
            System.out.println("9. Display Deleted Tasks");
            System.out.println("10. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addTask(tasks, sc);
                    break;
                case 2:
                    System.out.print("Enter Task No.: ");
                    int n = sc.nextInt();
                    sc.nextLine();
                    tasks.deleteTask(n - 1);
                    break;
                case 3:
                    System.out.print("Enter Task No.: ");
                    int taskNumber = sc.nextInt();
                    sc.nextLine();
                    tasks.updateTask(taskNumber - 1, sc);
                    break;
                case 4:
                    tasks.organizeTasksByCategory();
                    break;
                case 5:
                    tasks.sort(sc);
                    break;
                case 6:
                    tasks.filter(sc);
                    break;
                case 7:
                    tasks.displayTasks();
                    break;
                case 8:
                    tasks.displayTasksByCategory();
                    break;
                case 9:
                    tasks.displayDeletedTasks();
                    break;
                case 10:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid Choice!");
                    break;
            }
        }
    }

    public static void addTask(TaskManager tasks, Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter task category: ");
        String category = scanner.nextLine();
        System.out.print("Enter task deadline(yyyy-mm-dd): ");
        String deadline = scanner.nextLine();
        System.out.print("Set reminder? (true/false): ");
        boolean reminder = scanner.nextBoolean();
        Task newTask = new Task(title, description, category, deadline, reminder);
        tasks.addTask(newTask);
        System.out.println("Task added successfully!");
    }
}
