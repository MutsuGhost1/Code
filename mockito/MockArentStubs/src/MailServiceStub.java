import java.util.ArrayList;
import java.util.List;


public class MailServiceStub implements MailService {
    private List<Message> mMessages = new ArrayList<Message>();

    @Override
    public void send(String str) {
        mMessages.add(new Message(str));
    }

    public int numberSent(){
        return mMessages.size();
    }

}
