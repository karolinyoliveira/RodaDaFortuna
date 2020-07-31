package roda_da_fortuna;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import modelo.Jogador;
import modelo.Roda;
import modelo.Tabuleiro;

/**
 *
 * @author braulio, karoliny oliveira, caroline corrêia
 */
public class PainelJogoController {

    @FXML
    private GridPane paneJogadores;

    @FXML
    private Button comprarVogal, girarRoda, resolverPuzzle;

    @FXML
    private ImageView imagemRoda;

    @FXML
    private Label labelPuzzle, p1, p2, p3;

    @FXML
    private GridPane paneVogais, paneConsoantes;

    private final int quantidadeJogadores = 3;
    private final int custoVogal = 250;
    private int posicaoJogadorAtual = 0;

    private Tabuleiro tabuleiro;
    private Roda roda;
    private final Jogador[] jogadores = new Jogador[quantidadeJogadores];
    private Jogador jogadorAtual;

    private final SepiaTone selected = new SepiaTone(1.5);
    private final SepiaTone unselected = new SepiaTone(0);

    /**
     * Inicializa a cena da primeira jogada.
     */
    @FXML
    private void initialize() {
        // bloquear comprarVogal
        this.comprarVogal.disableProperty().set(true);

        // instanciar a Roda
        this.roda = new Roda();
        // setar a imagem atual da Roda
        imagemRoda.setImage(this.roda.getImagemAtual());

        // bloquear o painel das vogais
        this.paneVogais.disableProperty().set(true);
        // setar o evento clicarVogal em cada botao das vogais
        this.paneVogais.getChildren().forEach(n -> ((Button) n).setOnAction(e -> clicarVogal(e)));

        // bloquear o painel das consoantes
        this.paneConsoantes.disableProperty().set(true);
        // setar o evento clicarConsoante em cada botao das consoantes
        this.paneConsoantes.getChildren().forEach(n -> ((Button) n).setOnAction(e -> clicarConsoante(e)));
    }

    /**
     * Implementa a logica de comprar uma vogal.
     *
     * @param event
     */
    @FXML
    private void comprarVogalAction(ActionEvent event) {
        if (!this.isVogaisEsgotadas()) {
            //Desbloqueia painel de vogais
            this.paneVogais.disableProperty().set(false);

            //Bloqueia as outras opções
            this.comprarVogal.disableProperty().set(true);
            this.girarRoda.disableProperty().set(true);
            this.resolverPuzzle.disableProperty().set(true);

            //Pontuação
            this.jogadores[posicaoJogadorAtual].reduzirPontos(custoVogal);
            //Atualização de todo o tabuleiro
            this.p1.setText(this.jogadores[0].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(0)).setContent(this.p1);
            this.p2.setText(this.jogadores[1].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(1)).setContent(this.p2);
            this.p3.setText(this.jogadores[2].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(2)).setContent(this.p3);

        } else {
            alerta(AlertType.WARNING, "Vogais Esgotadas!", "Por favor, selecione outra opção.");
            this.paneVogais.disableProperty().set(true);
        }
    }

    /**
     * Implementa a logica de girar a roda.
     *
     * @param event
     */
    @FXML
    private void girarRodaAction(ActionEvent event) {
        //Giro da roda
        this.roda.girarRoda();
        imagemRoda.setImage(this.roda.getImagemAtual());

        //Avalia se o jogador perdeu a vez e seus pontos (bankrupt -2 e loseAturn -1)
        if (this.roda.getValorAtual() == -2) {
            alerta(AlertType.WARNING, "BANKRUPT!", "Você perdeu tudo.");
            this.jogadores[posicaoJogadorAtual].zerarPontos();

            //Atualização de todo o tabuleiro
            this.p1.setText(this.jogadores[0].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(0)).setContent(this.p1);
            this.p2.setText(this.jogadores[1].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(1)).setContent(this.p2);
            this.p3.setText(this.jogadores[2].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(2)).setContent(this.p3);

            avancarProximoJogador();
            return;
        }
        if (this.roda.getValorAtual() == -1) {
            avancarProximoJogador();
            alerta(AlertType.WARNING, "Perdeu a Vez!", "É a vez de " + this.jogadorAtual.getNome());
            return;
        }

        //Desbloquear o painel das consoantes só quando há consoantes disponíveis
        if (!this.isConsoantesEsgotadas()) {
            this.paneConsoantes.disableProperty().set(false);
        } else {
            alerta(AlertType.WARNING, "Consoantes Esgotadas!", "Por favor, selecione outra opção.");
            this.girarRoda.disableProperty().set(true);
        }

        //Bloquear todo o resto
        this.paneVogais.disableProperty().set(true);
        this.comprarVogal.disableProperty().set(true);
        this.girarRoda.disableProperty().set(true);
        this.resolverPuzzle.disableProperty().set(true);
    }

