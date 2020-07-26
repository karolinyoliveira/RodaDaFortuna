package modelo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import java.util.Random;

/**
 *
 * @author braulio, karoliny oliveira, caroline corrêia
 */
public class Roda {

    private EspacoRoda[] espacos;
    private EspacoRoda espacoAtual;

    private static final String EXTENSAO_IMAGEM = ".jpg";
    private static final int NUMERO_ESPACOS = 24;
    private static final String CAMINHO_IMAGENS = "figuras";

    /**
     * Inicializa a roda e faz o um giro.
     */
    public Roda() {
        inicializarRoda();
        girarRoda();
    }

    /**
     * Retorna o valor atual da roda
     *
     * @return int
     */
    public int getValorAtual() {
        return espacoAtual.getValor();
    }

    /**
     * Retorna a imagem atual da roda
     *
     * @return Image
     */
    public Image getImagemAtual() {
        return espacoAtual.getImagem();
    }

    /**
     * Gira a roda aleatoriamente, [0,23]
     */
    public void girarRoda() {
        Random rand = new Random();
        this.espacoAtual=espacos[rand.nextInt(23)];
    }

    /**
     * Inicializa os valores e as imagens da roda.
     */
    private void inicializarRoda() {
        this.espacos = new EspacoRoda[24];
        Image img[] = new Image[24];

        //Criando as imagens
        try {
            img[0] = new Image(new FileInputStream("figuras\\1_loseATurn" + EXTENSAO_IMAGEM));
            img[1] = new Image(new FileInputStream("figuras\\2_800" + EXTENSAO_IMAGEM));
            img[2] = new Image(new FileInputStream("figuras\\3_350" + EXTENSAO_IMAGEM));
            img[3] = new Image(new FileInputStream("figuras\\4_450" + EXTENSAO_IMAGEM));
            img[4] = new Image(new FileInputStream("figuras\\5_700" + EXTENSAO_IMAGEM));
            img[5] = new Image(new FileInputStream("figuras\\6_300" + EXTENSAO_IMAGEM));
            img[6] = new Image(new FileInputStream("figuras\\7_600" + EXTENSAO_IMAGEM));
            img[7] = new Image(new FileInputStream("figuras\\8_5000" + EXTENSAO_IMAGEM));
            img[8] = new Image(new FileInputStream("figuras\\9_300" + EXTENSAO_IMAGEM));
            img[9] = new Image(new FileInputStream("figuras\\10_600" + EXTENSAO_IMAGEM));
            img[10] = new Image(new FileInputStream("figuras\\11_300" + EXTENSAO_IMAGEM));
            img[11] = new Image(new FileInputStream("figuras\\12_500" + EXTENSAO_IMAGEM));
            img[12] = new Image(new FileInputStream("figuras\\13_800" + EXTENSAO_IMAGEM));
            img[13] = new Image(new FileInputStream("figuras\\14_550" + EXTENSAO_IMAGEM));
            img[14] = new Image(new FileInputStream("figuras\\15_400" + EXTENSAO_IMAGEM));
            img[15] = new Image(new FileInputStream("figuras\\16_300" + EXTENSAO_IMAGEM));
            img[16] = new Image(new FileInputStream("figuras\\17_900" + EXTENSAO_IMAGEM));
            img[17] = new Image(new FileInputStream("figuras\\18_500" + EXTENSAO_IMAGEM));
            img[18] = new Image(new FileInputStream("figuras\\19_300" + EXTENSAO_IMAGEM));
            img[19] = new Image(new FileInputStream("figuras\\20_900" + EXTENSAO_IMAGEM));
            img[20] = new Image(new FileInputStream("figuras\\21_bankrupt" + EXTENSAO_IMAGEM));
            img[21] = new Image(new FileInputStream("figuras\\22_600" + EXTENSAO_IMAGEM));
            img[22] = new Image(new FileInputStream("figuras\\23_400" + EXTENSAO_IMAGEM));
            img[23] = new Image(new FileInputStream("figuras\\24_300" + EXTENSAO_IMAGEM));
        } catch (FileNotFoundException e) {
            System.out.println("Erro:" + e.getMessage());
        }

        //Inicializando os espacosRoda (bankrupt -2 e loseAturn -1)
        espacos[0] = new EspacoRoda(-1, img[0]);
        espacos[1] = new EspacoRoda(800, img[1]);
        espacos[2] = new EspacoRoda(350, img[2]);
        espacos[3] = new EspacoRoda(450, img[3]);
        espacos[4] = new EspacoRoda(700, img[4]);
        espacos[5] = new EspacoRoda(300, img[5]);
        espacos[6] = new EspacoRoda(600, img[6]);
        espacos[7] = new EspacoRoda(5000, img[7]);
        espacos[8] = new EspacoRoda(300, img[8]);
        espacos[9] = new EspacoRoda(600, img[9]);
        espacos[10] = new EspacoRoda(300, img[10]);
        espacos[11] = new EspacoRoda(500, img[11]);
        espacos[12] = new EspacoRoda(800, img[12]);
        espacos[13] = new EspacoRoda(550, img[13]);
        espacos[14] = new EspacoRoda(400, img[14]);
        espacos[15] = new EspacoRoda(300, img[15]);
        espacos[16] = new EspacoRoda(900, img[16]);
        espacos[17] = new EspacoRoda(500, img[17]);
        espacos[18] = new EspacoRoda(300, img[18]);
        espacos[19] = new EspacoRoda(900, img[19]);
        espacos[20] = new EspacoRoda(-2, img[20]);
        espacos[21] = new EspacoRoda(600, img[21]);
        espacos[22] = new EspacoRoda(400, img[22]);
        espacos[23] = new EspacoRoda(300, img[23]);
    }

    /**
     * Classe interna para armazenar cada valor e imagem.
     */
    private class EspacoRoda {

        int valor;
        Image imagem;

        EspacoRoda(final int valor, final Image imagem) {
            this.valor = valor;
            this.imagem = imagem;
        }

        int getValor() {
            return valor;
        }

        Image getImagem() {
            return imagem;
        }
    }

}
