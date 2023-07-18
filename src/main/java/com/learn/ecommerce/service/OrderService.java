package com.learn.ecommerce.service;

import com.learn.ecommerce.entity.OrderList;
import com.learn.ecommerce.entity.OrderItem;
import com.learn.ecommerce.entity.Product;
import com.learn.ecommerce.entity.Users;
import com.learn.ecommerce.exception.BadRequestException;
import com.learn.ecommerce.exception.ResourceNotFoundException;
import com.learn.ecommerce.model.BasketRequest;
import com.learn.ecommerce.model.OrderRequest;
import com.learn.ecommerce.model.OrderResponse;
import com.learn.ecommerce.model.OrderStatus;
import com.learn.ecommerce.repository.OrderItemRepository;
import com.learn.ecommerce.repository.OrderRepository;
import com.learn.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private BasketService basketService;
    private LogOrderService logOrderService;

    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository, BasketService basketService, LogOrderService logOrderService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.basketService = basketService;
        this.logOrderService = logOrderService;
    }

    @Transactional
    public OrderResponse makeOrder(String username, OrderRequest request) {
        OrderList orderList = new OrderList();
        orderList.setId(UUID.randomUUID().toString());
        orderList.setTanggal(new Date());
        orderList.setNumber(generateOrderNumber());
        orderList.setUsers(new Users(username));
        orderList.setShipAddress(request.getShipAddress());
        orderList.setOrderStatus(OrderStatus.DRAFT);
        orderList.setOrderTime(new Date());
        List<OrderItem> items = new ArrayList<>();
        for (BasketRequest req : request.getItems()) {
            Product product = productRepository.findById(req.getProductId())
                    .orElseThrow(() -> new BadRequestException("Produk ID " + req.getProductId() + " tidak ditemukan"));

            if (product.getStock() < req.getQuantity()) {
                throw new BadRequestException("Stok tidak mencukup");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProduct(product);
            orderItem.setDescription(product.getName());
            orderItem.setQuantity(req.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setAmount(new BigDecimal(orderItem.getPrice().doubleValue() * orderItem.getQuantity()));
            orderItem.setOrderList(orderList);
            items.add(orderItem);
        }

        BigDecimal amount = BigDecimal.ZERO;
        for (OrderItem orderItem : items) {
            amount = amount.add(orderItem.getAmount());
        }

        orderList.setAmount(amount);
        orderList.setShipCost(request.getShipCost());
        orderList.setTotal(orderList.getAmount().add(orderList.getShipCost()));

        OrderList saved = orderRepository.save(orderList);
        for (OrderItem orderItem : items) {
            orderItemRepository.save(orderItem);
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepository.save(product);
            basketService.delete(username, product.getId());
        }

//        Pencatatan log
        logOrderService.insert(username, orderList, LogOrderService.DRAFT, "Pesanan sudah dibuat");
        OrderResponse orderResponse = new OrderResponse(saved,items);
        return orderResponse;
    }

    @Transactional
    public OrderList cancelOrder(String orderId, String userId) {
        OrderList orderList = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + orderId + " tidak ditemukan"));

        if (!userId.equals(orderList.getUsers().getId())) {
            throw new BadRequestException("Pesanan hanya dapat dibatalkan oleh yang bersangkutan");
        }

        if (!OrderStatus.DRAFT.equals(orderList.getOrderStatus())) {
            throw new BadRequestException("Pesanan ini tidak dapat dibatalkan karena sudah diproses");
        }

        orderList.setOrderStatus(OrderStatus.DIBATALKAN);
        OrderList saved = orderRepository.save(orderList);
        logOrderService.insert(userId, saved, LogOrderService.DIBATALKAN, "Pesanan berhasil dibatalkan");
        return saved;
    }

    @Transactional
    public OrderList receiveOrder(String orderId, String userId) {
        OrderList orderList = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + orderId + " tidak ditemukan"));

        if (!userId.equals(orderList.getUsers().getId())) {
            throw new BadRequestException("Pesanan hanya dapat dibatalkan oleh yang bersangkutan");
        }

        if (!OrderStatus.PENGIRIMAN.equals(orderList.getOrderStatus())) {
            throw new BadRequestException("Penerimaan gagal, status pesanan saat ini adalah " + orderList.getOrderStatus().name());
        }

        orderList.setOrderStatus(OrderStatus.SELESAI);
        OrderList saved = orderRepository.save(orderList);
        logOrderService.insert(userId, saved, LogOrderService.SELESAI, "Pesanan berhasil diterima");
        return saved;
    }

    @Transactional
    public OrderList paymentConfirmation(String orderId, String userId) {
        OrderList orderList = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + orderId + " tidak ditemukan"));

        if (!OrderStatus.DRAFT.equals(orderList.getOrderStatus())) {
            throw new BadRequestException("Konfirmasi pesanan gagal, status pesanan saat ini adalah " + orderList.getOrderStatus().name());
        }

        orderList.setOrderStatus(OrderStatus.PEMBAYARAN);
        OrderList saved = orderRepository.save(orderList);
        logOrderService.insert(userId, saved, LogOrderService.PEMBAYARAN, "Pembayaran telah dikonfirmasi");
        return saved;
    }

    @Transactional
    public OrderList packing(String orderId, String userId) {
        OrderList orderList = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + orderId + " tidak ditemukan"));

        if (!OrderStatus.PEMBAYARAN.equals(orderList.getOrderStatus())) {
            throw new BadRequestException("Packing pesanan gagal, status pesanan saat ini adalah " + orderList.getOrderStatus().name());
        }

        orderList.setOrderStatus(OrderStatus.PACKING);
        OrderList saved = orderRepository.save(orderList);
        logOrderService.insert(userId, saved, LogOrderService.PACKING, "Pesanan dalam proses packing");
        return saved;
    }

    @Transactional
    public OrderList send(String orderId, String userId) {
        OrderList orderList = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan dengan ID " + orderId + " tidak ditemukan"));

        if (!OrderStatus.PACKING.equals(orderList.getOrderStatus())) {
            throw new BadRequestException("Pengiriman pesanan gagal, status pesanan saat ini adalah " + orderList.getOrderStatus().name());
        }

        orderList.setOrderStatus(OrderStatus.PENGIRIMAN);
        OrderList saved = orderRepository.save(orderList);
        logOrderService.insert(userId, saved, LogOrderService.PENGIRIMAN, "Pesanan sedang dikirim");
        return saved;
    }

    public List<OrderList> findAllUserOrder(String userId, Integer page, Integer limit) {
        return orderRepository.findByUsersId(userId, PageRequest.of(page, limit, Sort.by("orderTime").descending()));
    }

    public List<OrderList> findAllOrder(String filterText, Integer page, Integer limit) {
        return orderRepository.search(filterText.toLowerCase(), PageRequest.of(page, limit, Sort.by("orderTime").descending()));
    }

    private String generateOrderNumber() {
        return String.format("%016d", System.nanoTime());
    }

}
