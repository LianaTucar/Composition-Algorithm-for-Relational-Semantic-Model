import java.util.ArrayList;
import java.util.List;

public class Obj {
	Concept type;
	List<RelationBetweenObjects> relationsFromObj, relationsToObj;
	
	public Obj(Concept type) {
		this.type = type;
		this.relationsFromObj = new ArrayList<>();
		this.relationsToObj = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Obj type: "+type.toString()+"\n");
		
		sb.append("Relations from obj:\n");
		for (RelationBetweenObjects r:relationsFromObj)
			sb.append(r.toString()+" ");
		
		sb.append("\nRelations to obj:\n");
		for (RelationBetweenObjects r:relationsToObj)
			sb.append(r.toString()+" ");
		
		return sb.toString();
	}
	
	public boolean containsAllRelations(Obj otherObj) {
		//relationsFromObj
		for (RelationBetweenObjects rel1:otherObj.relationsFromObj) {
			boolean g=false;
			for (RelationBetweenObjects rel2:this.relationsFromObj)
				if (rel1.definition.equals(rel2.definition) && (rel1.o2.equals(rel2.o2))) {
					g=true;
					break;
				}
			if (!g)
				return false;
		}
		
		//relationsToObj
		for (RelationBetweenObjects rel1:otherObj.relationsToObj) {
			boolean g=false;
			for (RelationBetweenObjects rel2: this.relationsToObj)
				if (rel1.definition.equals(rel2.definition) && (rel1.o1.equals(rel2.o1))) {
					g=true;
					break;
				}
			if (!g)
				return false;
		}
		
		return true;
	}
}
