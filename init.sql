-- 1. 확장 기능 활성화
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. 테이블 생성 (Spring AI 기본 스키마와 일치함)
CREATE TABLE IF NOT EXISTS vector_store (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(768) -- 1536 is the default embedding dimension
    );

-- 3. HNSW 인덱스 생성 (검색 속도 향상)
CREATE INDEX ON vector_store USING HNSW (embedding vector_cosine_ops);