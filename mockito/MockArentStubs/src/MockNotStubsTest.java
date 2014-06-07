import junit.framework.TestCase;

import static org.mockito.Mockito.*;

public class MockNotStubsTest extends TestCase {
    private static String TALISKER = "talisker" ;
    
    public void testOrderSendsMailIfUnfilledUsingStub(){
        // Arrange
        Order order = new Order(TALISKER, 51);
        MailServiceStub mailer = new MailServiceStub();
        Warehouse warehouse = new Warehouse();
        warehouse.add(TALISKER, 50);
        // Act
        order.setMailer(mailer);
        order.fill(warehouse);
        // Assert
        // It may add a new method to do state verfication
        assertEquals(1, mailer.numberSent());
    }

    public void testOrderSendsMailIfUnfilledUsingMock(){
        // Arrange
        Order order = new Order(TALISKER, 51);
        MailService mailer = mock(MailService.class);
        Warehouse warehouse = mock(Warehouse.class);        
        when(warehouse.hasInventory(anyString(), anyInt())).thenReturn(false);
        // Act
        order.setMailer(mailer);
        order.fill(warehouse);
        // Assert
        verify(warehouse).hasInventory(anyString(), anyInt());
        verify(mailer).send(anyString());
    }

    public void testOrderIsFilledIfEnoughInWareHouse(){
        // Arrange
        Order order = new Order(TALISKER, 50);
        Warehouse warehouse = new Warehouse();
        warehouse.add(TALISKER, 50);
        // Act
        order.fill(warehouse);
        // Assert
        assertTrue(order.isFilled());
        assertTrue(0 == warehouse.getInventory(TALISKER));
    }

    public void testOrderDoesNotRemoveIfNotEnoughInWareHouse(){
        // Arrange
        Order order = new Order(TALISKER, 51);
        Warehouse warehouse = new Warehouse();
        warehouse.add(TALISKER, 50);
        // Act
        order.fill(warehouse);
        // Assert
        assertFalse(order.isFilled());
        assertTrue(50 == warehouse.getInventory(TALISKER));
    }

    public void testOrderIsFilledIfEnoughInWareHouseUsingMock(){
        // Arrange
        Order order = new Order(TALISKER, 50);
        Warehouse warehouse = mock(Warehouse.class);
        when(warehouse.hasInventory(TALISKER, 50)).thenReturn(true);
        // Act
        order.fill(warehouse);
        // Assert
        // User still can verify the SUT, 
        // and it also can check the call flow ... (behavior verification)
        assertTrue(order.isFilled());
        verify(warehouse).hasInventory(TALISKER, 50);
    }


    public void testOrderDoesNotRemoveIfNotEnoughInWareHouseUsingMock(){
        // Arrange
        Order order = new Order(TALISKER, 51);
        Warehouse warehouse = mock(Warehouse.class);
        when(warehouse.hasInventory(TALISKER, 51)).thenReturn(false);
        // Act
        order.fill(warehouse);
        // Assert
        // User still can verify the SUT, 
        // and it also can check the call flow ... (behavior verification)
        assertFalse(order.isFilled());
        verify(warehouse).hasInventory(TALISKER, 51);
    }
}
