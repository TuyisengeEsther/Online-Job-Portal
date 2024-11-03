package com.example.online_job_protal.Controller;


import com.example.online_job_protal.Model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.online_job_protal.Model.UserModel;
import com.example.online_job_protal.Service.RoleService;
import com.example.online_job_protal.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "Signup";  // Looks for signup.html in the templates folder
    }

    // Display login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "loginPage";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpServletRequest request) {
        Optional<UserModel> user = userService.login(email, password);

        if (user.isPresent()) {
            HttpSession session = request.getSession();
            String role = user.get().getRole();

            session.setAttribute("email", user.get().getEmail());
            session.setAttribute("role", user.get().getRole());
            session.setAttribute("firstname", user.get().getFirstName());
            session.setAttribute("lastname", user.get().getLastName());
            model.addAttribute("role",user.get().getRole());
            List<String> allowedMenus = roleService.getAllowedMenus(role); // Use roleService directly

            model.addAttribute("allowedMenus", allowedMenus);

            switch (role) {
                case "administrator":
                    return "redirect:/AdminDashboard"; // Redirect to the admin dashboard
                case "Customer":
                    return "redirect:/UserDashboard"; // Redirect to the customer homepage
                case "accountant":
                    return "redirect:/UserDashboard"; // Redirect to the accountant dashboard
                case "Manager":
                    return "redirect:/UserDashboard"; // Redirect to the manager dashboard
                default:
                    return "redirect:/login"; //
            }
        } else {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "loginPage"; // Return to login page with error message
        }
    }

    @PostMapping("/signup")
    public String registerUser(UserModel user, Model model) {
        userService.saveUser(user);
        model.addAttribute("successMessage", "User Registration successful!!.");
        return "Signup";
    }
    @GetMapping("/AdminDashboard")
    public String showDashboard(@RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model, HttpSession session) {

        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");
        model.addAttribute("username", role);

        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserModel> users;

        if (!search.isEmpty()) {
            users = userService.searchUsers(search, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }

        model.addAttribute("users_data", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("search", search);

        return "AdminDashboard"; // Return the view name
    }



    @GetMapping("/UserDashboard")
    public String showUserDashboard(Model model,HttpSession session) {// Debugging output
        String firstname = (String) session.getAttribute("firstname");
        String lastname = (String) session.getAttribute("lastname");
        String role = (String) session.getAttribute("role");

        List<String> allowedMenus = roleService.getAllowedMenus(role); // Use roleService directly

        model.addAttribute("allowedMenus", allowedMenus);

        // Add attributes to the model
        model.addAttribute("username", role);
        model.addAttribute("firstname", firstname);
        model.addAttribute("lastname", lastname);// Replace with logged-in user's name
        return "UserDashboard"; // Name of the HTML file
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserModel user = userService.getUserById(id); // Use service to get user
        model.addAttribute("user", user);
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        return "EditUser"; // The name of the edit user HTML file
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserModel user) {
        user.setId(id);
        userService.saveUser(user); // Use service to save the updated user
        return "redirect:/AdminDashboard"; // Redirect back to the dashboard
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // Use service to delete the user
        return "redirect:/AdminDashboard"; // Redirect back to the dashboard
    }
    @GetMapping("/refresh")
    public String refreshDashboard(@RequestParam(defaultValue = "") String search,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model, HttpSession session) {
        return showDashboard(search, page, size, model, session);
    }

    @GetMapping("/admin/users")
    public String getUsers(@RequestParam(defaultValue = "") String search,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(defaultValue = "username") String sortField,
                           @RequestParam(defaultValue = "asc") String sortDir,
                           HttpSession session,
                           Model model) {

        String role = (String) session.getAttribute("role");
        // Set sorting direction and field
        Sort sort = Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch users based on search query and pagination
        Page<UserModel> users = search.isEmpty() ? userService.getAllUsers(pageable) : userService.searchUsers(search, pageable);

        // Populate the model attributes for use in the view
        model.addAttribute("users_data", users.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("username", role);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "AdminDashboard"; // Name of the view template
    }
    @GetMapping("/forgot-password")
    public String ForgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        // Generate reset token for the provided email
        String token = userService.createPasswordResetToken(email);

        if (token != null) {
            // Create a reset link (make sure to replace 'your-frontend-url' with your actual URL)
            String resetLink = "http://localhost:8080/resetpassword?token=" + token;

            try {
                // Send the reset email
                userService.sendResetEmail(email, resetLink);
                return ResponseEntity.ok("Reset email sent.");
            } catch (Exception e) {
                // Log the exception (you might want to use a logging framework here)
                System.err.println("Error sending reset email: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send reset email. Please try again later.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Email not found.");
    }

    // Endpoint to reset password
    @PostMapping("/reset-password-action")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword,Model model) {
        if (userService.validateToken(token)) {
             userService.updatePassword(token, newPassword);
            model.addAttribute("messages", "Password successfully reset.");
            return "/resetpassword";
        }
        return "Invalid or expired token.";
    }
    @GetMapping("/resetpassword")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        if (token == null || token.isEmpty()) {
            return "error";
        }
        model.addAttribute("token", token);
        return "resetPassword";
    }

}
