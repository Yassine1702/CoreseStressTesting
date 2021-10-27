
import fr.inria.corese.core.Graph;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CustomSimpler extends AbstractSampler {
    /*
    * this class is a custom sampler
    * it describes what occurs within our test
    * */
    private Corese  corese;

    public CustomSimpler() {
        this.corese = new Corese();
    }
    private static int counter ;

    @Override
    public SampleResult sample(Entry e) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        Graph graph = null;
        try {
            graph = corese.init();
                while(true){
                    corese.query( graph,
                            this.corese.queryBuilder(" insert data {ns1-instances:Jim"+UUID.randomUUID()+" a ns1:Man;ns1:age 18 .}")
                    );
                    incrCounter();

                    sampleResult.setSuccessful(true);
                }

        } catch (Exception err) {
            err.printStackTrace();
            sampleResult.setSuccessful(false);
        } finally {
            System.out.println(corese.GraphTrippleFormat(graph));
            sampleResult.sampleEnd();
        }
        return sampleResult;
    }

    @Override
    public void removed() {

        super.removed();
    }
    public synchronized  void incrCounter(){
        counter++;
        System.out.println("Thread num :"+JMeterContextService.getContext().getThreadNum()+", counter : " +counter);
    }
}
