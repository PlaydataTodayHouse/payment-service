package com.example.payment.controller;

import com.example.payment.config.JwtService;
import com.example.payment.config.TokenInfo;
import com.example.payment.domain.request.AddCartRequest;
import com.example.payment.domain.request.PurchaseCartRequest;
import com.example.payment.domain.request.RemoveCartRequest;
import com.example.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController
{

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/cart/add")
    public ResponseEntity<String> addProductToCart(@RequestBody AddCartRequest request,
                                                   @RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        paymentService.addProductToCart(userUUID, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("상품이 장바구니에 담겼습니다");
    }

    @DeleteMapping("/cart/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestBody RemoveCartRequest request,
                                                        @RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        paymentService.removeProductFromCart(userUUID, request.getProductId());
        return ResponseEntity.ok("상품이 장바구니로부터 제거되었습니다");
    }


    @PostMapping("/cart/purchase")
    public ResponseEntity<String> purchaseProductFromcCart(@RequestBody PurchaseCartRequest request,
                                                           @RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        paymentService.purchaseCart(userUUID, request.getName(), request.getAddress(), request.getPhoneNumber());
        return ResponseEntity.ok("상품 구매 성공");
    }

    @GetMapping("/balance")
    public ResponseEntity<Integer> getWalletBalance(@RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        int balance = paymentService.getWalletBalance(userUUID);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addWalletBalance(@RequestBody int amount,
                                                   @RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        paymentService.addWalletBalance(userUUID, amount);
        return ResponseEntity.ok("잔고 추가 성공");
    }

    @PostMapping("/subtract")
    public ResponseEntity<String> subtractWalletBalance(@RequestBody int amount,
                                                        @RequestHeader("Authorization") String token) {
        TokenInfo tokenInfo = jwtService.parseAccessToken(token.replace("Bearer ", ""));
        UUID userUUID = tokenInfo.getId();

        paymentService.subtractWalletBalance(userUUID, amount);
        return ResponseEntity.ok("잔고 사용");
    }
}
