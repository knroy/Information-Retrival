import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Komol on 4/27/2016.
 */
public class QueryProcessor {

    private Map<String,Integer> items;
    private ArrayList<String> mapKeys;
    private int tf[];
    private int df[];
    private double idf[];
    private String tempMap;
    private double[] normalizingValue;

    public QueryProcessor()
    {
        items = new HashMap<String, Integer>();
        mapKeys = new ArrayList<String>();
    }

    public void processNewQuery(String query)
    {
        String[] queryItemList = query.split(" ");
        int ln = queryItemList.length;
        for(int i=0;i<ln;i++)
        {
            String tmp = queryItemList[i].toLowerCase();
            if(items.containsKey(tmp)){
                items.put(tmp,items.get(tmp).intValue() + 1);
            }else{
                items.put(tmp,1);
            }
        }
    }

    public void doProcessInBackground()
    {
        generateItemKeys();
        initFrequencies();
        generateTF();
    }

    private void generateTempItemMap()
    {
        int ln = mapKeys.size();
        tempMap = "| ";
        for(int i=0;i<ln;i++)
        {
            if(i>0)
                tempMap += " | ";
            String item = mapKeys.get(i);
            String douneString = Double.toString(idf[i]);
            tempMap += getSpace(item.length() > douneString.length() ? item.length() : douneString.length());
        }
        tempMap += " |";
    }


    private String getSpace(int n) {
        String tmp = "";
        for (int i = 0; i < n; i++)
            tmp += " ";
        return tmp;
    }

    private void generateItemKeys()
    {
        for(String key : items.keySet()){
            mapKeys.add(key);
        }
    }

    private void initFrequencies()
    {
        int ln = mapKeys.size();
        tf = new int[ln];
        df = new int[ln];
        idf = new double[ln];
        for(int i=0;i<ln;i++){
            df[i] = 0;
            idf[i] = 0.0;
        }
    }

    public void generateTF()
    {
        int ln = mapKeys.size();
        for(int i=0;i<ln;i++)
        {
            tf[i] = items.get(mapKeys.get(i));
        }
    }

    public void populate(int pos,int value){
        df[pos] = value;
    }

    public void populate(int pos,double value){
        idf[pos] = value;
    }

    public ArrayList<String> getItemsKeys()
    {
        return mapKeys;
    }

    private void printHeads()
    {
        int ln = tempMap.length();
        String tmp = tempMap;
        int cnt = 0;
        for(int i=0;i<ln-1;i++)
        {
            if(tempMap.charAt(i) == '|')
            {
                String theName = mapKeys.get(cnt);
                cnt++;
                int l = 0;
                for(int j=i+2;j<theName.length() + i + 2;j++){
                    tmp = replace(tmp,j,theName.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printTF()
    {
        int ln = tempMap.length();
        String tmp = tempMap;
        int cnt = 0;
        for(int i=0;i<ln-1;i++)
        {
            if(tempMap.charAt(i) == '|')
            {
                String theName = Integer.toString(tf[cnt]);
                cnt++;
                int l = 0;
                for(int j=i+2;j<theName.length() + i + 2;j++){
                    tmp = replace(tmp,j,theName.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printDF()
    {
        int ln = tempMap.length();
        String tmp = tempMap;
        int cnt = 0;
        for(int i=0;i<ln-1;i++)
        {
            if(tempMap.charAt(i) == '|')
            {
                String theName = Integer.toString(df[cnt]);
                cnt++;
                int l = 0;
                for(int j=i+2;j<theName.length() + i + 2;j++){
                    tmp = replace(tmp,j,theName.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    private void printIDF()
    {
        int ln = tempMap.length();
        String tmp = tempMap;
        int cnt = 0;
        for(int i=0;i<ln-1;i++)
        {
            if(tempMap.charAt(i) == '|')
            {
                String theName = Double.toString(idf[cnt]);
                cnt++;
                int l = 0;
                for(int j=i+2;j<theName.length() + i + 2;j++){
                    tmp = replace(tmp,j,theName.charAt(l));
                    l++;
                }
            }
        }
        System.out.println(tmp);
    }

    public void printQueryTable()
    {
        System.out.println("\nQuery Table:\n");
        generateTempItemMap();
        int ln = mapKeys.size();
        int prn = 7 + tempMap.length();
        print__(prn);
        System.out.print(" Query ");
        printHeads();
        print__(prn);
        System.out.print(" TF    ");
        printTF();
        print__(prn);
        System.out.print(" DF    ");
        printDF();
        print__(prn);
        System.out.print(" IDF   ");
        printIDF();
        print__(prn);
    }

    private void print__(int n) {
        for (int i = 0; i < n; i++)
            System.out.print("-");
        System.out.println();
    }

    private String replace(String tmp, int pos, char ch) {
        StringBuilder strBuilder = new StringBuilder(tmp);
        strBuilder.setCharAt(pos, ch);
        String str = strBuilder.toString();
        return str;
    }

    public void printNormalizing()
    {
        System.out.println("\nNormalizing:");
        System.out.println("\n------------\n");
        int ln = mapKeys.size();
        normalizingValue = new double[ln];
        for(int i=0;i<ln;i++)
        {
            String key = mapKeys.get(i);
            System.out.print("Query( "+ key+" ): ");
            double nValue = calculateNormalizing(tf[i],df[i],idf[i]);
            System.out.println(nValue);
            normalizingValue[i] = nValue;
        }

        System.out.println();
    }

    public double[] getNormalizingValues()
    {
        return normalizingValue;
    }

    public double calculateNormalizing(int TF,int DF,double IDF)
    {
        return ((double)TF * IDF)/(double) DF;
    }

}
