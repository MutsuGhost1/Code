import java.util.List;


public class SUT2 {
    List<String> mList2;
    
    public SUT2(){
        System.out.println("SUT2's constructor");
    }
    
    public String get(int index){
        return mList2.get(index);
    }
    
    public boolean put(String obj){
        return mList2.add(obj);
    }
    
    public void setList(List<String> list){
        System.out.println("SUT2's setList");
        mList2 = list;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("SUT2's equals: (SUT2)obj).mList2 =" + ((SUT2)obj).mList2);
        System.out.println("SUT2's equals: mList2 =" + mList2);
        return ((SUT2)obj).mList2 == mList2;
    }
}
