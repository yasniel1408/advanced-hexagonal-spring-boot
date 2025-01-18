package com.yascode.infrastructure.out.jpa_db;

import com.yascode.BaseTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerJpaDbAdapterTest extends BaseTestContainer {

    @Autowired
    private JpaCustomerRepository jpaCustomerRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private CustomerJpaDbAdapter jpaDbAdapter;

    @BeforeEach
    void setUp() {
        // Limpia la tabla para partir de un estado conocido
        jpaCustomerRepository.deleteAll();
        // Instancia el adaptador con el repo inyectado
        jpaDbAdapter = new CustomerJpaDbAdapter(jpaCustomerRepository);
    }

    @Test
    void findById() {
        // 1. Creamos una entidad y la guardamos
        CustomerDao dao = new CustomerDao();
        dao.setName("John Doe");
        dao.setAge(30);
        dao.setStatus("ACTIVE");
        dao.setEmail("john.doe@example.com");

        CustomerDao saved = jpaDbAdapter.save(dao);

        // 2. Llamamos a findById con el ID recién generado
        Optional<CustomerDao> maybeCustomer = jpaDbAdapter.findById(saved.getId());

        // 3. Verificamos
        assertTrue(maybeCustomer.isPresent(), "Se esperaba que el customer existiera en la BD");
        assertEquals("john.doe@example.com", maybeCustomer.get().getEmail());
    }

    @Test
    void existByEmail() {
        // 1. Creamos un registro con un email específico
        CustomerDao dao = new CustomerDao();
        dao.setName("Jane");
        dao.setAge(25);
        dao.setStatus("ACTIVE");
        dao.setEmail("jane@example.com");

        jpaDbAdapter.save(dao);

        // 2. Verificamos que exista por email
        boolean exists = jpaDbAdapter.existByEmail("jane@example.com");
        assertTrue(exists, "Se esperaba que el email jane@example.com existiera");

        // 3. Verificamos un email que no existe
        boolean notExists = jpaDbAdapter.existByEmail("nope@example.com");
        assertFalse(notExists, "Se esperaba que no existiera el email nope@example.com");
    }

    @Test
    void existById() {
        // 1. Insertamos un registro
        CustomerDao dao = new CustomerDao();
        dao.setName("Foo");
        dao.setAge(40);
        dao.setStatus("ACTIVE");
        dao.setEmail("foo@example.com");

        CustomerDao saved = jpaDbAdapter.save(dao);

        // 2. Verificamos que exista por ID
        assertTrue(jpaDbAdapter.existById(saved.getId()), "Debería existir el registro con el ID " + saved.getId());

        // 3. Verificamos un ID inexistente
        assertFalse(jpaDbAdapter.existById(99999), "No debería existir el registro con el ID 99999");
    }

    @Test
    void save() {
        // 1. Creamos un objeto CustomerDao sin ID
        CustomerDao dao = new CustomerDao();
        dao.setName("Alice");
        dao.setAge(28);
        dao.setStatus("ACTIVE");
        dao.setEmail("alice@example.com");

        // 2. Guardamos
        CustomerDao saved = jpaDbAdapter.save(dao);

        // 3. Verificamos que se genere un ID
        assertNotNull(saved.getId(), "Se esperaba que el CustomerDao tuviera un ID generado");

        // 4. Revisamos que la data sea coherente
        Optional<CustomerDao> fromDb = jpaDbAdapter.findById(saved.getId());
        assertTrue(fromDb.isPresent());
        assertEquals("Alice", fromDb.get().getName());
    }

    @Test
    void deleteById() {
        // 1. Insertamos un registro
        CustomerDao dao = new CustomerDao();
        dao.setName("Bob");
        dao.setAge(35);
        dao.setStatus("ACTIVE");
        dao.setEmail("bob@example.com");

        CustomerDao saved = jpaDbAdapter.save(dao);

        // 2. Eliminamos por ID
        jpaDbAdapter.deleteById(saved.getId());

        // 3. Verificamos que ya no exista
        assertFalse(jpaDbAdapter.findById(saved.getId()).isPresent(),
                "Se esperaba que el registro con ID " + saved.getId() + " ya no existiera");
    }

    @Test
    void getAll() {
        // 1. Insertamos varios registros
        CustomerDao c1 = new CustomerDao();
        c1.setName("User1");
        c1.setAge(20);
        c1.setStatus("ACTIVE");
        c1.setEmail("u1@example.com");

        CustomerDao c2 = new CustomerDao();
        c2.setName("User2");
        c2.setAge(25);
        c2.setStatus("ACTIVE");
        c2.setEmail("u2@example.com");

        jpaDbAdapter.save(c1);
        jpaDbAdapter.save(c2);

        // 2. Obtenemos todos
        List<CustomerDao> all = jpaDbAdapter.getAll();

        // 3. Verificamos
        assertEquals(2, all.size(), "Se esperaban 2 registros en la BD");
    }

    @Test
    void update() {
        // 1. Insertamos un registro
        CustomerDao dao = new CustomerDao();
        dao.setName("Carol");
        dao.setAge(29);
        dao.setStatus("ACTIVE");
        dao.setEmail("carol@example.com");

        CustomerDao saved = jpaDbAdapter.save(dao);

        // 2. Modificamos algunos campos
        saved.setName("Caroline");
        saved.setAge(30);
        saved.setStatus("INACTIVE");
        saved.setEmail("caroline@example.com");

        // 3. Llamamos a update (en realidad .save(...) de JPA)
        jpaDbAdapter.update(saved);

        // 4. Verificamos que los cambios estén guardados
        Optional<CustomerDao> updated = jpaDbAdapter.findById(saved.getId());
        assertTrue(updated.isPresent());

        CustomerDao updatedDao = updated.get();
        assertEquals("Caroline", updatedDao.getName());
        assertEquals(30, updatedDao.getAge());
        assertEquals("INACTIVE", updatedDao.getStatus());
        assertEquals("caroline@example.com", updatedDao.getEmail());
    }
}