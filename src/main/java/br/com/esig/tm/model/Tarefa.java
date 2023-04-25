package br.com.esig.tm.model;

import javax.faces.event.NamedEvent;
import javax.faces.view.ViewScoped;

import br.com.esig.tm.enums.Prioridade;
import br.com.esig.tm.enums.Situacao;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@NamedEvent
@ViewScoped
public class Tarefa {
    private Long taskid;
	
	private String titulo;
	private String descricao;
	private String responsavel;
	private Prioridade prioridade;
	private Situacao situacao;
	private java.util.Date deadline;
	
}
