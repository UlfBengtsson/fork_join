package org.example;

import org.example.models.Company;
import org.example.models.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.App.getEmployees;

public class FindEmployeesWithFirstNameTask extends RecursiveTask<List<Employee>> {

    private final String firstName;
    private final int threshold;
    private final Company[] myArray;
    private int start;
    private int end;

    public FindEmployeesWithFirstNameTask(Company[] myArray, String firstName, int start, int end, int threshold) {
        this.firstName = firstName;
        this.threshold = threshold;
        this.myArray = myArray;
        this.start = start;
        this.end = end;
    }

    protected List<Employee> compute() {
        if (end - start < threshold) {

            //Todo - Implement your none threaded sequel code

            return result;

        } else {
            int midway = (end - start) / 2 + start;

            FindEmployeesWithFirstNameTask leftTask = new FindEmployeesWithFirstNameTask(myArray, firstName, start, midway, threshold);

            leftTask.fork();

            FindEmployeesWithFirstNameTask rightTask = new FindEmployeesWithFirstNameTask(myArray, firstName, midway + 1, end, threshold);

            return Stream.concat(rightTask.compute().stream(), leftTask.join().stream())
                    .collect(Collectors.toList());
        }
    }
}
