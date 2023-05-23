package com.example.MySQL_Practicce.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class DBConnectionTest {
    @Test
    void Connection(){
        Connection connection = ConnectionUtil.getConnection();
        assertThat(connection).isNotNull();
    }
}
