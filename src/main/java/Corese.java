import fr.inria.corese.core.Graph;
import fr.inria.corese.core.load.Load;
import fr.inria.corese.core.load.LoadException;
import fr.inria.corese.core.print.CSVFormat;
import fr.inria.corese.core.print.RDFFormat;
import fr.inria.corese.core.print.ResultFormat;
import fr.inria.corese.core.print.TripleFormat;
import fr.inria.corese.core.query.QueryProcess;
import fr.inria.corese.kgram.core.Mappings;
import fr.inria.corese.sparql.exceptions.EngineException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Corese {
    private   String RESOURCES_PATH = "src/main/resources/" ;
    private Map <String,String> prefixes ;
    private static Graph graph= null;

    public Corese() {
        /**
         * these prefixes are related to humans.rdf in resources
         * will be added in the begining of each query
         * */
        prefixes= new HashMap<String,String>(); ;
        this.prefixes.put("rdf","@prefix rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> .");
        this.prefixes.put("ns1","@prefix ns1:<http://www.inria.fr/2015/humans#> .");
        this.prefixes.put("ns1-instances","@prefix ns1-instances: <http://www.inria.fr/2015/humans-instances#>.");
    }
    public void addPrefix(String namespace,String uri){
        this.prefixes.put(namespace,uri);
    }
    public Map getPrefixes(){
        return this.prefixes;
    }

    public synchronized Graph init() throws LoadException {
        if(this.graph==null){
            this.graph = Graph.create(true);
            graph.setName("graph"+UUID.randomUUID());
            Load ld = Load.create(this.graph);
            ld.parse(RESOURCES_PATH + "human1.rdf");
        }
        return this.graph;
    }
    public Mappings query(Graph graph, String query) throws EngineException {
        QueryProcess exec = QueryProcess.create(graph);
        Mappings map  = exec.query(query);
        return map;
    }
    public ResultFormat resultFormat(Mappings map){
        return  ResultFormat.create(map);
    }

    public CSVFormat csvFormat(Mappings map){
       return  CSVFormat.create(map);
    }

    public TripleFormat GraphTrippleFormat(Graph graph){
        return  TripleFormat.create(graph);
    }
    public RDFFormat GraphRDFFormat(Graph graph){
        return  RDFFormat.create(graph);
    }

    public  String queryBuilder (String query)  {
        StringBuilder stringBuilder = new StringBuilder();
       /* for (Map.Entry mapentry : this.prefixes.entrySet()) {
            System.out.println("clé: "+mapentry.getKey()
                    + " | valeur: " + mapentry.getValue());
            stringBuilder.append(this.prefixes.get(item));
        }*/
        for (String item :this.prefixes.values()){
               stringBuilder.append(item);
        }
        stringBuilder.append(query);
        return  stringBuilder.toString();
    }

}
