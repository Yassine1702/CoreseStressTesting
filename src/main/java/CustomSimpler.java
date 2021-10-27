
import fr.inria.corese.core.Graph;
import fr.inria.corese.core.print.TripleFormat;
import fr.inria.corese.kgram.core.Mapping;
import fr.inria.corese.kgram.core.Mappings;
import fr.inria.corese.sparql.api.IDatatype;
import fr.inria.corese.sparql.exceptions.EngineException;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CustomSimpler extends AbstractSampler {
    /*
    * this class is a custom sampler
    * it describes what occurs within our test
    * */
    private Corese  corese;
    private static int counter =0 ;
    private static String query ;

    public CustomSimpler() {
        this.corese = new Corese();

    }

    @Override
    public SampleResult sample(Entry e) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        Graph graph = null;

        try {

            graph = corese.init();
               while(true){
                    corese.query(graph,
                           query(200)
                    );
                    // incrCounter()
                  calcJim(graph);

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
    public synchronized  void incrCounter(){
        counter++;
        System.out.println("Thread num :"+JMeterContextService.getContext().getThreadNum()+", counter : " +counter);
    }
    public synchronized  void calcJim(Graph graph) throws EngineException {
        Mappings map =  this.corese.query(graph,"@prefix ns1:<http://www.inria.fr/2015/humans#>. SELECT (COUNT(?x) as ?count) WHERE {?x ns1:age 18}");
        IDatatype dt = (IDatatype)    map.getValue("?count");
        System.out.println("Thread num :"+JMeterContextService.getContext().getThreadNum()+", counter Jim : " +dt.intValue());
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


}
