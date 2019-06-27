import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;


public class Composer {
	
	private boolean afisat = false; //pt debug
	public Ontology ontology;
	public Repository repository;
	public Query query;
	
	List<ServiceCall> callableServices;
	
	Map<Parameter, Obj> corespondent; //sa fac asta tot un serviceCall?? sau nu??/ oricum sa fac destul de clar diferenta intre ala in constructie si ala final..mhm..si solutiile finale sa le bag intr-o lista..sau mai vedem
		
	List<ServiceCall> composition = new ArrayList<>();
	
	ServiceCall servCall;
	List<ServiceCall> possibleCallsOfCurrentServ;
	
	public Composer(Ontology ontology, Repository repository, Query query) {
		this.ontology = ontology;
		this.repository = repository;
		this.query = query;
		
		callableServices = new ArrayList<>();
		//knowledge = new Knowledge();
		Knowledge.addProvided(query);	
	}
	
	boolean match(int poz, List<Parameter> params, List<Relation> relations, Obj obj) {
		Parameter p = params.get(poz);
		if (!ontology.isA(obj.type, p.type)) { // !p.type.equals(obj.type)) //aici cu subconcept //aici probabil as putea scoate if-ul asta(poate se misca mai rpd... !!! <<<<<<<----------------
			System.out.print(" not isA ");
			return false;
		}
		for (Relation rel:relations)
			if(rel.involves(p)) {
				if ((rel.x==p) && (corespondent.containsKey(rel.y)))
					if (!Knowledge.relationHolds(rel, obj, corespondent.get(rel.y)))
						return false;
				if ((rel.y==p) && (corespondent.containsKey(rel.x)))
					if (!Knowledge.relationHolds(rel, corespondent.get(rel.x), obj))
						return false;
			}		
		return true;
	}
	
	void genAll(int poz, List<Parameter> params, List<Relation> relations) {
		//System.out.println("gen all for serv "+servCall.service.name+" poz " + poz + " / " + params.size()+ "\n");
		if (poz == params.size()) {
			possibleCallsOfCurrentServ.add(new ServiceCall(servCall.service, corespondent));
			//System.out.println("found possible call");
			return;
		}
		
		Concept paramType = params.get(poz).type;
		for (Obj o: Knowledge.objOfType.get(paramType.name)) {
		//for (Obj o: Knowledge.getObjects())
			if (match(poz, params, relations, o)) {
				corespondent.put(params.get(poz), o);
				genAll(poz+1, params, relations);
				corespondent.remove(params.get(poz));
				if (possibleCallsOfCurrentServ.size()>10) //?? sa vad ce sa fac aici !!!!!!!!!!!!!!!!!!!!!!!! bulaneala
					break;
			}
		}
	}
	
	boolean genFirst(int poz, List<Parameter> params, List<Relation> relations) {
		if (poz == params.size()) {
			possibleCallsOfCurrentServ.add(new ServiceCall(servCall.service, corespondent));
			return true;
		}
		
		boolean found = false;
		Concept paramType = params.get(poz).type;
		//System.out.println("\n"+Knowledge.objOfType.get(paramType.name).size() + " nr obj\n");
		//System.out.println("type " + paramType.toString());
		
		/* PT debug
		 * if (!afisat)
			for (Obj o:Knowledge.getObjects())
				if (ontology.isA(o.type, paramType))
					if (!Knowledge.objOfType.get(paramType.name).contains(o)) {
						System.out.println("TZEAPA!! obj: "+o.toString()+" pt param "+paramType);
						afisat = true;
					}
		*/
		for (Obj o: Knowledge.objOfType.get(paramType.name))
			//for (Obj o: Knowledge.getObjects())
				if (match(poz, params, relations, o)) {
					corespondent.put(params.get(poz), o);
					if (genFirst(poz+1, params, relations))
						return true;
					corespondent.remove(params.get(poz));
				}
		return false;
	}

	
		
