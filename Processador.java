public class Processador {
    int tChegada;
    int tDuracao;
    int tRestante; 
    int tInicio = -1; 
    int tFim;

    public Processador(int tChegada, int tDuracao) {
        this.tChegada = tChegada;
        this.tDuracao = tDuracao;
        this.tRestante = tDuracao;
    }

    //pr o RR nao atrapalhar os demais
    public Processador clone() {
        return new Processador(tChegada, tDuracao);
    }
}
