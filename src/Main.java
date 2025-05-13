//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;

class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int count = 1;
    private final int id;
    private String title;
    private String description;
    private String priority;
    private String dueDate;

    public Task(String title, String description, String priority, String dueDate) {
        this.id = count++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public int getId() { return id; }

    public String getSummary() {
        return String.format("ID: %d | %s [%s] - %s", id, title, priority, dueDate);
    }

    public String toString() {
        return String.format("ID: %d\nTítulo: %s\nDescrição: %s\nPrioridade: %s\nData: %s\n",
                id, title, description, priority, dueDate);
    }
}

class TaskManager {
    private List<Task> tasks;
    private final String FILE_NAME = "tarefas.ser";

    public TaskManager() {
        tasks = loadTasks();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }

    public void removeTaskById(int id) {
        tasks.removeIf(t -> t.getId() == id);
        saveTasks();
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }
        for (Task t : tasks) {
            System.out.println(t.getSummary());
        }
    }

    public void showTaskDetails(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                System.out.println(t);
                return;
            }
        }
        System.out.println("Tarefa não encontrada.");
    }

    private void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Erro ao salvar tarefas.");
        }
    }

    private List<Task> loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Task>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager manager = new TaskManager();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> manager.listTasks();
                case "3" -> showTaskDetails();
                case "4" -> removeTask();
                case "0" -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==== Gerenciador de Tarefas ====");
        System.out.println("1. Adicionar Tarefa");
        System.out.println("2. Listar Tarefas");
        System.out.println("3. Ver Detalhes da Tarefa");
        System.out.println("4. Remover Tarefa");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
    }

    private static void addTask() {
        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Descrição: ");
        String desc = scanner.nextLine();

        System.out.print("Prioridade (Baixa, Média, Alta): ");
        String priority = scanner.nextLine();

        String dueDate;
        while (true) {
            System.out.print("Data de vencimento (dd/MM/yyyy): ");
            dueDate = scanner.nextLine();
            if (isValidDate(dueDate)) break;
            else System.out.println("Data inválida. Tente novamente.");
        }

        manager.addTask(new Task(title, desc, priority, dueDate));
        System.out.println("Tarefa adicionada com sucesso!");
    }

    private static void showTaskDetails() {
        System.out.print("ID da tarefa: ");
        int id = Integer.parseInt(scanner.nextLine());
        manager.showTaskDetails(id);
    }

    private static void removeTask() {
        System.out.print("ID da tarefa para remover: ");
        int id = Integer.parseInt(scanner.nextLine());
        manager.removeTaskById(id);
        System.out.println("Tarefa removida.");
    }

    private static boolean isValidDate(String date) {
        Pattern pattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        if (!pattern.matcher(date).matches()) return false;

        try {
            new SimpleDateFormat("dd/MM/yyyy").parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
