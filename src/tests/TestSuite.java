import models.KitchenModelTest;
import models.MenuItemModelTest;
import models.OrderItemModelTest;
import models.OrderModelTest;
import models.PostOrderModelTest;
import models.TableModelTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import services.AuthenticationServicesTest;
import services.MenuServicesTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    MenuServicesTest.class,
    AuthenticationServicesTest.class,
    KitchenModelTest.class,
    MenuItemModelTest.class,
    OrderItemModelTest.class,
    OrderModelTest.class,
    PostOrderModelTest.class,
    TableModelTest.class
})

public class TestSuite {
}
