package com.employees.utils;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.employees.dao.EmployeeDaoImpl;

public class GenerateId {
	static String prefix = "TEK";

	public String getId() throws IOException, ParseException{
		EmployeeDaoImpl obj=new EmployeeDaoImpl();
		JSONArray employees=obj.readJson();
		int size=employees.size();
		int newId;
		if(size==0) {
			return prefix+1;
		}
		else {
			JSONObject last=(JSONObject)employees.get(size-1);
			String id=(String)last.get("id");
			if(id!=null && id.startsWith(prefix)) {
				newId=Integer.parseInt(id.substring(3));
				newId++;
			}
			else {
				newId=1;
			}
		}
		return prefix+newId;
	}
}
