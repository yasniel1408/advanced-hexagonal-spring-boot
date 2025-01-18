package com.yascode.infrastructure.out.jdbc_db;

import com.yascode.BaseTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCAdapterTest extends BaseTestContainer {

    private CustomerJDBCAdapter customerJDBCAdapter;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Creamos una JdbcTemplate usando el DataSource del contenedor
        jdbcTemplate = new JdbcTemplate(getDataSource());
        // Inyectamos dicha JdbcTemplate en nuestro adapter
        customerJDBCAdapter = new CustomerJDBCAdapter(jdbcTemplate);
    }

    @Test
    void findById() {
        // 1. Insertamos un registro de prueba
        String insertSql = "INSERT INTO customer (name, age, status, email) VALUES ('John Doe', 30, 'ACTIVE', 'johndoe@example.com') RETURNING id";
        Integer generatedId = jdbcTemplate.queryForObject(insertSql, Integer.class);

        // 2. Usamos el método a probar
        Optional<CustomerDao> result = customerJDBCAdapter.findById(generatedId);

        // 3. Verificamos los resultados
        assertTrue(result.isPresent(), "Se esperaba que existiera el registro con ID " + generatedId);

        CustomerDao customer = result.get();
        assertEquals(generatedId, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals(30, customer.getAge());
        assertEquals("ACTIVE", customer.getStatus());
        assertEquals("johndoe@example.com", customer.getEmail());
    }

    @Test
    void existByEmail() {
        // 1. Insertamos un registro con un email conocido
        String email = "unique@example.com";
        String insertSql = "INSERT INTO customer (name, age, status, email) VALUES ('Jane', 25, 'ACTIVE', ?) RETURNING id";
        jdbcTemplate.queryForObject(insertSql, Integer.class, email);

        // 2. Verificamos que existByEmail devuelva true para ese email
        boolean exists = customerJDBCAdapter.existByEmail(email);
        assertTrue(exists, "Se esperaba que existiera el email " + email);

        // 3. Verificamos un email que no existe
        boolean notExists = customerJDBCAdapter.existByEmail("nope@example.com");
        assertFalse(notExists, "Se esperaba que NO existiera ese email");
    }

    @Test
    void existById() {
        // 1. Insertamos un registro y obtenemos su ID
        String insertSql = "INSERT INTO customer (name, age, status, email) VALUES ('Foo', 40, 'ACTIVE', 'foo@example.com') RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(insertSql, Integer.class);

        // 2. Verificamos que existById devuelva true para ese ID
        assertTrue(customerJDBCAdapter.existById(newId), "El ID " + newId + " debería existir");

        // 3. Verificamos que un ID inexistente devuelva false
        assertFalse(customerJDBCAdapter.existById(999999), "Se esperaba que no existiera el ID 999999");
    }

    @Test
    void save() {
        // 1. Creamos un objeto CustomerDao sin ID (la DB generará uno)
        CustomerDao newCustomer = new CustomerDao();
        newCustomer.setName("Alice");
        newCustomer.setAge(28);
        newCustomer.setStatus("ACTIVE");
        newCustomer.setEmail("alice@example.com");

        // 2. Guardamos con el método save() del adapter
        CustomerDao savedCustomer = customerJDBCAdapter.save(newCustomer);

        // 3. Verificamos que se haya asignado un ID
        assertNotNull(savedCustomer.getId(), "El ID de 'savedCustomer' debería haberse generado");

        // 4. Confirmamos que realmente se insertó en la BD con un SELECT
        Optional<CustomerDao> dbCustomer = customerJDBCAdapter.findById(savedCustomer.getId());
        assertTrue(dbCustomer.isPresent());

        assertEquals("Alice", dbCustomer.get().getName());
        assertEquals(28, dbCustomer.get().getAge());
        assertEquals("ACTIVE", dbCustomer.get().getStatus());
        assertEquals("alice@example.com", dbCustomer.get().getEmail());
    }

    @Test
    void deleteById() {
        // 1. Insertamos un registro
        String insertSql = "INSERT INTO customer (name, age, status, email) VALUES ('Bob', 35, 'ACTIVE', 'bob@example.com') RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(insertSql, Integer.class);

        // 2. Verificamos que sí existe antes de eliminar
        assertTrue(customerJDBCAdapter.existById(newId));

        // 3. Eliminamos el registro
        customerJDBCAdapter.deleteById(newId);

        // 4. Verificamos que ya no exista
        Optional<CustomerDao> result = customerJDBCAdapter.findById(newId);
        assertFalse(result.isPresent(), "Se esperaba que el registro fuera eliminado");
    }

    @Test
    void getAll() {
        // 1. Limpiamos la tabla para evitar contaminación de otros tests (opcional, depende de tu flujo)
        jdbcTemplate.update("DELETE FROM customer");

        // 2. Insertamos múltiples registros
        jdbcTemplate.update("INSERT INTO customer (name, age, status, email) VALUES ('User1', 20, 'ACTIVE', 'u1@example.com')");
        jdbcTemplate.update("INSERT INTO customer (name, age, status, email) VALUES ('User2', 25, 'ACTIVE', 'u2@example.com')");
        jdbcTemplate.update("INSERT INTO customer (name, age, status, email) VALUES ('User3', 30, 'INACTIVE', 'u3@example.com')");

        // 3. Obtenemos todos
        List<CustomerDao> allCustomers = customerJDBCAdapter.getAll();

        // 4. Verificamos
        assertEquals(3, allCustomers.size(), "Se esperaban 3 registros en la BD");

        // También puedes validar que contenga datos específicos
        // (Por simplicidad, solo validamos el tamaño aquí)
    }

    @Test
    void update() {
        // 1. Insertamos un registro
        String insertSql = "INSERT INTO customer (name, age, status, email) VALUES ('Carol', 29, 'ACTIVE', 'carol@example.com') RETURNING id";
        Integer newId = jdbcTemplate.queryForObject(insertSql, Integer.class);

        // 2. Obtenemos el objeto para actualizar
        Optional<CustomerDao> optionalCustomer = customerJDBCAdapter.findById(newId);
        assertTrue(optionalCustomer.isPresent());
        CustomerDao customerToUpdate = optionalCustomer.get();

        // 3. Modificamos datos
        customerToUpdate.setName("Caroline");
        customerToUpdate.setAge(30);
        customerToUpdate.setStatus("INACTIVE");
        customerToUpdate.setEmail("caroline@example.com");

        // 4. Llamamos a update
        CustomerDao updatedCustomer = customerJDBCAdapter.update(customerToUpdate);

        // 5. Verificamos que se hayan guardado los cambios
        assertEquals(newId, updatedCustomer.getId());
        assertEquals("Caroline", updatedCustomer.getName());
        assertEquals(30, updatedCustomer.getAge());
        assertEquals("INACTIVE", updatedCustomer.getStatus());
        assertEquals("caroline@example.com", updatedCustomer.getEmail());

        // 6. Verificamos también en la BD
        Optional<CustomerDao> dbCustomer = customerJDBCAdapter.findById(newId);
        assertTrue(dbCustomer.isPresent());
        assertEquals("Caroline", dbCustomer.get().getName());
        assertEquals(30, dbCustomer.get().getAge());
        assertEquals("INACTIVE", dbCustomer.get().getStatus());
        assertEquals("caroline@example.com", dbCustomer.get().getEmail());
    }
}