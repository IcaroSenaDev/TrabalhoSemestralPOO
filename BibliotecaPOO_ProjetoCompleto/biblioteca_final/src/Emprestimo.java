/** Registro que associa uma pessoa a um livro durante um empréstimo. */
public class Emprestimo implements Exportavel {
	private int id;
	private Pessoa pessoa;
	private Livro livro;
	private String dataEmprestimo;
	private boolean devolvido;

	public Emprestimo(Pessoa pessoa, Livro livro, String data) {
		this.pessoa = pessoa;
		this.livro = livro;
		this.dataEmprestimo = data;
		this.devolvido = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa p) {
		pessoa = p;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro l) {
		livro = l;
	}

	public String getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(String d) {
		dataEmprestimo = d;
	}

	public boolean isDevolvido() {
		return devolvido;
	}

	public void setDevolvido(boolean d) {
		devolvido = d;
	}

	/** Devolução idempotente: libera o livro apenas se ainda estiver aberto. */
	public void devolver() {
		if (!devolvido) {
			devolvido = true;
			livro.setDisponivel(true);
		}
	}

	public String paraLinhaTabela() {
		return String.format("%-4d | %-22s | %-30s | %-10s | %-10s", id, pessoa.getNome(), livro.getTitulo(),
				dataEmprestimo, devolvido ? "Devolvido" : "Em aberto");
	}
}
