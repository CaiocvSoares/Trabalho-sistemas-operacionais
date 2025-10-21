import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        String diretorio = "E:\\";

        for (int i = 1; i <= 10; i++) {
            String numero = String.format("%02d", i);
            String nomeEntrada = "TESTE-" + numero + ".txt";
            String nomeSaida = "TESTE-" + numero + "-RESULTADO.txt";

            String resultados = "";
            int quantum = 0;

            Path caminhoEntrada = Paths.get(diretorio, nomeEntrada);
            Path caminhoSaida = Paths.get(diretorio, nomeSaida);

            try (
                BufferedReader leitor = Files.newBufferedReader(caminhoEntrada, StandardCharsets.UTF_8);
                BufferedWriter escritor = Files.newBufferedWriter(caminhoSaida, StandardCharsets.UTF_8)
            ){
                String linha;
                String [] linhas = new String [2]; 
                int contador = 0;
                List<Processador> processos =  new ArrayList<>();
                Escalonador objeto = new Escalonador();
                while ((linha = leitor.readLine()) != null) {
                    if  (contador == 0){
                        quantum = Integer.parseInt(linha);
                        contador++;
                    }
                    else{
                        linhas = linha.split(" ");
                        Processador process = new Processador ( Integer.parseInt(linhas[0]), Integer.parseInt(linhas[1]));
                        processos.add(process);
                    }
                    
                }
                for(int l = 0; l < 4; l++){
                    switch (l) {
                            case 0:
                                resultados = objeto.calcularFIFO(processos);
                                escritor.write(String.valueOf(resultados)); 
                                escritor.newLine();
                                break;
                            case 1:
                                resultados = objeto.calcularSJF(processos);
                                escritor.write(String.valueOf(resultados)); 
                                escritor.newLine();
                                break;
                            case 2:
                                resultados = objeto.calcularSRT(processos);
                                escritor.write(String.valueOf(resultados)); 
                                escritor.newLine();
                                break;
                            default:
                                resultados = objeto.calcularRR(processos, quantum);
                                escritor.write(String.valueOf(resultados)); 
                                escritor.newLine();
                                break;
                    }        
                }
                
                System.out.println("Gerado: " + nomeSaida);

            } catch (IOException e) {
                System.err.println("Erro ao processar " + nomeEntrada + ": " + e.getMessage());
            }
        }

        System.out.println("Todos os arquivos foram processados.");
    }
}