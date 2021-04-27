import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Course
 * Class for holding info to one course
 * Author: Kilian Waltl
 * Last-Change: 27.04.2021
 */
public class Course {
    private String id;
    private String url;
    private List<Lecture> lectureList = new ArrayList<>();

    /**
     * Creates a new Course object, will automatically get the url given a correct course-id
     *
     * @param id  Refers to a specific course from tu vienna (e.g. 5.01 would be "Bachelor Informatik 2016"
     * @throws Exception  Is thrown when either the specified id wasn't found or a connection wasn't possible
     */
    public Course(String id) throws Exception{
        this.id = id;
        getUrlFromId();
    }

    /**
     * Prints the next 10 lectures to the console, will also get rid of any duplicates that might be in the list
     */
    public void printNextTenLectures(){
        Set<Lecture> l = new HashSet<>(lectureList);
        lectureList = new ArrayList<>(l);
        Collections.sort(lectureList);
        for(int i=0;i<10;i++){
            System.out.println(lectureList.get(i).toString());
            System.out.println();
        }
    }

    /**
     * Searches for all lecture-links on the webpage of the specified course
     *
     * @return A HashSet of all related lectures found to the specified course
     * @throws Exception  Is thrown when there was a problem with the connection e.g. time out
     */
    public HashSet<String> getRelatedLectures() throws Exception{
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .readTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(url).build();
        String courseHtml = client.newCall(request).execute().body().string();
        Document doc = Jsoup.parse(courseHtml);

        List<String> lectureLinks = doc.body().getElementsByClass("what").eachAttr("href");
        return new HashSet<>(lectureLinks);
    }


    /**
     * Returns the size of the list of related lectures
     *
     * @return size of lecture-list
     */
    public int size() {
        return lectureList.size();
    }

    /**
     * Adds a lecture to the related lecture list
     *
     * @param lecture  The lecture you want to add
     * @return true on success
     */
    public boolean add(Lecture lecture) {
        return lectureList.add(lecture);
    }

    /**
     * Removes the specified lecture from the course's list
     *
     * @param o  The lecture object you want to remove
     * @return true on success
     */
    public boolean remove(Object o) {
        return lectureList.remove(o);
    }

    /**
     * Returns the specified lecture you want to get
     *
     * @param index  The index of the lecture you want to get
     * @return the specified lecture
     */
    public Lecture get(int index) {
        return lectureList.get(index);
    }

    /**
     * Sets the lecture at the specified index
     *
     * @param index  The index of the lecture you want to replace
     * @param element  The lecture you want to change it to
     * @return the lecture at the specified index
     */
    public Lecture set(int index, Lecture element) {
        return lectureList.set(index, element);
    }

    /**
     * Removes the specified lecture from the list
     *
     * @param index  The index of the lecture you want to remove
     * @return the lecture object you removed
     */
    public Lecture remove(int index) {
        return lectureList.remove(index);
    }

    /**
     * Returns the index of the lecture you specified
     *
     * @param o  The lecture you want the index of
     * @return the index of the lecture you specified, will return -1 if the lecture is not in the list
     */
    public int indexOf(Object o) {
        return lectureList.indexOf(o);
    }

    private void getUrlFromId() throws Exception{
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .writeTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .readTimeout(Planner.TIMEOUT_TIME_IN_SEC, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(Planner.COURSE_LIBRARY_URL).build();
        String html = client.newCall(request).execute().body().string();
        Document doc = Jsoup.parse(html);

        Elements links = doc.body().getElementsByClass("link");

        for(Element link : links){
            if(link.text().startsWith(id+" ")) {
                url = Planner.MAIN_URL + link.attr("href");
            }
        }
        if(url.equals(""))
            throw new Exception("No course found!");
    }

    /**
     * Returns the id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the url
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the id and updates the url with the new id
     *
     * @param id  The id you want to change it to
     * @throws Exception Is thrown when either the new id was not found or there was a problem with the connection
     */
    public void setId(String id) throws Exception{
        this.id = id;
        getUrlFromId();
    }
}
