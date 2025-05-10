-- Criar sequences
CREATE SEQUENCE controle.PRONTUARIO_STEP_EXECUTION_SEQ;
CREATE SEQUENCE controle.PRONTUARIO_JOB_EXECUTION_SEQ;
CREATE SEQUENCE controle.PRONTUARIO_JOB_SEQ;

-- Tabela para armazenar metadados de execução de jobs
CREATE TABLE controle.PRONTUARIO_JOB_INSTANCE  (
                                           JOB_INSTANCE_ID BIGINT  PRIMARY KEY ,
                                           VERSION BIGINT,
                                           JOB_NAME VARCHAR(100) NOT NULL ,
                                           JOB_KEY VARCHAR(32) NOT NULL
);

-- Tabela para armazenar execuções de jobs
CREATE TABLE controle.PRONTUARIO_JOB_EXECUTION (
                                           JOB_EXECUTION_ID BIGINT  PRIMARY KEY ,
                                           VERSION BIGINT,
                                           JOB_INSTANCE_ID BIGINT NOT NULL,
                                           CREATE_TIME TIMESTAMP NOT NULL,
                                           START_TIME TIMESTAMP DEFAULT NULL,
                                           END_TIME TIMESTAMP DEFAULT NULL,
                                           STATUS VARCHAR(10),
                                           EXIT_CODE VARCHAR(20),
                                           EXIT_MESSAGE VARCHAR(2500),
                                           LAST_UPDATED TIMESTAMP,
                                           constraint JOB_INSTANCE_EXECUTION_FK foreign key (JOB_INSTANCE_ID)
                                               references controle.PRONTUARIO_JOB_INSTANCE(JOB_INSTANCE_ID)
);

-- Tabela para armazenar parâmetros do job
CREATE TABLE controle.PRONTUARIO_JOB_EXECUTION_PARAMS  (
                                                   JOB_EXECUTION_ID BIGINT NOT NULL ,
                                                   PARAMETER_NAME VARCHAR(100) NOT NULL ,
                                                   PARAMETER_TYPE VARCHAR(100) NOT NULL ,
                                                   PARAMETER_VALUE VARCHAR(2500) ,
                                                   IDENTIFYING CHAR(1) NOT NULL ,
                                                   constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                       references controle.PRONTUARIO_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Tabela para armazenar contexto da execução do job
CREATE TABLE controle.PRONTUARIO_JOB_EXECUTION_CONTEXT  (
                                                    JOB_EXECUTION_ID BIGINT PRIMARY KEY,
                                                    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                                    SERIALIZED_CONTEXT VARCHAR(2500),
                                                    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                        references controle.PRONTUARIO_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Tabela para armazenar metadados de execução de steps
CREATE TABLE controle.PRONTUARIO_STEP_EXECUTION  (
                                             STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY ,
                                             VERSION BIGINT NOT NULL,
                                             STEP_NAME VARCHAR(100) NOT NULL,
                                             JOB_EXECUTION_ID BIGINT NOT NULL,
                                             CREATE_TIME TIMESTAMP NOT NULL,
                                             START_TIME TIMESTAMP DEFAULT NULL ,
                                             END_TIME TIMESTAMP DEFAULT NULL,
                                             STATUS VARCHAR(10),
                                             COMMIT_COUNT BIGINT ,
                                             READ_COUNT BIGINT ,
                                             FILTER_COUNT BIGINT ,
                                             WRITE_COUNT BIGINT ,
                                             READ_SKIP_COUNT BIGINT ,
                                             WRITE_SKIP_COUNT BIGINT ,
                                             PROCESS_SKIP_COUNT BIGINT ,
                                             ROLLBACK_COUNT BIGINT ,
                                             EXIT_CODE VARCHAR(20) ,
                                             EXIT_MESSAGE VARCHAR(2500) ,
                                             LAST_UPDATED TIMESTAMP,
                                             constraint JOB_EXECUTION_STEP_FK foreign key (JOB_EXECUTION_ID)
                                                 references controle.PRONTUARIO_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Tabela para armazenar contexto da execução do step
CREATE TABLE controle.PRONTUARIO_STEP_EXECUTION_CONTEXT  (
                                                     STEP_EXECUTION_ID BIGINT PRIMARY KEY,
                                                     SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                                     SERIALIZED_CONTEXT VARCHAR(2500),
                                                     constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                         references controle.PRONTUARIO_STEP_EXECUTION(STEP_EXECUTION_ID)
);

-- Índices para melhorar a performance
CREATE INDEX IDX_JOB_INST ON controle.PRONTUARIO_JOB_INSTANCE (JOB_NAME);
CREATE INDEX IDX_JOB_EXEC ON controle.PRONTUARIO_JOB_EXECUTION (JOB_INSTANCE_ID);
CREATE INDEX IDX_JOB_EXEC_START_TIME ON controle.PRONTUARIO_JOB_EXECUTION (START_TIME);
CREATE INDEX IDX_STEP_EXEC ON controle.PRONTUARIO_STEP_EXECUTION (JOB_EXECUTION_ID);
CREATE INDEX IDX_STEP_EXEC_START_TIME ON controle.PRONTUARIO_STEP_EXECUTION (START_TIME);