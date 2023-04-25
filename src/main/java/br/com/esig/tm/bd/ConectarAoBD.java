package br.com.esig.tm.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
public class ConectarAoBD implements CommandLineRunner {
	
	@Getter
	@Setter
	private AcoesBD acoesBD = new AcoesBD();
	
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String usuario;
    
    @Value("${spring.datasource.password}")
    private String senha;
    
    public Connection getConexao() throws SQLException {
        return DriverManager.getConnection(url, usuario, senha);
    }

	@Override
	public void run(String... args) throws Exception {
		acoesBD.criarTabela(getConexao(), "Tarefas");
		acoesBD.lerTarefas(getConexao(), "Tarefas");
	}
	
}
