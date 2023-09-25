package com.example.payment.service;

import com.example.payment.client.api.ProductClient;
import com.example.payment.client.api.UserClient;
import com.example.payment.config.auth.TokenInfo;
import com.example.payment.config.kafka.OrderKafkaData;
import com.example.payment.config.kafka.OrderProducer;
import com.example.payment.domain.dto.Product;
import com.example.payment.domain.entity.Cart;
import com.example.payment.domain.entity.CartItem;
import com.example.payment.domain.entity.Order;
import com.example.payment.domain.entity.Wallet;
import com.example.payment.domain.request.BuyProductRequest;
import com.example.payment.domain.request.PurchaseRequest;
import com.example.payment.repository.CartRepository;
import com.example.payment.repository.OrderRepository;
import com.example.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;
    private final OrderProducer orderProducer;

    public void addProductToCart(TokenInfo tokenInfo, Long productId, int quantity) {
        Product product = productClient.getById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        Cart cart = cartRepository.findByUserUUID(tokenInfo.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUserUUID(tokenInfo.getId());
            cartRepository.save(cart);
        }

        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setQuantity(quantity);
        cart.getItems().add(item);
        item.setCart(cart);

        cartRepository.save(cart);
    }

    public void removeProductFromCart(TokenInfo tokenInfo, Long productId) {
        Cart cart = cartRepository.findByUserUUID(tokenInfo.getId());

        CartItem itemToRemove = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            cart.getItems().remove(itemToRemove);
            cartRepository.save(cart);
        } else {
            throw new RuntimeException("장바구니에 상품이 없습니다.");
        }
    }

    public void purchaseCart(PurchaseRequest request, TokenInfo tokenInfo) {
        Cart cart = cartRepository.findByUserUUID(tokenInfo.getId());

        int walletBalance = getWalletBalance(tokenInfo.getId());
        if (walletBalance < request.getTotalPrice()) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        subtractWalletBalance(tokenInfo.getId(), request.getTotalPrice());

        List<OrderKafkaData> orderKafkaDataList = request.getBuyProducts().stream()
                .map(BuyProductRequest::toOrderKafkaData)
                .toList();

        orderProducer.send(orderKafkaDataList);

        // 주문 정보 생성 및 저장
        Order order = Order.builder()
                .userUUID(tokenInfo.getId())
                .username(tokenInfo.getName())
                .address(tokenInfo.getAddress())
                .phoneNumber(tokenInfo.getPhoneNumber())
                .orderTime(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);
    }


    public int getWalletBalance(UUID userUUID) {
        Wallet wallet = walletRepository.findById(userUUID).orElse(null);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for the user");
        }
        return wallet.getBalance();
    }

    public void addWalletBalance(UUID userUUID, int amount) {
        Wallet wallet = walletRepository.findById(userUUID).orElse(new Wallet());
        if (wallet.getUserUUID() == null) {
            wallet.setUserUUID(userUUID);
        }
        wallet.addBalance(amount);
        walletRepository.save(wallet);
    }

    public void subtractWalletBalance(UUID userUUID, int amount) {
        Wallet wallet = walletRepository.findById(userUUID).orElse(null);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for the user");
        }
        wallet.subtractBalance(amount);
        walletRepository.save(wallet);
    }
}
