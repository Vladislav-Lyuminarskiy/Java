package FileEditor;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
public class CSVFileEditor {
    public static void show(String _data, BufferedReader _fileReader) throws IOException{
        StringBuilder text = new StringBuilder();
        text.append("| YEAR |     TOTAL |    BIRTHS |    DEATHS |      RATE |\n| ---- | --------- | --------- | --------- | --------- |\n");
        String line;
        List<String> years = Arrays.asList(_data.split(","));
        try (_fileReader) {
            while ((line = _fileReader.readLine()) != null) {
                if (years.contains(line.split(",")[0]) || _data.equals("all")) {
                    String[] data = line.split(",");
                    int prirost = Integer.parseInt(data[2]) - Integer.parseInt(data[3]);
                    text.append(String.format("| %s | %9s | %9s | %9s | %9s |\n", data[0], data[1], data[2], data[3], Integer.toString(prirost)));
                }
            }
        }
        System.out.println(text.toString());
    }

    public static void update(String _data, BufferedReader _fileReader, File _file) throws IOException{
        try {
            StringBuilder text = new StringBuilder();
            String line;
            String year = _data.split(",")[0];
            String successMsg = "created";
            while((line = _fileReader.readLine()) != null){
                if (line.split(",")[0].equals(year)){
                    text.append(_data);
                    text.append("\n");
                    successMsg = "updated";
                }
                else {
                    text.append(line);
                    text.append("\n");
                }
            }
            if (successMsg.equals("created")) {
                text.append(_data);
                text.append("\n");
                text = sort(text);
            }
            try (FileWriter fileWriter = new FileWriter(_file)){
                fileWriter.write(text.toString());
                System.out.println(String.format("%s %s in %s", year, successMsg, _file.getName()));
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public static void delete(String _data, BufferedReader _fileReader, File _file) {
        try {
            StringBuilder text = new StringBuilder();
            String line;
            List<String> years = Arrays.asList(_data.split(","));
            StringBuilder deletedLines = new StringBuilder();
            while ((line = _fileReader.readLine()) != null) {
                if (years.contains(line.split(",")[0])){
                    deletedLines.append(String.format("%s deleted in %s\n", line.split(",")[0], _file.getName()));
                }
                else {
                    text.append(line);
                    text.append("\n");
                }
            }
            try (FileWriter fileWriter = new FileWriter(_file)) {
                fileWriter.write(text.toString());
                System.out.println(deletedLines.toString());
            }
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }
    private static StringBuilder sort(StringBuilder text){
        var lines = Arrays.asList(text.toString().split("\n"));
        lines.sort((new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.split(",")[0].compareTo(o2.split(",")[0]);
            }
        }));
        text = new StringBuilder();
        for (var line:
             lines) {
            text.append(line);
            text.append("\n");
        }
        return text;
    }
}
