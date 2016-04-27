import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Komol on 4/27/2016.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static DocumentSorting docSorting;
    private static SortBasedOnAnswers sortBasedOnAnswers;

    public static void main(String[] args) {

        System.out.print("Enter total Number of Documnets: ");
        int n = scanner.nextInt();
        docSorting = new DocumentSorting(n);
        scanner.nextLine();
        String docName;
        String doc;

        for (int i = 1; i <= n; i++) {
            docName = "DOC" + Integer.toString(i);
            //System.out.print("Enter " + docName + ": ");
            doc = scanner.nextLine();
            String[] docItems = doc.split(" ");
            int ln = docItems.length;
            int[] mat = new int[ln];
            //System.out.print("Enter TF for " + docName + ": ");
            for (int j = 0; j < ln; j++) {
                mat[j] = scanner.nextInt();
            }
            docSorting.addNewDocDetails(docName, docItems, mat);
            scanner.nextLine();
        }

        docSorting.doProcessInBackground();

        System.out.println("\nNow Enter Queries: (enter \"#\" to stop)\n");

        String query = scanner.nextLine();
        while (!query.equals("#")) {

            QueryProcessor queryProcessor = new QueryProcessor();

            queryProcessor.processNewQuery(query);
            queryProcessor.doProcessInBackground();

            ArrayList<String> queryItems = queryProcessor.getItemsKeys();
            int qln = queryItems.size();

            for (int j = 0; j < qln; j++) {
                String qItem = queryItems.get(j);
                queryProcessor.populate(j, docSorting.getDFValue(qItem));
                queryProcessor.populate(j, docSorting.getIDFValue(qItem));
            }

            queryProcessor.printQueryTable();
            queryProcessor.printNormalizing();


            double[] queryInfo = queryProcessor.getNormalizingValues();

            //Now Cosine Similarity
            double[] finalanswers = docSorting.printCosineSimilarity(queryInfo, qln, queryItems);
            sortBasedOnAnswers = new SortBasedOnAnswers();
            for (int i = 1; i <= n; i++) {
                String name = "DOC" + Integer.toString(i);
                sortBasedOnAnswers.addNewDoc(name, finalanswers[i - 1]);
            }

            sortBasedOnAnswers.sort();
            sortBasedOnAnswers.print();


            query = scanner.nextLine();
        }
    }
}
