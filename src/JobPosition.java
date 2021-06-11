public enum JobPosition {
    IT_Manager("IT_Manager", 3000, 4500),
    Accountant("Accountant", 3000, 4500),
    CEO("CEO", 3000, 20000),
    SQL_Developer("SQL_Developer", 3000, 5000),
    Web_Designer("Web_Designer", 3000, 5000),
    Web_Developer("Web_Developer", 3000, 5000),
    Help_Desk_Worker("Help_Desk_Worker", 3000, 3500),
    Software_Engineer("Software_Engineer", 3000, 5000),
    Information_Security_Analyst("Information_Security_Analyst", 3000, 5000),
    Network_Administrator("Network_Administrator", 3000, 3500),
    Application_Developer("Application_Developer", 3000, 5000),
    Computer_Programmer("Computer_Programmer", 3000, 15000);

    private final String nameOfJobPosition;
    private final int minSalary;
    private final int maxSalary;

    JobPosition(String nameOfJobPosition, int minSalary, int maxSalary) {
        this.nameOfJobPosition = nameOfJobPosition;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }

    public String getNameOfJobPosition() {
        return nameOfJobPosition;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }
}