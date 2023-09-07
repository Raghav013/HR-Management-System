package com.thinking.machines.hr.bl.interfaces.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import java.util.*;
public interface EmployeeManagerInterface
{
public void addEmployee(EmployeeInterface employee) throws BLException;
public void updateEmployee(EmployeeInterface employee) throws BLException;
public void removeEmployee(String employeeId) throws BLException;
public Set<EmployeeInterface> getEmployees();
public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException;
public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException;
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException;
public boolean employeeIdExists(String employeeId);
public boolean employeePANNumberExists(String panNumber);
public boolean employeeAadharCardNumberExists(String aadharCardNumber);
public int getEmployeeCount();
public int getEmployeeCountByDesignationCode(int designationCode) throws BLException;
public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException;
public boolean employeeAlloted(int designationCode) throws BLException;
}