/**
 * @author lidong 16-12-28.
 */
public class BaseJob {

    public static void main(String[] args) throws Exception {
//        String propsFile = System.getenv("JOB_PROP_FILE");
//        Properties prop = new Properties();
//        prop.load(new BufferedReader(new FileReader(propsFile)));
//
//        String jobName = System.getenv("JOB_NAME");

        BaseJob job = new BaseJob();
        job.run();
    }

    public void run() throws Exception {
        System.out.println("job running.....................");
    }

    public void cancel() throws Exception {
        System.out.println("job cancel.....................");
    }

}
