import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/** Regras para empréstimos, limites, disponibilidade e devolução. */
public class EmprestimoServico {
	private final Repositorio<Emprestimo> repo;
	private final PessoaServico pessoas;
	private final LivroServico livros;
	private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public EmprestimoServico(Repositorio<Emprestimo> r, PessoaServico p, LivroServico l) {
		repo = r;
		pessoas = p;
		livros = l;
	}

	private LocalDate parse(String s) {
		try {
			return LocalDate.parse(s, F);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Data inválida. Use dd/MM/yyyy.");
		}
	}

	public String registrar(int idPessoa, int idLivro, String data) {
		LocalDate d = parse(data);
		Pessoa p = pessoas.buscarPorId(idPessoa);
		Livro l = livros.buscarPorId(idLivro);
		if (p == null)
			return "Pessoa não encontrada.";
		if (l == null)
			return "Livro não encontrado.";
		if (!l.isDisponivel())
			return "Livro indisponível.";
		int abertos = 0;
		for (Emprestimo e : listar())
			if (!e.isDevolvido() && e.getPessoa().getId() == idPessoa)
				abertos++;
		if (abertos >= p.getLimiteEmprestimos())
			return "Limite de empréstimos atingido (" + p.getLimiteEmprestimos() + ").";
		l.setDisponivel(false);
		repo.adicionar(new Emprestimo(p, l, d.format(F)));
		return "Empréstimo registrado.";
	}

	public String devolver(int id) {
		Emprestimo e = repo.buscarPorId(id);
		if (e == null)
			return "Empréstimo não encontrado.";
		if (e.isDevolvido())
			return "Este empréstimo já foi devolvido.";
		e.devolver();
		return "Devolução registrada.";
	}

	public boolean remover(int id) {
		Emprestimo e = repo.buscarPorId(id);
		if (e == null)
			return false;
		if (!e.isDevolvido())
			e.devolver();
		return repo.remover(id);
	}

	public List<Emprestimo> listar() {
		return repo.listarTodos();
	}

	public boolean temEmprestimoAbertoPessoa(int id) {
		for (Emprestimo e : listar())
			if (!e.isDevolvido() && e.getPessoa().getId() == id)
				return true;
		return false;
	}

	public boolean temEmprestimoAbertoLivro(int id) {
		for (Emprestimo e : listar())
			if (!e.isDevolvido() && e.getLivro().getId() == id)
				return true;
		return false;
	}

	public List<Emprestimo> buscarPorNomePessoa(String nome) {
		String q = nome.toLowerCase(Locale.ROOT);
		List<Emprestimo> r = new ArrayList<>();
		for (Emprestimo e : listar())
			if (e.getPessoa().getNome().toLowerCase(Locale.ROOT).contains(q))
				r.add(e);
		return r;
	}

	public List<Emprestimo> listarOrdenadoPorData() {
		List<Emprestimo> l = listar();
		l.sort(Comparator.comparing(e -> parse(e.getDataEmprestimo())));
		return l;
	}
}
