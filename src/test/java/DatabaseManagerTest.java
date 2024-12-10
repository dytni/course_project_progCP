import com.hatsukha.nikolai.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseManagerTest {
    private DatabaseManager databaseManager;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private WarehouseRepository warehouseRepository;
    private ProductOperationRepository productOperationRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        warehouseRepository = mock(WarehouseRepository.class);
        productOperationRepository = mock(ProductOperationRepository.class);

        databaseManager = new DatabaseManager(userRepository, productRepository, warehouseRepository, productOperationRepository);
    }

    @Test
    void testGetUserId() {
        when(userRepository.getUserId("admin")).thenReturn(1);

        int userId = databaseManager.getUserId("admin");
        assertEquals(1, userId);

        verify(userRepository, times(1)).getUserId("admin");
    }

    @Test
    void testRegisterUser() {
        when(userRepository.registerUser("testUser", "hashedPassword", "USER")).thenReturn(true);

        boolean result = databaseManager.registerUser("testUser", "hashedPassword", "USER");
        assertTrue(result);

        verify(userRepository, times(1)).registerUser("testUser", "hashedPassword", "USER");
    }

    @Test
    void testAddProduct() {
        when(productRepository.addProduct("Phone", "Smartphone", "Electronics", 0.5, 0.1)).thenReturn(true);

        boolean result = databaseManager.addProduct("Phone", "Smartphone", "Electronics", 0.5, 0.1);
        assertTrue(result);

        verify(productRepository, times(1)).addProduct("Phone", "Smartphone", "Electronics", 0.5, 0.1);
    }

    @Test
    void testGetAllProducts() {
        List<String> mockProducts = Arrays.asList("1,Phone,Smartphone,Electronics,0.5,0.1", "2,Laptop,Electronics,2.5,1.0");
        when(productRepository.getAllProducts()).thenReturn(mockProducts);

        List<String> products = databaseManager.getAllProducts();
        assertEquals(2, products.size());
        assertEquals("1,Phone,Smartphone,Electronics,0.5,0.1", products.get(0));

        verify(productRepository, times(1)).getAllProducts();
    }

    @Test
    void testGetOrdersByWarehouse() {
        Map<String, Integer> mockData = Map.of("Warehouse1", 10, "Warehouse2", 15);
        when(productOperationRepository.getOrdersByWarehouse()).thenReturn(mockData);

        Map<String, Integer> ordersByWarehouse = databaseManager.getOrdersByWarehouse();
        assertEquals(2, ordersByWarehouse.size());
        assertEquals(10, ordersByWarehouse.get("Warehouse1"));
        assertEquals(15, ordersByWarehouse.get("Warehouse2"));

        verify(productOperationRepository, times(1)).getOrdersByWarehouse();
    }

    @Test
    void testAddOperation() {
        when(productOperationRepository.addOperation(1, 2, 3, "приём", 50)).thenReturn(true);

        boolean result = databaseManager.addOperation(1, 2, 3, "приём", 50);
        assertTrue(result);

        verify(productOperationRepository, times(1)).addOperation(1, 2, 3, "приём", 50);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.deleteProduct(1)).thenReturn(true);

        boolean result = databaseManager.deleteProduct(1);
        assertTrue(result);

        verify(productRepository, times(1)).deleteProduct(1);
    }

    @Test
    void testGetAllOperationsByUser() {
        List<String> mockOperations = Arrays.asList("1,1,2,приём,50", "2,1,3,списание,20");
        when(productOperationRepository.getOperationsByUser(1)).thenReturn(mockOperations);

        List<String> operations = databaseManager.getAllOperationsByUser(1);
        assertEquals(2, operations.size());
        assertEquals("1,1,2,приём,50", operations.get(0));

        verify(productOperationRepository, times(1)).getOperationsByUser(1);
    }
}
