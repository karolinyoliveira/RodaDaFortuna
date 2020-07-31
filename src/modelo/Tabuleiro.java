package modelo;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/*
 *
 * @author braulio, karoliny oliveira, caroline corrêia
 */
public class Tabuleiro {

    private final String puzzle;
    private String palpitePuzzle;
    private int palpitesRestantes;
    private String respostaPuzzle;

    /*
     * Seta o puzzle do jogo e inicializa o tabuleiro.
     * @param puzzle 
     */
    public Tabuleiro(String puzzle) {
        this.puzzle = puzzle.toUpperCase();
        this.palpitePuzzle = new String();
        inicializarTabuleiro();
        this.respostaPuzzle = new String();
    }

    /*
     * Inicializa <code>palpitesRestantes</code>, so considera as letras, e
     * <code>palpitePuzzle</code>.
     */
    private void inicializarTabuleiro() {

        palpitePuzzle = puzzle;

        CharacterIterator ci = new StringCharacterIterator(palpitePuzzle);

        while (ci.current() != CharacterIterator.DONE) {

            //pega só letras:  
            if ((ci.current() != ',' && ci.current() != '.') && (ci.current() != '!') && (ci.current() != ' ') && (ci.current() != '?')) {

                palpitePuzzle = palpitePuzzle.replace(ci.current(), '_');
            }

            ci.next();
        }

    }

    /*
     * Recebe o palpite do jogador
     * @param palpite
     * @return true se bateu o palpite
     */
    public boolean receberPalpite(String palpite) {
        if (palpite.compareToIgnoreCase(this.puzzle)!=0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Retorna os palpites do jogo a serem mostrados na tela.
     *
     * @return String
     */
    public String getPalpitePuzzle() {
        return this.palpitePuzzle;
    }

    public int getPalpitesRestantes() {
        return palpitesRestantes;
    }

    public String getPuzzle() {
        return puzzle;
    }
    
    public void setPalpitePuzzle(String novo){
        this.palpitePuzzle = novo;
    }

}
