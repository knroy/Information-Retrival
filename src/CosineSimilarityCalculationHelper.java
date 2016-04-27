import java.text.DecimalFormat;

/**
 * Created by Komol on 4/27/2016.
 */
public class CosineSimilarityCalculationHelper {

    private static double log2(double x, double y) {
        return roundTwoDecimals(Math.log(x / y) / Math.log(2));
    }

    private static double roundTwoDecimals(double d) {
        DecimalFormat fourDForm = new DecimalFormat("#.####");
        return Double.valueOf(fourDForm.format(d));
    }

    public static double leftDenominator(int[] tf, int ln, int total) {
        double data = 0.0;
        for (int i = 0; i < ln; i++) {
            if (tf[i] != 0) {
                double idf = log2((double) total, (double) tf[i]);
                data += idf * idf;
            }
        }
        data = roundTwoDecimals(Math.sqrt(data));
        return data;
    }

    public static double rightDenominator(double[] queryInfo, int ln) {
        double data = 0.0;
        for (int i = 0; i < ln; i++) {
            data += queryInfo[i] * queryInfo[i];
        }
        data = roundTwoDecimals(Math.sqrt(data));
        return data;
    }

    public static double finalCal(double top, double deno_left, double deno_right) {
        return roundTwoDecimals(top / (deno_left * deno_right));
    }



}
