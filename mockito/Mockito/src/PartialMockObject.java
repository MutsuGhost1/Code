
public class PartialMockObject {
    public String m1(){
        return "PartialMockObject:" + "m1";
    }
    
    public String m2(){
        return p() + "m3";
    }
    
    public String m3(){
        return pp() + "m3";
    }
    
    public String p(){
        return "PartialMockObject:";
    }
    
    private String pp(){
        return p();
    }
}
