package Task;

import java.util.Objects;

public class Task implements Comparable {
    private final int tag;

    public Task(int tag) {
        if (tag < 1 || tag > 9) {
            throw new IllegalArgumentException("create fail : tag = " + tag);
        }
        this.tag = tag;
    }

    public Task next() {
        return new Task(this.tag + 1);
    }

    @Override
    public String toString() {
        return String.valueOf(tag);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Task)) {
            throw new IllegalArgumentException();
        }

        Task task = (Task) o;
        return Integer.compare(this.tag, task.tag);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return tag == task.tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}