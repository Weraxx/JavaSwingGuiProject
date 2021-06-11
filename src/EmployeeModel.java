import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel extends AbstractTableModel {

    private List<Employee> list;

    EmployeeModel() {
        list = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        list.add(employee);
        fireTableStructureChanged();
    }

    public void removeEmployee(int index) {
        list.remove(index);
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        String[] headers = {"Name", "Surname", "Seniority", "Salary", "Job position"};
        return headers[columnIndex];
    }

    @Override
    public int getRowCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 2) {
            return String.class;
        } else if (columnIndex == 4) {
            return Enum.class;
        } else {
            return Integer.class;
        }
    }

    @Override
    public void setValueAt(Object val, int rowIndex, int columnIndex) {
        Employee employee = list.get(rowIndex);
        if (columnIndex == 0) {
            employee.setName((String) val);
        } else if (columnIndex == 1) {
            employee.setSurname((String) val);
        } else if (columnIndex == 2) {
            employee.setSeniority((Integer) val);
        } else if (columnIndex == 3) {
            employee.setSalary((Integer) val);
        } else {
            if (val != null) {
                JobPosition position = switch (val.toString()) {
                    case "IT_Manager" -> JobPosition.IT_Manager;
                    case "Accountant" -> JobPosition.Accountant;
                    case "CEO" -> JobPosition.CEO;
                    case "SQL_Developer" -> JobPosition.SQL_Developer;
                    case "Web_Designer" -> JobPosition.Web_Designer;
                    case "Web_Developer" -> JobPosition.Web_Developer;
                    case "Help_Desk_Worker" -> JobPosition.Help_Desk_Worker;
                    case "Software_Engineer" -> JobPosition.Software_Engineer;
                    case "Information_Security_Analyst" -> JobPosition.Information_Security_Analyst;
                    case "Network_Administrator" -> JobPosition.Network_Administrator;
                    case "Application_Developer" -> JobPosition.Application_Developer;
                    case "Computer_Programmer" -> JobPosition.Computer_Programmer;
                    default -> null;
                };
                employee.setJobPosition(position);
            }
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = list.get(rowIndex);
        if (columnIndex == 0) {
            return employee.getName();
        } else if (columnIndex == 1) {
            return employee.getSurname();
        } else if (columnIndex == 2) {
            return employee.getSeniority();
        } else if (columnIndex == 3) {
            return employee.getSalary();
        } else {
            return employee.getJobPosition();
        }
    }
}
