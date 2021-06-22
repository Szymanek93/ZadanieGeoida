package pl.szymansky.ZadanieGeoida;

import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Piotr Szymański
 */
@Service
public class PointService {

//    //Wprowadzanie szerokości geograficznej szukanego punktu
//    public double insertPointX() throws IOException {
//        double rangeXMax = (PointProvider.readRow(3));
//        double rangeXMin = (PointProvider.readRow(1));
//        Scanner pointX = new Scanner(System.in);
//        System.out.println("Wprowadź szerokość geograficzną");
//        double X = pointX.nextDouble();
//        if (X >= rangeXMin && X <= rangeXMax) {
//            return X;
//        } else {
//            System.out.println("Podana wartość szerokości geograficznej jest poza zakresem, wprowadź poprawną wartość:");
//            return insertPointX();
//        }
//    }
//
//    // Wprowadzanie długości geograficznej szukanego punktu
//    public double insertPointY() throws IOException {
//        double rangeYMax = (PointProvider.readRow(4));
//        double rangeYMin = (PointProvider.readRow(2));
//        Scanner pointY = new Scanner(System.in);
//        System.out.println("Wprowadź długość geograficzną");
//        double Y = pointY.nextDouble();
//        if (Y >= rangeYMin && Y <= rangeYMax) {
//            return Y;
//        } else {
//            System.out.println("Podana wartość długości geograficznej jest poza zakresem, wprowadź poprawną wartość:");
//            return insertPointY();
//        }
//    }

    //dczytywanie skoku siatki grid
    public double findStep() throws IOException {
        return ((PointProvider.readRow(3) - PointProvider.readRow(1)) / (PointProvider.readRow(6)));
    }
    // wartość wspolrzednych wezlów po uwzglednieniu osi X

    public double roundNum(double a) {
        double number = Math.round(a);
        return (number / 100);
    }
    public double searchXNodeDown(double pointX) throws IOException {
        double rangeXMin = (PointProvider.readRow(1));
        double gridStep = findStep();
        return roundNum(((pointX - rangeXMin) / gridStep) - ((pointX - rangeXMin) % gridStep) / 100);
    }

    public double searchYNodeDown(double pointY) throws IOException {
        int rangeYMin = (int) PointProvider.readRow(2);
        double gridStep = findStep();
        return roundNum(((pointY - rangeYMin) / gridStep) - ((pointY - rangeYMin) % gridStep) / 100);
    }

    public double searchXNodeUp(double pointX) throws IOException {
        double rangeXMin = (PointProvider.readRow(1));
        double gridStep = findStep();
        return roundNum(((pointX - rangeXMin + 0.01) / gridStep) - ((pointX - rangeXMin) % gridStep) / 100);
    }

    public double searchYNodeUp(double pointY) throws IOException {
        int rangeYMin = (int) PointProvider.readRow(2);
        double gridStep = findStep();
        return roundNum(((pointY - rangeYMin + 0.01) / gridStep) - ((pointY - rangeYMin) % gridStep) / 100);
    }

    public double searchValueNode(double nodeX, double nodeY) throws IOException {
        double gridStep = findStep();
        int gridStartInFile = 7;
        int colNumber = (int) PointProvider.readRow(5) + 1;
        double nodeNumber;
        if (nodeX == 0) {
            nodeNumber = roundNum((((nodeY / gridStep))) + gridStartInFile) * 100;
        } else {
            nodeNumber = roundNum((((nodeX / gridStep) * colNumber) + ((nodeY / gridStep))) + gridStartInFile) * 100;
        }
        int nodeNumberParse = (int) nodeNumber;
        return PointProvider.readRow(nodeNumberParse);

    }

    public double biLinearInterpolation(double pointX, double pointY) throws IOException {
        //współrzędne zredukowane do początku osi
        double pointXred = pointX - PointProvider.readRow(1);
        double pointYred = pointY - PointProvider.readRow(2);
        //wyznaczanie współrzędnych punktów wezłowych
        double Xdown = searchXNodeDown(pointX);
        double Xup = searchXNodeUp(pointX);
        double Ydown = searchYNodeDown(pointY);
        double Yup = searchYNodeUp(pointY);
        //pobieranie wartości z punktów węzłowych
        double valueNode1 = searchValueNode(Xdown, Ydown);
        double valueNode2 = searchValueNode(Xup, Ydown);
        double valueNode3 = searchValueNode(Xup, Yup);
        double valueNode4 = searchValueNode(Xdown, Yup);
        //obliczenie składowych funkcji
        double fR1 = (((Yup - pointYred) / (Yup - Ydown)) * valueNode1) + (((pointYred - Ydown) / (Yup - Ydown)) * valueNode4);
        double fR2 = (((Yup - pointYred) / (Yup - Ydown)) * valueNode2) + (((pointYred - Ydown) / (Yup - Ydown)) * valueNode3);
        return ((Xup - pointXred) / (Xup - Xdown) * fR1) + ((pointXred - Xdown) / (Xup - Xdown) * fR2);

    }

