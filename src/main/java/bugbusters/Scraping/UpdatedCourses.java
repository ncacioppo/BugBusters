package bugbusters.Scraping;

import bugbusters.Course;
import bugbusters.Day;
import bugbusters.MeetingTime;
import bugbusters.Term;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class UpdatedCourses {

    public LocalDateTime timeStamp;
    public ArrayList<Pair<Course, Pair<Integer, Integer>>> updatedCourseList;

    public boolean status = false;

    public UpdatedCourses(){
        timeStamp = LocalDateTime.now();
        updatedCourseList = new ArrayList<>();

        try {

            URI uri = new URI("http://10.18.110.187/api/classes.json");
            JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
            JSONObject jsonObject = new JSONObject(tokener);

            JSONArray jsonCourses = jsonObject.getJSONArray("classes");
            int count = 1;
            int countSize = 0;
            for (Object bigObject : jsonCourses){

                ArrayList<MeetingTime> meetingTimes = new ArrayList<>();

                JSONObject object = (JSONObject)  bigObject;

                int credits = (int) Math.round(object.getDouble("credits"));

                String faculty = "";
                JSONArray facultyList = object.getJSONArray("faculty");
                String[] facultyLong = ((String) facultyList.get(0)).split(", ");
                if (facultyLong.length > 1) {
                    faculty = facultyLong[1].split(" ")[0] + " " + facultyLong[0];
                } else if (facultyLong.length == 0){
                    faculty = facultyLong[0];
                }

                String location = object.getString("location");
                String name = object.getString("name");
                int number = object.getInt("number");
                String section = object.getString("section");

                String potSemester = object.getString("semester");

                Term term = null;
                if (potSemester.split("_").length>1) {
    //                    System.out.println("Year: " + Integer.parseInt(potSemester.split("_")[0]));
                    term = new Term(potSemester.split("_")[1], Integer.parseInt(potSemester.split("_")[0]));
                }

                String department = object.getString("subject");

                JSONArray times = object.getJSONArray("times");


                for (Object meetingBigObject : times){
                    JSONObject meetingObject = (JSONObject) meetingBigObject;
                    String day = meetingObject.getString("day");
                    String start = meetingObject.getString("start_time");
                    String end = meetingObject.getString("end_time");

                    Day actualDay = null;
                    switch(day){
                        case "M":
                            actualDay = Day.MONDAY;
                            break;
                        case "T":
                            actualDay = Day.TUESDAY;
                            break;
                        case "W":
                            actualDay = Day.WEDNESDAY;
                            break;
                        case "R":
                            actualDay = Day.THURSDAY;
                            break;
                        case "F":
                            actualDay = Day.FRIDAY;
                    }

                    LocalTime startTime = LocalTime.of(Integer.parseInt(start.split(":")[0]), Integer.parseInt(start.split(":")[1]), Integer.parseInt(start.split(":")[0]));
                    LocalTime endTime = LocalTime.of(Integer.parseInt(end.split(":")[0]), Integer.parseInt(end.split(":")[1]), Integer.parseInt(end.split(":")[0]));

                    if (actualDay != null) {
                        MeetingTime meetingTime = new MeetingTime(actualDay, startTime, endTime);
                        meetingTimes.add(meetingTime);
                    }
                }


                //Just collecting for future
                int openSeats = object.getInt("open_seats");
                int totalSeats = object.getInt("total_seats");

                if (section.toCharArray().length >0) {
                    Course tempCourse = new Course(count, name, "Description", department, number, term, section.charAt(0), faculty, meetingTimes, credits);
                    count += 1;

                    updatedCourseList.add(Pair.of(tempCourse, Pair.of(openSeats, totalSeats)));
                    countSize += 1;
                } else {
                    Course tempCourse = new Course(count, name, "Description", department, number, term, 'A', faculty, meetingTimes, credits);
                    count += 1;
                    countSize += 1;

                    updatedCourseList.add(Pair.of(tempCourse, Pair.of(openSeats, totalSeats)));
                }


    //                System.out.println(name);
            }
            int maxYear = 0;
            for (Pair<Course, Pair<Integer, Integer>> pair : updatedCourseList){
                if (pair.getLeft().getTerm().getYear() > maxYear ){
                    maxYear = pair.getLeft().getTerm().getYear();
                }
            }
            System.out.println("Done");
            status = true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Failed");
            status = false;
        }

    }
}
