import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Escalonador {
    float mediatempresp, mediatempesp, mediaturn;
    int quantum = 0;

    //Modelo
    
    void Elementos(){
        //Fazer alguma logica pra pegar elementos para aplicar 
    }

    public void calcularFIFO(List<Processador> processos) {
        //pega pelo tempo de chegada
        processos.sort(Comparator.comparingInt(p -> p.tChegada));

        float somaResp = 0, somaEsp = 0, somaTurn = 0;
        int tempoAtual = 0;

        for (Processador p : processos) {
            p.tInicio = Math.max(tempoAtual, p.tChegada);
            p.tFim = p.tInicio + p.tDuracao;

            int tempoResposta = p.tInicio - p.tChegada;
            int tempoEspera = p.tFim - p.tChegada - p.tDuracao;
            int turnaround = p.tFim - p.tChegada;

            somaResp += tempoResposta;
            somaEsp += tempoEspera;
            somaTurn += turnaround;

            tempoAtual = p.tFim;
        }
        //medias
        mediatempresp = somaResp / processos.size();
        mediatempesp = somaEsp / processos.size();
        mediaturn = somaTurn / processos.size();
    }

    void calcularSJF (){
        /*
        calcular tempo de espera
        calcular tempo de execução
        calcular tempo de resposta
        Calcular turnaround (tempo de espera + execução)
        soma acumulativa e media
        */
    }
    void calcularSRT(){
        /*
        calcular tempo de espera
        calcular tempo de execução
        calcular tempo de resposta
        Calcular turnaround (tempo de espera + execução)
        soma acumulativa e media
        */
    }

    public void calcularRR(List<Processador> processos, int quantum) {
        //copia pr n mudar a lista original
        List<Processador> copia = new ArrayList<>();
        for (Processador p : processos)
            copia.add(p.clone());
        copia.sort(Comparator.comparingInt(p -> p.tChegada));
    
        Queue<Processador> fila = new LinkedList<>();
        int tempoAtual = 0, index = 0;
        float somaResp = 0, somaEsp = 0, somaTurn = 0;

        while (!fila.isEmpty() || index < copia.size()) {

            while (index < copia.size() && copia.get(index).tChegada <= tempoAtual) {
                fila.add(copia.get(index));
                index++;
            }

            if (fila.isEmpty()) {
                tempoAtual = copia.get(index).tChegada;
                continue;
            }
    
            Processador p = fila.poll();
    
            // marca o início da primeira
            if (p.tInicio == -1)
                p.tInicio = tempoAtual;

            int tempoExec = Math.min(quantum, p.tRestante);
            p.tRestante -= tempoExec;
            tempoAtual += tempoExec;

            while (index < copia.size() && copia.get(index).tChegada <= tempoAtual) {
                fila.add(copia.get(index));
                index++;
            }

            if (p.tRestante > 0) {
                fila.add(p);
            } 
            // calcula tempos finais
            else {
                p.tFim = tempoAtual;
                somaResp += p.tInicio - p.tChegada;
                somaEsp += (p.tFim - p.tChegada - p.tDuracao);
                somaTurn += (p.tFim - p.tChegada);
            }
        }
    
        // Calcula médias dos tempos
        mediatempresp = somaResp / processos.size();
        mediatempesp = somaEsp / processos.size();
        mediaturn = somaTurn / processos.size();
    }
    

}
