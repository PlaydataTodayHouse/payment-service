package com.example.payment.service;

import com.example.payment.client.api.ProductClient;
import com.example.payment.client.api.UserClient;
import com.example.payment.domain.dto.Product;
import com.example.payment.domain.dto.User;
import com.example.payment.domain.entity.Cart;
import com.example.payment.domain.entity.CartItem;
import com.example.payment.domain.entity.Order;
import com.example.payment.domain.entity.Wallet;
import com.example.payment.repository.CartRepository;
import com.example.payment.repository.OrderRepository;
import com.example.payment.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final CartRepository cartRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Autowired
    public PaymentService(CartRepository cartRepository,
                          WalletRepository walletRepository,
                          OrderRepository orderRepository,
                          ProductClient productClient,
                          UserClient userClient) {
        this.cartRepository = cartRepository;
        this.walletRepository = walletRepository;
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
    }


    public void addProductToCart(UUID userUUID, Long productId, int quantity) {
        User user = userClient.getByUUID(userUUID);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Product product = productClient.getById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        Cart cart = cartRepository.findByUserUUID(userUUID);
        if (cart == null) {
            cart = new Cart();
            cart.setUserUUID(userUUID);
            cartRepository.save(cart);
        }

        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setQuantity(quantity);
        cart.getItems().add(item);
        item.setCart(cart);

        cartRepository.save(cart);
    }

    public void removeProductFromCart(UUID userUUID, Long productId) {
        User user = userClient.getByUUID(userUUID);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Cart cart = cartRepository.findByUserUUID(userUUID);

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

    public void purchaseCart(UUID userUUID, String name, String address, String phoneNumber) {
        User user = userClient.getByUUID(userUUID);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Cart cart = cartRepository.findByUserUUID(userUUID);

        double totalCost = 0;
        for (CartItem item : cart.getItems()) {
            Product product = productClient.getById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found");
            }
//            totalCost += product.getPrice() * item.getQuantity();
//            product쪽에 getPrice로 가격 조회하는 메소드 구현시 수정 예정
        }

        int walletBalance = getWalletBalance(userUUID);
        if (walletBalance < totalCost) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        subtractWalletBalance(userUUID, (int) totalCost);

        // 주문 정보 생성 및 저장
        Order order = new Order();
        order.setUserUUID(userUUID);
        order.setName(name);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setOrderTime(LocalDateTime.now());

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