	List<ServiceCall> searchForPossibleCalls(Service serv) {
		servCall = new ServiceCall(serv);
		possibleCallsOfCurrentServ = new ArrayList<>();
		corespondent = new HashMap<>();
		TimeTracker.startTimer(Part.genAll);
		genAll(0, serv.inputParams, serv.preconditions); //se schimba servCall
		TimeTracker.stopTimer(Part.genAll);
		return possibleCallsOfCurrentServ;
	}
	
	boolean canCall(Service serv) {
		TimeTracker.startTimer(Part.canCall);
		servCall = new ServiceCall(serv);
		possibleCallsOfCurrentServ = new ArrayList<>();
		corespondent = new HashMap<>();
		boolean ans = genFirst(0, serv.inputParams, serv.preconditions); //se schimba servCall
		TimeTracker.stopTimer(Part.canCall);
		return ans;
	}
	
	void call(ServiceCall servCall) {
		TimeTracker.startTimer(Part.call);
		Knowledge.addServiceOutputs(servCall);
		composition.add(servCall);
		TimeTracker.stopTimer(Part.call);
	}
	
	void parcurgeCompConexa(Obj o, Service serviceToAdd, Map<Obj, Parameter> paramUsedForObject) {
		//adaug elem asta nou la serviciu
		Parameter.contor++;
		Parameter p = new Parameter(o.toString()+Parameter.contor, o.type);
		paramUsedForObject.put(o, p);
		serviceToAdd.inputParams.add(p);
		
		//parcurge relationsFromObj
		for (RelationBetweenObjects rel:o.relationsFromObj) {
			Obj otherInRel = rel.o2;
			if (!paramUsedForObject.containsKey(otherInRel))
				parcurgeCompConexa(otherInRel, serviceToAdd, paramUsedForObject);
			Relation r = new Relation(rel.definition, p, paramUsedForObject.get(otherInRel));
			serviceToAdd.preconditions.add(r);
		}
		
		//parcurerelationsToObj
		for (RelationBetweenObjects rel:o.relationsToObj) {
			Obj otherInRel = rel.o1;
			if (!paramUsedForObject.containsKey(otherInRel))
				parcurgeCompConexa(otherInRel, serviceToAdd, paramUsedForObject);
			Relation r = new Relation(rel.definition, paramUsedForObject.get(otherInRel), p);
			serviceToAdd.preconditions.add(r);
		}
			
	}
	
	void addObjsAndRelsFromCC(ServiceCall servCall, Service serviceToAdd, Map<Obj, Parameter> paramUsedForObject) {
		List<Parameter> params = new ArrayList<>(servCall.service.outputParams);  //astea toate is obiecte noi create in addCorespondentForOutput()
		params.addAll(servCall.service.inputParams); //doar de la astia obtin relatii ..?cred?
		for (Parameter p:params) {
			Obj objUsedInCallForP = servCall.paramToObjCoresp.get(p);
			if (!paramUsedForObject.containsKey(objUsedInCallForP))
				parcurgeCompConexa(objUsedInCallForP, serviceToAdd, paramUsedForObject);
		}
		
	}
	
	void addRelsFromServicePostconditions(ServiceCall servCall, Service serviceToAdd, Map<Obj, Parameter> paramUsedForObject) {
		for (Relation rel:servCall.service.postconditions) {
			Obj o1 = servCall.paramToObjCoresp.get(rel.x);
			Parameter p1InNewServ = paramUsedForObject.get(o1);
			Obj o2 = servCall.paramToObjCoresp.get(rel.y);
			Parameter p2InNewServ = paramUsedForObject.get(o2);
			Relation r = new Relation(rel.definition, p1InNewServ, p2InNewServ);
			serviceToAdd.preconditions.add(r);
		}
	}
	
