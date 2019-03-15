package com.redhat.cajun.navy.datagenerate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateFullNames {

	private Random random = new Random();
	private List<String> firstNamesList = Collections.synchronizedList(new ArrayList<String>());
	private List<String>  lastNamesList = Collections.synchronizedList(new ArrayList<String>());
	
	public String getFullName()
	{
		
	    if(firstNamesList.size() == 0)
	    {
	    	initFirstNames();
	    }
	    
	    if(lastNamesList.size() == 0)
	    {
	    	initLastNames();
	    }
	    
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(firstNamesList.remove(random.nextInt(firstNamesList.size())));
		stringBuilder.append(" ");
		stringBuilder.append(lastNamesList.remove(random.nextInt(lastNamesList.size())));
		
		return stringBuilder.toString(); 
	}
	
	public static void main(String[] args) {
		GenerateFullNames generateFullNames = new GenerateFullNames();
	
		boolean running = true;
		int count = 0;
		while(running)
		{
			System.err.println(generateFullNames.getFullName());
			count++;
			
			if(count > 1000)
			{
				running = false;
			}
		}
		
	}
	
	
	private void initLastNames()
	{
		lastNamesList.addAll(Arrays.asList(
				//Last names of the unified demo creators
				"Tyrrell",
				"Mosher",
				"Tison",
				"Jarae",
				"Echevarria",
				"Shaaf",
				
				//100 Most popular last names 
				"Smith",
				"Johnson",
				"Williams",
				"Jones",
				"Brown",
				"Davis",
				"Miller",
				"Wilson",
				"Moore",
				"Taylor",
				"Anderson",
				"Thomas",
				"Jackson",
				"White",
				"Harris",
				"Martin",
				"Thompson",
				"Garcia",
				"Martinez",
				"Robinson",
				"Clark",
				"Rodriguez",
				"Lewis",
				"Lee",
				"Walker",
				"Hall",
				"Allen",
				"Young",
				"Hernandez",
				"King",
				"Wright",
				"Lopez",
				"Hill",
				"Scott",
				"Green",
				"Adams",
				"Baker",
				"Gonzalez",
				"Nelson",
				"Carter",
				"Mitchell",
				"Perez",
				"Roberts",
				"Turner",
				"Phillips",
				"Campbell",
				"Parker",
				"Evans",
				"Edwards",
				"Collins",
				"Stewart",
				"Sanchez",
				"Morris",
				"Rogers",
				"Reed",
				"Cook",
				"Morgan",
				"Bell",
				"Murphy",
				"Bailey",
				"Rivera",
				"Cooper",
				"Richardson",
				"Cox",
				"Howard",
				"Ward",
				"Torres",
				"Peterson",
				"Gray",
				"Ramirez",
				"James",
				"Watson",
				"Brooks",
				"Kelly",
				"Sanders",
				"Price",
				"Bennett",
				"Wood",
				"Barnes",
				"Ross",
				"Henderson",
				"Coleman",
				"Jenkins",
				"Perry",
				"Powell",
				"Long",
				"Patterson",
				"Hughes",
				"Flores",
				"Washington",
				"Butler",
				"Simmons",
				"Foster",
				"Gonzales",
				"Bryant",
				"Alexander",
				"Russell",
				"Griffin",
				"Diaz",
				"Hayes"
				));
	}
	/*
	 *Top Baby names for 2017 plus some of the team members  
	 */
	private void initFirstNames()
	{
		firstNamesList.addAll(Arrays.asList(
				//Unified demo first names
				"Jim",
				"Justin",
				"Chuck",
				"Bernard",
				"Mike",
				"Syed",
				
				//200 of the most popular male and female names from 2017
				"Liam",	
				"Noah",	
				"William",	
				"James",	
				"Logan",	
				"Emma",
				"Olivia",
				"Ava",
				"Isabella",
				"Sophia",
				"Mia",
				"Benjamin",
				"Mason",
				"Elijah",	
				"Oliver",	
				"Jacob",	
				"Lucas",	
				"Michael",	
				"Alexander",	
				"Elizabeth",
				"Ethan",	
				"Avery",
				"Daniel",	
				"Sofia",
				"Matthew",	
				"Ella",
				"Aiden",	
				"Madison",
				"Henry",	
				"Scarlett",
				"Joseph",
				"Victoria",
				"Jackson",	
				"Aria",
				"Samuel",	
				"Grace",
				"Sebastian",	
				"Chloe",
				"David",	
				"Camila",
				"Carter",	
				"Penelope",
				"Wyatt",	
				"Riley",
				"Jayden",	
				"Layla",
				"John",
				"Lillian",
				"Owen",	
				"Nora",
				"Dylan",	
				"Zoey",
				"Luke",	
				"Mila",
				"Gabriel",	
				"Aubrey",
				"Anthony",	
				"Hannah",
				"Isaac",	
				"Lily",
				"Grayson",	
				"Addison",
				"Jack",	
				"Eleanor",
				"Julian",	
				"Natalie",
				"Levi",	
				"Luna",
				"Christopher",	
				"Savannah",
				"Joshua",	
				"Brooklyn",
				"Andrew",	
				"Leah",
				"Lincoln",	
				"Zoe",
				"Mateo",	
				"Stella",
				"Ryan",	
				"Hazel",
				"Jaxon",	
				"Ellie",
				"Nathan",	
				"Paisley",
				"Aaron",	
				"Audrey",
				"Isaiah",	
				"Skylar",
				"Thomas",	
				"Violet",
				"Charles",	
				"Claire",
				"Caleb",	
				"Bella",
				"Josiah",	
				"Aurora",
				"Christian",	
				"Lucy",
				"Hunter",	
				"Anna",
				"Eli",	
				"Samantha",
				"Jonathan",	
				"Caroline",
				"Connor",	
				"Genesis",
				"Landon",	
				"Aaliyah",
				"Adrian",	
				"Kennedy",
				"Asher",	
				"Kinsley",
				"Cameron",	
				"Allison",
				"Leo",	
				"Maya",
				"Theodore",	
				"Sarah",
				"Jeremiah",	
				"Madelyn",
				"Hudson",	
				"Adeline",
				"Robert",	
				"Alexa",
				"Easton",	
				"Ariana",
				"Nolan",	
				"Elena",
				"Nicholas",	
				"Gabriella",
				"Ezra",	
				"Naomi",
				"Colton",	
				"Alice",
				"Angel",	
				"Sadie",
				"Brayden",	
				"Hailey",
				"Jordan",	
				"Eva",
				"Dominic",	
				"Emilia",
				"Austin",	
				"Autumn",
				"Ian",	
				"Quinn",
				"Adam",	
				"Nevaeh",
				"Elias",	
				"Piper",
				"Jaxson",	
				"Ruby",
				"Greyson",	
				"Serenity",
				"Jose",	
				"Willow",
				"Ezekiel",	
				"Everly",
				"Carson",
				"Cora",
				"Evan",	
				"Kaylee",
				"Maverick",	
				"Lydia",
				"Bryson",	
				"Aubree",
				"Jace",	
				"Arianna",
				"Cooper",	
				"Eliana",
				"Xavier",	
				"Peyton",
				"Parker",	
				"Melanie",
				"Roman",	
				"Gianna",
				"Jason",	
				"Isabelle",
				"Santiago",	
				"Julia",
				"Chase",	
				"Valentina",
				"Sawyer",	
				"Nova",
				"Gavin",	
				"Clara",
				"Leonardo",	
				"Vivian",
				"Kayden",	
				"Ayden",	
				"Jameson",	
				"Charlotte",
				"Amelia",
				"Evelyn",
				"Abigail",
				"Harper",
				"Emily",
				"Reagan",
				"Mackenzie",
				"Madeline"
				));
	}
	

}
