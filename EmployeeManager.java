package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.math.*;
public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Set<EmployeeInterface> employeesSet;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;

private static EmployeeManagerInterface employeeManager=null;

private EmployeeManager() throws BLException
{
populateDataStructures();
}

private void populateDataStructures() throws BLException
{
this.employeeIdWiseEmployeesMap=new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
this.employeesSet=new TreeSet<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
Set<EmployeeInterface> ets;
try
{
Set<EmployeeDTOInterface> dlEmployees;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationInterface designation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
for(EmployeeDTOInterface employeeDTO:dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(employeeDTO.getEmployeeId());
employee.setName(employeeDTO.getName());
designation=designationManager.getDesignationByCode(employeeDTO.getDesignationCode());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)employeeDTO.getDateOfBirth().clone());
employee.setGender((employeeDTO.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(employeeDTO.getIsIndian());
employee.setBasicSalary(employeeDTO.getBasicSalary());
employee.setPANNumber(employeeDTO.getPANNumber());
employee.setAadharCardNumber(employeeDTO.getAadharCardNumber());

ets=designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(employee);
designationCodeWiseEmployeesMap.put(designation.getCode(),ets);
}
else
{
ets.add(employee);
}

this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
this.employeesSet.add(employee);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null)
{
EmployeeManager.employeeManager=new EmployeeManager();
}
return employeeManager;
}



//************************************* add ***********************************************

public void addEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null.");
throw blException;
}

String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
Boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();

employeeId=employeeId.trim();
if(employeeId.length()>0) blException.addException("employeeId","EmployeeId is alloted by us.");
if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Name required");
}                                                                         

if(designation==null) blException.addException("designation","Designation required");
int code=0;
if(designation!=null)code=designation.getCode();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.DesignationCodeExists(code)==false)
{
blException.addException("designation","Invalid Designation.");
}
if(dateOfBirth==null) blException.addException("dateOfBirth","Date of Birth required.");
if(gender==' ') blException.addException("gender","Gender required.");
if(isIndian==null) blException.addException("isIndian","Nationality required");
if(basicSalary==null) blException.addException("basicSalary","Salary required.");
if(basicSalary!=null && basicSalary.signum()==-1) blException.addException("basicSalary","Salary can't be negative.");
if(panNumber==null)
{
blException.addException("panNumber","PAN number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("panNumber","PAN number required.");
}

if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Aadhar Card number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) blException.addException("aadharCardNumber","Aadhar Card number required.");
}


if(panNumber!=null && panNumber.length()>0)
{
if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
blException.addException("PanNumber","This PAN number is already alloted to an Employee.");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
blException.addException("AadharCardNumber","This Aadhar Card Number is already alloted to an Employee.");
}
}

if(blException.hasExceptions()) throw blException;

try
{
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setName(name);
employeeDTO.setDesignationCode(code);
employeeDTO.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setDateOfBirth((Date)dateOfBirth.clone());
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
new EmployeeDAO().add(employeeDTO);
employee.setEmployeeId(employeeDTO.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employeeId);
dsEmployee.setName(name);
dsEmployee.setDesignation(designationManager.getDesignationByCode(designation.getCode()));;
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);

Set<EmployeeInterface> ets=designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(dsEmployee.getDesignation().getCode(),ets);
}
else
{
ets.add(dsEmployee);
}

employeeIdWiseEmployeesMap.put(employeeId.toUpperCase(),dsEmployee);
panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
employeesSet.add(dsEmployee);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}

}

//************************************* update **********************************************

public void updateEmployee(EmployeeInterface employee) throws BLException
{
BLException blException=new BLException();
if(employee==null)
{
blException.setGenericException("Employee is null.");
throw blException;
}

String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
Boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();

if(employeeId==null)
{
blException.addException("employeeId","Employee Id required against which the data has to be updated");
throw blException;
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","Employee Id required against which the data has to be updated");
throw blException;
}
if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
{
blException.addException("employeeId","This Employee ID doesnt exists.");
throw blException;
}

EmployeeInterface ee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String oldPan=ee.getPANNumber();
String oldAadhar=ee.getAadharCardNumber();

if(name==null)
{
blException.addException("name","Name required");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","Name required");
}                                                                         

if(designation==null) blException.addException("designation","Designation required");
int code=0;
if(designation!=null)code=designation.getCode();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.DesignationCodeExists(code)==false)
{
blException.addException("designation","Invalid Designation.");
}
if(dateOfBirth==null) blException.addException("dateOfBirth","Date of Birth required.");
if(gender==' ') blException.addException("gender","Gender required.");
if(isIndian==null) blException.addException("isIndian","Nationality required");
if(basicSalary==null) blException.addException("basicSalary","Salary required.");
if(basicSalary!=null && basicSalary.signum()==-1) blException.addException("basicSalary","Salary can't be negative.");
if(panNumber==null)
{
blException.addException("panNumber","PAN number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0) blException.addException("panNumber","PAN number required.");
}

if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Aadhar Card number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) blException.addException("aadharCardNumber","Aadhar Card number required.");
}

