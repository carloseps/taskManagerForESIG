package br.com.esig.tm.bd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.esig.tm.enums.Prioridade;
import br.com.esig.tm.enums.Situacao;
import br.com.esig.tm.model.Tarefa;
import lombok.Getter;
import lombok.Setter;

public class AcoesBD {

	@Getter
	@Setter
	List<Tarefa> tarefas = new ArrayList<>();

	public void criarTabela(Connection conn, String nomeTabela) {
		Statement declaracao;
		try {
			String comandoCriarTabela = "create table if not exists " + nomeTabela
					+ " (taskID SERIAL, titulo varchar(50) not null, descricao varchar(100), responsavel varchar(50) not null, prioridade varchar(5), situacao varchar(15), deadline date, primary key(taskID));";
			declaracao = conn.createStatement();
			declaracao.executeUpdate(comandoCriarTabela);
		} catch (Exception e) {
			System.err.println("Falha ao tentar criar a tabela! Possivelmente ela já existe" + e);
		}
	}

	public int cadastrarTarefa(Connection conn, String nomeTabela, String titulo, String descricao, String responsavel,
			Prioridade prioridade, Situacao situacao, Date deadline) {
		Statement declaracao;
		try {
			String comandoCadastrarTarefa = String.format(
					"insert into %s(titulo, descricao, responsavel, prioridade, situacao, deadline) values('%s', '%s', '%s', '%s', '%s', '%s');",
					nomeTabela, titulo, descricao, responsavel, prioridade, situacao.getDescricao(), deadline);
			declaracao = conn.createStatement();
			declaracao.executeUpdate(comandoCadastrarTarefa);
			System.out.println("Tarefa cadastrada com sucesso!");
			return 1;
		} catch (Exception e) {
			System.err.println("Falha ao tentar cadastrar tarefa " + e);
			return 0;
		}
	}

	public int concluirTarefa(Connection conn, String nomeTabela, Long taskid) {
		Statement declaracao;
		try {
			String comandoConcluirTarefa = String.format("update %s set situacao='%s' where taskid = '%s'", nomeTabela,
					Situacao.Concluida.getDescricao(), taskid);
			declaracao = conn.createStatement();
			declaracao.executeUpdate(comandoConcluirTarefa);
			System.out.println("Tarefa concluida com sucesso!");
			return 1;
		} catch (Exception e) {
			System.err.println("Falha ao tentar concluir tarefa " + e);
			return 0;
		}
	}

	public int removerTarefa(Connection conn, String nomeTabela, Long taskid) {
		Statement declaracao;
		try {
			String comandoRemoverTarefa = String.format("delete from %s where taskid = %s", nomeTabela, taskid);
			declaracao = conn.createStatement();
			declaracao.executeUpdate(comandoRemoverTarefa);
			System.out.println("Tarefa removida com sucesso!");
			return 1;
		} catch (Exception e) {
			System.err.println("Falha ao tentar remover tarefa " + e);
			return 0;
		}
	}

	public int atualizarTarefa(Connection conn, String nomeTabela, String titulo, String descricao, String responsavel,
			Prioridade prioridade, String situacao, Date deadline, Long taskid) {
		Statement declaracao;
		try {
			String comandoAtualizarTarefa = String.format(
					"update %s set titulo='%s', descricao='%s', responsavel='%s', prioridade='%s', deadline='%s', situacao='%s' where taskid = '%s'",
					nomeTabela, titulo, descricao, responsavel, prioridade, deadline, situacao, taskid);
			declaracao = conn.createStatement();
			declaracao.executeUpdate(comandoAtualizarTarefa);
			System.out.println("Tarefa atualizada com sucesso!");
			return 1;
		} catch (Exception e) {
			System.err.println("Falha ao tentar atualizar tarefa " + e);
			return 0;
		}
	}

	public List<Tarefa> lerTarefas(Connection conn, String nomeTabela) {
		Statement declaracao;
		ResultSet resultSet = null;
		try {
			String comandolerTarefas = String.format("select * from %s", nomeTabela);
			declaracao = conn.createStatement();
			resultSet = declaracao.executeQuery(comandolerTarefas);

			while (resultSet.next()) {
				Tarefa tarefa = new Tarefa();
				tarefa.setTaskid(resultSet.getLong("taskid"));
				tarefa.setTitulo(resultSet.getString("titulo"));
				tarefa.setDescricao(resultSet.getString("descricao"));
				tarefa.setResponsavel(resultSet.getString("responsavel"));
				String prioridadeString = resultSet.getString("prioridade");
				Prioridade prioridade = Prioridade.valueOf(prioridadeString);
				tarefa.setPrioridade(prioridade);
				String situacaoString = resultSet.getString("situacao");
				Situacao situacao = analisarSituacao(situacaoString);
				tarefa.setSituacao(situacao);
				Date dataSQL = resultSet.getDate("deadline");
				java.util.Date dataUtil = new java.util.Date(dataSQL.getTime());
				tarefa.setDeadline(dataUtil);

				tarefas.add(tarefa);
			}
		} catch (Exception e) {
			System.err.println("Falha ao ler tarefas cadastradas" + e);
		}
		return tarefas;
	}

	public Situacao analisarSituacao(String descricao) {
		if (Situacao.EmAndamento.getDescricao().equals(descricao)) {
			return Situacao.EmAndamento;
		} else {
			return Situacao.Concluida;
		}
	}

}
