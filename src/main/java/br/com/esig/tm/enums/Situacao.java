package br.com.esig.tm.enums;

public enum Situacao {
	EmAndamento("Em andamento"),
    Concluida("Concluída");

    private String descricao;

    Situacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
	
}
