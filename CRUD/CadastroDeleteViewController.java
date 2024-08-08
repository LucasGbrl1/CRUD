import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastroDeleteViewController {
    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private PasswordField confirmarSenhaField;

    @FXML
    private Button cadastrarButton;

    @FXML
    private Button atualizarButton;

    @FXML
    private Button deletarButton;

    @FXML
    private Button buscarButton;

    @FXML
    private Label situacaoLabel;

    @FXML
    private void handleCadastrar() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        String confirmarSenha = confirmarSenhaField.getText();

        if (senha.equals(confirmarSenha)) {
            inserirDadosNoBanco(nome, email, senha);
        } else {
            situacaoLabel.setText("As senhas não coincidem. Tente novamente.");
        }
    }

    @FXML
    private void handleAtualizar() {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();
        atualizarDadosNoBanco(nome, email, senha);
    }

    @FXML
    private void handleDeletar() {
        String email = emailField.getText();
        deletarDadosNoBanco(email);
    }

    @FXML
    private void handleBuscar() {
        String email = emailField.getText();
        buscarDadosNoBanco(email);
    }

    private void inserirDadosNoBanco(String nome, String email, String senha) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
            String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, senha);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                situacaoLabel.setText("Usuário cadastrado com sucesso!");
            } else {
                situacaoLabel.setText("Erro ao cadastrar usuário.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            situacaoLabel.setText("Erro ao conectar ao banco de dados.");
        }
    }

    private void atualizarDadosNoBanco(String nome, String email, String senha) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
            String sql = "UPDATE usuarios SET nome = ?, senha = ? WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, senha);
            preparedStatement.setString(3, email);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                situacaoLabel.setText("Usuário atualizado com sucesso.");
            } else {
                situacaoLabel.setText("Erro ao atualizar usuário.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            situacaoLabel.setText("Erro ao conectar ao banco de dados.");
        }
    }

    private void deletarDadosNoBanco(String email) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
            String sql = "DELETE FROM usuarios WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                situacaoLabel.setText("Usuário deletado com sucesso.");
            } else {
                situacaoLabel.setText("Erro ao deletar usuário.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            situacaoLabel.setText("Erro ao conectar ao banco de dados.");
        }
    }

    private void buscarDadosNoBanco(String email) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db");
            String sql = "SELECT * FROM usuarios WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String senha = resultSet.getString("senha");
                nomeField.setText(nome);
                senhaField.setText(senha);
                confirmarSenhaField.setText(senha);
                situacaoLabel.setText("Usuário encontrado: " + nome);
            } else {
                situacaoLabel.setText("Usuário não encontrado.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            situacaoLabel.setText("Erro ao conectar ao banco de dados.");
        }
    }

    private void abrirTelaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Tela de Login");
            stage.setScene(scene);
            stage.show();

            Stage cadastroStage = (Stage) cadastrarButton.getScene().getWindow();
            cadastroStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
