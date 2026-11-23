/** Contrato para objetos identificáveis e exibidos em tabelas. */
public interface Exportavel {
	int getId();

	void setId(int id);

	String paraLinhaTabela();
}
