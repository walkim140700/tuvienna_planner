import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Planner
 * Main class of the tu vienna planner project
 * Author: Kilian Waltl
 * Last-Change: 27.04.2021
 */
public class Planner {

    /**
     * Edit should the url to the tu vienna change
     */
    public static final String MAIN_URL="https://ufind.univie.ac.at/de/";

    /**
     * Edit should the url to the course catalog change
     */
    public static final String COURSE_LIBRARY_URL="https://ufind.univie.ac.at/de/vvz.html";

    /**
     * Edit should you experience connection troubles
     */
    public static final int TIMEOUT_TIME_IN_SEC=20;

    /**
     * Change to true for more debugging info about Exceptions
     */
    public static final boolean DEBUG=false;

    public static void main(String[] args) {
        System.setProperty("http.agent", "");
        long startTime = System.currentTimeMillis();
        String courseId = args[0];
        Course course;
        try {
            course = new Course(courseId);
            HashSet<String> lectureList = course.getRelatedLectures();
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            for(String lectureURL:lectureList){
                LectureSearchTask thread = new LectureSearchTask(lectureURL, course);
                executor.execute(thread);
            }
            executor.shutdown();
            double progress=((double)executor.getCompletedTaskCount())/executor.getTaskCount()*100;
            while(!executor.isTerminated()){
                if(progress<((double)executor.getCompletedTaskCount())/executor.getTaskCount()*100-10) {
                    progress = ((double) executor.getCompletedTaskCount()) / executor.getTaskCount() * 100;
                    System.out.format("%.2f%% finished\n", progress);
                }
            }

            System.out.println();
            course.printNextTenLectures();
            long endTime = System.currentTimeMillis();
            System.out.format("Showing 10 of %d lectures in %.2f secs",course.size(),(double)(endTime-startTime)/1000);
        } catch (Exception e) {
            if(DEBUG)
                e.printStackTrace();
            System.out.println("ERROR: Can't retrieve data - make sure you are connected to the internet!");
        }
    }
}
