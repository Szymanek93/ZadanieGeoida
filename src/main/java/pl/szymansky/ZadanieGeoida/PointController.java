package pl.szymansky.ZadanieGeoida;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Formatter;

@Controller
public class PointController {

    @RequestMapping("/")
    public String home() {
        return "point";
    }

    @RequestMapping("/biLinear")
    public double biLinear(Model model, Point form) throws IOException {
        double result = PointServices.biLinearInterpolation(form.getX(), form.getY());
        model.addAttribute("result", result);
        return result;
    }

    @RequestMapping("/biCubic")
    public double biCubic(Model model, Point form) throws IOException {
        double result = PointServices.biCubicInterpolation(form.getX(), form.getY());
        model.addAttribute("result", result);
        return result;
    }


    @RequestMapping("/calculate")
    public String doCalculations(Model model, Point form) throws IOException {
        double result = 0.00;
        switch (form.getOperation()) {
            case "dwuliniowa":
                result = PointServices.biLinearInterpolation(form.getX(), form.getY());
               if (result%0.0001>=0.00005) {
                   result=result-(result%0.0001)+0.0001;
               }
               else{
                   result=result-(result%0.0001);
               }
                break;
            case "dwusześcienna":
                result = PointServices.biCubicInterpolation(form.getX(), form.getY());
                if (result%0.0001>=0.00005) {
                    result=result-(result%0.0001)+0.0001;
                }
                else{
                    result=result-(result%0.0001);
                }
                break;
        }
        StringBuilder sbuf = new StringBuilder();
        Formatter fmt = new Formatter(sbuf);
        fmt.format ("%.4f%n", result);
        model.addAttribute("result", fmt);
        return "result";
    }

}

