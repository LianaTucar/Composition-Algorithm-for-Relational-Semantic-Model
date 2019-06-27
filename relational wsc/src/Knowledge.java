import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.security.provider.certpath.ResponderId.Type;

public class Knowledge {
	public static List<RelationBetweenObjects> relationsBetweenObjects = new ArrayList<>(); //relatiile is peste obiecte
	private static List<Obj> objects = new ArrayList<>();
	public static Map<String, List<Obj>> objOfType = new HashMap<>();
	private static Ontology ontology;
	
	private Knowledge() {	
	}
	
	public static void init(Ontology ont) {
		ontology = ont;
		for (Concept c:ont.concepts.values())
			objOfType.put(c.name, new ArrayList<>());		
	}
	
	public static List<Obj> getObjects() {
		return objects;
	}

	public static boolean relationHolds(Relation relation, Obj o1, Obj o2) {
		for (RelationBetweenObjects relBObj:relationsBetweenObjects)
			if (relBObj.definition==relation.definition)
				if ((relBObj.o1 == o1) && (relBObj.o2==o2))
					return true;
		return false;
	}
	
	public static void addObject(Obj object) {
		objects.add(object);
		Concept type =  ontology.concepts.get(object.type.name);
		boolean afis=false;
		while (type!=null) {
			//System.out.println("While");
			if (afis)
				System.out.println("debug "+type.name);
			objOfType.get(type.name).add(object);
			type=type.superConcept;
		}
	}
	
	public static void addObjects(List<Obj> objectsToAdd) {
		for (Obj o:objectsToAdd)
			addObject(o);
		//objects.addAll(objectsToAdd); //aici cred ca ar trebui validat ca obiectele sunt "noi"
	}
	
	
	private static void addRelation(RelationBetweenObjects rel) {
		relationsBetweenObjects.add(rel);
		rel.o1.relationsFromObj.add(rel);
		rel.o2.relationsToObj.add(rel);
	}
	
	private static void addRelations(List<RelationBetweenObjects> relsBetweenObjects) {
		for (RelationBetweenObjects rel: relsBetweenObjects) {
			addRelation(rel);
		}
	}
	
	public static void addProvided(Query query) {
		Map<Parameter, Obj> paramToObj = new HashMap<>();
		for (Parameter param:query.provided) {
			Obj obj = new Obj(param.type);
			paramToObj.put(param, obj);
			addObject(obj);
		}
		
		for (Relation rel:query.providedRelations) {
			Obj o1 = paramToObj.get(rel.x);
			Obj o2 = paramToObj.get(rel.y);
			RelationBetweenObjects objRel = new RelationBetweenObjects(rel.definition, o1, o2);
			//relationsBetweenObjects.add(objRel); ???????
			addRelation(objRel);
		}
	}
	
	public static void addServiceOutputs(ServiceCall servCall) {
		List<Obj> objectsToAdd = new ArrayList<>();
		for (Parameter outParam:servCall.service.outputParams) {
			Obj o = new Obj(outParam.type);
			objectsToAdd.add(o);
			servCall.paramToObjCoresp.put(outParam, o);
		}
		addObjects(objectsToAdd);
		
		List<RelationBetweenObjects> rels = new ArrayList<>();
		for (Relation rel:servCall.service.postconditions) {
			RelationBetweenObjects relBO = new RelationBetweenObjects(rel.definition, servCall.paramToObjCoresp.get(rel.x), servCall.paramToObjCoresp.get(rel.y));
			rels.add(relBO);
		}
		addRelations(rels);
	}
	
	
	public static boolean existsSimilarOrBetterObject(Obj newObj) {
		for (Obj knownObject:objects) {
			if (knownObject.type.equals(newObj.type)) //aici de apelat cu isA......
				if (knownObject.containsAllRelations(newObj))
					return true;
		}
		return false;
	}
	
	/*public boolean providesUsefulInformation(ServiceCall servCall) {
		for (Parameter param:servCall.service.outputParams){
			Obj obtainedObj = servCall.paramToObjCoresp.get(param);
			if (!existsSimilarOrBetterObject(obtainedObj)) {
				return true;
			}
		}
		return false;
	}*/
	
	public static int amountOfUsefulInformation(ServiceCall servCall) {
		TimeTracker.startTimer(Part.amountOfUsefulInformation);
		int nrUsefulOuts = 0;
		for (Parameter param:servCall.service.outputParams){
			Obj obtainedObj = servCall.paramToObjCoresp.get(param);
			if (!existsSimilarOrBetterObject(obtainedObj)) {
				nrUsefulOuts+=1;
			}
		}
		TimeTracker.stopTimer(Part.amountOfUsefulInformation);
		return nrUsefulOuts;
	}
}
