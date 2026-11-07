package com.example.testing;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import com.example.models.Checkout;
import com.example.models.CheckoutData;
import com.example.page.CheckoutPage;
import com.example.page.LoginPage;
import com.example.page.ProductDetail;

@Test(groups = {"regression"})
public class CustomerFlowTesting extends BaseFlowTest {

     // --- KHAI BÁO CÁC BIẾN TOÀN CỤC ĐỂ LƯU TRẠNG THÁI TRANG ---
    private ProductDetail productDetail;
    private CheckoutPage checkoutPage;
    private int initialCount;
    // all @test -> belong to -> beforeclass
     @Test(priority = 1)
    public void testLoginAndAddToCart() {
        System.out.println("Bước 1: Đăng nhập và thêm sản phẩm vào giỏ hàng");
        openUrl("/login");
        
        // Thực hiện chuỗi login -> thêm vào giỏ và hứng kết quả vào biến toàn cục productDetail
        productDetail = new LoginPage(driver)
                .typeLogin("admin@example.com", "Admin@123")
                .clickProductDetail()
                .clickChooseSize()
                .clickAddToCart();
    }

    @Test(dependsOnMethods = {"testLoginAndAddToCart"})
    public void testVerifyCartBadgeIncrement() {
        System.out.println("Bước 2: Kiểm tra số lượng item trong giỏ hàng tăng lên");

        // 1. Lấy số lượng badge ban đầu
        initialCount = productDetail.getCartBadgeCount();

        // 2. Click thêm lần nữa và verify trực tiếp (tận dụng Fluent assertion nếu có)
        productDetail.clickAddToCart()
                     .verifyBadgeIncremented(initialCount);
    }
    @Test(dependsOnMethods = {"testVerifyCartBadgeIncrement"})
    public void testNavigateToCheckout() {
        System.out.println("Bước 3: Đi tới trang Checkout và lấy thông tin");

        // Chuyển tiếp từ trang Product sang trang Checkout, lưu vào biến checkoutPage
        checkoutPage = productDetail.clickCart()
                                    .clickCheckout();
        
        CheckoutData checkout = checkoutPage.getInfoProduct();
        // Bạn có thể dùng object 'checkout' này để quyết định assert gì đó nếu cần
    }

    // Nhận dữ liệu 'data' (Checkout) từ DataProvider của TestNG truyền vào
    @Test(dependsOnMethods = {"testNavigateToCheckout"}, dataProvider = "checkoutDataProvider")
    public void testSubmitOrderAndVerifyError(Checkout data) {
        System.out.println("Bước 4: Điền thông tin đặt hàng và verify lỗi ở Tab khác");

        // Sử dụng biến checkoutPage từ bước trước để điền thông tin và kiểm tra lỗi
        boolean order_check_othertab = checkoutPage.TypeInfomation(
                data.getFirstName(), data.getLastName(), data.getEmail(), data.getAddress(),
                data.getCity(), data.getPostalCode(), data.getCountry(), data.getPaymentMethod(),
                data.getErrorMessage()
        ).clickOrderPage().checkOtherTabError();

        assertTrue(order_check_othertab, " lol wrong");
    }


    
}