if(panNumber!=null && panNumber.length()>0)
{
if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
EmployeeInterface panEmployee=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
if(!(panEmployee.getEmployeeId().equalsIgnoreCase(employeeId))) blException.addException("panNumber","This PAN number is already alloted to an Employee.");
}
}


if(aadharCardNumber!=null && aadharCardNumber.length()>0)
{
if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
EmployeeInterface aadharEmployee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
if(!(aadharEmployee.getEmployeeId().equalsIgnoreCase(employeeId))) blException.addException("aadharCardNumber","This aadhar card number is already alloted to an Employee.");
}
}
if(blException.hasExceptions()) throw blException;
try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTOInterface employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(ee.getEmployeeId());
employeeDTO.setName(name);
employeeDTO.setDesignationCode(code);
employeeDTO.setDateOfBirth((Date)dateOfBirth.clone());
employeeDTO.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
employeeDTO.setIsIndian(isIndian);
employeeDTO.setBasicSalary(basicSalary);
employeeDTO.setPANNumber(panNumber);
employeeDTO.setAadharCardNumber(aadharCardNumber);
employeeDAO.update(employeeDTO);

//removing from data structures
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(ee.getDesignation().getCode());
if(ets.size()==1)
{
ets.remove(ee);
designationCodeWiseEmployeesMap.remove(ee.getDesignation().getCode());
}
else
{
ets.remove(ee);
}

employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(oldPan.toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(oldAadhar.toUpperCase());
employeesSet.remove(employee);

EmployeeInterface employeeDS=new Employee();
employeeDS.setEmployeeId(ee.getEmployeeId());
employeeDS.setName(name);
DesignationInterface designationDS=designationManager.getDesignationByCode(code);
employeeDS.setDesignation(designation);
employeeDS.setDateOfBirth((Date)dateOfBirth.clone());
employeeDS.setGender((gender=='M')?GENDER.MALE:GENDER.FEMALE);
employeeDS.setIsIndian(isIndian);
employeeDS.setBasicSalary(basicSalary);
employeeDS.setPANNumber(panNumber);
employeeDS.setAadharCardNumber(aadharCardNumber);

//adding again to data structures

ets=designationCodeWiseEmployeesMap.get(code);

if(ets==null)
{
ets=new TreeSet<>();
ets.add(employeeDS);
designationCodeWiseEmployeesMap.put(code,ets);
}
else
{
ets.add(employeeDS);
}


employeeIdWiseEmployeesMap.put(employeeId.toUpperCase(),employeeDS);
panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),employeeDS);
aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),employeeDS);
employeesSet.add(employeeDS);

}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}

}

//************************************* remove **********************************************

public void removeEmployee(String employeeId) throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("employeeId","Employee Id required against which the data has to be removed");
throw blException;
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","Employee Id required against which the data has to be removed");
throw blException;
}
if(!employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase()))
{
blException.addException("employeeId","This Employee ID doesnt exists.");
throw blException;
}

EmployeeInterface employee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
int code=employee.getDesignation().getCode();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
try
{
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(employeeId);

Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(code);
if(ets.size()==1)
{
ets.remove(employee);
designationCodeWiseEmployeesMap.remove(code);
}
else
{
ets.remove(employee);
}


employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
panNumberWiseEmployeesMap.remove(panNumber.toUpperCase());
aadharCardNumberWiseEmployeesMap.remove(aadharCardNumber.toUpperCase());
employeesSet.remove(employee);

}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}


}


//********************************* getEmployees *******************************************

