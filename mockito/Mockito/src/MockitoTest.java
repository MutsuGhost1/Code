import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;


public class MockitoTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void test01(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>)mock(List.class);

        // Act
        // Action on the object
        mockedList.add("one");
        mockedList.clear();

        // Assert
        // Verify the result
        verify(mockedList).add("one");
        verify(mockedList).clear();

        // Act
        // Action on the object
        mockedList.add("one");
        mockedList.clear();

        // Assert
        // Verify the result.
        // The interactions should be accumulated unless the mock object is reset
        verify(mockedList, times(2)).add("one");
        verify(mockedList, times(2)).clear();
    }

    @SuppressWarnings("unchecked")
    public void test02(){
        // Arrange
        // Create the mock object, it can be a concrete class
        LinkedList<String> mockedList = (LinkedList<String>) mock(LinkedList.class);

        // Act
        // Stubbing it.
        // Make it return what you want
        when(mockedList.get(0)).thenReturn("first");
        // The latter stubbing will override the former one
        when(mockedList.get(0)).thenReturn("firstfirst");
        System.out.println("test02:" + mockedList.get(0));
        System.out.println("test02:" + mockedList.get(0));

        // Assert
        verify(mockedList, times(2)).get(0);
    }

    @SuppressWarnings("unchecked")
    public void test03(){
        // Arrange
        // Create the mock object, it can be a concrete class
        LinkedList<String> mockedList = (LinkedList<String>) mock(LinkedList.class);

        // Act
        // The default return value for object is null.
        // For primitive type, the default value for int is 0.
        //                   , the default value for boolean is false.
        System.out.println("test03:" + mockedList.get(0));

        // Assert
        // times(1) is the default value, if you don't specify it
        verify(mockedList, times(1)).get(0);
    }

    // Argument Matching Using Default Matcher
    @SuppressWarnings("unchecked")
    public void test04(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        when(mockedList.get(anyInt())).thenReturn("stubbing string");
        
        // Act
        for(int i=0 ; i<10; i++){
            System.out.println("["+ i + "]:" + mockedList.get(i));
        }

        // Assert
        verify(mockedList, times(10)).get(anyInt());
    }
    
    @SuppressWarnings("rawtypes")
    private class IsListOfTwoElements extends ArgumentMatcher<List>{
        @Override
        public boolean matches(Object argument) {
            return 2 == ((List)argument).size();
        }
        
    }
    
    @SuppressWarnings("rawtypes")
    private List listOfTwoElements(){
        return argThat(new IsListOfTwoElements());
    }
    
    // Argument Matching Using Custom Matcher
    @SuppressWarnings("unchecked")
    public void test05(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        when(mockedList.addAll(listOfTwoElements())).thenReturn(true);

        // Act
        mockedList.addAll(Arrays.asList("one", "two"));

        // Assert
        verify(mockedList).addAll(listOfTwoElements());
    }

    // verify exact number of invocations / at least x / at most / never
    @SuppressWarnings("unchecked")
    public void test06(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);

        // Act
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("two");
        mockedList.add("three");
        mockedList.add("three");
        mockedList.add("three");

        // Assert
        verify(mockedList).add("one");
        verify(mockedList, times(1)).add("one");
        verify(mockedList, times(2)).add("two");
        verify(mockedList, times(3)).add("three");
        
        verify(mockedList, never()).add("none");
        verify(mockedList, atLeastOnce()).add("one");
        verify(mockedList, atLeast(2)).add("two");
        verify(mockedList, atMost(5)).add("three");
    }

    // verification in order, using single mock
    @SuppressWarnings("unchecked")
    public void test07(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> singleMock = (List<String>) mock(List.class);

        // Act
        singleMock.add("one");
        singleMock.add("two");

        // Assert
        InOrder inOrder = inOrder(singleMock);
        inOrder.verify(singleMock).add("one");
        inOrder.verify(singleMock).add("two");
    }

    // verification in order, using multiple mock
    @SuppressWarnings("unchecked")
    public void test08(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> firstMock = (List<String>) mock(List.class);
        List<String> secondMock = (List<String>) mock(List.class);

        // Act
        firstMock.add("one");
        secondMock.add("two");
        firstMock.add("three");
        secondMock.add("four");

        // Assert
        // Verification in order is flexible - you don't have to
        //   verify all interactions one-by-one but only those you're
        //   interested in testing in order
        InOrder inOrder = inOrder(firstMock, secondMock);
        inOrder.verify(firstMock).add("one");
        inOrder.verify(secondMock).add("two");
        inOrder.verify(firstMock).add("three");
        inOrder.verify(secondMock).add("four");
    }

    // finding redundant invocations
    public void test09(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);

        // Act
        mockedList.add("one");
        mockedList.add("two");

        // Assert
        verify(mockedList).add("one");
        verify(mockedList).add("two"); // mark it to fail
        
        verifyNoMoreInteractions(mockedList);
    }

    // stubbing consecutive calls(iterator-style stubbing)
    @SuppressWarnings("unchecked")
    public void test10(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);

        // Act
        when(mockedList.get(0)).thenReturn("0").thenReturn("1");
        // when(mockedList.get(0)).thenReturn("0","1");

        // Assert
        assertTrue("0".equals(mockedList.get(0)));
        assertTrue("1".equals(mockedList.get(0)));
    }

    private class Util{
        public boolean asyncCall(){
            return false;
        }
    }
    
    // stubbing with callback
    //   it can be used to simulate the response callback
    public void test11(){
        // Arrange
        Util mock = mock(Util.class);

        // Act
        when(mock.asyncCall()).thenAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // do the response call
                System.out.println("test11: answer callback");
                return Boolean.valueOf(true);
            }
        });

        // Assert
        assertTrue(mock.asyncCall());
        verify(mock).asyncCall();
    }

    // doReturn using mock
    public void test12(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);

        // Act
        // When you use mock, it's equal to when(mockedList.get(0)).thenReturn("0")
        doReturn("one").when(mockedList).get(0);
        // when(mockedList.get(0)).thenReturn("0");
        
        // Assert
        assertTrue("one".equals(mockedList.get(0)));
        verify(mockedList).get(0);
    }
    
    // doReturn using spy
    public void test13(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> spyList = (List<String>) spy(new LinkedList<String>());

        // Act
        // When you use spy, it's not equal to when(spyList.get(0)).thenReturn("0")
        doReturn("one").when(spyList).get(0);
        // when(spyList.get(0)).thenReturn("0");
        
        // Assert
        assertTrue("one".equals(spyList.get(0)));
        verify(spyList).get(0);
    }

    // doThrow
    @SuppressWarnings("unchecked")
    public void test14(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        
        // it's equal to
        // doThrow(new RuntimeException()).when(mockedList).toString();
        doThrow(RuntimeException.class).when(mockedList).toString();
        
        try{
            System.out.println(mockedList);
        }catch(RuntimeException e){
            /// pass
        }
    }

    // rewrite case 10 using doAnswer
    @SuppressWarnings("rawtypes")
    public void test15(){
        // Arrange
        Util mock = mock(Util.class);

        // Act
        doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // do the response call
                System.out.println("test11: answer callback");
                return Boolean.valueOf(true);
            }
        }).when(mock).asyncCall();

        // Assert
        assertTrue(mock.asyncCall());
        verify(mock).asyncCall();
    }

    // doNothing
    // it's rarely to use doNothing, the following is an example
    @SuppressWarnings("unchecked")
    public void test16(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        doNothing().doThrow(new RuntimeException()).when(mockedList).clear();

        // Act
        // does nothing the first time:
        mockedList.clear();
        
        try{
            // throws RuntimeException the next time:
            mockedList.clear();
        }catch(RuntimeException e){
            
        }
    }
    
    private class Util2{
        public String A(){
            return "A";
        }

        private String B(){
            return "B";
        }
        
        public String AB(){
            return A() + B();
        }
    }
    
    // doRealCall
    // mock object can also be used to do partial mock
    public void test17(){
        Util2 mockUtil2 = mock(Util2.class);
        assertTrue(null == mockUtil2.A());

        when(mockUtil2.A()).thenCallRealMethod();
        assertTrue("A".equals(mockUtil2.A()));
        
        // mark any one to fail
        when(mockUtil2.B()).thenCallRealMethod();
        when(mockUtil2.AB()).thenCallRealMethod();
        assertTrue("AB".equals(mockUtil2.AB()));
    }
    
    // reset mock
    @SuppressWarnings("unchecked")
    public void test18(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        
        // Act
        mockedList.get(0);
        reset(mockedList);

        // Assert
        verify(mockedList, never()).get(0);
    }

    // capturing arguments for further assertions
    @SuppressWarnings("unchecked")
    public void test19(){
        // Arrange
        // Create the mock object, even it's an interface
        List<String> mockedList = (List<String>) mock(List.class);
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        
        // Act
        mockedList.add("Hello World");
        mockedList.add("two");
        mockedList.add("three");
        
        // Assert
        verify(mockedList, times(3)).add(argument.capture());
        
        System.out.println("test19:" + argument.getAllValues());
        // print the argument of last call
        System.out.println("test19:" + argument.getValue());
    }
    
    // change the default value of unstubbed invocations
    //   use RETURNS_SMART_NULLS to know which null pointer exception
    //   is caused by unstubing
    @SuppressWarnings("rawtypes")
    public void test20(){
        // Arrange
        // Create the mock object, even it's an interface
        List mockedList =  mock(List.class, RETURNS_SMART_NULLS);
        ListIterator iterator = mockedList.listIterator();
        
        try{
            System.out.println("test20:" + iterator.nextIndex());
        }catch(SmartNullPointerException e){
            
        }
    }

    // dependency injection using the constructor
    @Mock private List<String> mList;
    @InjectMocks private SUT mSUT1;
    public void test21(){
        // Arrange
        System.out.println("test21: start");
        MockitoAnnotations.initMocks(this);
        // Act
        // Assert
        assertNotNull(mList);
        assertNotNull(mSUT1);
        assertTrue(mSUT1.equals(new SUT(mList)));
        System.out.println("test21: end");
    }

    // dependency injection using the setter
    @Mock private List<String> mList2;
    @InjectMocks private SUT2 mSUT2;
    public void test22(){
        // Arrange
        System.out.println("test22: start");
        MockitoAnnotations.initMocks(this);
        // Act
        // Assert
        assertNotNull(mList2);
        assertNotNull(mSUT2);
        
        SUT2 sut2 = new SUT2();
        sut2.setList(mList2);
        assertTrue(mSUT2.equals(sut2));
        System.out.println("test22: end");
    }

    // dependency injection using field
    @Mock(name="mList3") private List<String> mLIST3;
    @InjectMocks private SUT3 mSUT3;
    public void test23(){
        // Arrange
        System.out.println("test23: start");
        MockitoAnnotations.initMocks(this);
        // Act
        // Assert
        assertNotNull(mLIST3);
        assertNotNull(mSUT3);
        
        SUT3 sut3 = new SUT3();
        sut3.mList3 = mLIST3;
        assertTrue(mSUT3.equals(sut3));
        System.out.println("test23: end");
    }
    
    // change the default value of unstubbed invocations
    //   use CALLS_REAL_METHODS to simulate partial mock
    public void test24(){
        // Arrange
        // Create the mock object, even it's an interface
        PartialMockObject mock =  mock(PartialMockObject.class, CALLS_REAL_METHODS);
        PartialMockObject spy = spy(new PartialMockObject());
        
        when(mock.m3()).thenReturn("m3 is stubbed");
        System.out.println("test24: mock.m1()=" + mock.m1());
        System.out.println("test24: mock.m2()=" + mock.m2());
        System.out.println("test24: mock.m3()=" + mock.m3());

        when(spy.m3()).thenReturn("m3 is stubbed");
        System.out.println("test24: spy.m1()=" + spy.m1());
        System.out.println("test24: spy.m2()=" + spy.m2());
        System.out.println("test24: spy.m3()=" + spy.m3());
    }

    @SuppressWarnings("unchecked")
    public void test25(){
        // Arrange
        // Create the mock object, even it's an interface
        final List<String> mockedList = (List<String>) mock(List.class);

        // Act
        // When you use mock, it's equal to when(mockedList.get(0)).thenReturn("0")
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2500);
                    // when(mockedList.get(0)).thenReturn("0");
                    doReturn("one").when(mockedList).get(0);
                    assertTrue("one".equals(mockedList.get(0)));
                } catch(InterruptedException e){
                    
                }
            }
        });
        t1.start();

        // Assert
        long start, end, elapsed;
        start = System.currentTimeMillis();
        // if the condition is satisfied, it won't be blocked
        verify(mockedList, timeout(3000)).get(0);
        end = System.currentTimeMillis();
        elapsed = end - start;
        assertTrue(elapsed > 2000);
    }
}