    /**
     * Implementa a logica de tentar adivinhar o puzzle.
     *
     * @param event
     */
    @FXML
    private void resolverPuzzleAction(ActionEvent event) {

        //Abre janela para o jogador dar sua resposta
        TilePane resolverPuzzle = new TilePane();
        TextInputDialog solucao = new TextInputDialog("Qual é a frase secreta?");
        solucao.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        solucao.setHeaderText("Resolva o Puzzle");
        solucao.showAndWait();

        //Analisa resposta
        if (this.tabuleiro.receberPalpite(solucao.getEditor().getText())) {
            alerta(AlertType.INFORMATION, "Fim de Jogo!", this.jogadorAtual.getNome() + " ganhou " + this.jogadorAtual.getPontos() + "!");
            Platform.exit();
            System.exit(0);
        } else {
            avancarProximoJogador();
            alerta(AlertType.INFORMATION, "Resposta Incorreta!", "É a vez de " + this.jogadorAtual.getNome());
        }
    }

    /**
     * Implementa a logica de clicar em uma CONSOANTE.
     *
     * @param event
     */
    private void clicarConsoante(ActionEvent event) {
        //Verifica se a consoante existe na frase secreta e atualiza o puzzle
        int flag = 0;
        StringBuilder consoPresente = new StringBuilder(this.tabuleiro.getPalpitePuzzle());

        //pegar a letra 
        int y = event.toString().length() - 3;
        char letraC = event.toString().charAt(y);

        String c = this.tabuleiro.getPuzzle();

        //ver se a consoante está na frase secreta       
        CharacterIterator ciC = new StringCharacterIterator(c);

        while (ciC.current() != CharacterIterator.DONE) {
            if (letraC == c.charAt(ciC.getIndex())) {
                consoPresente.setCharAt(ciC.getIndex(), letraC);
                flag = 1;
            }
            ciC.next();
        }

        //Bloqueia a letra selecionada
        ObservableList<Node> consoClicada = this.paneConsoantes.getChildren();
        consoClicada.stream().filter((node) -> (node == event.getSource())).forEachOrdered((node) -> {
            node.setDisable(true);
        });

        if (flag == 1) {
            //atualiza o puzzle
            this.tabuleiro.setPalpitePuzzle(consoPresente.toString());
            //preenche o puzzle com a consoante acertada
            this.labelPuzzle.setText(this.tabuleiro.getPalpitePuzzle());

            //Pontuação
            this.jogadores[posicaoJogadorAtual].aumentarPontos(this.roda.getValorAtual());

            //Atualização no tabuleiro
            this.p1.setText(this.jogadores[0].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(0)).setContent(this.p1);
            this.p2.setText(this.jogadores[1].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(1)).setContent(this.p2);
            this.p3.setText(this.jogadores[2].getPontos());
            ((TitledPane) paneJogadores.getChildren().get(2)).setContent(this.p3);

        } else {
            avancarProximoJogador();
        }

        //Desbloqueia opções
        if (Integer.parseInt(this.jogadorAtual.getPontos()) >= custoVogal) {
            this.comprarVogal.disableProperty().set(false);
        }
        this.girarRoda.disableProperty().set(false);
        this.resolverPuzzle.disableProperty().set(false);

        //Bloqueia painel de consoantes
        this.paneConsoantes.disableProperty().set(true);

        //Verifica se todas as letras foram utilizadas
        isFimDoJogo();
    }

    /**
     * Implementa a logica de clicar em uma VOGAL.
     *
     * @param event
     */
    private void clicarVogal(ActionEvent event) {
        //Verificar se existe a vogal selecionada na frase secreta e atualiza o puzzle
        int flag1 = 0;
        StringBuilder vogalPresente = new StringBuilder(this.tabuleiro.getPalpitePuzzle());

        //pegar a letra 
        int x = event.toString().length() - 3;
        char letraV = event.toString().charAt(x);

        String v = this.tabuleiro.getPuzzle();

        //ver se a vogal está na frase secreta       
        CharacterIterator ciV = new StringCharacterIterator(v);

        while (ciV.current() != CharacterIterator.DONE) {
            if (letraV == v.charAt(ciV.getIndex())) {
                vogalPresente.setCharAt(ciV.getIndex(), letraV);
                flag1 = 1;
            }
            ciV.next();
        }

        if (flag1 == 1) {
            //atualiza o puzzle
            this.tabuleiro.setPalpitePuzzle(vogalPresente.toString());

            //preenche o puzzle com a vogal acertada
            this.labelPuzzle.setText(this.tabuleiro.getPalpitePuzzle());
        }

        //Desbloqueia opções
        if (Integer.parseInt(this.jogadorAtual.getPontos()) >= custoVogal) {
            this.comprarVogal.disableProperty().set(false);
        }
        this.girarRoda.disableProperty().set(false);
        this.resolverPuzzle.disableProperty().set(false);

        //Bloqueia painel de vogais e letra selecionada
        this.paneVogais.disableProperty().set(true);

        //Desativa a letra selecionada
        ObservableList<Node> vogalClicada = this.paneVogais.getChildren();
        vogalClicada.stream().filter((node) -> (node == event.getSource())).forEachOrdered((node) -> {
            node.setDisable(true);
        });

        //Verifica se todas as letras foram utilizadas
        isFimDoJogo();
    }

