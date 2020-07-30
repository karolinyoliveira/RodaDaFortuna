package roda_da_fortuna;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
    private Label labelPuzzle;

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
            this.paneVogais.disableProperty().set(false);
            this.jogadores[posicaoJogadorAtual].reduzirPontos(custoVogal);
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
        solucao.setHeaderText("Resolva o Puzzle");
        Button ok = new Button("Ok");
        solucao.showAndWait();

        //Analisa resposta
        if (this.tabuleiro.receberPalpite(solucao.getEditor().getText())) {
            alerta(AlertType.INFORMATION, "Fim de Jogo!", this.jogadorAtual.getNome() + " Venceu!");
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
        if (0 == 0) {
            this.jogadores[posicaoJogadorAtual].aumentarPontos(this.roda.getValorAtual());
            // } else {
            avancarProximoJogador();
        }

        //Desbloqueia opções
        this.girarRoda.disableProperty().set(false);
        this.resolverPuzzle.disableProperty().set(false);

        //Bloqueia painel de consoantes e letra selecionada
        this.paneConsoantes.disableProperty().set(true);

        ObservableList<Node> consoClicada = this.paneConsoantes.getChildren();
        consoClicada.stream().filter((node) -> (node == event.getSource())).forEachOrdered((node) -> {
            node.setDisable(true);
        });
    }

    /**
     * Implementa a logica de clicar em uma VOGAL.
     *
     * @param event
     */
    private void clicarVogal(ActionEvent event) {
        //Verifica se a vogal existe na frase secreta e atualiza o puzzle
        if (0 == 0) {
            this.jogadores[posicaoJogadorAtual].aumentarPontos(this.roda.getValorAtual());
            // } else {
            avancarProximoJogador();
        }
        //Desbloqueia opções
        this.girarRoda.disableProperty().set(false);
        this.resolverPuzzle.disableProperty().set(false);

        //Bloqueia painel de consoantes e letra selecionada
        this.paneVogais.disableProperty().set(true);

        //Desativa a letra selecionada
        ObservableList<Node> vogalClicada = this.paneVogais.getChildren();
        vogalClicada.stream().filter((node) -> (node == event.getSource())).forEachOrdered((node) -> {
            node.setDisable(true);
        });
    }

    /**
     * Verifica se o jogo terminou, ou seja, se todas as letras do puzzle foram
     * preenchidas, e, em caso positivo, exibe uma mensagem informando quem
     * ganhou e termina a aplicacao.
     */
    private void isFimDoJogo() {
        //Verifica se todas as consoantes e vogais foram selecionadas
        if (0 == 0 && 0 == 0) {

            //Alerta de fim de jogo e finalização do programa
            alerta(AlertType.INFORMATION, "Fim de Jogo!", this.jogadorAtual.getNome() + " Venceu!");
            Platform.exit();
            System.exit(0);
        }
        return;
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

        ObservableList<Node> verificaConsoEsgotada = this.paneVogais.getChildren();
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
        if (Integer.parseInt(this.jogadorAtual.getPontos()) >= 250) {
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
