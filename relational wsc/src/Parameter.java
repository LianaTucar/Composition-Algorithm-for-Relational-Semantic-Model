public class Parameter implements Comparable{
	public String name;
	public Concept type;
	public int nr_rels;
	public static int contor=0;

	//??
	public Parameter(String name, Concept type) {
		this.name = name;
		this.type = type;
		this.nr_rels = 0;
	}    
	
	public String toString() {
		return "Param Type " + type.name + " " + name + ",";
	}

	@Override
	public int compareTo(Object arg0) {
		Parameter other = (Parameter)arg0;
		return new Integer(other.nr_rels).compareTo(new Integer(this.nr_rels));
	}
}
