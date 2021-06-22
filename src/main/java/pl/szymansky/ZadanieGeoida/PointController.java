package pl.szymansky.ZadanieGeoida;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author Piotr Szymański
 */
@Controller
class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @RequestMapping("/")
    String home() {
        return "point";
    }

    @RequestMapping("/calculate")
    String calculate(Model model, Point form) throws IOException {
        double result = pointService.calculate(form);
        String format = String.format("%.4f%n", result);
        model.addAttribute("result", format);
        return "result";
    }

}

