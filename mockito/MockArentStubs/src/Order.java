
public class Order {
    private MailService mMailer;
    private String mName;
    private int mNum;
    
    public Order(String name, int num){
        mName = name;
        mNum = num;
    }
    
    public void fill(Warehouse house){
        if(house.hasInventory(mName, mNum)){
            mNum = 0;
        }else{
            if(null != mMailer)
                mMailer.send(mName + " is not enough, only has " + mNum);
        }
    }
    
    public void setMailer(MailService mailer){
        mMailer = mailer;
    }
    
    public boolean isFilled(){
        return 0 == mNum;
    }
}
