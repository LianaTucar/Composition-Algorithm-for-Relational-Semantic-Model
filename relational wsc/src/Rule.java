import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Rule {
	public String name;
	public List<Relation> premise, conclusion; //e bn modelat asa??
	public List<Parameter> parameters;
	public Map<String, Parameter> var_to_param = new HashMap<>();
	
	private List<Relation> readRelations(Scanner sc, Concept root, Map<String, RelationDefinition> relDefinitions){
		List<Relation> relations = new ArrayList<>();
		String nextLineStr = sc.nextLine(); Scanner ls = new Scanner(nextLineStr);
        while (ls.hasNext()) {
            String relName = ls.next();
            String v1 = ls.next(); String v2 = ls.next();
            Parameter p1 = var_to_param.get(v1); Parameter p2 = var_to_param.get(v2);
            RelationDefinition rd = relDefinitions.get(relName);
            Relation rel = new Relation(rd, p1, p2);
            relations.add(rel);
        }
        return relations;
	}
	
	public Rule(Scanner sc, Concept root, Map<String, RelationDefinition> relDefinitions) {
		String nextLineStr = sc.nextLine();
        Scanner ls = new Scanner(nextLineStr);
        //read name
		name = ls.next();
		//read variables
		parameters = new ArrayList<>();
        while (ls.hasNext()) {
            String var = ls.next();
            Parameter param = new Parameter(var, root);
            this.parameters.add(param);
            var_to_param.put(var, param);
        }
        //read premise
        premise = readRelations(sc, root, relDefinitions);
        //read conclusion
        conclusion = readRelations(sc, root, relDefinitions);
	}
	
}
