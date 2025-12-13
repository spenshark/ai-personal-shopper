package spring.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.ai.global.base.ApiResponse;
import spring.ai.global.exception.code.status.SuccessStatus;
import spring.ai.service.ProductService;

@Tag(name = "Product Controller", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/load")
    @Operation(summary = "CSV 상품 데이터 Vector DB 적재 API", description = "resources/products.csv 파일을 읽어 Vector DB에 저장합니다.")
    public ApiResponse<String> loadProducts() {
        productService.loadProducts();
        return ApiResponse.onSuccess(null, "데이터가 vectorDB에 성공적으로 저장되었습니다.");
    }
}
