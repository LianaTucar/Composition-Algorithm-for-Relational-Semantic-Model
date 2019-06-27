import java.util.List;

public class RelationDefinition {
	public String name;
	public List<String> properties; //aici sa fie lista de prop..tranz..symetrica, reflexiva...
	
	public RelationDefinition(String name) {
		this.name = name;
	}
	
	
	//aici poate de verificat daca se compara bine...
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o==null)
			return false;
		if (o instanceof RelationDefinition)
			return ((RelationDefinition) o).name.equals(this.name);//??vezi compararea pe stringuri
		return false;
	}
}
