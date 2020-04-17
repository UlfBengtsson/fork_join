package org.example.models;

public class Company {
    private long id;
    private String name;
    private long employees;

    public Company(long id, String name, long employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getEmployeesCount() {
        return employees;
    }
}
