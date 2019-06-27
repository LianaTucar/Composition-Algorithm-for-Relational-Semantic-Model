import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Ontology {
	public Map<String, Concept> concepts;	
	public Map<String, RelationDefinition> relDefinitions;
	public int nrConcepts, nrRelations, nrRules;
	public Concept root = new Concept("root"); //de creat si realizat legaturile corecte la asta !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public List<Rule> rules;
	
	public boolean isA(Concept sub, Concept supr) {
		Concept c = concepts.get(sub.name);
		//bulaneala !!!!
		if (c==null) {
			c = new Concept(sub.name);
			c.superConcept = root;
			concepts.put(c.name, c);
			Knowledge.objOfType.put(c.name, new ArrayList<>()); //??mhm...
		}
		//aici se termina bulaneala...e pt teste gresite unde am concepte in query care nu-s in ontologie..le pun frunze
		supr =  concepts.get(supr.name);
		//System.out.println("sub "+sub);
		//System.out.println("concept "+c);
		//System.out.println("supr "+supr);
		while (c!=null) {
			if (c.equals(supr))
				return true;
			c=c.superConcept;
		}
		return false;
	}
	
	private void addConcIfDoesntExist(String concName) {
		if (!concepts.containsKey(concName))
			concepts.put(concName, new Concept(concName));
	}
	
	private void readNumbers(Scanner sc) {
		//read numbers
        this.nrConcepts = sc.nextInt();
        this.nrRelations = sc.nextInt();
        this.nrRules = sc.nextInt();
        sc.nextLine(); sc.nextLine();
	}
	
	
	/*private Concept find(Concept conc) { //o sa trebuiasca sa scap de functia asta si sa fac un map
		for (Concept c:concepts)
			if (c.equals(conc))
				return c;
		return null;
	}*/
	
	private void readConcepts(Scanner sc) {
		//read concepts
        concepts = new HashMap<>();
        concepts.put(root.name, root);
        for (int i = 0; i < this.nrConcepts; i++) {
            Scanner ls = new Scanner(sc.nextLine());
            String concName = ls.next();
            String parentConcName = ls.next();
            
            //Concept concept = new Concept(concName);
            //Concept parentConcept = new Concept(parentConcName);
            
            addConcIfDoesntExist(concName);
            addConcIfDoesntExist(parentConcName);
            
            Concept concept = concepts.get(concName);
            Concept parentConcept = concepts.get(parentConcName);
            
            if (!parentConcept.equals(concept)) {
            	concept.superConcept = parentConcept;
                parentConcept.subConcepts.add(concept);
            }else {
            	concept.superConcept = root;
            	root.subConcepts.add(concept);
            }
        }
	}
	
	private void readRelations(Scanner sc) {
		//read relations
        relDefinitions = new HashMap<>();
        sc.nextLine();
        for (int i = 0; i < this.nrRelations; i++) {
            Scanner ls = new Scanner(sc.nextLine());
            String relName = ls.next();
            RelationDefinition rd = new RelationDefinition(relName); 
            relDefinitions.put(rd.name, rd);
            while (ls.hasNext()) {
                String prop = ls.next();
                if ("transitive".equals(prop) ||
                    "symetric".equals(prop) ||
                    "reflexive".equals(prop)) {
                    rd.properties.add(prop);
                }
            }
        }
		
	}
	
	private void readRules(Scanner sc, Concept root) {
		this.rules = new ArrayList<>();
		sc.nextLine();
        for (int ii = 0; ii < nrRules; ii++) {
            Rule rule = new Rule(sc, root, relDefinitions);
            rules.add(rule);            
            sc.nextLine();
        }
	}
	
	public Ontology(String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		readNumbers(sc);
		readConcepts(sc);
        readRelations(sc);
        readRules(sc, root); 
	}
	
	private void adaugaIerarhie(StringBuilder sb) {
		for (String cName: concepts.keySet()) {
			sb.append(cName+" : ");
			Concept c = concepts.get(cName);
			for (Concept subC:c.subConcepts)
				sb.append(subC.name+" ");
			sb.append("\n");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\nOntology:\n");
		sb.append("nr concepts = " + concepts.size());
		sb.append("  nr relations = " + relDefinitions.size());
		sb.append("  nr rules = " + rules.size());
		sb.append("\n ierarhie ");
		sb.append("\nconcepts: \n");
		adaugaIerarhie(sb);
		return sb.toString();
	}
}
