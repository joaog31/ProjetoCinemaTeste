package br.com.cinesmart.utilitario;

/**
 * Interface para geração de números aleatórios.
 * Em produção, usará geradores reais.
 * Nos testes, será mockada para garantir testes determinísticos.
 */
public interface GeradorAleatorio {
    /**
     * Gera um número inteiro aleatório entre min (inclusivo) e max (exclusivo).
     * @param min valor mínimo (inclusivo)
     * @param max valor máximo (exclusivo)
     * @return número inteiro aleatório
     */
    int sortearInteiro(int min, int max);

    /**
     * Gera um número real aleatório entre 0.0 e 1.0.
     * @return número real entre 0 e 1
     */
    double sortearDouble();
}
