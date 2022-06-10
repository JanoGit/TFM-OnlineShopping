package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.daos.OrderRepository;
import com.tfm.secureappspring.data.daos.ProductRepository;
import com.tfm.secureappspring.data.daos.PurchasedProductRepository;
import com.tfm.secureappspring.data.daos.UserRepository;
import com.tfm.secureappspring.data.models.Order;
import com.tfm.secureappspring.data.models.Product;
import com.tfm.secureappspring.data.models.PurchasedProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Controller
@SessionAttributes("cart")
@RequestMapping(value = "/Cart")
public class CartController {
    List<Product> cart;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PurchasedProductRepository purchasedProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    private final String HTTP_STATUS_KEY = "httpStatus";
    private final String HTTP_STATUS_400 = "400";
    private final String HTTP_STATUS_REASON_PHRASE_KEY = "httpStatus.reasonPhrase";
    private final String HTTP_STATUS_REASON_PHRASE_400 = "BAD REQUEST";

    @GetMapping(value = "/Index")
    public String index(Model model, HttpSession httpSession) {
        this.cart = (List<Product>) httpSession.getAttribute("cart");
        if (this.cart == null) {
            return "Cart/Index";
        }
        Map<Integer, Product> products = this.groupByIdAndTransformToMapWithPurchasedAmountOfEachProductStoredInFieldAmountOfProduct(this.cart);
        model.addAttribute("products", products);

        return "Cart/Index";
    }

    @GetMapping(value = "/Add/{id}")
    public String add(@PathVariable Integer id, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        Product product = productRepository.getById(id);
        if (product.getId() == null) {
            return "redirect:/error/404";
        }
        this.cart = (List<Product>) httpSession.getAttribute("cart");
        if (this.cart == null) {
            this.cart = new ArrayList<>();
        }
        this.cart.add(product);
        model.addAttribute("cart", this.cart);

        return "redirect:/Products/Index";
    }

    @GetMapping(value = "/Remove/{id}")
    public String remove(@PathVariable Integer id, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute(HTTP_STATUS_KEY, HTTP_STATUS_400);
            redirectAttributes.addFlashAttribute(HTTP_STATUS_REASON_PHRASE_KEY, HTTP_STATUS_REASON_PHRASE_400);

            return "redirect:/error";
        }
        Product product = productRepository.getById(id);
        if (product.getId() == null) {
            return "redirect:/error/404";
        }
        this.cart = (List<Product>) httpSession.getAttribute("cart");
        if (this.cart == null) {
            return "redirect:/error";
        }
        for (Product p : this.cart) {
            if (Objects.equals(p.getId(), id)) {
                this.cart.remove(p);
                break;
            }
        }
        model.addAttribute("cart", this.cart);

        return "redirect:/Cart/Index";
    }

    @PostMapping(value = "/Buy")
    public String buy(HttpSession httpSession, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal().equals("anonymousUser") ||
                auth.getAuthorities().stream().anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_ANONYMOUS"))) {
            return "redirect:/Users/Login";
        }
        this.cart = (List<Product>) httpSession.getAttribute("cart");
        if (this.cart == null) {
            return "Cart/Index";
        }
        List<Order> orders = this.orderRepository.getAll();
        orders.sort(Comparator.comparing(Order::getId).reversed());
        Map<Integer, Product> shoppingCart = this.groupByIdAndTransformToMapWithPurchasedAmountOfEachProductStoredInFieldAmountOfProduct(this.cart);
        Order order = new Order();
        Integer orderId = orders.isEmpty() ? 1 : (orders.get(0).getId() + 1);
        order.setId(orderId);
        order.setCost(0.0);
        User user = (User) auth.getPrincipal();
        order.setUser(this.userRepository.findUserByMail(user.getUsername()));
        this.orderRepository.saveEntity(order.getId(), order.getCost(), order.getUser().getId());
        for (Map.Entry<Integer, Product> entry : shoppingCart.entrySet()) {
            if (this.productRepository.getById(entry.getKey()).getAmount() < entry.getValue().getAmount()) {
                redirectAttributes.addFlashAttribute("errorAmount", "Amount selected of " +
                        entry.getValue().getName() + " is over the current stock.");
            }
            Product product = this.productRepository.getById(entry.getKey());
            PurchasedProduct purchasedProduct = PurchasedProduct.builder()
                    .product(product)
                    .quantity(entry.getValue().getAmount())
                    .order(order)
                    .build();
            this.purchasedProductRepository.save(purchasedProduct);
            product.setAmount(product.getAmount() - entry.getValue().getAmount());
            this.productRepository.save(product);
            order.setCost(order.getCost() + entry.getValue().getAmount() * entry.getValue().getPrice());
        }
        this.orderRepository.updateCost(order.getCost(), order.getId());
        sessionStatus.setComplete();

        return "redirect:/Cart/Index";
    }

    private Map<Integer, Product> groupByIdAndTransformToMapWithPurchasedAmountOfEachProductStoredInFieldAmountOfProduct(
            List<Product> sessionCart) {
        Map<Integer, List<Product>> productsMap = sessionCart.stream().collect(groupingBy(Product::getId));
        Map<Integer, Product> shoppingCart = new HashMap<>();

        for (Map.Entry<Integer, List<Product>> entry : productsMap.entrySet()) {
            Integer purchasedAmount = entry.getValue().size();
            entry.getValue().get(0).setAmount(purchasedAmount);
            shoppingCart.put(entry.getKey(), entry.getValue().get(0));
        }
        return shoppingCart;
    }
}
