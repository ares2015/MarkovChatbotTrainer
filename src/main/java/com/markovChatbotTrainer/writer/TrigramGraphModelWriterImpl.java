package com.markovChatbotTrainer.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Oliver on 4/27/2017.
 */
public class TrigramGraphModelWriterImpl implements TrigramGraphModelWriter, Runnable {

    private String path;

    private Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph;

    public TrigramGraphModelWriterImpl(String path, Map<String, Map<String, TreeMap<Integer, List<String>>>> trigramGraph) {
        this.path = path;
        this.trigramGraph = trigramGraph;
    }

    @Override
    public void run() {
        try {
            writeTrigramModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeTrigramModel() throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);
            Iterator<Map.Entry<String, Map<String, TreeMap<Integer, List<String>>>>> iterator = trigramGraph.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, Map<String, TreeMap<Integer, List<String>>>> next = iterator.next();
                String token1 = next.getKey();
                Map<String, TreeMap<Integer, List<String>>> map = next.getValue();
                Iterator<Map.Entry<String, TreeMap<Integer, List<String>>>> iterator2 = map.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<String, TreeMap<Integer, List<String>>> next2 = iterator2.next();
                    String token2 = next2.getKey();
                    stringBuilder.append(token1 + " " + token2);
                    stringBuilder.append(">>>>>");
                    TreeMap<Integer, List<String>> map2 = next2.getValue();
                    Iterator<Map.Entry<Integer, List<String>>> iterator3 = map2.entrySet().iterator();
                    while (iterator3.hasNext()) {
                        Map.Entry<Integer, List<String>> next3 = iterator3.next();
                        Integer frequency = next3.getKey();
                        List<String> tokens = next3.getValue();
                        stringBuilder.append("#");
                        stringBuilder.append(frequency);
                        stringBuilder.append("@");
                        for (String token : tokens) {
                            stringBuilder.append(token);
                            stringBuilder.append("@");
                        }
                    }
                }
                System.out.println(stringBuilder.toString());
                bw.write(stringBuilder.toString());
                bw.newLine();
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
