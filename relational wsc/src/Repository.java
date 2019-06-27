import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Repository {
	List<Service> services;
	private Set<Parameter> parcurs;
	
	public Repository(String fileName, Ontology ontology) throws FileNotFoundException {
		services = new ArrayList<>();
        Scanner sc = new Scanner(new File(fileName));
        int nrServices = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < nrServices; i++) {
            Service s = new Service(sc, ontology);
            Collections.sort(s.inputParams); //asta e pt "optimizarea" backtrackingului?? mai vedem aici
            services.add(s);            
        }
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#services (" + services.size() + ") are :\n");
		
		for (Service s: services) {
			sb.append(s.toString());
		}
		return sb.toString();
	}
	
	
	
	public String detCompConexeInput() {
		StringBuilder sb = new StringBuilder();
		for (Service serv:services) {
			int nrComp = serv.nrCompConexeInput();
			sb.append("serv "+ serv.name + " are "+ nrComp+" comp si "+ serv.inputParams.size() + " param " + serv.preconditions.toString() + " rel\n");
		}
		return sb.toString();
	}
	
	public void addRules(List <Rule> rules) {
		for (Rule r:rules) {
			Service serv = new Service("Rule_"+r.name, r.parameters, new ArrayList<>(), r.premise, r.conclusion);
			services.add(serv);
		}
	}
	
}
