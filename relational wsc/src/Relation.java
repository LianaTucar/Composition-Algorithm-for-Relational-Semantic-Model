
public class Relation {
	public RelationDefinition definition;
	public Parameter x,y;
	
	public Relation(RelationDefinition def, Parameter x, Parameter y) {
		this.definition = def;
		this.x = x;	x.nr_rels += 1;
		this.y = y; y.nr_rels += 1;
	}
	
	public String toString() {
		return "Rel "+definition.toString()+" "+x.toString()+" "+y.toString();
	}
	
	public boolean involves(Parameter p) {
		return (this.x == p) || (this.y==p);
	}
	
	//de verificat ca relatiile se compara cum trebuie daca e nevoie
	
	/*
	@Override
	public int hashCode() {
		return definition.hashCode();
	}
	*/
	
	/*
	@Override
	//????????????????????????????????
	public boolean equals(Object o) {
		if (o==null)
			return false;
		if (o instanceof Relation)
			return ((Relation) o).name.equals(this.name);//??vezi compararea pe stringuri
		return false;
	}*/
}
