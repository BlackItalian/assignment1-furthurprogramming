package main.java;

import java.time.LocalTime;

public class Course {

	String coursename;
	int capacity;
	String year;
	String deliverymode;
	String lectureday;
	LocalTime lecturetime;
	double lectureduration;

	public Course() {
	}

	public Course(String coursename, int capacity, String year, String deliverymode, String lectureday,
			LocalTime lecturetime, double lectureduration) {

		this.coursename = coursename;
		this.capacity = capacity;
		this.year = year;
		this.deliverymode = deliverymode;
		this.lectureday = lectureday;
		this.lecturetime = lecturetime;
		this.lectureduration = lectureduration;
	}

	public String getCoursename() {
		return coursename;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getYear() {
		return year;
	}

	public String getDeliverymode() {
		return deliverymode;
	}

	public String getLectureday() {
		return lectureday;
	}

	public LocalTime getLecturetime() {
		return lecturetime;
	}

	public double getLectureduration() {
		return lectureduration;
	}

	@Override
	public String toString() {
		return getCoursename() + getCapacity() + getYear() + getDeliverymode() + getLectureday() +
				getLecturetime() + getLectureduration();
	}
}
