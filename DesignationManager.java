package com.thinking.machines.hr.bl.managers;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationsMap;
private Map<String,DesignationInterface> titleWiseDesignationsMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManagerInterface designationManager=null;

private DesignationManager() throws BLException
{
populateDataStructures();
}

private void populateDataStructures() throws BLException
{
this.codeWiseDesignationsMap=new HashMap<>();
this.titleWiseDesignationsMap=new HashMap<>();
this.designationsSet=new TreeSet<>();
try
{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations=new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface designationDTO:dlDesignations)
{
designation=new Designation();
designation.setCode(designationDTO.getCode());
designation.setTitle(designationDTO.getTitle());
this.codeWiseDesignationsMap.put(new Integer(designation.getCode()),designation);
this.titleWiseDesignationsMap.put(designation.getTitle().toUpperCase(),designation);
this.designationsSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

public static DesignationManagerInterface getDesignationManager() throws BLException
{
if(designationManager==null)
{
DesignationManager.designationManager=new DesignationManager();
}
return designationManager;
}

//************************************** add ***************************************

public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code!=0)
{
blException.addException("code","Code should be zero");
throw blException;
}
if(title==null)
{
blException.addException("title","Title required");
throw blException;
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
throw blException;
}
}
try
{
DesignationInterface designationbl=new Designation();
designation.setTitle(title);
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
new DesignationDAO().add(designationDTO);
System.out.println("DesignationDTO code is : "+designationDTO.getCode());
designation.setCode(designationDTO.getCode());
code=designation.getCode();
codeWiseDesignationsMap.put(code,designation);
titleWiseDesignationsMap.put(title.toUpperCase(),designation);
designationsSet.add(designation);
System.out.println("Designation code is : "+designation.getCode());
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

//************************************** update ***************************************

public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code<0)
{
blException.addException("code","Invalid code "+code);
throw blException;
}

if(codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","Code doesn't exists");
throw blException;
}

if(title==null)
{
blException.addException("title","Title required");
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}


try
{
DesignationInterface blDesignation=codeWiseDesignationsMap.get(code);
if(titleWiseDesignationsMap.containsKey(title.toUpperCase()) && code!=blDesignation.getCode())
{
blException.addException("title","Title "+title+" already exists.");
}
DesignationDTOInterface dlDesignation=new DesignationDTO();
dlDesignation.setCode(code);
dlDesignation.setTitle(title);
new DesignationDAO().update(dlDesignation);
//removing the record that is to be updated from DS
codeWiseDesignationsMap.remove(code);
titleWiseDesignationsMap.remove(title.toUpperCase());
designationsSet.remove(blDesignation);
//updating or inserting new data to DS
codeWiseDesignationsMap.put(blDesignation.getCode(),blDesignation);
titleWiseDesignationsMap.put(blDesignation.getTitle().toUpperCase(),blDesignation);
designationsSet.add(blDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

//************************************** remove ***************************************

public void removeDesignation(int code) throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","Invalid code "+code);
throw blException;
}

if(codeWiseDesignationsMap.containsKey(code)==false)
{
blException.addException("code","Code doesn't exists");
}
DesignationInterface blDesignation=codeWiseDesignationsMap.get(code);
try
{
new DesignationDAO().delete(code);
//removing the record that is to be updated from DS
codeWiseDesignationsMap.remove(code);
titleWiseDesignationsMap.remove(blDesignation.getTitle().toUpperCase());
designationsSet.remove(blDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}

//********************************** getDesignationByCode *********************************

public DesignationInterface getDesignationByCode(int code) throws BLException
{
BLException blException=new BLException();
DesignationInterface designation = codeWiseDesignationsMap.get(code);
if(designation==null)
{
blException.addException("code","Invalid code");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}

//********************************** getDSDesignationByCode *********************************

/*This method is built for the internal use only
It differs from the getDesignationByCode() in such a way that, that method returns the clone of the designation that has been put in the data structures of DesignationManager. 
This method will return the original pointer extracted from the data structures, as we will not put clones of designations in the data structures of EmployeeManager.
Also, this method is not declared public.. therefore it only can be used inside this package.. not outside this package.
*/

DesignationInterface getDSDesignationByCode(int code)
{
DesignationInterface designation=codeWiseDesignationsMap.get(code);
return designation;
}



//********************************** getDesignationByTitle *********************************

public DesignationInterface getDesignationByTitle(String title) throws BLException
{
BLException blException=new BLException();
DesignationInterface designation = titleWiseDesignationsMap.get(title.toUpperCase());
if(designation==null)
{
blException.addException("code","Invalid title");
throw blException;
}
DesignationInterface d=new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}

//********************************** DesignationCodeExists *********************************

public boolean DesignationCodeExists(int code)
{
return codeWiseDesignationsMap.containsKey(code);
}

//********************************** DesignationTitleExists *********************************

public boolean DesignationTitleExists(String title)
{
return titleWiseDesignationsMap.containsKey(title.toUpperCase());
}

//********************************** getDesignationCount *********************************

public int getDesignationCount()
{
return designationsSet.size();
}

//********************************** getDesignations *********************************

public Set<DesignationInterface> getDesignations()
{
Set<DesignationInterface> blSet;
blSet=new TreeSet();
try
{
//Set<DesignationDTOInterface> dlSet;
//dlSet=new DesignationDAO().getAll();
for(DesignationInterface des:designationsSet)
{
DesignationInterface designation=new Designation();
designation.setCode(des.getCode());
designation.setTitle(des.getTitle());
blSet.add(designation);
}
}catch(DAOException daoException) 
{
}
return blSet;
}
}