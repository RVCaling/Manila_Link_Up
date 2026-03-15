package com.manilalinkup.app;

public class Job {
    private String title, company, location, salary, schedule;

    public Job(String title, String company, String location, String salary, String schedule) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.schedule = schedule;
    }

    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getSalary() { return salary; }
    public String getSchedule() { return schedule; }
}