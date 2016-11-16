package cse.its.helper;
/**
 * type of transport
 * @author Vo tinh
 *
 */
public enum TypeOfTransport {
	CAR(0), MOTOR(1), BUS(2), TRUCK(3);
	final int type;
	TypeOfTransport(int val){
        this.type = val;
    }

    private int getValue(){
        return this.type;
    }
    
    public static TypeOfTransport getTransportType(int val) {
    	TypeOfTransport result = null;
        for (TypeOfTransport tmp : TypeOfTransport.values()) {
            if(tmp.getValue() == val)  {
            	result = tmp;
                break;
            }
        }
        return result;
    }   
}
