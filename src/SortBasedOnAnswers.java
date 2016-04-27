import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Komol on 4/28/2016.
 */
public class SortBasedOnAnswers {
    private ArrayList<String> docNames;
    private ArrayList<Double> answers;

    public SortBasedOnAnswers()
    {
        docNames = new ArrayList<String>();
        answers = new ArrayList<Double>();
    }

    public void addNewDoc(String name,Double ans){
        docNames.add(name);
        answers.add(ans);
    }

    public void sort()
    {
        // Bubble sort :v
        int ln = docNames.size();
        for(int i=0;i<ln-1;i++)
        {
            for(int j=0;j<ln-i-1;j++){
                if(answers.get(j) < answers.get(j+1)){
                    double tmp = answers.get(j);
                    answers.set(j,answers.get(j+1));
                    answers.set(j+1,tmp);

                    String str =  docNames.get(j);
                    docNames.set(j,docNames.get(j+1));
                    docNames.set(j+1,str);

                }
            }
        }
    }

    public void print()
    {
        int ln = docNames.size();
        for(int i=0;i<ln;i++)
        {
            if(i>0)
                System.out.print(" > ");
            String str = docNames.get(i);
            System.out.print(str);
        }
        System.out.println();
    }

}
