package lista;

import static lista.ListaMemoriaDisponivel.*;

public class Bloco<T> {
    private String endereco;
    private Integer tamanho;
    private Bloco<T> proximo;

    public Bloco(String endereco) {
        if (endereco.indexOf("h") != -1) {
            endereco = endereco.substring(0, endereco.length() - 1);
        }
        this.endereco = endereco;
        this.proximo = null;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getFimBloco() {
        return IntToHexa(hexaToInt(endereco) + tamanho - 1);
    }

    public Bloco getProximo() {
        return proximo;
    }

    public void setProximo(Bloco proximo) {
        this.proximo = proximo;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }


    public void atualizarEndereco(Integer lenght) {
        int novoEndereco = Integer.parseInt(this.endereco, 16) + lenght;
        this.endereco = Integer.toHexString(novoEndereco);
    }

    @Override
    public String toString() {
        String bloco = "[";
        if (this != null) {
            bloco += getEndereco() + "h, " + getTamanho();
        }
        bloco += "]";

        return bloco;
    }
}
