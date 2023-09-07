package com.thinking.machines.hr.bl.interfaces.pojo;
import java.util.*;
public interface DesignationInterface extends java.io.Serializable,Comparable<DesignationInterface>
{
public void setCode(int code);
public int getCode();
public void setTitle(String title);
public String getTitle();
}