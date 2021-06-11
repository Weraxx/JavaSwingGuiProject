public class Employee {
    protected String name;
    protected String surname;
    protected int seniority;
    protected int salary;
    protected JobPosition jobPosition;

    public Employee(String name, String lastName, int seniority, int salary, JobPosition jobPosition) {
        this.name = name;
        this.surname = lastName;
        this.seniority = seniority;
        this.salary = salary;
        this.jobPosition = jobPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(JobPosition jobPosition) {
        this.jobPosition = jobPosition;
    }
}