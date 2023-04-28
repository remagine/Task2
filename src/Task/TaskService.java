package Task;

import java.util.*;

public class TaskService {
    private static final TaskService taskService = new TaskService();
    private static final TaskRepository taskRepository = TaskRepository.getInstance();

    private TaskService() {
    }

    public static TaskService getInstance() {
        return taskService;
    }

    public void create() {
        try {
            Task minTask = getMinTask();
            taskRepository.addTask(minTask);
        } catch (IllegalArgumentException e) {
            taskRepository.addCreateFailCount();
        }
    }

    public void execute(int tag) {
        try {
            Task task = new Task(tag);
            Set<Task> taskSet = taskRepository.getTaskSet();
            if (!taskSet.contains(task)) {
                throw new IllegalArgumentException("execute failed : empty task " + tag);
            }
            taskRepository.deleteTask(task);
        } catch (Exception e) {
            taskRepository.addExecutionFailCount(tag);
        }
    }

    private Task getMinTask() {
        TreeSet<Task> taskSet = taskRepository.getTaskSet();
        if (taskSet.size() == 0) {
            return new Task(1);
        }
        Task minTask = taskSet.first();
        if (!minTask.equals(new Task(1))) {
            return new Task(1);
        }

        Task nextTask = minTask.createNextMinTask();
        while (taskSet.contains(nextTask)) {
            nextTask = nextTask.createNextMinTask();
        }

        return nextTask;
    }

    public void printTaskResult() {
        Set<Task> validTags = getValidTasks();
        Map<Integer, Integer> executeFailMap = taskRepository.getExecuteFailMap();
        List<Map.Entry<Integer, Integer>> entryList = new LinkedList<>(executeFailMap.entrySet());
        Comparator<Map.Entry<Integer, Integer>> valueComparator = Comparator.comparingInt(Map.Entry::getValue);
        entryList.sort(valueComparator.reversed());

        System.out.println("사용가능한 TAG:" + validTags.toString());
        System.out.println("TASK 생성 실패:" + taskRepository.getCreateFailCount());
        System.out.println("TASK 수행 실패한 태그:" + entryList);
    }

    private Set<Task> getValidTasks() {
        Set<Task> taskSet = taskRepository.getTaskSet();
        Set<Task> validSet = new HashSet<>();
        for(int i = 1; i < 10 ; i++){
            Task task = new Task(i);
            if(!taskSet.contains(task)){
                validSet.add(task);
            }
        }

        return validSet;
    }
}
