package org.example;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App 
{
    public static void main( String[] args )
    {
        int[] data = new int[300_000_000]; //1G

        for (int i = 0; i < data.length; i++) {
            data[i] = ThreadLocalRandom.current().nextInt();
        }

        doSeqMaxNumber(data);

        //doForkJoinMaxNumber(data);

        doParallelStreamMaxNumber(data);
    }

    static void doSeqMaxNumber(int[] data) {
        long startTime = System.nanoTime();

        int max = Integer.MIN_VALUE;
        for (int value : data) {
            if (value > max) {
                max = value;
            }
        }

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        System.out.println("Max value found:" + max);
        System.out.println("Time nano: " + duration);
    }

    static void doForkJoinMaxNumber(int[] data) {
        long startTimeFJ = System.nanoTime();

        ForkJoinPool pool = new ForkJoinPool();

        FindMaxTask task = new FindMaxTask(data, 0, data.length - 1, data.length / 6);

        int maxFJ = pool.invoke(task);

        long endTimeFJ = System.nanoTime();

        long durationFJ = (endTimeFJ - startTimeFJ);  //divide by 1000000 to get milliseconds.

        System.out.println("Max value found:" + maxFJ);
        System.out.println("Time nano: " + durationFJ);
    }

    static void doParallelStreamMaxNumber(int[] data) {

        long startTime = System.nanoTime();

        IntStream streamData = Arrays.stream(data);

        OptionalInt result = streamData.parallel().max();

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.

        if (result.isPresent()) {
            System.out.println("Max: " +  result.getAsInt());
            System.out.println("Time nano: " + duration);
        }
    }
}
