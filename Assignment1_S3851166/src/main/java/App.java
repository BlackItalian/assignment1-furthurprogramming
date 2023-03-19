package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class App {
	
	static boolean programRunning = true;
	private static ArrayList<Course> courses = new ArrayList<Course>();
	static ArrayList<String> tempCourses = new ArrayList<String>();
	static ArrayList<String> selectedCourses = new ArrayList<String>();
	static Scanner userInput = new Scanner(System.in);
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
	
	public static void main(String[] args) {
		while (programRunning) {
			populateList();
			System.out.println(courses);
			programRunning = false;
	        System.out.println("Welcome to MyTimetable!");
			mainMenu();
		}
	}

	public static ArrayList<Course> populateList(){
		String path = "src/main/resources/course.csv";
		String line = "";
		int lineNumber = 0;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			while((line = br.readLine()) != null) {
				if(lineNumber == 0) {
					lineNumber++;
					continue;
				}
				String[] arrofCourses = line.split(",");
				courses.add(new Course(arrofCourses[0], Integer.parseInt(arrofCourses[1]), arrofCourses[2], arrofCourses[3], arrofCourses[4], LocalTime.parse(arrofCourses[5],formatter), Double.parseDouble(arrofCourses[6])));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
    
//Main Menu   
    public static void mainMenu() {
    	tempCourses.clear();
        System.out.println("---------------------------------------------");
        System.out.println(">  Select from main menu");
        System.out.println("---------------------------------------------");
        System.out.print("1) Search by keyword to enroll"
                            + "\n2) Show my enrolled courses"
                            + "\n3) Withdraw from a course"
                            + "\n4) Exit"
                            + "\nPlease select: ");

        int userSelection = userInput.nextInt();
        
        switch (userSelection) {
	        case 1:
	        	enrollInCourse();
				break;
	        case 2:
	        	enrolledCourses();
	        	break;
	        case 3:
	        	withdrawCourse();
	        	break;
	        case 4:
	        	System.exit(1);
	        	break;
			default:
		        System.out.println("---------------------------------------------");
		        System.out.println("Invalid Selection");
		        System.out.println("---------------------------------------------");
		        break;
		}
    }
    
    public static void enrollInCourse() {
		
    	boolean valid = true;
    	
    	System.out.print("Enter Keyword: ");
    	String keyword = userInput.next();
    	
        System.out.println("---------------------------------------------");
        System.out.println("> Select from mathcing list");
        System.out.println("---------------------------------------------");
        
        while (valid) {
        	int count = 1;
	        for(Course course: courses) {
	        	if(course.getCoursename().contains(keyword)) {
	        		System.out.println("\t" + count + ") " + course.getCoursename());
	        		tempCourses.add(course.getCoursename());
	        		count++;
	        	}
	        }
	        System.out.println("\t" + count + ") " + "Go to main menu");
	        System.out.print("Please select: ");
	        
	        int selection = userInput.nextInt();
	        
	        if (selection < count) {
	        	selectedCourses.add(tempCourses.get(selection - 1));
	        	System.out.println("You have now enrolled in the course " + tempCourses.get(selection - 1));
	        	valid = false;
	        	mainMenu();
	        	break;
	        }
	        else if(selection == count){
				mainMenu();
			}
	        System.out.println("---------------------------------------------");
	        System.out.println("Invalid Selection");
	        System.out.println("---------------------------------------------");
        }
    }
    
    public static void enrolledCourses() {
		int count = 1;
		
        System.out.println("---------------------------------------------");
		System.out.println("You have enrolled in the following course(s):");
        System.out.println("---------------------------------------------");
		for(Course course: courses) {

			if(course.getCoursename().contains(tempCourses.get(count - 1))) {
				//change lecture time to date/time format so that can display lecture time block
				System.out.println(course.getCoursename() + course.getDeliverymode() + course.getLectureday() + course.getLecturetime());
				count++;
			}
		}
    }
    
    public static void withdrawCourse() {
		int count = 1;
		
        System.out.println("---------------------------------------------");
		System.out.println("Please choose a course to withdraw:");
        System.out.println("---------------------------------------------");
        System.out.println(selectedCourses);
        for(Course course: courses) {

			if(course.getCoursename().contains(selectedCourses.get(count - 1))) {
				long[] result = addHours(course.getLectureduration());
				Long hours = result[0];
				Long minutes = result[1];
				System.out.println(course.getCoursename() + course.getDeliverymode() + course.getLectureday() + course.getLecturetime() + "-" + course.getLecturetime().plusHours(hours).plusMinutes(minutes));
				count++;
			}
		}	
        System.out.println("here");
        String selection = userInput.next();
        
        System.out.println("\t" + count + ") " + "Go to main menu");
        System.out.print("Please select: ");
	}
    
    public static long[] addHours(double d) {
		long hours = (long)d;
		long minutes = (long) ((d - hours) * 10);
		if (minutes == 5) {
			minutes = 30;
		}
		return new long[] {hours, minutes};
	}
}
//create function for iterating through the course arraylist to reuse the code block






















