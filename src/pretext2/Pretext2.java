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
        StringBuilder atributos = lerArquivo("discover", ".names");
        //    *************
        String arquivoNames = atributos.toString();
        arquivoNames = arquivoNames.replaceAll("(att_class.\n)", "@RELATION teste-incial");
        arquivoNames = arquivoNames.replaceAll("filename:string:ignore.", "\n");
        arquivoNames = arquivoNames.replaceAll("\":integer\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("\":real\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("(att_class:nominal\\(\")", "@ATTRIBUTE classe {");
        arquivoNames = arquivoNames.replaceAll("\"\\)\\.", "}");
        arquivoNames = arquivoNames.replaceAll("\",\"", ",");
        arquivoNames = arquivoNames.replaceAll("\"", "@ATTRIBUTE ");
        System.out.println("Salvando Atributos");
        salvarArquivo(arquivoNames.toString(), nomeArquivo);

        System.out.println("Lendo dados");
        StringBuilder dados = lerArquivo("discover", ".data");
        String arquivoData = dados.toString();
        arquivoData = "@DATA" + "\n" + arquivoData;
        arquivoData = arquivoData.replaceAll("\".*\",", "");
        System.out.println("Salvando Dados");
        salvarArquivo(arquivoData.toString(), nomeArquivo);
      // ****************** 
        //  System.out.println("Lendo dados");
        //  StringBuilder dados = lerArquivo("discover", ".data");

        //System.out.println(ar);
        //System.out.println(pretextToARFF(dados, atributos));
        //  salvarArquivo(pretextToARFF(dados.toString(), atributos.toString()).toString(), nomeArquivo);
        // pretextToARFF(atributos.toString(), dados.toString(), nomeArquivo);
      //  salvarArquivo(nomeArquivo, nomeArquivo);
        // salvarArquivo(pretextToARFF(dados, atributos));
    }

    public static void pretextToARFF(String arquivoData, String arquivoNames, String nomeArquivo) {
        System.out.println("convertendo atributos");
        arquivoNames = arquivoNames.replaceAll("(att_class.\n)", "@RELATION teste-incial");
        arquivoNames = arquivoNames.replaceAll("filename:string:ignore.", "\n");
        arquivoNames = arquivoNames.replaceAll("\":integer\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("\":real\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("(att_class:nominal\\(\")", "@ATTRIBUTE classe {");
        arquivoNames = arquivoNames.replaceAll("\"\\)\\.", "}");
        arquivoNames = arquivoNames.replaceAll("\",\"", ",");
        arquivoNames = arquivoNames.replaceAll("\"", "@ATTRIBUTE ");
        System.out.println("Salvando Atributos");
        salvarArquivo(arquivoNames.toString(), nomeArquivo);

        System.out.println("convertendo dados");
        arquivoData = "@DATA" + "\n" + arquivoData;
        arquivoData = arquivoData.replaceAll("\".*\",", "");
        System.out.println("Salvando Dados");
        salvarArquivo(arquivoData.toString(), nomeArquivo);

       // StringBuilder arquivoFinal = new StringBuilder();
        //arquivoFinal.append(arquivoNames).append("\n").append(arquivoData);
        // return arquivoFinal;
    }

    public static String convertArquivoData(String arquivoData) {
        arquivoData = "@DATA" + "\n" + arquivoData;
        arquivoData = arquivoData.replaceAll("\".*\",", "");
        return arquivoData;
    }

    public static String convertArquivoNames(String arquivoNames) {
        arquivoNames = arquivoNames.replaceAll("(att_class.\n)", "@RELATION teste-incial");
        arquivoNames = arquivoNames.replaceAll("filename:string:ignore.", "\n");
        arquivoNames = arquivoNames.replaceAll("\":integer\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("\":real\\.", " NUMERIC");
        arquivoNames = arquivoNames.replaceAll("(att_class:nominal\\(\")", "@ATTRIBUTE classe {");
        arquivoNames = arquivoNames.replaceAll("\"\\)\\.", "}");
        arquivoNames = arquivoNames.replaceAll("\",\"", ",");
        arquivoNames = arquivoNames.replaceAll("\"", "@ATTRIBUTE ");
        return arquivoNames;
    }

    public static StringBuilder lerArquivo(String nome, String extensao) {
        StringBuilder linha = new StringBuilder();
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
                    linha.append(br.readLine());
                    linha.append("\n");
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

    public static void salvarArquivo(String texto, String nomeArquivo) {
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
            try (FileWriter fw = new FileWriter(arquivo, true);
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
