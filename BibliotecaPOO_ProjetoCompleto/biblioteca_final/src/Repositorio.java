import java.util.List;

/** Operações básicas de persistência em coleção. */
public interface Repositorio<T extends Exportavel> {
	void adicionar(T item);

	boolean remover(int id);

	T buscarPorId(int id);

	List<T> listarTodos();
}
