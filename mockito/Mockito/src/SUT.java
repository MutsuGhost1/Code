import java.util.List;


public class SUT {
    List<String> mList;
    
    public SUT(List<String> list){
        System.out.println("SUT's constructor");
        System.out.println("SUT's mList:" + mList);
        mList = list;
    }
    
    public String get(int index){
        return mList.get(index);
    }
    
    public boolean put(String obj){
        return mList.add(obj);
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("SUT'equals: (SUT)obj).mList =" + ((SUT)obj).mList);
        System.out.println("SUT'equals: mList =" + mList);
        return ((SUT)obj).mList == mList;
    }

}
