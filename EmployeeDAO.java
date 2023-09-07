package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.enums.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import java.text.*;
import java.io.*;
import java.math.*;
import java.util.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private static final String FILE_NAME="employee.data";

//*********************************** add ********************************************

public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
if(name.length()==0) throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
if(!new DesignationDAO().codeExists(designationCode)) throw new DAOException("Invalid Designation code : "+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Invalid Date of Birth");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Invalid salary : "+basicSalary);
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is null");
if(panNumber.length()==0) throw new DAOException("Length of PAN number is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null");
if(aadharCardNumber.length()==0) throw new DAOException("Length of Aadhar card number is zero");

try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
int lastGeneratedEmployeeId;
int recordCount;
String lastGeneratedEmployeeIdString;
String recordCountString;
if(randomAccessFile.length()==0)
{
lastGeneratedEmployeeId=10000000;
recordCount=0;
lastGeneratedEmployeeIdString=String.format("%-10s","10000000");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else
{
lastGeneratedEmployeeId=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
}
String fPanNumber,fAadharCardNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=0;i<7;i++) randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
throw new DAOException("Employee with similar PAN number exists");
}
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
throw new DAOException("Employee wiith similar aadhar card number exists");
}
}
String employeeId;
lastGeneratedEmployeeId++;
recordCount++;
lastGeneratedEmployeeIdString=String.valueOf(lastGeneratedEmployeeId);
recordCountString=String.valueOf(recordCount);
employeeId="A"+lastGeneratedEmployeeIdString;
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(String.valueOf(designationCode)+"\n");
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dateString=sdf.format(dateOfBirth);
randomAccessFile.writeBytes(dateString+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.seek(0);
lastGeneratedEmployeeIdString=String.format("%-10d",lastGeneratedEmployeeId);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
recordCountString=String.format("%-10d",recordCount);
randomAccessFile.writeBytes(recordCountString+"\n");
employeeDTO.setEmployeeId(employeeId);  
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}



//*********************************** update ********************************************

public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee is null");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Employee Id is null");
if(employeeId.length()==0) throw new DAOException("Employee Id is of zero length");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Name is null");
if(name.length()==0) throw new DAOException("Length of name is zero");
int designationCode=employeeDTO.getDesignationCode();
if(!new DesignationDAO().codeExists(designationCode)) throw new DAOException("Invalid Designation code : "+designationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();
if(dateOfBirth==null) throw new DAOException("Invalid Date of Birth");
char gender=employeeDTO.getGender();
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("Invalid salary : "+basicSalary);
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is null");
if(panNumber.length()==0) throw new DAOException("Length of PAN number is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is null");
if(aadharCardNumber.length()==0) throw new DAOException("Length of Aadhar card number is zero");
boolean employeeIdFound=false;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid Employee ID "+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close(); 
throw new DAOException("Invalid employee ID "+employeeId);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee ID "+employeeId);
}
String fEmployeeId,fPANNumber,fAadharCardNumber;
String panNumberFoundAgainstEmployeeId="";
String aadharCardNumberFoundAgainstEmployeeId="";
long foundAt=0;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(i=1;i<=6;i++) randomAccessFile.readLine();
fPANNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(employeeIdFound==false && employeeId.equalsIgnoreCase(fEmployeeId))
{
employeeIdFound=true;
}
if(panNumberFound==false && fPANNumber.equalsIgnoreCase(panNumber))
{
panNumberFound=true;
panNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberFound=true;
aadharCardNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(employeeIdFound && panNumberFound && aadharCardNumberFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee Id "+employeeId);
}
boolean panNumberExists=false;
boolean aadharCardNumberExists=false;
if(panNumberFound && panNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
panNumberExists=true;
}
if(aadharCardNumberFound && aadharCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
aadharCardNumberExists=true;
}
if(panNumberExists && aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Entered PAN number and aadhar card number already exists against a record");
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("Entered PAN number already exists against a record");
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Entered Aadhar card number already exists against a record");
}
SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
randomAccessFile.seek(foundAt);
for(i=1;i<=9;i++)randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(designationCode+"\n");
randomAccessFile.writeBytes(simpleDateFormat.format(dateOfBirth)+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
randomAccessFile.close();
tmpRandomAccessFile.setLength(0);

}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}


//*********************************** delete ********************************************


public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Employee Id is null");
if(employeeId.length()==0) throw new DAOException("Employee Id is of zero length");
boolean employeeIdFound=false;
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid Employee ID "+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close(); 
throw new DAOException("Invalid employee ID "+employeeId);
}
String lastGeneratedEmployeeIdString=randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee ID "+employeeId);
}
String fEmployeeId;
long foundAt=0;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(i=1;i<=8;i++) randomAccessFile.readLine();
if(employeeId.equalsIgnoreCase(fEmployeeId))
{
employeeIdFound=true;
break;
}
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee Id "+employeeId);
}
recordCount--;
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedEmployeeIdString+"\n");
String recordCountString=String.format("%-10d",recordCount);
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
tmpRandomAccessFile.setLength(0);

}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}


}

