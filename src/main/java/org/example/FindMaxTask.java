package org.example;

import java.util.concurrent.RecursiveTask;

public class FindMaxTask extends RecursiveTask<Integer> {

    private final int threshold;
    private final int[] myArray;
    private int start;
    private int end;

    public FindMaxTask(int[] myArray, int start, int end, int threshold) {
        this.threshold = threshold;
        this.myArray = myArray;
        this.start = start;
        this.end = end;
    }

    protected Integer compute() {
        if (end - start < threshold) {
            int max = Integer.MIN_VALUE;
            for (int value : myArray) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        } else {
            int midway = (end - start) / 2 + start;

            FindMaxTask leftMax = new FindMaxTask(myArray, start, midway, threshold);

            leftMax.fork();

            FindMaxTask rightMax = new FindMaxTask(myArray, midway + 1, end, threshold);

            return Math.max(rightMax.compute(), leftMax.join());
        }
    }
}
