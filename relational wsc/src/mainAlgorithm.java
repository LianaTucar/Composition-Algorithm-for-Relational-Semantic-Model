import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class mainAlgorithm {
	
	static String dummyTest = "teste/dummy test/";
	static String testeTable1 = "teste/din articol ordonate/table 1 - generate/";
	static String dinArticolT1 = testeTable1+"test1 - cu ont sch/";
	static String dinArticolT2 = testeTable1+"test2 - cu ont sch/";
	static String dinArticolT3 = testeTable1+"test3 - cu ont sch/";
	static String dinArticolT4 = testeTable1+"test4 2/"; //??mhm...oare trebuie reguli???
	
	static String dinArticolV2T1 = "teste/teste din articol v2/1/";
	static String concurs = "teste/transformare_teste_spre_format_nou/";	
	static String concursT1 = "teste/transformare_teste_spre_format_nou/07/";
	
	static String testSpecial = "teste/test facut de mana sa pice al lui paul/";
	static String testSpecialInfinit = "teste/test facut de mana verificare infinit/";
	static String testReguli = "teste/test facut de mana cu reguli2/";
	
	static String test = "";
	
	private static void afisare_stats(boolean solved, List<ServiceCall> composition, double execTime) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter("stats.txt"));
	    String str = "";
	    int nrRules = 0, nrServs = 0;
	    // count services and rules
	    for (ServiceCall servCall:composition)
	    	if (servCall.service.name.startsWith("Rule"))
	    		nrRules++;
	    	else
	    		nrServs++;
	    if (solved) {
	    	str += "Solved.\n";
	    	str += "matched services " + nrServs + "\n";
	    	str += "matched rules " + nrRules + "\n";
	    	str += "execution time in seconds: " + execTime / 1000.0 +"\n";
	    	for (ServiceCall servCall:composition)
	    		str+=servCall.service.name+" ";
	    	str+="\n";
	    }else
	    	str+="Not Solved\n";
	    writer.write(str);
	    writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		TimeTracker.init();
		Ontology ontology = new Ontology(test+"ontology.txt");
		Repository repository = new Repository(test+"repository.txt", ontology);
		repository.addRules(ontology.rules);
		//System.out.println(repository.toString());
		Query query = new Query(test+"query.txt", ontology);
		
		System.out.println("inainte de knowledge init");
		Knowledge.init(ontology);
		Composer c = new Composer(ontology, repository, query);
		
		TimeTracker.startTimer(Part.all);
		List<ServiceCall> comp = c.solve();
		TimeTracker.stopTimer(Part.all);
		
		//System.out.println(comp);
		//for (ServiceCall sc:comp)
		//	System.out.println(sc.service.name);
		//System.out.println("composition size " + comp.size() + "\n");
	
		boolean solved = c.canAnswerQuery();
		if (solved)
			System.out.println("Solved\n");
		else
			System.out.println("Not solved\n");
		System.out.println(TimeTracker.printValues());
		
		//System.out.println(repository.detCompConexeInput());
		//System.out.println(repository.toString());
		//System.out.println(query.toString());
		afisare_stats(solved, comp, TimeTracker.getTotalTime());
		
	}
}
