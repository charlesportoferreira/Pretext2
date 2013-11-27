package pretext2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class Pretext2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String nomeArquivo = args.length > 0 ? args[0] : "resultado.arff";
        
        System.out.println("Lendo Atributos");
        String atributos = lerArquivo("discover", ".names");
        System.out.println("Lendo dados");
        String dados = lerArquivo("discover", ".data");

            //System.out.println(ar);
        //System.out.println(pretextToARFF(dados, atributos));
        salvarArquivo(pretextToARFF(dados, atributos), nomeArquivo);
       // salvarArquivo(pretextToARFF(dados, atributos));

    }

    public static String pretextToARFF(String arquivoData, String arquivoNames) {
        System.out.println("convertendo atributos");
        arquivoNames = arquivoNames.replaceAll("(att_class.\n)", "@RELATION teste-incial");
        arquivoNames = arquivoNames.replaceAll("filename:string:ignore.", "\n");
        arquivoNames = arquivoNames.replaceAll("\":integer\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("\":real\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("(att_class:nominal\\(\")", "@ATTRIBUTE classe {");
        arquivoNames = arquivoNames.replaceAll("\"\\)\\.", "}");
        arquivoNames = arquivoNames.replaceAll("\",\"", ",");
        arquivoNames = arquivoNames.replaceAll("\"", "@ATTRIBUTE ");

        System.out.println("convertendo dados");
        arquivoData = "@DATA" + "\n" + arquivoData;
        arquivoData = arquivoData.replaceAll("\".*\",", "");

        String arquivoFinal = arquivoNames + "\n" + arquivoData;
        return arquivoFinal;
    }

    public static String lerArquivo(String nome, String extensao) {
        String linha = "";
        File arquivo = new File(nome + extensao);
        int qtdLinha = 0;

        // logica para contar o numero de linhas do arquivo
        LineNumberReader linhaLeitura;
        try {
            linhaLeitura = new LineNumberReader(new FileReader(arquivo));
            try {
                linhaLeitura.skip(arquivo.length());
                qtdLinha = linhaLeitura.getLineNumber();
                // System.out.println("numero de linhas = " + qtdLinha);
            } catch (IOException ex) {
                Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            FileReader fr = new FileReader(arquivo);
            //System.out.println(arquivo.length());
            BufferedReader br = new BufferedReader(fr);
            int i = 0;
            try {
                while (br.ready()) {
                    linha += br.readLine();
                    linha += "\n";
                    i = i + 1;

                    //imprime de dez em dez %
                    if ((i * 100.0 / qtdLinha) % 10.0 == 0) {
                        System.out.println(i * 100 / qtdLinha + "% lido");
                    }

                }
                br.close();
                fr.close();

            } catch (IOException ex) {
                Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return linha;
    }

    public static void salvarArquivo(String texto,String nomeArquivo ) {
        File arquivo = new File(nomeArquivo);
        boolean existe = arquivo.exists();
        try {
            if (!existe) {
                arquivo.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            try (FileWriter fw = new FileWriter(arquivo);
                    BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(texto);
                bw.newLine();
                bw.close();
                fw.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Pretext2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
