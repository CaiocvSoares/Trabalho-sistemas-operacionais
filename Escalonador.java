import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Escalonador {
    float mediatempresp, mediatempesp, mediaturn;
    public int quantum = 0;

    public void calcularFIFO(List<Processador> processos) {
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

    public void calcularSJF(List<Processador> processos) {
        processos.sort(Comparator.comparingInt(p -> p.tChegada));

        List<Processador> prontos = new ArrayList<>();
        int tempoAtual = 0;
        float somaResp = 0, somaEsp = 0, somaTurn = 0;

        while (!processos.isEmpty() || !prontos.isEmpty()) {
            if (!processos.isEmpty() && processos.get(0).tChegada <= tempoAtual) {
                prontos.add(processos.remove(0));
            }

            if (prontos.isEmpty()) {
                tempoAtual = processos.get(0).tChegada;
                continue;
            }
            prontos.sort(Comparator.comparingInt(p -> p.tDuracao));
            Processador p = prontos.remove(0);

            p.tInicio = tempoAtual;
            p.tFim = tempoAtual + p.tDuracao;
            tempoAtual = p.tFim;

            somaResp += p.tInicio - p.tChegada;
            somaEsp += p.tInicio - p.tChegada;
            somaTurn += p.tFim - p.tChegada;
        }

        mediatempresp = somaResp / processos.size();
        mediatempesp = somaEsp / processos.size();
        mediaturn = somaTurn / processos.size();
    }

    public void calcularSRT(List<Processador> processos) {
        processos.sort(Comparator.comparingInt(p -> p.tChegada));

        PriorityQueue<Processador> fila = new PriorityQueue<>(
                Comparator.comparingInt(p -> p.tRestante));

        int tempoAtual = 0, index = 0;
        float somaResp = 0, somaEsp = 0, somaTurn = 0;

        // enquanto houver processos a chegar ou na fila
        while (!fila.isEmpty() || index < processos.size()) {
            
            while (index < processos.size() && processos.get(index).tChegada <= tempoAtual) {
                fila.add(processos.get(index));
                index++;
            }

            if (fila.isEmpty()) {
                // se nenhum processo chegou ainda, avança o tempo
                tempoAtual = processos.get(index).tChegada;
                continue;
            }

            Processador p = fila.poll();

            
            if (p.tInicio == -1) {
                p.tInicio = tempoAtual;
                somaResp += p.tInicio - p.tChegada;
            }

            
            p.tRestante--;
            tempoAtual++;

            
            while (index < processos.size() && processos.get(index).tChegada <= tempoAtual) {
                fila.add(processos.get(index));
                index++;
            }

            if (p.tRestante > 0) {
                fila.add(p);
            } else {
                // processo terminou
                p.tFim = tempoAtual;
                somaEsp += p.tFim - p.tChegada - p.tDuracao;
                somaTurn += p.tFim - p.tChegada;
            }
        }

        mediatempesp = somaEsp / processos.size();
        mediatempresp = somaResp / processos.size();
        mediaturn = somaTurn / processos.size();
    }

    public void calcularRR(List<Processador> processos, int quantum) {
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

            if (p.tInicio == -1) {
                p.tInicio = tempoAtual;
                somaResp += p.tInicio - p.tChegada;
            }

            int tempoExec = Math.min(quantum, p.tRestante);
            p.tRestante -= tempoExec;
            tempoAtual += tempoExec;

            while (index < copia.size() && copia.get(index).tChegada <= tempoAtual) {
                fila.add(copia.get(index));
                index++;
            }

            if (p.tRestante > 0) {
                fila.add(p);
            } else {
                p.tFim = tempoAtual;
                somaEsp += (p.tFim - p.tChegada - p.tDuracao);
                somaTurn += (p.tFim - p.tChegada);
            }
        }

        mediatempresp = somaResp / processos.size();
        mediatempesp = somaEsp / processos.size();
        mediaturn = somaTurn / processos.size();
    }
}
