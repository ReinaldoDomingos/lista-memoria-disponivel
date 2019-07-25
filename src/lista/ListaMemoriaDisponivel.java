package lista;

public class ListaMemoriaDisponivel<T extends Comparable<T>> {
    private Bloco<T> inicio = new Bloco("00000");

    public ListaMemoriaDisponivel() {
        this.inicio.setTamanho(4096);
    }

    public void devolver(String endereco, Integer tamanho) {
        Bloco<T> novo = new Bloco(endereco);
        novo.setTamanho(tamanho);
        int fimNovo = hexaToInt(novo.getFimBloco());
        if (fimNovo <= 4096) {
            this.adicionarBlocoMemoriaDisponivel(novo);
        }

    }

    public boolean solicitar(Integer lenght) {
        Bloco<T> blocoDisponivel = this.buscaBlocoMemoria(lenght);
        int tamanhoAtual;
        if (inicio == null) {
            return false;
        }

        if (blocoDisponivel != null && blocoDisponivel.getTamanho().compareTo(lenght) != -1) {
            if (blocoDisponivel.getTamanho().compareTo(lenght) == 0) {
                this.removerBlocoMemoria(blocoDisponivel.getEndereco());
                return true;
            } else {
                tamanhoAtual = blocoDisponivel.getTamanho();
                blocoDisponivel.setTamanho(tamanhoAtual - lenght);
                blocoDisponivel.atualizarEndereco(lenght);
                return true;
            }
        } else if (this.inicio != null && this.inicio.getTamanho().compareTo(lenght) != -1) {
            if (this.inicio.getTamanho().compareTo(lenght) == 1) {
                tamanhoAtual = this.inicio.getTamanho();
                this.inicio.setTamanho(tamanhoAtual - lenght);
                this.inicio.atualizarEndereco(lenght);
                return true;
            } else {
                this.removerBlocoMemoria(this.inicio.getEndereco());
                return true;
            }
        } else {
            Bloco<T> proximo;
            if (inicio != null && blocoDisponivel != null) {
                proximo = blocoDisponivel.getProximo();
                if (proximo != null) {
                    int tamanhoProximo = proximo.getTamanho();
                    if (isEqual(tamanhoProximo, lenght)) {
                        removerBlocoMemoria(blocoDisponivel, proximo);
                        return true;
                    } else if (isMaior(tamanhoProximo, lenght)) {
                        proximo.setTamanho(tamanhoProximo - lenght);
                        proximo.atualizarEndereco(lenght);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Bloco<T> juntarBlocos(Bloco<T> anterior, Bloco<T> bloco) {
        boolean isbegin = false;
        if (isEqual(anterior.getEndereco(), inicio.getEndereco()) || isEqual(bloco.getEndereco(), inicio.getEndereco()))
            isbegin = true;
        anterior.setTamanho(anterior.getTamanho() + bloco.getTamanho());
        if (isbegin) {
            if (bloco.getProximo() != null)
                anterior.setProximo(bloco.getProximo());
            inicio = anterior;
        }
        return anterior;
    }

    private Bloco<T> juntarBlocoAtras(Bloco<T> anterior, Bloco<T> bloco) {
        boolean isbegin = false;
        Bloco<T> proximo;
        proximo = anterior.getProximo();
        if (isEqual(anterior.getEndereco(), inicio.getEndereco()) || isEqual(bloco.getEndereco(), inicio.getEndereco()))
            isbegin = true;
        bloco.setTamanho(bloco.getTamanho() + proximo.getTamanho());
        anterior.setProximo(bloco);
        if (isbegin) inicio = bloco;
        return bloco;
    }

    private Bloco<T> juntarBlocosMeio(Bloco<T> anterior, Bloco bloco, boolean meio) {
        Bloco<T> prox = anterior.getProximo();
        juntarBlocos(anterior, bloco);
        if (meio) {
            juntarBlocosMeio(anterior, prox, false);
            anterior.setProximo(prox.getProximo());
        }
        return anterior;
    }

    private void removerBlocoMemoria(Bloco<T> anterior, Bloco<T> bloco) {
        if (bloco.getProximo() == null)
            anterior.setProximo(null);
    }

    private void removerBlocoMemoria(String endereco) {
        Bloco<T> anterior = this.buscaBlocoMemoria(endereco);
        if (anterior == null) {
            this.inicio = this.inicio.getProximo();
        } else {
            Bloco<T> proximo = anterior.getProximo();
            if (proximo != null && proximo.getEndereco().compareTo(endereco) == 0) {
                proximo = null;
            }
        }

    }

    private Bloco<T> buscaBlocoMemoria(String endereco) {
        Bloco<T> ponteiro = this.inicio;

        Bloco anterior = null;
        while (ponteiro != null && ponteiro.getEndereco().compareTo(endereco) == -1) {
            anterior = ponteiro;
            ponteiro = ponteiro.getProximo();
        }

        return anterior;
    }

    private Bloco<T> buscaBlocoMemoria(Integer lenght) {
        Bloco<T> ponteiro = this.inicio;

        Bloco anterior = null;
        while (ponteiro != null && ponteiro.getTamanho().compareTo(lenght) != 1
                && ponteiro.getTamanho().compareTo(lenght) != 0) {
            anterior = ponteiro;
            ponteiro = ponteiro.getProximo();
        }

        return anterior;
    }

    public boolean adicionarBlocoMemoriaDisponivel(Bloco<T> novo) {
        int inicioNovo = hexaToInt(novo.getEndereco());
        int fimNovo = hexaToInt(novo.getFimBloco());
        boolean existe = this.existeBlocoMemoria(novo.getEndereco());
        if (!existe) {
            Bloco<T> anterior = this.buscaAnterior(novo.getEndereco());
            int fimAnterior;
            if (anterior != null) {
                Bloco<T> proximo = anterior.getProximo();
                if (proximo != null) {
                    fimAnterior = hexaToInt(anterior.getFimBloco());
                    int inicioProximo = hexaToInt(proximo.getEndereco());
                    if (this.podeAdicionar(inicioProximo, fimNovo, inicioNovo, fimAnterior)) {
                        if (isFronteira(fimAnterior, inicioNovo, fimNovo, inicioProximo)) {
                            juntarBlocosMeio(anterior, novo, true);
                            return true;
                        }
                        if (isFronteira(fimAnterior, inicioNovo)) {
                            juntarBlocos(anterior, novo);
                            return true;
                        }
                        if (isFronteira(fimNovo, inicioProximo)) {
                            juntarBlocoAtras(anterior, novo);
                            return true;
                        }
                        novo.setProximo(anterior.getProximo());
                        anterior.setProximo(novo);
                        return true;
                    }
                    return false;
                }
                fimAnterior = hexaToInt(anterior.getFimBloco());
                if (isFronteira(fimAnterior, inicioNovo)) {
                    juntarBlocos(anterior, novo);
                    return true;
                }
                if (this.isMenor(fimAnterior, inicioNovo)) {
                    anterior.setProximo(novo);
                    return true;
                }
            } else if (this.inicio == null) {
                novo.setProximo(this.inicio);
                this.inicio = novo;
            } else {
                int inicioInicio = hexaToInt(this.inicio.getEndereco());
                fimAnterior = hexaToInt(this.inicio.getFimBloco());
                if (isFronteira(fimNovo, hexaToInt(inicio.getEndereco()))) {
                    juntarBlocos(novo, inicio);
                    return true;
                }
                if (this.isMenor(fimAnterior, inicioNovo)) {
                    this.inicio.setProximo(novo);
                    return true;
                }

                if (this.isMenor(fimNovo, inicioInicio)) {
                    novo.setProximo(this.inicio);
                    this.inicio = novo;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFronteira(int fimAnterior, int inicioNovo, int fimNovo, int inicioProximo) {
        return isFronteira(fimAnterior, inicioNovo) && isFronteira(fimNovo, inicioProximo);
    }

    private boolean isFronteira(int fimAnterior, int inicioNovo) {
        return (fimAnterior + 1) == inicioNovo;
    }

    public boolean existeBlocoMemoria(String endereco) {
        Bloco<T> anterior = this.buscaAnterior(endereco);
        Bloco<T> bloco = new Bloco(endereco);
        if (this.inicio != null) {
            if (anterior == null && this.inicio.getEndereco().compareTo(bloco.getEndereco()) == 0) {
                return true;
            }

            if (anterior == null) {
                return false;
            }

            Bloco<T> proximo = anterior.getProximo();
            if (anterior != null && anterior.getEndereco().compareTo(bloco.getEndereco()) == 0) {
                return true;
            }

            if (proximo != null && this.isMenor(proximo.getEndereco(), bloco.getEndereco())) {
                return true;
            }
        }

        return false;
    }

    public Bloco<T> buscaAnterior(String endereco) {
        Bloco<T> anterior = null;
        Bloco ponteiro = this.inicio;

        while (ponteiro != null && isMenor(ponteiro.getEndereco(), endereco)) {
            anterior = ponteiro;
            ponteiro = ponteiro.getProximo();
        }

        return anterior;
    }

    public static String IntToHexa(Integer integer) {
        return Integer.toHexString(integer);
    }

    public static Integer hexaToInt(String hexa) {
        return Integer.parseInt(hexa, 16);
    }

    private boolean podeAdicionar(int inicioProximo, int fimNovo, int inicioNovo, int fimAnterior) {
        return inicioProximo > fimNovo && inicioNovo > fimAnterior;
    }

    private boolean isMenor(String endereco1, String endereco2) {
        return hexaToInt(endereco1).compareTo(hexaToInt(endereco2)) == -1;
    }

    private boolean isMenor(Integer tamanho1, Integer tamanho2) {
        return tamanho1.compareTo(tamanho2) == -1;
    }

    private boolean isMaior(Integer tamanho1, Integer tamanho2) {
        return tamanho1.compareTo(tamanho2) == 1;
    }

    private boolean isEqual(String endereco1, String endereco2) {
        return hexaToInt(endereco1).compareTo(hexaToInt(endereco2)) == 0;
    }

    private boolean isEqual(Integer tamanho1, Integer tamanho2) {
        return tamanho1.compareTo(tamanho2) == 0;
    }

    public String toString() {
        String retorno = " → " + this.inicio;
        Bloco<T> ponteiro = null;
        if (this.inicio != null) {
            ponteiro = this.inicio.getProximo();
        }

        while (ponteiro != null) {
            retorno += " → " + ponteiro;
            ponteiro = ponteiro.getProximo();
        }

        return retorno + " → ";
    }

    public void imprimirDisponivel() {
        System.out.println("Memória Livre:\n" + this);
    }

    public void imprimirIndisponivel() {
        String retorno = " → ";
        int inicioIndisponivel = 0;
        int inicioInicio;
        if (inicio != null) {
            inicioInicio = hexaToInt(inicio.getEndereco());
            if (inicioInicio != 0) {
                retorno += "[" + IntToHexa(inicioIndisponivel) + "h, " + inicioInicio + "] → ";
            }
            if (inicio.getTamanho() < 4096 && inicio.getProximo() != null) {
                Bloco<T> anterior = inicio;
                Bloco<T> ponteiro = inicio.getProximo();
                while (ponteiro != null) {
                    inicioIndisponivel = hexaToInt(anterior.getFimBloco()) + 1;
                    int fimIndisponivel = hexaToInt(ponteiro.getEndereco()) - inicioIndisponivel;
                    System.out.println(inicioIndisponivel);
                    System.out.println(fimIndisponivel);
                    retorno += "[" + IntToHexa(inicioIndisponivel) + "h, " + fimIndisponivel + "] → ";
                    anterior = ponteiro;
                    ponteiro = ponteiro.getProximo();
                }
            } else {
                inicioIndisponivel = hexaToInt(inicio.getFimBloco()) + 1;
                int fimIndisponivel = 4096 - inicio.getTamanho();
                retorno += "[" + IntToHexa(inicioIndisponivel) + "h, " + fimIndisponivel + "] → ";
            }
        } else {
            retorno += "[0h,4096] → ";
        }

        System.out.println("Memória Ocupada:\n" + retorno);
    }
}
