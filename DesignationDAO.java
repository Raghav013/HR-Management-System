package com.thinking.machines.hr.dl.dao;
import java.util.*;
import java.io.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;


public class DesignationDAO implements DesignationDAOInterface
{
private static final String FILE_NAME="designation.data";

//******************************************* add ********************************************************

public void add(DesignationDTOInterface designationDTO) throws DAOException
{

//********************************AMIT'S CODE **********************************
if(designationDTO == null) throw new DAOException("Designation is NULL");

String title = designationDTO.getTitle();

if(title == null) throw new DAOException("Designation is null");

title = title.trim();

if(title.length() == 0) throw new  DAOException(" Length of Designation is zero ");

try
{
File file = new File(FILE_NAME);
RandomAccessFile randomAccessFile;
randomAccessFile = new RandomAccessFile( file , "rw");

int LastGeneratedCode = 0;
int RecordCount = 0;

String LastGeneratedCodeString = "";
String RecordCountString = "";

if(randomAccessFile.length() == 0)
{
LastGeneratedCodeString = "0";
while(LastGeneratedCodeString.length() < 10) LastGeneratedCodeString += " ";

RecordCountString = "0";
while(RecordCountString.length() < 10) RecordCountString += " ";

randomAccessFile.writeBytes(LastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(RecordCountString);
randomAccessFile.writeBytes("\n");
}
else
{
LastGeneratedCodeString = randomAccessFile.readLine().trim();
RecordCountString = randomAccessFile.readLine().trim();


LastGeneratedCode = Integer.parseInt(LastGeneratedCodeString);
RecordCount = Integer.parseInt(RecordCountString);
}

int fcode;
String fTitle;

while(randomAccessFile.getFilePointer() < randomAccessFile.length())
{
fcode = Integer.parseInt(randomAccessFile.readLine());
fTitle = randomAccessFile.readLine();

if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException(" Designation  : " + title  + "Exists");
}
}

int code = LastGeneratedCode +1;
randomAccessFile.writeBytes( String.valueOf(code));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title);
randomAccessFile.writeBytes("\n");

designationDTO.setCode(code);
LastGeneratedCode++;
RecordCount++;

LastGeneratedCodeString = String.valueOf(LastGeneratedCode);
while(LastGeneratedCodeString.length() < 10) LastGeneratedCodeString += " ";

RecordCountString = String.valueOf(RecordCount);
while(RecordCountString.length() < 10) RecordCountString += " ";

randomAccessFile.seek(0);
randomAccessFile.writeBytes( LastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes( RecordCountString);
randomAccessFile.writeBytes("\n");
randomAccessFile.close();


}catch(IOException ioException)
{
throw new DAOException( ioException.getMessage());
} 

/*
***************************MY CODE ***************************

if(designationDTO==null) throw new DAOException("Designation is null");
String title=designationDTO.getTitle();
title=title.trim();
if(title.length()==0) throw new DAOException("Invalid title "+title);
try
{
int lastGeneratedCode;
int recordCount;
String lastGeneratedCodeString;
String recordCountString;
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
lastGeneratedCode=0;
recordCount=0;
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
}
else
{
lastGeneratedCode=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
fTitle=randomAccessFile.readLine();
if(title.equalsIgnoreCase(fTitle))
{
randomAccessFile.close();
throw new DAOException("Title : "+title+" already exists");
}
}
}
int code=lastGeneratedCode+1;
System.out.println("Data layer code : "+code);

designationDTO.setCode(code);
randomAccessFile.writeBytes(String.valueOf(code));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title);
randomAccessFile.writeBytes("\n");
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=String.valueOf(lastGeneratedCode);
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
randomAccessFile.close();
System.out.println("Data layer code : "+code);
System.out.println("Code in the designationDTO is : "+code);

}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
*/
}

//******************************************* update ********************************************************

public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
int code=designationDTO.getCode();
if(code<=0) throw new DAOException("Invalid code "+code);
String title=designationDTO.getTitle();
title=title.trim();
if(title.length()==0) throw new DAOException("Invalid title "+title);
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid code "+code);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
int fCode;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
if(fCode==code)
{
found=true;
break;
}
randomAccessFile.readLine();
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(code!=fCode && fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
throw new DAOException("Title "+title+" already exists");
}
}
randomAccessFile.seek(0);
File tmpFile=new File("tmp.data");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
else
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(title);
tmpRandomAccessFile.writeBytes("\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
}

//******************************************* delete ********************************************************

public void delete(int code) throws DAOException
{
if(code<=0) throw new DAOException("Invalid code "+code);
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid code "+code);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
String fTitle;
int fCode;
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
if(fCode==code)
{
found=true;
break;
}
randomAccessFile.readLine();
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
randomAccessFile.seek(0);
File tmpFile=new File("tmp.data");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode!=code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fCode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
recordCount=Integer.parseInt(tmpRandomAccessFile.readLine().trim());
recordCount-=1;
String recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10)recordCountString+=" ";
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
}

//******************************************* getAll ********************************************************

public Set<DesignationDTOInterface> getAll() throws DAOException
{
Set<DesignationDTOInterface> designations;
designations=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(!file.exists()) return designations;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return designations;
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
return designations;
}
int fCode;
String fTitle;
DesignationDTOInterface designationDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
designations.add(designationDTO);
}
randomAccessFile.close();
return designations;
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return designations;
}

//******************************************* getByCode ********************************************************

public DesignationDTOInterface getByCode(int code) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid code "+code);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fCode==code)
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
randomAccessFile.close();
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid code "+code);
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return null;
}

//******************************************* getByTitle ********************************************************

public DesignationDTOInterface getByTitle(String title) throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(!file.exists()) throw new DAOException("Invalid title "+title);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid title "+title);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
if(recordCount==0)
{
randomAccessFile.close();
throw new DAOException("Invalid title "+title);
}
int fCode;
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(fCode);
designationDTO.setTitle(fTitle);
randomAccessFile.close();
return designationDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid title "+title);
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return null;
}

//******************************************* codeExists ********************************************************

public boolean codeExists(int code) throws DAOException
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
int fCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fCode=Integer.parseInt(randomAccessFile.readLine());
if(fCode==code)
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
System.out.println(ioException.getMessage());
}
return false;
}

//******************************************* titleExists ********************************************************

public boolean titleExists(String title) throws DAOException
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
String fTitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
fTitle=randomAccessFile.readLine();
if(fTitle.equalsIgnoreCase(title))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
System.out.println(ioException.getMessage());
}
return false;
}

//******************************************* getCount ********************************************************

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
System.out.println(ioException.getMessage());
}
return 0;
}
}