package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

	boolean programRunning = true;
	ArrayList<Course> courses = new ArrayList<Course>();
	ArrayList<String> tempCourses = new ArrayList<String>();
	ArrayList<String> withdrawCourses = new ArrayList<String>();
	ArrayList<String> selectedCourses = new ArrayList<String>();
	Scanner userInput = new Scanner(System.in);
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

	public static void main(String[] args) {
		new App();
	}

	public App() {
		populateList();
		System.out.println("Welcome to MyTimetable!");
		mainMenu();
	}

	public ArrayList<Course> populateList() {
		String path = "Assignment1_S3851166/src/main/resources/course.csv";
		String line = "";
		int lineNumber = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {

			while ((line = br.readLine()) != null) {
				if (lineNumber == 0) {
					lineNumber++;
					continue;
				}
				String[] arrofCourses = line.split(",");
				courses.add(new Course(arrofCourses[0], Integer.parseInt(arrofCourses[1]), arrofCourses[2],
						arrofCourses[3], arrofCourses[4], LocalTime.parse(arrofCourses[5], formatter),
						Double.parseDouble(arrofCourses[6])));
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

	// Main Menu
	public void mainMenu() {
		boolean valid = false;
		int userSelection = 0;
		while (!valid) {
			tempCourses.clear();
			System.out.println("---------------------------------------------");
			System.out.println(">  Select from main menu");
			System.out.println("---------------------------------------------");
			System.out.print("1) Search by keyword to enroll"
					+ "\n2) Show my enrolled courses"
					+ "\n3) Withdraw from a course"
					+ "\n4) Exit"
					+ "\nPlease select: ");
			try {
				userSelection = userInput.nextInt();

				if (userSelection == 1) {
					valid = true;
					enrollInCourse();
				} else if (userSelection == 2) {
					valid = true;
					enrolledCourses();
				} else if (userSelection == 3) {
					valid = true;
					withdrawCourse();
				} else if (userSelection == 4) {
					System.exit(1);
				} else if (userSelection > 4) {
					throw new InvalidInput();

				}
			} catch (InvalidInput e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
			} catch (InputMismatchException e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
				userInput.next();
			}
		}
	}

	public void enrollInCourse() throws InvalidInput {

		boolean valid = true;

		System.out.print("Enter Keyword: ");
		String keyword = userInput.next();

		System.out.println("---------------------------------------------");
		System.out.println("> Select from mathcing list");
		System.out.println("---------------------------------------------");

		// Show selected list
		while (valid) {
			int count = 1;
			for (Course course : courses) {
				if (course.getCoursename().toLowerCase().contains(keyword.toLowerCase())) {
					System.out.println("\t" + count + ") " + course.getCoursename());
					tempCourses.add(course.getCoursename());
					count++;
				}
			}
			System.out.println("\t" + count + ") " + "Go to main menu");
			System.out.print("Please select: ");

			// List Selection
			try {
				int selection = userInput.nextInt();

				if (selection < count && !selectedCourses.contains(tempCourses.get(selection - 1))) {
					selectedCourses.add(tempCourses.get(selection - 1));
					System.out.println("You have now enrolled in the course " + tempCourses.get(selection - 1));
					valid = false;
					mainMenu();
					break;
				} else if (selection < count && selectedCourses.contains(tempCourses.get(selection - 1))) {
					System.out.println("---------------------------------------------");
					System.out.println("Already enrolled in this course");
					System.out.println("---------------------------------------------");
				} else if (selection == count) {
					mainMenu();
				} else if (selection < count) {
					throw new InvalidInput();
				}
			} catch (InvalidInput e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
				System.out.println("---------------------------------------------");
			} catch (InputMismatchException e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
				System.out.println("---------------------------------------------");
				userInput.next();
			}
		}
	}

	public void enrolledCourses() {

		boolean valid = true;

		// Check if enrolled in any courses
		if (selectedCourses.size() <= 0) {
			System.out.println("---------------------------------------------");
			System.out.println("Not yet Enrolled in any course(s)");
			mainMenu();
		}

		// Display enrolled courses
		while (valid) {
			System.out.println("---------------------------------------------");
			System.out.println("You have enrolled in the following course(s):");
			System.out.println("---------------------------------------------");

			int count = 1;
			int loopCount = 1;
			while (loopCount != courses.size()) {
				for (Course course : courses) {
					if (selectedCourses.size() >= count
							&& course.getCoursename().contains(selectedCourses.get(count - 1))) {
						long[] result = addHours(course.getLectureduration());
						Long hours = result[0];
						Long minutes = result[1];
						System.out.println(
								"\t" + count + ")" + course.getCoursename() + course.getDeliverymode()
										+ course.getLectureday()
										+ course.getLecturetime() + "-"
										+ course.getLecturetime().plusHours(hours).plusMinutes(minutes));
						count++;
					}
				}
				loopCount++;
			}
			mainMenu();
		}
	}

	public void withdrawCourse() throws InvalidInput {

		boolean valid = true;
		withdrawCourses.clear();

		// Check if enrolled in any courses
		if (selectedCourses.size() <= 0) {
			System.out.println("---------------------------------------------");
			System.out.println("Not yet Enrolled in any course(s)");
			mainMenu();
		}

		// Show enrolled list
		while (valid) {
			System.out.println("---------------------------------------------");
			System.out.println("Please choose a course to withdraw:");
			System.out.println("---------------------------------------------");

			int count = 1;
			int loopCount = 1;
			while (loopCount != courses.size()) {
				for (Course course : courses) {
					if (selectedCourses.size() >= count
							&& course.getCoursename().contains(selectedCourses.get(count - 1))) {
						long[] result = addHours(course.getLectureduration());
						Long hours = result[0];
						Long minutes = result[1];
						withdrawCourses.add(course.getCoursename());
						System.out.println("\t" + count + ")" + course.getCoursename() + course.getDeliverymode()
								+ course.getLectureday()
								+ course.getLecturetime() + "-"
								+ course.getLecturetime().plusHours(hours).plusMinutes(minutes));
						count++;
					}
				}
				loopCount++;
			}

			System.out.println("\t" + count + ") " + "Go to main menu");
			System.out.print("Please select: ");

			// Select course to withdraw
			try {
				int selection = userInput.nextInt();

				if (selection < count) {
					selectedCourses.remove(withdrawCourses.get(selection - 1));
					valid = false;
					mainMenu();
				} else if (selection == count) {
					mainMenu();
				} else if (selection < count) {
					throw new InvalidInput();
				}
			} catch (InvalidInput e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
				System.out.println("---------------------------------------------");
			} catch (InputMismatchException e) {
				System.out.println("---------------------------------------------");
				System.out.println("Invalid Selection");
				System.out.println("---------------------------------------------");
			}
		}

	}

	// Convert decimal time lengths to hours and minutes
	public static long[] addHours(double d) {
		long hours = (long) d;
		long minutes = (long) ((d - hours) * 10);
		if (minutes == 5) {
			minutes = 30;
		}
		return new long[] { hours, minutes };
	}

}