package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.daos.ProductRepository;
import com.tfm.secureappspring.data.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Controller
@SessionAttributes("cart")
@RequestMapping(value = "Cart")
public class CartController {
    List<Product> cart;
    @Autowired
    private ProductRepository productRepository;
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
        Map<Integer, List<Product>> productsMap = this.cart.stream().collect(groupingBy(Product::getId));
        Map<Integer, Product> products = new HashMap<>();

        for (Map.Entry<Integer, List<Product>> entry : productsMap.entrySet()) {
            Integer purchasedAmount = entry.getValue().size();
            entry.getValue().get(0).setAmount(purchasedAmount);
            products.put(entry.getKey(), entry.getValue().get(0));
        }
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
}
