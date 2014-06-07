import java.util.List;


public class SUT3 {
    List<String> mList3;
    
    public SUT3(){
        System.out.println("SUT3's constructor");
    }
    
    public String get(int index){
        return mList3.get(index);
    }
    
    public boolean put(String obj){
        return mList3.add(obj);
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("SUT3's equals: (SUT3)obj).mList3 =" + ((SUT3)obj).mList3);
        System.out.println("SUT3's equals: mList3 =" + mList3);
        return ((SUT3)obj).mList3 == mList3;
    }
}
