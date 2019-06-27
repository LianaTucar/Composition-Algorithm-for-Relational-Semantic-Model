import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCall implements Comparable<ServiceCall>{
	public Service service;
	public Map<Parameter, Obj> paramToObjCoresp;
	public int score;
	
	public ServiceCall(Service serv, Map<Parameter, Obj> paramToObjCoresp) {
		this.service = serv;
		this.paramToObjCoresp = new HashMap<>(paramToObjCoresp);
	}
	
	public ServiceCall() {
		this.paramToObjCoresp = new HashMap<>();
	}
	
	public ServiceCall(Service serv) {
		this.service = serv;
		this.paramToObjCoresp = new HashMap<>();
	}
	
	public ServiceCall(ServiceCall servCall) {
		this.service = servCall.service;
		this.paramToObjCoresp = new HashMap<>(servCall.paramToObjCoresp);
	}
	
	public void addCorespondentForOutput() {
		TimeTracker.startTimer(Part.addCorespondentForOutput);
		for (Parameter outParam:this.service.outputParams) {
			Obj o = new Obj(outParam.type);
			this.paramToObjCoresp.put(outParam, o);
		}
		TimeTracker.stopTimer(Part.addCorespondentForOutput);
	}
	
	
	@Override
	public int compareTo(ServiceCall otherServCall) {
		if (this.score < otherServCall.score)
			return -1;
		if (this.score == otherServCall.score)
			return 0;
		return 1;
	}
	
}
