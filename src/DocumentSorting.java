import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Komol on 4/27/2016.
 */
public class DocumentSorting {

    private ArrayList<String[]> itemsPerDoc;
    private ArrayList<int[]> tfsPerDoc;
    private ArrayList<String> itemlist;
    private Map<String, int[]> apperPerDoc;
    private int[] df;
    private double[] idf;
    private int totalDocNumber;
    private int maxDocLength;
    private String tmpItemMapping;

    public DocumentSorting(int totalDocNumber) {
        this.totalDocNumber = totalDocNumber;
        itemlist = new ArrayList<String>();
        itemsPerDoc = new ArrayList<String[]>();
        tfsPerDoc = new ArrayList<int[]>();
        apperPerDoc = new HashMap<String, int[]>();
    }

    public void doProcessInBackground() {
        //printItemList();
        generateDocMaxLenght();
        generateAppearPerDoc();
        generateDF();
        generateIDF();
        generateTmpTable();
        printDocTable();
    }


    public void addNewDocDetails(String docName, String[] items, int[] tf) {
        int ln = items.length;
        String[] uniqueStringItems = new String[0];
        ArrayList<String> tmp = new ArrayList<String>();
        ArrayList<Integer> tmp_int = new ArrayList<Integer>();
        for (int i = 0; i < ln; i++) {
            String lowerCaseItems = items[i].toLowerCase();
            makeItemList(lowerCaseItems);
            if (!tmp.contains(lowerCaseItems)) {
                tmp.add(lowerCaseItems);
                tmp_int.add(new Integer(tf[i]));
            }
        }
        uniqueStringItems = (String[]) tmp.toArray(uniqueStringItems);
        ln = uniqueStringItems.length;
        int[] uTf = new int[ln];
        for (int i = 0; i < ln; i++) {
            uTf[i] = tmp_int.get(i).intValue();
        }
        itemsPerDoc.add(uniqueStringItems);
        tfsPerDoc.add(uTf);
    }

    private void makeItemList(String itemName) {
        if (!itemlist.contains(itemName))
            itemlist.add(itemName);
    }

    private void printItemList() {
        System.out.println(itemlist);
    }

    private void generateAppearPerDoc() {
        int ln = itemlist.size();
        for (int i = 1; i <= totalDocNumber; i++) {
            int apperInt[] = new int[ln];
            String[] itemListPerDocs = itemsPerDoc.get(i - 1);
            int[] tf = tfsPerDoc.get(i - 1);
            String docName = "DOC" + Integer.toString(i);
            for (int j = 0; j < ln; j++) {
                int pos = isHave(itemListPerDocs, itemlist.get(j));
                if (pos >= 0)
                    apperInt[j] = tf[pos];
                else
                    apperInt[j] = 0;
            }
            apperPerDoc.put(docName, apperInt);
        }
    }

    private void generateDF() {
        int ln = itemlist.size();
        df = new int[ln];
        int[][] con = new int[totalDocNumber][ln];
        for (int i = 1; i <= totalDocNumber; i++) {
            int[] tmp = apperPerDoc.get("DOC" + Integer.toString(i));
            con[i - 1] = tmp;
        }

        for (int i = 0; i < ln; i++) {
            int cnt = 0;
            for (int j = 0; j < totalDocNumber; j++) {
                if (con[j][i] != 0)
                    cnt++;
            }
            df[i] = cnt;
        }
    }

    private void generateIDF() {
        int ln = itemlist.size();
        idf = new double[ln];
        for (int i = 0; i < ln; i++) {
            idf[i] = log2((double) totalDocNumber, (double) df[i]);
        }
    }