    /**
     * Verifica se o jogo terminou, ou seja, se todas as letras do puzzle foram
     * preenchidas, e, em caso positivo, exibe uma mensagem informando quem
     * ganhou e termina a aplicacao.
     */
    private void isFimDoJogo() {
        //Verifica se todas as consoantes e vogais foram selecionadas
        if (isConsoantesEsgotadas() == true && isVogaisEsgotadas() == true) {
            alerta(AlertType.INFORMATION, "Fim de Jogo!", "Não houve vencedores.");
            Platform.exit();
            System.exit(0);
        } //Verifica se o  puzzle foi resolvido no último palpite de letra
        else if (this.tabuleiro.receberPalpite(this.tabuleiro.getPalpitePuzzle()) == true) {
            this.comprarVogal.disableProperty().set(true);
            this.girarRoda.disableProperty().set(true);
        }
    }

    /**
     * Valida se todos os botoes das VOGAIS foram clicados, i.e., ficaram
     * desativados.
     *
     * @return boolean
     */
    private boolean isVogaisEsgotadas() {
        int botaoV = 0;

        ObservableList<Node> verificaVogalEsgotada = this.paneVogais.getChildren();
        botaoV = verificaVogalEsgotada.stream().filter((node) -> (node.isDisable() == true)).map((_item) -> 1).reduce(botaoV, Integer::sum);

        if (botaoV == verificaVogalEsgotada.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Valida se todos os botoes das CONSOANTES foram clicados, i.e., ficaram
     * desativados.
     *
     * @return boolean
     */
    private boolean isConsoantesEsgotadas() {
        int botaoC = 0;

        ObservableList<Node> verificaConsoEsgotada = this.paneConsoantes.getChildren();
        botaoC = verificaConsoEsgotada.stream().filter((node) -> (node.isDisable() == true)).map((_item) -> 1).reduce(botaoC, Integer::sum);

        if (botaoC == verificaConsoEsgotada.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Cria e exibe uma janela de alerta.
     *
     * @param type
     * @param titulo
     * @param conteudo
     */
    private void alerta(AlertType type, String titulo, String conteudo) {
        Alert alert = new Alert(type);
        alert.setHeaderText(titulo);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    /**
     * Avanca para o proximo jogador. Adiciona o efeito de selecionado, i.e.,
     * <code>selected</code>, no <code>TitledPane</code> do jogador atual.
     */
    private void avancarProximoJogador() {
        //Avança para o próximo jogador e remove o efeito selected
        ((TitledPane) this.paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(unselected);

        if (posicaoJogadorAtual < 2) {
            posicaoJogadorAtual++;
        } else {
            posicaoJogadorAtual = 0;
        }
        this.jogadorAtual = this.jogadores[posicaoJogadorAtual];

        //Verifica se o jogador tem dinheiro para habilitar ComprarVogal
        if (Integer.parseInt(this.jogadorAtual.getPontos()) >= custoVogal) {
            this.comprarVogal.disableProperty().set(false);
        }

        //Atualiza GUI do player selecionado
        ((TitledPane) this.paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(selected);

    }

    /**
     * Recebe o puzzle e os nomes dos jogadores.
     *
     * @param puzzle
     * @param nomeJogadores
     */
    public void setPuzzleENomeJogadores(final String puzzle, final String... nomeJogadores) {
        this.tabuleiro = new Tabuleiro(puzzle);
        this.labelPuzzle.setText(this.tabuleiro.getPalpitePuzzle());

        for (int i = 0; i < quantidadeJogadores; i++) {
            ((TitledPane) this.paneJogadores.getChildren().get(i)).setText(nomeJogadores[i]);

            this.jogadores[i] = new Jogador(nomeJogadores[i]);
            this.jogadores[i].setRoda(this.roda);
        }

        this.jogadorAtual = this.jogadores[posicaoJogadorAtual];
        ((TitledPane) this.paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(selected);
    }

}
