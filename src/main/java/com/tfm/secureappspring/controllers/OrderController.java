package com.tfm.secureappspring.controllers;

import com.tfm.secureappspring.data.daos.OrderRepository;
import com.tfm.secureappspring.data.daos.UserRepository;
import com.tfm.secureappspring.data.models.Order;
import com.tfm.secureappspring.data.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/Orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Secured("ROLE_AUTHENTICATED")
    @GetMapping(value = "/Index")
    public String index(Model model) {
        List<Order> orders = this.orderRepository.getAll();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_ADMIN"))) {
            model.addAttribute("orders", orders);
            return "Orders/Index";
        } else {
            org.springframework.security.core.userdetails.User userDetails =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            User user = this.userRepository.findUserByMail(userDetails.getUsername());
            orders = orders.stream()
                    .filter(order -> order.getUser().getId().equals(user.getId())).collect(Collectors.toList());
            model.addAttribute("orders", orders);
            return "Orders/Index";
        }
    }

    @Secured("ROLE_AUTHENTICATED")
    @GetMapping(value = "/Details/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("httpStatus", "400");
            redirectAttributes.addFlashAttribute("httpStatus.reasonPhrase", "BAD REQUEST");

            return "redirect:/error";
        }
        Optional<Order> order = this.orderRepository.getOneById(id);
        if (order.isEmpty()) {
            return "redirect:/error/404";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        User user = this.userRepository.findUserByMail(userDetails.getUsername());
        if (!Objects.equals(user.getId(), order.get().getUser().getId()) &&
                !user.getRole().toString().equalsIgnoreCase("ADMIN")) {
            return "redirect:/Orders/Index";
        }
        model.addAttribute("order", order.get());
        return "Orders/Details";
    }
}
