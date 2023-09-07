
package com.thinking.machines.hr.bl.interfaces.managers;
import java.util.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
public interface DesignationManagerInterface
{
public void addDesignation(DesignationInterface designation) throws BLException;
public void updateDesignation(DesignationInterface designation) throws BLException;
public void removeDesignation(int code) throws BLException;
public DesignationInterface getDesignationByCode(int code) throws BLException;
public DesignationInterface getDesignationByTitle(String title) throws BLException;
public boolean DesignationCodeExists(int code);
public boolean DesignationTitleExists(String title);
public int getDesignationCount();
public Set<DesignationInterface> getDesignations();
}
