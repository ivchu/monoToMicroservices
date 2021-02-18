package com.epam.mentoring.services;

import com.epam.mentoring.dtos.CartItemDto;
import com.epam.mentoring.models.GroupVariant;
import com.epam.mentoring.models.Order;
import com.epam.mentoring.models.OrderItem;
import com.epam.mentoring.models.Product;
import com.epam.mentoring.models.ProductGroup;
import com.epam.mentoring.models.ProductImage;
import com.epam.mentoring.repositories.GroupRepository;
import com.epam.mentoring.repositories.OrderRepository;
import com.epam.mentoring.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class EcommerceService {

    private final AtomicInteger cartServiceIndex = new AtomicInteger(0);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${instance.service.cart}")
    private String cartServiceName;
    @Autowired
    private DiscoveryClient discoveryClient;

    /* PRODUCT */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(long id) {
        return productRepository.findOne(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public String addProductImage(final String productId, final String filename) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        ProductImage image = new ProductImage();
        image.setProductId(Long.parseLong(productId));
        image.setPath(filename);

        try {
            String res = session.save(image).toString();
            session.getTransaction().commit();
            return res;
        } catch (HibernateException e) {
            System.out.print(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return "";
    }

    /* GROUPS */
    public List<ProductGroup> getGroups() {
        return groupRepository.findAll();
    }

    public ProductGroup getGroup(long id) {
        return groupRepository.findOne(id);
    }

    public ProductGroup saveGroup(ProductGroup group) {
        return groupRepository.save(group);
    }

    /* ORDERS */
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(long id) {
        return orderRepository.findOne(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order createOrder(String cartId, Order order) {
        String cartServiceUrl = getCartServiceUrl() + "/cart/" + cartId;
        log.info("Calling cart service {} for cart with id {}", cartServiceUrl, cartId);
        CartItemDto[] body = restTemplate.getForEntity(cartServiceUrl, CartItemDto[].class).getBody();
        order = addCartItemsToOrders(Arrays.asList(body), order);
        if (order == null) {
            log.error("Order not set.");
        }
        order = saveOrder(order);
        return order;
    }

    private String getCartServiceUrl() {
        List<ServiceInstance> cartServices = discoveryClient.getInstances(cartServiceName);
        return cartServices.get(
                cartServiceIndex.getAndAccumulate(cartServices.size(),
                        (current, n) -> current >= n - 1 ? 0 : current + 1)).getUri().toString();
    }

    private Order addCartItemsToOrders(List<CartItemDto> cartItemDtos, Order order) {
        cartItemDtos.forEach(
                cartItem -> {
                    Product prod = getProduct(cartItem.getProductId());
                    int qty = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;
                    long variantId = cartItem.getVariantId();

                    for (int i = 0; i < qty; i++) {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setProduct(prod);
                        if (variantId > 0) {
                            GroupVariant v = new GroupVariant();
                            v.setId(variantId);
                            orderItem.setGroupVariant(v);
                        }
                        orderItem.setOrder(order);
                        order.getItems().add(orderItem);
                    }
                });

        return order;
    }
}
