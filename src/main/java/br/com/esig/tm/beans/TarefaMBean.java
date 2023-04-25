package br.com.esig.tm.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.esig.tm.bd.AcoesBD;
import br.com.esig.tm.bd.ConectarAoBD;
import br.com.esig.tm.enums.Prioridade;
import br.com.esig.tm.enums.Situacao;
import br.com.esig.tm.model.Tarefa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ViewScoped
@Named
//Tem o objetivo de: -Criar -Atualizar -Listar -Remover (Tarefas)
public class TarefaMBean implements Serializable {
	// serialVersion padrão
	private static final long serialVersionUID = 1L;

	// Dados que o usuário vai informar vão popular esses atributos
	private String titulo;
	private String descricao;
	private String responsavel;
	private Prioridade prioridade;
	// Esse não será populado pelo usuário, já que ao cadastrar uma tarefa ela
	// automaticamente consta como "Em andamento".
	private Situacao situacao;
	private java.util.Date deadline;

	@Autowired
	private ConectarAoBD conecBD = new ConectarAoBD();

	AcoesBD acoesBD = conecBD.getAcoesBD();

	List<Tarefa> tarefas = new ArrayList<>();

	Tarefa tarefaSelecionada = new Tarefa();

	List<Tarefa> tarefasEmAndamento = new ArrayList<>();

	List<Tarefa> tarefasConcluidas = new ArrayList<>();

	List<Tarefa> tarefasFiltradas = new ArrayList<>();

	private String[] situacoes = { Situacao.EmAndamento.getDescricao(), Situacao.Concluida.getDescricao() };

	@PostConstruct
	public void init() {
		try {
			preencherListasDeTarefas(acoesBD.lerTarefas(conecBD.getConexao(), "Tarefas"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("TAREFAS >>>" + tarefas);
		System.out.println("TAREFAS EM ANDAMENTO >>>" + tarefasEmAndamento);
		System.out.println("TAREFAS CONCLUIDAS >>>" + tarefasConcluidas);

		System.out.println("TAREFA SELECIONADA >>>" + tarefaSelecionada.getTitulo());

	}

	@SuppressWarnings("static-access")
	public void preencherTarefaParaCadastrar() {
		Date deadlineSQL = converterData(this.getDeadline());
		try {
			if (acoesBD.cadastrarTarefa(conecBD.getConexao(), "Tarefas", this.getTitulo(), this.getDescricao(),
					this.getResponsavel(), this.getPrioridade(), situacao.EmAndamento, deadlineSQL) == 1) {
				exibirMensagemStatus("A operação foi concluída com sucesso!");
			} else {
				exibirMensagemStatus("Ocorreu um erro na operação! Tente novamente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void preencherTarefaParaEditar() {
		Date deadlineSQL = converterData(this.getDeadline());
		try {
			if (acoesBD.atualizarTarefa(conecBD.getConexao(), "Tarefas", this.getTitulo(), this.getDescricao(),
					this.getResponsavel(), this.getPrioridade(), tarefaSelecionada.getSituacao().getDescricao(), deadlineSQL,
					tarefaSelecionada.getTaskid()) == 1) {
				exibirMensagemStatus("A operação foi concluída com sucesso!");
			} else {
				exibirMensagemStatus("Ocorreu um erro na operação! Tente novamente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void preencherListasDeTarefas(List<Tarefa> listTarefas) {
		for (Tarefa tarefa : listTarefas) {
			tarefas.add(tarefa);
			if (tarefa.getSituacao().equals(Situacao.EmAndamento)) {
				tarefasEmAndamento.add(tarefa);
			} else {
				tarefasConcluidas.add(tarefa);
			}
		}
	}

	public void deletarTarefa(Tarefa tarefa) {
		try {
			acoesBD.removerTarefa(conecBD.getConexao(), "Tarefas", tarefa.getTaskid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\n>>>> Removendo a tarefa " + tarefa.getTitulo());
		tarefas.remove(tarefa);
	}

	public void concluirTarefa(Tarefa tarefa) {
		try {
			acoesBD.concluirTarefa(conecBD.getConexao(), "Tarefas", tarefa.getTaskid());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tarefa.setSituacao(Situacao.Concluida);
	}

	public void exibirMensagemStatus(String mensagem) {
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, null);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form");
	}

	public Date converterData(java.util.Date deadlineUtil) {
		Date dataSQL = new Date(deadlineUtil.getTime());
		System.err.println(deadlineUtil.getTime());
		return dataSQL;
	}

	public Prioridade[] tiposPrioridades() {
		return Prioridade.values();
	}

	public void determinarTarefaSelecionada(Tarefa tarefa) {
		System.out.println("Tarefa q ta mandando p atualizar >>>" + tarefa.getTitulo());
		setTarefaSelecionada(tarefa);
		System.out.println("Tarefa Selecionada >>> " + tarefaSelecionada.getTitulo());
	}

	public void editarTarefa(Tarefa tarefa) {
		Date deadlineSQL = converterData(tarefa.getDeadline());
		try {
			if (acoesBD.atualizarTarefa(conecBD.getConexao(), "Tarefas", tarefa.getTitulo(), tarefa.getDescricao(),
					tarefa.getResponsavel(), tarefa.getPrioridade(), tarefa.getSituacao().getDescricao(), deadlineSQL,
					tarefaSelecionada.getTaskid()) == 1) {
				exibirMensagemStatus("A operação foi concluída com sucesso!");
			} else {
				exibirMensagemStatus("Ocorreu um erro na operação! Tente novamente");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
