
import fr.inria.corese.core.Graph;
import fr.inria.corese.kgram.core.Mappings;
import fr.inria.corese.sparql.api.IDatatype;
import fr.inria.corese.sparql.exceptions.EngineException;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterThread;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;



public class CustomSampler extends AbstractSampler {
    /*
    * this class is a custom sampler
    * it describes what occurs within our test
    * */
    private static Corese  corese=null;
    private static int counter =0 ;
    private static String query ;
    private static Graph graph = null;


    public CustomSampler() {
           this.corese=getCorese();

    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();


        try {
            this.graph = this.corese.init();

               while(true){

                    corese.query(this.graph,
                           query(200)
                    );

                    incrCounter();
                    // calcJim(this.graph);
                    sampleResult.setSuccessful(true);
                }


        } catch (Exception err) {
            err.printStackTrace();
            sampleResult.setSuccessful(false);
        } finally {
            // System.out.println(TripleFormat.create(graph));
            sampleResult.sampleEnd();
        }
        return sampleResult;
    }

    @Override
    public void removed() {

        super.removed();
    }
    public synchronized  void incrCounter() throws EngineException {

               if(counter == 10000){
                   calcJim(this.graph);
                   counter = 0;
               }
               else{
                   counter+=200;
               }

    }
    public synchronized  void calcJim(Graph graph) throws EngineException {

        Mappings map =  this.corese.query(graph,"@prefix ns1:<http://www.inria.fr/2015/humans#>. SELECT (COUNT(?x) as ?count) WHERE {?x ns1:age 18}");
        IDatatype dt = (IDatatype)    map.getValue("?count");

        System.out.println("Thread num :"+JMeterContextService.getContext().getThreadNum()+
                ", counter Jim : " +dt.intValue()+" graph: "+ this.graph.getName());
    }

    private String query (int numRep){

        StringBuilder builder = new StringBuilder();
        Map<String,String> prefixes =  this.corese.getPrefixes();
        for (String item :prefixes.values()){
            builder.append(item);
        }

        for (int i=0;i<numRep;i++){
            builder.append(" insert data {ns1-instances:Jim"+UUID.randomUUID()+" a ns1:Man;ns1:name:'Jim';ns1:age 18 .};");

        }
       return  builder.toString();
    }

    private synchronized Corese getCorese(){
        if(this.corese == null){
            this.corese = new Corese();
        }
            return this.corese;
    }
/*
*
*   public synchronized  void incrCounter(long duration) throws EngineException {
        JMeterThread thread = JMeterContextService.getContext().getThread();
        if(counter >= 10000) {
            if(thread.getThreadNum()!=0){
                System.out.println("Thread Num : "+thread.getThreadNum()+" graph :"+this.graph.getName());
                return ;
            }else{
                System.out.println("Thread Num : "+thread.getThreadNum()+" : INSERT QUERY DURATION = "+ duration);
                calcJim(this.graph);
                counter = 0;
            }

        }else{
            counter+=200;
        }

    }
* */

    /*
    *
    *    while(true){
                  Instant start = Instant.now();
                    corese.query(this.graph,
                           query(200)
                    );
                    Instant end =Instant.now();
                    incrCounter(Duration.between(start,end).toMillis());
                    // calcJim(this.graph);
                    sampleResult.setSuccessful(true);
                }
    * */

    /**
     *  public synchronized  void calcJim(Graph graph) throws EngineException {
     *         Instant start = Instant.now();
     *         Mappings map =  this.corese.query(graph,"@prefix ns1:<http://www.inria.fr/2015/humans#>. SELECT (COUNT(?x) as ?count) WHERE {?x ns1:age 18}");
     *         IDatatype dt = (IDatatype)    map.getValue("?count");
     *         Instant end = Instant.now();
     *         System.out.println("Thread num :"+JMeterContextService.getContext().getThreadNum()+
     *                 ", counter Jim : " +dt.intValue()+" graph: "+ this.graph.getName()+" counting time : "+ Duration.between(start,end).toMillis());
     *     }
     * */
}
