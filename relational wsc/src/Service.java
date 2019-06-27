import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Service {
	public String name;
	public List<Parameter> inputParams, outputParams;
	public List<Relation> preconditions, postconditions;
	
	public HashMap<String, Parameter> params = new HashMap<>();
	
	private Set<Parameter> parcurs;
	
	public Service() {
		inputParams = new ArrayList<>();
		outputParams = new ArrayList<>();
		preconditions = new ArrayList<>();
		postconditions = new ArrayList<>();
	}
	
	private List<Parameter> readParamList(Scanner ls) {
		List<Parameter> paramList = new ArrayList();
		while (ls.hasNext()) {
            String type = ls.next();
            String name = ls.next();
            Parameter p = new Parameter(name, new Concept(type));
            paramList.add(p);
            params.put(p.name, p);
        }
		return paramList;
	}
	
	public Service(Scanner scanner, Ontology ontology) {
		name = scanner.nextLine();
        
		//read input&output params
		inputParams = readParamList(new Scanner(scanner.nextLine()));
		outputParams = readParamList(new Scanner(scanner.nextLine()));
		
        
        //read relations
        Scanner ls = new Scanner(scanner.nextLine());
        preconditions = new ArrayList<>();
        postconditions = new ArrayList<>();
        while (ls.hasNext()) {
            String relName = ls.next();
            String v1 = ls.next(),  v2 = ls.next();
            if (!params.containsKey(v1))
            	System.out.println(v1);
            if (!params.containsKey(v2))
            	System.out.println(v2);
            Parameter p1 = params.get(v1), p2 = params.get(v2);
            RelationDefinition rd = ontology.relDefinitions.get(relName);
            Relation rel = new Relation(rd, p1, p2);
            if (inputParams.contains(p1) && inputParams.contains(p2))
            	preconditions.add(rel);
            else
            	postconditions.add(rel);
        }        
        scanner.nextLine();   
	}
	
	public Service(String name, List<Parameter> inputParams, List<Relation> preconditions) {
		this.name = name;
		this.inputParams = inputParams;
		this.preconditions = preconditions;
		
		this.outputParams = new ArrayList<>();
		this.postconditions = new ArrayList<>();
	}
	
	public Service(String name, List<Parameter> inputParams, List<Parameter> outParams, List<Relation> preconditions, List<Relation> postconditions) {
		this.name = name;
		this.inputParams = inputParams;
		this.outputParams = outParams;
		this.preconditions = preconditions;
		this.postconditions = postconditions;
	}
	
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Service "+this.name + "\n");
		
		sb.append("Input Parameters:\n");
		for (Parameter p: inputParams)
			sb.append(p.toString()+" ");
		sb.append("\nOutput Parameters:\n");
		for (Parameter p: outputParams)
			sb.append(p.toString()+" ");
		sb.append("\nPreconditions:\n");
		for (Relation r: preconditions)
			sb.append(r.toString()+" ");
		sb.append("\nPostconditions:\n");
		for (Relation r: postconditions)
			sb.append(r.toString()+";");
		sb.append("\n\n");
		return sb.toString();
	}
	
	private void parcurge(Parameter p) {
		parcurs.add(p);
		for (Relation r:preconditions) {
			if (r.x.equals(p))
				if (!parcurs.contains(r.y))
					parcurge(r.y);
			if (r.y.equals(p))
				if (!parcurs.contains(r.x))
					parcurge(r.x);
		}
	}
	
	public int nrCompConexeInput() {
		int nrComp = 0;
		parcurs = new HashSet<>();
		for (Parameter p:this.inputParams)
			if (!parcurs.contains(p)) {
				parcurge(p);
				nrComp++;
			}
		return nrComp;
	}
}
