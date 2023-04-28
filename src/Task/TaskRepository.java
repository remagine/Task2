package Task;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TaskRepository {
    private static final TaskRepository taskRepository = new TaskRepository();
    private int createFailCount;
    private final Map<Integer, Integer> executeFailMap = new TreeMap<>();
    private final TreeSet<Task> taskSet = new TreeSet<>();

    private TaskRepository() {
    }

    public static TaskRepository getInstance() {
        return taskRepository;
    }

    public int getCreateFailCount() {
        return createFailCount;
    }

    public Map<Integer, Integer> getExecuteFailMap() {
        return executeFailMap;
    }

    public TreeSet<Task> getTaskSet() {
        return taskSet;
    }

    public void addTask(Task minTask) {
        taskSet.add(minTask);
    }

    public void addCreateFailCount() {
        this.createFailCount++;
    }

    public void deleteTask(Task task) {
        taskSet.remove(task);
    }

    public void addExecutionFailCount(int tag) {
        int count = executeFailMap.getOrDefault(tag, 0);
        executeFailMap.put(tag, count + 1);
    }
}
