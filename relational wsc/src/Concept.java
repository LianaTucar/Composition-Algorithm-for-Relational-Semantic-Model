import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Concept {
	public String name;
	public Concept superConcept;
	public Set<Concept> subConcepts;
	
	public Concept(String name) {
		this.name = name;
		subConcepts = new HashSet<>();
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o==null)
			return false;
		if (o instanceof Concept)
			return ((Concept) o).name.equals(this.name);//??vezi compararea pe stringuri
		return false;
	}
	
	@Override
	public String toString() {
		return this.name; //aici probabil se poate scrie mai mult
	}
}
