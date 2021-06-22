package pl.szymansky.ZadanieGeoida;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * @author Piotr Szymański
 */
public class PointProvider {
    // Odczytywanie wskazanego wiersza z pliku geoidy
    public static double readRow(int n) throws IOException {
        File file = new File("EVRF 2007.gsf");
        LineNumberReader ln = new LineNumberReader(new FileReader(file));
        String row = null;
        boolean isNumeric = true;
        for (int i = 0; i <= n - 1; i++) {
            row = ln.readLine();
        }
        isNumeric(row);
        double rowValue = Double.parseDouble(row);
        ln.close();
        return rowValue;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("we wskzazanym pkt wartośc geoidy wynosi N");
            //TODO zmień na ang
            return false;
        }
    }
}