public Set<EmployeeInterface> getEmployees()
{
Set<EmployeeInterface> blEmployees=new TreeSet<>();
try
{
EmployeeInterface employee;
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
Set<EmployeeDTOInterface> dlEmployees=employeeDAO.getAll();
try
{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
for(EmployeeDTOInterface employeeDTO:dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(employeeDTO.getEmployeeId());
employee.setName(employeeDTO.getName());
employee.setDesignation(designationManager.getDesignationByCode(employeeDTO.getDesignationCode()));
employee.setDateOfBirth((Date)employeeDTO.getDateOfBirth().clone());
employee.setGender((employeeDTO.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(employeeDTO.getIsIndian());
employee.setBasicSalary(employeeDTO.getBasicSalary());
employee.setPANNumber(employeeDTO.getPANNumber());
employee.setAadharCardNumber(employeeDTO.getAadharCardNumber());
blEmployees.add(employee);
}
}catch(BLException be)
{

}

}catch(DAOException daoException)
{
}
return blEmployees;
}

//********************************* getEmployeeByEmployeeId *********************************

public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
{
BLException blException=new BLException();
if(employeeId==null)
{
blException.addException("employeeId","Employee Id required to get the data.");
throw blException;
}
employeeId=employeeId.trim();
if(employeeId.length()==0)
{
blException.addException("employeeId","Employee Id required to get the data.");
throw blException;
}
EmployeeInterface employee=employeeIdWiseEmployeesMap.get(employeeId);
if(employee==null)
{
blException.addException("employeeId","This employee ID doesn't exists");
throw blException;
}
DesignationInterface designationDS,designation;
EmployeeInterface ee=new Employee();
ee.setEmployeeId(employee.getEmployeeId());
ee.setName(employee.getName());
designationDS=employee.getDesignation();
designation=new Designation();
designation.setCode(designationDS.getCode());
designation.setTitle(designationDS.getTitle());
ee.setDesignation(designation);
ee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
ee.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
ee.setIsIndian(employee.getIsIndian());
ee.setBasicSalary(employee.getBasicSalary());
ee.setPANNumber(employee.getPANNumber());
ee.setAadharCardNumber(employee.getAadharCardNumber());
return ee;
}

//********************************* getEmployeeByPANNumber *********************************

public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException
{
BLException blException=new BLException();
if(panNumber==null)
{
blException.addException("panNumber","Invalid PAN number.");
throw blException;
}
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber","Invalid PAN number.");
throw blException;
}
EmployeeInterface employee=panNumberWiseEmployeesMap.get(panNumber);
if(employee==null)
{
blException.addException("panNumber","This PAN number doesn't exists");
throw blException;
}
EmployeeInterface ee=new Employee();
ee.setEmployeeId(employee.getEmployeeId());
ee.setName(employee.getName());
DesignationInterface designationDS=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(designationDS.getCode());
designation.setTitle(designationDS.getTitle());
ee.setDesignation(designation);
ee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
ee.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
ee.setIsIndian(employee.getIsIndian());
ee.setBasicSalary(employee.getBasicSalary());
ee.setPANNumber(employee.getPANNumber());
ee.setAadharCardNumber(employee.getAadharCardNumber());
return ee;
}

//******************************* getEmployeeByAadharCardNumber *****************************

public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
{
BLException blException=new BLException();
if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber","Invalid Aadhar card number.");
throw blException;
}
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("aadharCardNumber","Invalid Aadhar card number.");
throw blException;
}
EmployeeInterface employee=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber);
if(employee==null)
{
blException.addException("aadharCardNumber","This Aadhar card number doesn't exists");
throw blException;
}
EmployeeInterface ee=new Employee();
ee.setEmployeeId(employee.getEmployeeId());
ee.setName(employee.getName());
DesignationInterface designationDS=employee.getDesignation();
DesignationInterface designation=new Designation();
designation.setCode(designationDS.getCode());
designation.setTitle(designationDS.getTitle());
ee.setDesignation(designation);
ee.setDateOfBirth((Date)employee.getDateOfBirth().clone());
ee.setGender((employee.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
ee.setIsIndian(employee.getIsIndian());
ee.setBasicSalary(employee.getBasicSalary());
ee.setPANNumber(employee.getPANNumber());
ee.setAadharCardNumber(employee.getAadharCardNumber());
return ee;
}

//********************************* employeeIdExists **************************************

public boolean employeeIdExists(String employeeId)
{
return employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase());
}

//********************************* employeePANNumberExists **************************************

public boolean employeePANNumberExists(String panNumber)
{
return panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase());
}

//*************************** employeeAadharCardNumberExists **********************************

public boolean employeeAadharCardNumberExists(String aadharCardNumber)
{
return aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase());
}

//********************************* getEmployeeCount ************************************

public int getEmployeeCount()
{
return employeesSet.size();
}

//************************* getEmployeeCountByEmployeeCode ***************************

public int getEmployeeCountByDesignationCode(int employeeCode) throws BLException
{
Set<EmployeeInterface> ets=designationCodeWiseEmployeesMap.get(employeeCode);
if(ets==null)return 0;
return ets.size();
}

//************************* getEmployeeByEmployeeCode ***************************

public Set<EmployeeInterface> getEmployeesByDesignationCode(int employeeCode) throws BLException
{
Set<EmployeeInterface> ets=designationCodeWiseEmployeesMap.get(employeeCode);
if(ets==null) return employees;
Set<EmployeeInterface> employeeTS;
employeeTS=new TreeSet<>();
DesignationInterface designation,designationDS;
EmployeeInterface employee;
for(EmployeeInterface employeeDS:ets)
{
employee=new Employee();
employee.setEmployeeId(employeeDS.getEmployeeId());
employee.setName(employeeDS.getName());
designationDS=employeeDS.getDesignation();
designation=new Designation();
designation.setCode(designationDS.getCode());
designation.setTitle(designationDS.getTitle());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)employeeDS.getDateOfBirth().clone());
employee.setGender((employeeDS.getGender()=='M')?GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(employeeDS.getIsIndian());
employee.setBasicSalary(employeeDS.getBasicSalary());
employee.setPANNumber(employeeDS.getPANNumber());
employee.setAadharCardNumber(employeeDS.getAadharCardNumber());
employeeTS.add(employee);
}
if(employeeTS==null)
{
BLException blException=new BLException();
blException.setGenericException("No Employee found with this Designation Code");
throw blException;
}
return employeeTS;
}

//********************************** employeeAlloted************************************

public boolean employeeAlloted(int employeeCode) throws BLException
{
return designationCodeWiseEmployeesMap.containsKey(employeeCode);
}
}
