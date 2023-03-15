package utils;
import java.util.LinkedList;
import java.util.Queue;

public class Log {
    private Queue<Request> buffer;
    private int commitIndex;

    public Log() {
        this.buffer = new LinkedList<>();
        this.commitIndex = -1;
    }

    public synchronized void append(Request operation) {
        this.buffer.add(operation);
    }

    public synchronized boolean canCommit(int index) {
        return index == this.commitIndex + 1;
    }

    public synchronized void commit(int index) {
        if (canCommit(index)) {
            this.commitIndex = index;
        }
    }

    public synchronized Request get(int index) {
        if (index > this.commitIndex || index < this.commitIndex - this.buffer.size() + 1) {
            return null;
        }
        return ((LinkedList<Request>) this.buffer).get(index - (this.commitIndex - this.buffer.size() + 1));
    }
}
