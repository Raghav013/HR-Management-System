package com.thinking.machines.hr.pl.model;
import java.util.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.interfaces.managers.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.managers.*;
import com.thinking.machines.hr.bl.exceptions.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

public class DesignationModel extends AbstractTableModel
{
private int count=0;
private java.util.List<DesignationInterface> plList;
private Map<Integer,String> m;
private String[] columnTitle;
public DesignationModel()
{
columnTitle=new String[2];
columnTitle[0]="S. No.";
columnTitle[1]="Designations";
m=new HashMap();
populateDataStructures();

}

public int getIndexOf(DesignationInterface designation)
{
int i=-36;
DesignationInterface plDesignation;
for(i=0;i<plList.size();i++)
{
plDesignation=plList.get(i);
if(plDesignation==designation) return i;
}
return i;
}

DesignationInterface getByTitle(String title,boolean allowPartial)
{
for(DesignationInterface des: plList) if(des.getTitle().compareToIgnoreCase(title)==0) return des;
return null;
}

public Object getValueAt(int rowIndex,int columnIndex)
{
m.put(rowIndex+1,plList.get(count).getTitle());
count++;
return m;
}

public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}

public int getColumnCount()
{
return columnTitle.length;
}

public int getRowCount()
{
return plList.size();
}

public Class getColumnClass(int columnIndex)
{
Class c=null;
try
{
if(columnIndex==0) c.forName("java.lang.Integer");
if(columnIndex==1) c.forName("java.lang.String");
}catch(ClassNotFoundException cnfe)
{
}
return c;
}

public boolean isCellEditable()
{
return false;
}

private void populateDataStructures()
{
plList=new LinkedList<>();
try
{
Set<DesignationInterface> blSet;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
blSet=designationManager.getDesignations();

System.out.println(blSet.size());

for(DesignationInterface des: blSet) System.out.println("Code : "+des.getCode()+" , Title : "+des.getTitle());

for(DesignationInterface des: blSet)
{
plList.add(des);
}
Collections.sort(plList,(l1,l2)-> l1.getTitle().compareToIgnoreCase(l2.getTitle()));
System.out.println("***********************");
for(DesignationInterface des: plList) System.out.println("Code : "+des.getCode()+" , Title : "+des.getTitle());
}catch(BLException blException)
{
}
}
}