    public double searchXNodeDownBiCubic(double pointX, double addStep) throws IOException {
        double rangeXMin = (PointProvider.readRow(1));
        double gridStep = findStep();
        return roundNum(((pointX - rangeXMin + addStep) / gridStep) - ((pointX - rangeXMin + addStep) % gridStep) / 100);
    }

    public double searchYNodeDownBiCubic(double pointY, double addStep) throws IOException {
        double rangeYMin = PointProvider.readRow(2);
        double gridStep = findStep();
        return roundNum(((pointY - rangeYMin + addStep) / gridStep) - ((pointY - rangeYMin + addStep) % gridStep) / 100);
    }

    public double biCubicInterpolation(double pointX, double pointY) throws IOException {
        double valueBicubicIntepolation;
        String textX = Double.toString(Math.abs(pointX));
        int integerPlacesX = textX.indexOf('.');
        int decimalPlacesX = textX.length() - integerPlacesX - 1;

        String textY = Double.toString(Math.abs(pointX));
        int integerPlacesY = textY.indexOf('.');
        int decimalPlacesY = textY.length() - integerPlacesY - 1;
        if (decimalPlacesX == 2 && decimalPlacesY == 2) {
            valueBicubicIntepolation = searchValueNode(searchXNodeDown(pointX), searchYNodeDown(pointY));

        } else {
            double valueNode1 = searchValueNode((searchXNodeDownBiCubic(pointX, (0))), searchYNodeDownBiCubic(pointY, (0)));
            double valueNode2 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.01))), searchYNodeDownBiCubic(pointY, 0));
            double valueNode3 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.01))), searchYNodeDownBiCubic(pointY, (0.01)));
            double valueNode4 = searchValueNode((searchXNodeDownBiCubic(pointX, (0))), searchYNodeDownBiCubic(pointY, (0.01)));
            double valueNode5 = searchValueNode((searchXNodeDownBiCubic(pointX, (-0.01))), searchYNodeDownBiCubic(pointY, (-0.01)));
            double valueNode6 = searchValueNode((searchXNodeDownBiCubic(pointX, (0))), searchYNodeDownBiCubic(pointY, (-0.01)));
            double valueNode7 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.01))), searchYNodeDownBiCubic(pointY, (-0.01)));
            double valueNode8 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.02))), searchYNodeDownBiCubic(pointY, (-0.01)));
            double valueNode9 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.02))), searchYNodeDownBiCubic(pointY, (0)));
            double valueNode10 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.02))), searchYNodeDownBiCubic(pointY, (0.01)));
            double valueNode11 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.02))), searchYNodeDownBiCubic(pointY, (0.02)));
            double valueNode12 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.01))), searchYNodeDownBiCubic(pointY, (0.02)));
            double valueNode13 = searchValueNode((searchXNodeDownBiCubic(pointX, (0.00))), searchYNodeDownBiCubic(pointY, (0.02)));
            double valueNode14 = searchValueNode((searchXNodeDownBiCubic(pointX, (-0.01))), searchYNodeDownBiCubic(pointY, (0.02)));
            double valueNode15 = searchValueNode((searchXNodeDownBiCubic(pointX, (-0.01))), searchYNodeDownBiCubic(pointY, (0.01)));
            double valueNode16 = searchValueNode((searchXNodeDownBiCubic(pointX, (-0.01))), searchYNodeDownBiCubic(pointY, (0.00)));
            System.out.println("B");
            valueBicubicIntepolation = (valueNode1 + valueNode2 + valueNode3 + valueNode4 + valueNode5 +
                    valueNode6 + valueNode7 + valueNode8 + valueNode9 + valueNode10 + valueNode11 + valueNode12 +
                    valueNode13 + valueNode14 + valueNode15 + valueNode16) / 16;

        }
        return valueBicubicIntepolation;
    }

    public double calculate(Point form) throws IOException {
        double result = 0.00;
        switch (form.getOperation()) {
            case BILINEAR:
                result = biLinearInterpolation(form.getX(), form.getY());
                if (result % 0.0001 >= 0.00005) {
                    result = result - (result % 0.0001) + 0.0001;
                } else {
                    result = result - (result % 0.0001);
                }
                break;
            case BICUBIC:
                result = biCubicInterpolation(form.getX(), form.getY());
                if (result % 0.0001 >= 0.00005) {
                    result = result - (result % 0.0001) + 0.0001;
                } else {
                    result = result - (result % 0.0001);
                }
                break;
        }
        return result;
    }
}
