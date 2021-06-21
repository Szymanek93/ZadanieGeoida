package pl.szymansky.ZadanieGeoida;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Formatter;
import java.util.Scanner;

@Service
public class PointServices {
    public PointServices() throws IOException {
}
    final int colNumber= (int) readRow(5);
    final int rowNumber= (int) readRow(6);

    // Odczytywanie wskzanego wiersza z pliku geoidy
    public static double readRow(int n) throws IOException {
        File file = new File("EVRF 2007.gsf");
        LineNumberReader ln = new LineNumberReader(new FileReader(file));
        String row = null;
        boolean isNumeric = true;
        for (int i = 0; i <= n-1; i++) {
            row = ln.readLine();
        }
        isNumeric(row);
        double rowValue = Double.parseDouble(row);
        ln.close();
        return rowValue;
    }
    //Wprowadzanie szerokości geograficznej szukanego punktu
    public static double insertPointX() throws IOException {
        double rangeXMax = (readRow(3));
        double rangeXMin = (readRow(1));
        Scanner pointX = new Scanner(System.in);
        System.out.println("Wprowadź szerokość geograficzną");
        double X = pointX.nextDouble();
        if (X>=rangeXMin && X<=rangeXMax) {
            return X;
        } else {
            System.out.println("Podana wartość szerokości geograficznej jest poza zakresem, wprowadź poprawną wartość:");
            return insertPointX();
        }
    }
    // Wprowadzanie długości geograficznej szukanego punktu
    public static double insertPointY() throws IOException {
        double rangeYMax = (readRow(4));
        double rangeYMin = (readRow(2));
        Scanner pointY = new Scanner(System.in);
        System.out.println("Wprowadź długość geograficzną");
        double Y = pointY.nextDouble();
        if (Y>=rangeYMin && Y<=rangeYMax) {
            return Y;
        } else {
            System.out.println("Podana wartość długości geograficznej jest poza zakresem, wprowadź poprawną wartość:");
            return insertPointY();
        }
    }
    //dczytywanie skoku siatki grid
    public static double findStep() throws IOException {
        double step = ((readRow(3)-readRow(1))/(readRow(6)));
        return step;
    }
    // wartość wspolrzednych wezlów po uwzglednieniu osi X

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            System.out.println("we skzazanym pkt wartośc geoidy wynosi N");
            return false;
        }
    }
    public static double roundNum(double a){
        double number = Math.round(a);
        return (number/100) ;
    }

    public static double searchXNodeDown(double pointX) throws IOException {
        Double rangeXMin = (readRow(1));
        double gridStep = findStep();
        double nodeX = roundNum(((pointX-rangeXMin)/gridStep)-((pointX-rangeXMin)%gridStep)/100);
        return nodeX;
    }
    public static double searchYNodeDown(double pointY) throws IOException {
        int rangeYMin = (int) readRow(2);
        double gridStep = findStep();
        double nodeY = roundNum(((pointY-rangeYMin)/gridStep)-((pointY-rangeYMin)%gridStep)/100);
        return nodeY;
    }
    public static double searchXNodeUp(double pointX) throws IOException {
        Double rangeXMin = (readRow(1));
        double gridStep = findStep();
        double nodeX = roundNum(((pointX-rangeXMin+0.01)/gridStep)-((pointX-rangeXMin)%gridStep)/100);
        return nodeX;
    }
    public static double searchYNodeUp(double pointY) throws IOException {
        int rangeYMin = (int) readRow(2);
        double gridStep = findStep();
        double nodeY = roundNum(((pointY-rangeYMin+0.01)/gridStep)-((pointY-rangeYMin)%gridStep)/100);
        return nodeY;
    }

    public static double searchValueNode(double nodeX, double nodeY) throws IOException {
        double gridStep = findStep();
        int gridStartInFile = 7;
        int colNumber= (int) readRow(5)+1;
        double nodeNumber = 0;
        if (nodeX == 0){
            nodeNumber = roundNum((((nodeY/gridStep)))+gridStartInFile)*100;
//            System.out.println("a");
        }
        else{
            nodeNumber = roundNum((((nodeX/gridStep)*colNumber)+((nodeY/gridStep)))+gridStartInFile)*100;
//            System.out.println("b");
        }
        int nodeNumberParse = (int) nodeNumber;
//        System.out.println(nodeNumberParse);
        double vauleNode=readRow(nodeNumberParse);
//        System.out.println(vauleNode);
        return vauleNode;

    }
    public static double biLinearInterpolation(double pointX, double pointY) throws IOException {
        //współrzędne zredukowane do początku osi
        double pointXred=pointX-readRow(1);
        double pointYred=pointY-readRow(2);
        //wyznaczanie współrzędnych punktów wezłowych
        double Xdown = searchXNodeDown(pointX);
        double Xup= searchXNodeUp(pointX);
        double Ydown=searchYNodeDown(pointY);
        double Yup=searchYNodeUp(pointY);
        //pobieranie wartości z punktów węzłowych
        double valueNode1 = searchValueNode(Xdown,Ydown);
        double valueNode2 = searchValueNode(Xup,Ydown);
        double valueNode3 = searchValueNode(Xup,Yup);
        double valueNode4 = searchValueNode(Xdown,Yup);
        //obliczenie składowych funkcji
        double fR1 = (((Yup-pointYred)/(Yup-Ydown))*valueNode1)+(((pointYred-Ydown)/(Yup-Ydown))*valueNode4);
        double fR2 = (((Yup-pointYred)/(Yup-Ydown))*valueNode2)+(((pointYred-Ydown)/(Yup-Ydown))*valueNode3);
        double pointValue=((Xup-pointXred)/(Xup-Xdown)*fR1)+((pointXred-Xdown)/(Xup-Xdown)*fR2);
        return pointValue;

    }

    public static double searchXNodeDownBiCubic(double pointX, double addStep) throws IOException {
        double rangeXMin = (readRow(1));
        double gridStep = findStep();
        double nodeX = roundNum(((pointX-rangeXMin+addStep)/gridStep)-((pointX-rangeXMin+ addStep)%gridStep)/100);
        return nodeX;
    }
    public static double searchYNodeDownBiCubic(double pointY, double addStep) throws IOException {
        double rangeYMin = readRow(2);
        double gridStep = findStep();
        double nodeY = roundNum(((pointY - rangeYMin + addStep) / gridStep) - ((pointY-rangeYMin+ addStep)%gridStep)/100);
        return nodeY;
    }

    public static double biCubicInterpolation(double pointX, double pointY) throws IOException {
        double valueBicubicIntepolation=0;
        int pointXInt= (int)(((pointX*10)%0.1)*100);
        int pointYInt= (int)(((pointY*10)%0.1)*100);
        if (pointXInt==0 && pointYInt==0){
            valueBicubicIntepolation=searchValueNode(searchXNodeDown(pointX),searchYNodeDown(pointY));
            System.out.println("A");

        }else
            {
        double valueNode1 = searchValueNode((searchXNodeDownBiCubic(pointX,(0))),searchYNodeDownBiCubic(pointY,(0)));
        double valueNode2 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.01))),searchYNodeDownBiCubic(pointY,0));
        double valueNode3 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.01))),searchYNodeDownBiCubic(pointY,(0.01)));
        double valueNode4 = searchValueNode((searchXNodeDownBiCubic(pointX,(0))),searchYNodeDownBiCubic(pointY,(0.01)));
        double valueNode5 = searchValueNode((searchXNodeDownBiCubic(pointX,(-0.01))),searchYNodeDownBiCubic(pointY,(-0.01)));
        double valueNode6 = searchValueNode((searchXNodeDownBiCubic(pointX,(0))),searchYNodeDownBiCubic(pointY,(-0.01)));
        double valueNode7 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.01))),searchYNodeDownBiCubic(pointY,(-0.01)));
        double valueNode8 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.02))),searchYNodeDownBiCubic(pointY,(-0.01)));
        double valueNode9 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.02))),searchYNodeDownBiCubic(pointY,(0)));
        double valueNode10 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.02))),searchYNodeDownBiCubic(pointY,(0.01)));
        double valueNode11= searchValueNode((searchXNodeDownBiCubic(pointX,(0.02))),searchYNodeDownBiCubic(pointY,(0.02)));
        double valueNode12 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.01))),searchYNodeDownBiCubic(pointY,(0.02)));
        double valueNode13 = searchValueNode((searchXNodeDownBiCubic(pointX,(0.00))),searchYNodeDownBiCubic(pointY,(0.02)));
        double valueNode14 = searchValueNode((searchXNodeDownBiCubic(pointX,(-0.01))),searchYNodeDownBiCubic(pointY,(0.02)));
        double valueNode15 = searchValueNode((searchXNodeDownBiCubic(pointX,(-0.01))),searchYNodeDownBiCubic(pointY,(0.01)));
        double valueNode16 = searchValueNode((searchXNodeDownBiCubic(pointX,(-0.01))),searchYNodeDownBiCubic(pointY,(0.00)));
                System.out.println("B");
        valueBicubicIntepolation=(valueNode1+valueNode2+valueNode3+valueNode4+valueNode5+
                valueNode6+valueNode7+valueNode8+valueNode9+valueNode10+valueNode11+valueNode12+
                valueNode13+valueNode14+valueNode15+valueNode16)/16;

    }
        return valueBicubicIntepolation;
    }
}
