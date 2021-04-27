import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * LectureSearchTask
 * A class implementing the Runnable interface for use with the ThreadPoolExecutor class
 * Author: Kilian Waltl
 * Last-Change: 27.04.2021
 */
public class LectureSearchTask implements Runnable{
    private String lectureURL;
    Course course;

    /**
     * Creates a new LectureSearchTask object
     *
     * @param lectureURL  The url of the lecture you want to get info about
     * @param course  The course you want to add it to
     */
    public LectureSearchTask(String lectureURL, Course course) {
        this.lectureURL = lectureURL;
        this.course = course;
    }

    /**
     * Will try to add all lectures it finds to the courses lecture list
     * Will print errors to the console if there was a connection problem or if the HTML was not read correctly
     */
    @Override
    public void run(){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .readTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(Planner.MAIN_URL+lectureURL).build();
        String courseHtml = null;
        try {
            courseHtml = client.newCall(request).execute().body().string();
            Document doc = Jsoup.parse(courseHtml);
            Elements elements = doc.getElementsByClass("event line future");
            elements.addAll(doc.getElementsByClass("event line next"));
            String name = doc.getElementsByClass("usse-id-courselong").first().getElementsByClass("what").first().text();
            for(Element e:elements){
                String date = e.getElementsByClass("date").text();
                String time = e.getElementsByClass("time").text();
                String location = e.getElementsByClass("room").text();
                time=time.substring(0,time.indexOf(" "));
                Date dateTime = new SimpleDateFormat("dd.MM.yyyy/hh:mm").parse(date + Year.now().getValue()+"/"+time);
                if(dateTime.after(new Date()))
                    course.add(new Lecture(name,dateTime,lectureURL,location));
            }
        } catch (IOException e) {
            if(Planner.DEBUG)
                e.printStackTrace();
            System.out.println("ERROR: Could not retrieve lecture data for url: "+lectureURL+"\nMake sure your are connected to the internet!");
        } catch (ParseException e) {
            if(Planner.DEBUG)
                e.printStackTrace();
            System.out.println("ERROR: Could not convert the found date correctly, please try again!");
        }
    }
}
