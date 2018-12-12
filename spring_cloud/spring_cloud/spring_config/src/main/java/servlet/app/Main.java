package servlet.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Main {


    @GetMapping("get")
    public String get(HttpServletRequest request) {
        Object u = request.getServletContext().getAttribute("user");

        if (u instanceof User) {
            User user = (User) u;
            System.out.println(user.getName());
        }

        return "ok";
    }

}