	boolean providesUsefulInformation(ServiceCall servCall) {
		//System.out.println("checking provides for serv" + servCall.service.name);
		TimeTracker.startTimer(Part.providesUsefulInformation);
		
		Service fictiveServ = new Service();
		Map<Obj, Parameter> paramUsedForObject = new HashMap<>();
		
		addObjsAndRelsFromCC(servCall, fictiveServ, paramUsedForObject);
		addRelsFromServicePostconditions(servCall, fictiveServ, paramUsedForObject);
		//List<Relation> rels = new ArrayList<>(servCall.service.postconditions); rels.addAll(servCall.service.preconditions);
		boolean ans = !canCall(fictiveServ);
		TimeTracker.stopTimer(Part.providesUsefulInformation);
		//System.out.println("FICTIV "+fictiveServ.toString());
		return ans;
	}
	
	boolean constructionPhase() {
		boolean added = false;
		
		for (Service serv:repository.services) {
			System.out.println("in construction service "+serv.name);
			List<ServiceCall> possibleCalls = searchForPossibleCalls(serv);
			/*for (ServiceCall servCall:possibleCalls) { //asta e pt sortare..dar nu pare sa imbunatateasca (cel putin nu pe testul 6)
				servCall.addCorespondentForOutput();
				servCall.score = Knowledge.amountOfUsefulInformation(servCall); // sa vad daca asta mai ajuta la ceva...poate trebuie facuta inainte de for??????!!!!!!!!!!!
			}
			Collections.sort(possibleCalls);
			*/
			
			//if (possibleCalls.size()>10)
			//	 possibleCalls = possibleCalls.subList(0, 9);
			//System.out.println("analiza serv " + serv.name + "\n");
			/*if (possibleCalls.size() == 0)
				System.out.println("impossible call serv " + serv.name + "\n");
			*/
			for (ServiceCall servCall:possibleCalls) {
				
				//servCall.score = servCall.service.outputParams.size(); de testat si asa !!!!!!!!!!!!!!!!!
				
				//servCall.score = Knowledge.amountOfUsefulInformation(servCall); // sa vad daca asta mai ajuta la ceva...poate trebuie facuta inainte de for??????!!!!!!!!!!!!
				servCall.addCorespondentForOutput();
				if (providesUsefulInformation(servCall)){
					System.out.println("call serv "+servCall.service.name);
					call(servCall);
					System.out.println("finished call");
					added=true;
					break;
				}else {
					System.out.println("useless call serv "+servCall.service.name);
				}
			}
		}
		
		return added;
	}
	
	/*void addCalls(PriorityQueue<ServiceCall> calls) {
		for (Service serv:repository.services) {
			List<ServiceCall> possibleCalls = searchForPossibleCalls(serv);
			for (ServiceCall servCall:possibleCalls) {
				servCall.addCorespondentForOutput();
				//servCall.score = servCall.service.outputParams.size(); de testat si asa !!!!!!!!!!!!!!!!!
				servCall.score = Knowledge.amountOfUsefulInformation(servCall); // sa vad daca asta mai ajuta la ceva...poate trebuie facuta inainte de for??????!!!!!!!!!!!!
				calls.add(servCall);
			}
		}
	}
	
	boolean constructionPhaseWithPriority() {
		boolean added = false;
		PriorityQueue<ServiceCall> calls = new PriorityQueue<>();
		addCalls(calls);
		
		
		
		
		return added;
	}
	*/
	
	boolean canAnswerQuery() {
		TimeTracker.startTimer(Part.canAswerQuery);
		List<Parameter> inps = new ArrayList<>(query.required); inps.addAll(query.provided);
		List<Relation> rels = new ArrayList<>(query.requiredRelations); rels.addAll(query.providedRelations);
		
		Service queryServ = new Service("query",inps, rels);
		//System.out.println("query serv: "+queryServ.toString());
		boolean canAnswerQuery = canCall(queryServ);
		TimeTracker.stopTimer(Part.canAswerQuery);
		return canAnswerQuery;
	}
	
	public List<ServiceCall> solve() {
		boolean sch = true;
		while (!canAnswerQuery()) {
			sch=false;
			sch = constructionPhase();
			System.out.println("in while mare");
			if (!sch)
				break;
		}
		return composition;
	}
	
}
