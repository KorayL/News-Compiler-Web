package news_compiler.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with endpoint(s) for checking the health/status of the server.
 */
@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {
    /**
     * Returns a simple message to indicate that the server is running.
     * @return a simple message
     */
    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}
