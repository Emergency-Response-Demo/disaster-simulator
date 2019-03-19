package com.redhat.cajun.navy.datagenerate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneratePhoneNumbers {

	private Random random = new Random();                                                       
	private List<String> nationalAreaCodesList = Collections.synchronizedList(new ArrayList<String>());
	private List<String> ncAreaCodesList = Collections.synchronizedList(new ArrayList<String>());
	private DecimalFormat fourDigits = new DecimalFormat("0000");
	
	public String getPhoneNumber()
	{
		    String areaCode = "";
		    
		    int percentage = random.nextInt(100);
		    
		    //Use the NC area codes for 90% of the time
		    if(percentage < 90) {
		    	if(ncAreaCodesList.size() == 0)
			    {
			    	initNCAreaCodes();
			    }
		    	areaCode = ncAreaCodesList.remove(random.nextInt(ncAreaCodesList.size()));
		    }else {
		    	if(nationalAreaCodesList.size() == 0)
			    {
			    	initNationalAreaCodes();
			    }
		    	areaCode = nationalAreaCodesList.remove(random.nextInt(nationalAreaCodesList.size()));
		    }
		    
		    StringBuilder stringBuilder = new StringBuilder();
			
		    stringBuilder.append("(");
			stringBuilder.append(areaCode);
			stringBuilder.append(") ");
			
			stringBuilder.append("555");
			stringBuilder.append("-");
			stringBuilder.append(fourDigits.format(random.nextInt(9999)));
			
			return stringBuilder.toString(); 
	}
	
	public static void main(String[] args) {
		GeneratePhoneNumbers generatePhoneNumbers = new GeneratePhoneNumbers();
	
		boolean running = true;
		int count = 0;
		while(running)
		{
			System.err.println(generatePhoneNumbers.getPhoneNumber());
			count++;
			
			if(count > 1000)
			{
				running = false;
			}
		}
	}
	
	private void initNationalAreaCodes()
	{
		nationalAreaCodesList.addAll(Arrays.asList(
				//Area codes for the Unified demo
				"720",
				"303",
				"910",
				"414",
				"859",
				"202"));
	}
				
	private void initNCAreaCodes()
	{
		ncAreaCodesList.addAll(Arrays.asList(
				//Area codes for the Unified demo
				"704",
				"828",
				"651",
				"336",
				"252",
				"984"
				));
	}

}