    private int isHave(String[] str, String item) {
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(item))
                return i;
        }
        return -1;
    }


    private double roundTwoDecimals(double d) {
        DecimalFormat fourDForm = new DecimalFormat("#.####");
        return Double.valueOf(fourDForm.format(d));
    }

    private double log2(double x, double y) {
        return roundTwoDecimals(Math.log(x / y) / Math.log(2));
    }

    private void generateDocMaxLenght() {
        int tmp = -1;
        for (int i = 1; i <= totalDocNumber; i++) {
            String docName = "DOC" + Integer.toString(i);
            if (docName.length() > tmp)
                tmp = docName.length();
        }
        maxDocLength = tmp;
    }

    private void generateTmpTable() {
        int ln = itemlist.size();
        String tmp = "| ";
        for (int l = 0; l < ln; l++) {
            String str = itemlist.get(l);
            String doubleNumer = Double.toString(idf[l]);
            if (l > 0) {
                tmp += " | ";
            }
            tmp += getSpace(str.length() > doubleNumer.length() ? str.length() : doubleNumer.length());
        }
        tmp += " |";
        tmpItemMapping = tmp;
    }

    private void printHeads() {
        int ln = tmpItemMapping.length();
        String tmp = tmpItemMapping;
        int cnt = 0;
        for (int i = 0; i < ln - 1; i++) {
            if (tmp.charAt(i) == '|') {
                String theItem = itemlist.get(cnt);
                cnt++;
                int l = 0;
                for (int j = i + 2; j < theItem.length() + i + 2; j++) {
                    tmp = afterReplace(tmp, j, theItem.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printDocAppearance() {
        int prn = maxDocLength + 2 + tmpItemMapping.length();
        int ln = tmpItemMapping.length();
        for (int i = 1; i <= totalDocNumber; i++) {
            if (i > 1)
                print__(prn);
            String docName = "DOC" + Integer.toString(i);
            System.out.print(" " + docName + getSpace(maxDocLength - docName.length()) + " ");
            int[] mat = apperPerDoc.get(docName);
            String tmp = tmpItemMapping;
            int cnt = 0;
            for (int k = 0; k < ln - 1; k++) {
                if (tmp.charAt(k) == '|') {
                    String theItem = Integer.toString(mat[cnt]);
                    cnt++;
                    int l = 0;
                    for (int j = k + 2; j < theItem.length() + k + 2; j++) {
                        tmp = afterReplace(tmp, j, theItem.charAt(l));
                        l++;
                    }
                }
            }
            System.out.println(tmp);
        }
    }

    private void printDF() {
        int ln = tmpItemMapping.length();
        String dfname = "DF";
        System.out.print(" " + dfname + getSpace(maxDocLength - dfname.length()) + " ");
        String tmp = tmpItemMapping;
        int cnt = 0;
        for (int k = 0; k < ln - 1; k++) {
            if (tmp.charAt(k) == '|') {
                String theItem = Integer.toString(df[cnt]);
                cnt++;
                int l = 0;
                for (int j = k + 2; j < theItem.length() + k + 2; j++) {
                    tmp = afterReplace(tmp, j, theItem.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printIDF() {
        int ln = tmpItemMapping.length();
        String idfName = "IDF";
        System.out.print(" " + idfName + getSpace(maxDocLength - idfName.length()) + " ");
        String tmp = tmpItemMapping;
        int cnt = 0;
        for (int k = 0; k < ln - 1; k++) {
            if (tmp.charAt(k) == '|') {
                String theItem = Double.toString(idf[cnt]);
                cnt++;
                int l = 0;
                for (int j = k + 2; j < theItem.length() + k + 2; j++) {
                    tmp = afterReplace(tmp, j, theItem.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printDocTable() {
        System.out.print("\nDOC table with DF and IDF:\n");
        System.out.println("--------------------------\n");
        int prn = maxDocLength + 2 + tmpItemMapping.length();
        print__(prn);
        System.out.print(getSpace(maxDocLength + 2));
        printHeads();
        print__(prn);
        printDocAppearance();
        print__(prn);
        printDF();
        print__(prn);
        printIDF();
        print__(prn);
    }

    private String getSpace(int n) {
        String tmp = "";
        for (int i = 0; i < n; i++)
            tmp += " ";
        return tmp;
    }

    private String afterReplace(String tmp, int pos, char ch) {
        StringBuilder strBuilder = new StringBuilder(tmp);
        strBuilder.setCharAt(pos, ch);
        String str = strBuilder.toString();
        return str;
    }

    private void print__(int n) {
        for (int i = 0; i < n; i++)
            System.out.print("-");
        System.out.println();
    }

    public int getDFValue(String str) {
        int pos = getItemPosition(str);
        if (pos >= 0)
            return df[pos];
        return -1;
    }

    public double getIDFValue(String str) {
        int pos = getItemPosition(str);
        if (pos >= 0)
            return idf[pos];
        return -1;
    }

    private int getItemPosition(String str) {
        for (int i = 0; i < itemlist.size(); i++)
            if (itemlist.get(i).equals(str))
                return i;
        return -1;
    }

    public double[] printCosineSimilarity(double[] res,int resLn,ArrayList<String> list)
    {
        System.out.println("\nCosine Similarity:\n");
        double[] ans = new double[totalDocNumber];
        for(int i=0;i<totalDocNumber;i++)
        {
            String docName = "DOC" + Integer.toString(i+1);
            int[] inTF = apperPerDoc.get(docName); // Individual Appearacne Per Doc
            System.out.print("(" + docName + "," + "Q) : ");
            double up = 0.0;
            for(int j=0;j<resLn;j++)
            {
                String qItem = list.get(j);
                int pos = getItemPosition(qItem);
                int d = inTF[pos];
                if(d!=0){
                    up += res[j] * log2((double)totalDocNumber,(double)d);
                }
            }
            up = roundTwoDecimals(up);
            double deno_left = CosineSimilarityCalculationHelper.leftDenominator(inTF,itemlist.size(),totalDocNumber);
            double deno_right = CosineSimilarityCalculationHelper.rightDenominator(res,resLn);
            double finalAns = roundTwoDecimals(up / (deno_left*deno_right));
            System.out.println(finalAns);
            ans[i] = finalAns;
        }
        return ans;
    }

}
