package org.example.orderservice.controller;

import org.example.orderservice.dto.OnlineOrderDTO;
import org.example.orderservice.dto.OrderItemDTO;
import org.example.orderservice.entity.OnlineOrder;
import org.example.orderservice.entity.OrderItem;
import org.example.orderservice.entity.User;
import org.example.orderservice.service.OrderItemService;
import org.example.orderservice.service.OrderService;
import org.example.orderservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserService userService;

    public OrderController(OrderService orderService, OrderItemService orderItemService, UserService userService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
    }

    // ----- order management -----
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<OnlineOrderDTO>> findOrders(@RequestParam(required = false) Long userId) {
        List<OnlineOrder> onlineOrders = userId != null?
                orderService.findOrdersByUserId(userId): orderService.findAllOrders();

        List<OnlineOrderDTO> orderDTOs = onlineOrders.stream()
                .map(orderService::convertOnlineOrderToDTO)  // or a Mapper::
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OnlineOrderDTO>> findMyOrders(Principal principal) {
        // Get the authenticated user
        User user = userService.getUserByUsername(principal.getName());
        List<OnlineOrder> onlineOrders = orderService.findOrdersByUserId(user.getId());

        List<OnlineOrderDTO> orderDTOs = onlineOrders.stream()
                .map(orderService::convertOnlineOrderToDTO)  // or a Mapper::
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrder(@PathVariable UUID orderId) {
        OnlineOrder order = orderService.getOrderById(orderId);
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(orderItemService::convertOrderItemToDTO)  // or a Mapper::
                .collect(Collectors.toList());
        return ResponseEntity.ok(itemDTOs);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getMyOrder(@PathVariable UUID orderId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        OnlineOrder order = orderService.getOrderById(orderId);

        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(orderItemService::convertOrderItemToDTO)  // or a Mapper::
                .collect(Collectors.toList());

        return ResponseEntity.ok(itemDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable UUID orderId) {
        orderService.markOrderAsCompleted(orderId);
        return ResponseEntity.ok("Order marked as completed successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId) {
        orderService.markOrderAsCancelled(orderId);
        return ResponseEntity.ok("Order marked as canceled successfully!");
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place-order")
    public ResponseEntity<OnlineOrderDTO> placeOrder(@RequestBody List<OrderItemDTO> itemDTOS, Principal principal) {
    // Get the authenticated user
        User user = userService.getUserByUsername(principal.getName());

        // Convert DTOs to domain entities
        List<OrderItem> orderItems = itemDTOS.stream()
                .map(orderItemService::createOrderItemFromDTO)
                .collect(Collectors.toList());

        // Insert the order
        OnlineOrder onlineOrder = orderService.insertOrder(user, orderItems);

        // Return the DTO
        return ResponseEntity.ok(orderService.convertOnlineOrderToDTO(onlineOrder));
    }
}
