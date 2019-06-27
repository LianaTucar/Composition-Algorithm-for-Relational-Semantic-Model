package relationalwsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class RelationalWSCMain {
    public static Ontology ontology;
    public static Repository repository;
    public static Service userQuery;
    public static Knowledge knowledge;

    // Composition builder data:
    public static Object matchObjects[];
    public static Service currentService;
    public static boolean serviceMatch;
    
    // Random;
    public static Random random = new Random();
    static private final String OUTPUT = "./output.txt";
    static private final String STATS_FILE = "./stats.txt";
    static private final String FILE_SUFFIX = ".txt";
    // statistics:
    public static ArrayList<String> matchedServicesAndRules = new ArrayList();
    public static int matchedServices = 0;
    public static int matchedRules = 0;

    public static void main(String[] args) throws FileNotFoundException {
        
        PrintStream outstream = null;
        try {
            /* outstream = new PrintStream(new FileOutputStream(OUTPUT));
            System.setOut(outstream);
            */
        } catch (Exception e) {
        }               
        
        ontology = new Ontology();
        ontology.readFromFile("./ontology" + FILE_SUFFIX);
        //ontology.printOntology();
        
        repository = new Repository();
        repository.readFromFile("./repository" + FILE_SUFFIX);
        //repository.printRepository();
        
        userQuery = new Service();
        userQuery.readFromScanner(new Scanner(new File("./query" + FILE_SUFFIX)));
        ArrayList<Parameter> tmp = userQuery.in; userQuery.in = userQuery.out; userQuery.out = tmp;
        userQuery.buildVariableRelInput();
        //userQuery.printService();
        repository.repo.put(userQuery.name, userQuery);
        System.out.println("-------------------------------------------------------------- Input parsing done.");
        
        knowledge = new Knowledge(ontology);        
        callService(userQuery, new ArrayList<>());
        System.out.println("Knowledge after learning user input and running rules:");
        knowledge.runInferenceRules();
        //knowledge.printKnowledge();
        
        System.out.println("-------------------------------------------------------------- Start building composition.");
        boolean newServiceCall = true;
        boolean querySolved = false;
        while (newServiceCall) {
            newServiceCall = false;
            // try to call any new service :)
            for (String serviceName : repository.repo.keySet()) {
                currentService = repository.repo.get(serviceName);
                if (canCallService(currentService)) {
                    if (currentService.name.equals(userQuery.name)) {
                        querySolved = true;
                        newServiceCall = false;
                        break;
                    }
                    ArrayList<Object> matchesTmp = new ArrayList<>();
                    for (int i = 0; i < currentService.in.size(); i++) {
                        matchesTmp.add(matchObjects[i]);
                    }
                    callService(currentService, matchesTmp);
                    //knowledge.runInferenceRules();
                    //System.out.printf("Called service %s with params:", currentService.name);
                    RelationalWSCMain.matchedServicesAndRules.add(currentService.name);
                    matchedServices++;
                    /*
                    for (int i = 0; i < currentService.in.size(); i++) {
                        System.out.printf("%s(%s) ", currentService.in.get(i).name, matchObjects[i].name);
                    }
                    System.out.printf(", knowledge after is:\n");
                    */
                    
                    knowledge.printKnowledge();
                    newServiceCall = true;
                }
            }
        }
        PrintWriter pw = new PrintWriter(new File(STATS_FILE));
        if (querySolved) {
            
            //
            
            pw.printf("Solved.\n%d\n%d\n", matchedServices, matchedRules);
            System.out.print("Solved user query with: ");
            for (int i = 0; i < userQuery.in.size(); i++) {
                System.out.printf("%s(%s) ", userQuery.in.get(i).name, matchObjects[i].name);
            }
            System.out.println();
        } else {
            pw.printf("Not Solved\n");
            System.out.println("Could not solve user query.");
        }
        for (String str : matchedServicesAndRules) {
            pw.printf("%s ", str);
        }
        pw.println();
        pw.close();
    }
    
    static void bktService(int level) {
        if (level == currentService.in.size()) {
            boolean alreadyCalled = false;
            for (int i = 0; i < currentService.inputsHistoryObjects.size(); i++) { // for every historic call
                int j = 0;
                for (j = 0; j < currentService.in.size(); j++) {
                    if (!matchObjects[j].name.equals(currentService.inputsHistoryObjects.get(i)[j].name) || 
                         matchObjects[j].relationsDirect.hashCode() != currentService.inputsHistoryRelationsHash.get(i)[j]) {
                        break;
                    }
                }
                if (j == currentService.in.size()) {
                    alreadyCalled = true;
                }
            }
            if (alreadyCalled) {
                serviceMatch = false;
            } else {
                serviceMatch = true;
            }
        } else {
            String type = currentService.in.get(level).type;
            if (!knowledge.objectsByType.containsKey(type)) {
                return;
            }
            ArrayList<Object> randObjects =  new ArrayList<Object>(knowledge.objectsByType.get(type));
            for (int i = 1; i < randObjects.size(); i++) {
                int pswap = random.nextInt(i+1);
                Object tmp = randObjects.get(i);
                randObjects.set(i, randObjects.get(pswap));
                randObjects.set(pswap, tmp);
            }
            //for (Object candidate : knowledge.objectsByType.get(type)) {
            for (Object candidate : randObjects) {
                // try to put candidate object on this level
                boolean match = true;
                for (int i = 0; i < level && match; i++)  {
                    if (serviceMatch) {
                        match = false;
                        break; // get out of backtracking!
                    }
                    // check relations from param. i to param. level
                    for (Relation rel : currentService.variableRelInput.get(i)) {
                        if (rel.second.equals(currentService.in.get(level).name) &&
                            (!matchObjects[i].relations.containsKey((candidate.name)) ||
                             !matchObjects[i].relations.get(candidate.name).contains(rel.name))) {
                            match = false;
                            break;
                        }
                    }
                    // check relations from param. level to param. i
                    for (Relation rel : currentService.variableRelInput.get(level)) {
                        if (rel.second.equals(currentService.in.get(i)) &&
                            !candidate.relations.get(matchObjects[i]).contains(rel.name)) {
                            match = false;
                            break;
                        }
                    }
                }
                if (match && !serviceMatch) {
                    matchObjects[level] = candidate;
                    bktService(level + 1);                        
                }
            }
        }
    }
    // Returns true if the service can be called and loads matchObjects[]    
    static boolean canCallService(Service service) {
        matchObjects = new Object[service.in.size()];
        serviceMatch = false;
        bktService(0);
        return serviceMatch;
    }
    
    /* does not check objectsMatches, they must be valid */
    // service - service to call
    // objects - the object matching input parameteres in order. If empty, ignore
    private static void callService(Service service, ArrayList<Object> objectsMatches) {
        
        if (!service.name.equals(userQuery.name)) {
            Object[] copy = new Object[service.in.size()];
            int[] hashes = new int[service.in.size()];
            for (int i = 0; i < service.in.size(); i++) {
                copy[i] = matchObjects[i];
                matchObjects[i].relationsDirect.clear();
                // collect all distinct relations to calculate HASH.
                for (String obj : matchObjects[i].relations.keySet()) {
                    for (String rel : matchObjects[i].relations.get(obj)) {
                        matchObjects[i].relationsDirect.add(rel);                        
                    }
                }
                hashes[i] = matchObjects[i].relationsDirect.hashCode();
            }
            repository.repo.get(service.name).inputsHistoryObjects.add(copy);
            repository.repo.get(service.name).inputsHistoryRelationsHash.add(hashes);
        }
        
        ArrayList<Object> outputObj = new ArrayList<>();
        for (Parameter par : service.out) {
            Object newObj = new Object(service.name + "_" + service.inputsHistoryObjects.size() + "_" + par.name, par.type);
            knowledge.addNewObject(newObj);
            outputObj.add(newObj);
        }
        for (Relation rel : service.relations) {
            // output x output relations
            for (int o1 = 0; o1 < outputObj.size(); o1++) {
                Object obj1 = outputObj.get(o1);
                for (int o2 = 0; o2 < outputObj.size(); o2++) {
                    Object obj2 = outputObj.get(o2);
                    // obj1 -> obj2 rel ?
                    if (rel.first.equals(service.out.get(o1).name) &&
                        rel.second.equals(service.out.get(o2).name)) {
                        knowledge.addRelation(rel, obj1, obj2);
                        // objects should be in knowledge already
                    }
                }
            }
            // input x output relations
            if (objectsMatches.isEmpty()) {
                continue; // this is for user query mock only
            }
            for (int i1 = 0; i1 < service.in.size(); i1++) {
                Object objIn = objectsMatches.get(i1);
                for (int o2 = 0; o2 < service.out.size(); o2++) {
                    Object objOut = outputObj.get(o2);
                    // objIn -> objOut rel ?
                    if (rel.first.equals(service.in.get(i1).name) &&
                        rel.second.equals(service.out.get(o2).name)) { 
                        knowledge.addRelation(rel, objIn, objOut);
                    }
                    // objOut -> objIn rel ?
                    if (rel.first.equals(service.out.get(o2).name) &&
                        rel.second.equals(service.in.get(i1).name)) { 
                        knowledge.addRelation(rel, objOut, objIn);
                    }
                }
            }
        }
    }
}