//*********************************** getAll ********************************************


public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<EmployeeDTOInterface>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return employees;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return employees;
}
EmployeeDTOInterface employeeDTO;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
char gender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
}
gender=randomAccessFile.readLine().charAt(0);
if(gender=='M')employeeDTO.setGender(GENDER.MALE);
else if(gender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//****************************** getByDesignationCode **********************************


public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
if(!new DesignationDAO().codeExists(designationCode)) throw new DAOException("Invalid Designation code");
Set<EmployeeDTOInterface> employees;
employees=new TreeSet<EmployeeDTOInterface>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return employees;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return employees;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return employees;
}
EmployeeDTOInterface employeeDTO;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId,fName;
int fDesignationCode;
char gender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode!=designationCode)
{
for(int i=1;i<=6;i++)randomAccessFile.readLine();
continue;
}
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
}
gender=randomAccessFile.readLine().charAt(0);
if(gender=='M')employeeDTO.setGender(GENDER.MALE);
else if(gender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employees.add(employeeDTO);
}
return employees;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//****************************** IsDesignationAlloted **********************************

public boolean isDesignationAlloted(int designationCode) throws DAOException
{
if(!new DesignationDAO().codeExists(designationCode)) throw new DAOException("Invalid Designation code");
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return false;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
EmployeeDTOInterface employeeDTO;
String fEmployeeId,fName;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
randomAccessFile.close();
return true;
}
for(int i=1;i<=6;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//*********************************** getByEmployeeId ************************************

public EmployeeDTOInterface getByEmployeeId(String employeeId) throws DAOException
{
if(employeeId==null)throw new DAOException("Invalid employee id");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Length of employee Id is zero");
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid employee ID : "+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
throw new DAOException("Invalid employee ID : "+employeeId);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid employee ID : "+employeeId);
}
EmployeeDTOInterface employeeDTO;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId;
String fName;
int fDesignationCode;
char gender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
}
gender=randomAccessFile.readLine().charAt(0);
if(gender=='M')employeeDTO.setGender(GENDER.MALE);
else if(gender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid Employee ID : "+employeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

// **************************** getByPANNumber ***********************************

public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid PAN number : "+panNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid PAN number : "+panNumber);
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid PAN number : "+panNumber);
}
EmployeeDTOInterface employeeDTO;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId;
char fGender;
String fName;
String fPanNumber;
String fAadharCardNumber;
int fDesignationCode;
Date fDateOfBirth=null;
BigDecimal fBasicSalary;
boolean fIsIndian;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
}
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
else if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid PAN number : "+panNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//**************************** getByAadharCardNumber ******************************

public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid AadharCard number : "+aadharCardNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) throw new DAOException("Invalid AadharCard number : "+aadharCardNumber);
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid AadharCard number : "+aadharCardNumber);
}
EmployeeDTOInterface employeeDTO;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String fEmployeeId;
char fGender;
String fName;
String fPanNumber;
String fAadharCardNumber;
int fDesignationCode;
boolean fIsIndian;
BigDecimal fBasicSalary;
Date fDateOfBirth=null;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
}
fGender=randomAccessFile.readLine().charAt(0);
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
if(fGender=='M')employeeDTO.setGender(GENDER.MALE);
if(fGender=='F')employeeDTO.setGender(GENDER.FEMALE);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid AadharCard number : "+aadharCardNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}

//****************************** employeeIdExists ************************************

public boolean employeeIdExists(String employeeId) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
String fEmployeeId;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
randomAccessFile.close();
return true;
}
for(int i=1;i<=8;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}

//****************************** panNumberExists **************************************

public boolean panNumberExists(String panNumber) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
String fPanNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=1;i<=7;i++) randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
return true;
}
randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//***************************** aadharCardNumberExists ***********************************
public boolean aadharCardNumberExists(String aadharCardNumber) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) 
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return false;
}
String fAadharCardNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(int i=1;i<=8;i++) randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}

//********************************* getCount ****************************************

public int getCount() throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return 0;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return 0;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}

//********************************* getCountByDesignation **********************************

public int getCountByDesignation(int designationCode) throws DAOException
{
if(!new DesignationDAO().codeExists(designationCode)) throw new DAOException("Invalid Designation code");
int count=0;
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return count;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0) return count;
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return count;
}
EmployeeDTOInterface employeeDTO;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode) count++;
for(int i=1;i<=6;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return count;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
}