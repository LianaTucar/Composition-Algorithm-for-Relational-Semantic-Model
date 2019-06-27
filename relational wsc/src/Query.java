import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Query {
	public String name;
	public List <Parameter> provided, required;
	public List <Relation> providedRelations, requiredRelations;
	
	public void readQuery(Scanner scanner, Ontology ontology) {
		Service serv = new Service(scanner, ontology);
		this.name = serv.name;
		this.provided = serv.inputParams; //sa vad daca se copiaza bine listele
		this.required = serv.outputParams;
		this.providedRelations = serv.preconditions;
		this.requiredRelations = serv.postconditions;
	}
	
	public Query(String fileName, Ontology ontology) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		readQuery(sc, ontology);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Query "+this.name + "\n");
		
		sb.append("Provided Parameters:\n");
		for (Parameter p: provided)
			sb.append(p.toString()+" ");
		sb.append("\nRequired Parameters:\n");
		for (Parameter p: required)
			sb.append(p.toString()+" ");
		sb.append("\nProvided relations:\n");
		for (Relation r: providedRelations)
			sb.append(r.toString()+" ");
		sb.append("\nRequired relations:\n");
		for (Relation r: requiredRelations)
			sb.append(r.toString()+";");
		sb.append("\n\n");
		return sb.toString();
	}
	
	
}
