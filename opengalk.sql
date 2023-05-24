--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2 (Debian 15.2-1.pgdg110+1)
-- Dumped by pg_dump version 15.2 (Debian 15.2-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: college; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA college;


ALTER SCHEMA college OWNER TO postgres;

--
-- Name: paper; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA paper;


ALTER SCHEMA paper OWNER TO postgres;

--
-- Name: user; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "user";


ALTER SCHEMA "user" OWNER TO postgres;

--
-- Name: college_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.college_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.college_info_id_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: college_info; Type: TABLE; Schema: college; Owner: postgres
--

CREATE TABLE college.college_info (
    id bigint DEFAULT nextval('public.college_info_id_seq'::regclass) NOT NULL,
    college_name character varying NOT NULL,
    create_time timestamp without time zone NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    remark character varying
);


ALTER TABLE college.college_info OWNER TO postgres;

--
-- Name: 0877d41ba06447e38667756a45c2e5ea; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper."0877d41ba06447e38667756a45c2e5ea" (
    id smallint NOT NULL,
    type smallint NOT NULL,
    subject character varying NOT NULL,
    option_a character varying NOT NULL,
    option_b character varying NOT NULL,
    option_c character varying NOT NULL,
    option_d character varying NOT NULL,
    answer character varying NOT NULL,
    has_imgs smallint DEFAULT 0 NOT NULL
);


ALTER TABLE paper."0877d41ba06447e38667756a45c2e5ea" OWNER TO postgres;

--
-- Name: 14a266a743ac47e4bb3ea4e01323a98e; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper."14a266a743ac47e4bb3ea4e01323a98e" (
    id smallint NOT NULL,
    type smallint NOT NULL,
    subject character varying NOT NULL,
    option_a character varying NOT NULL,
    option_b character varying NOT NULL,
    option_c character varying NOT NULL,
    option_d character varying NOT NULL,
    answer character varying NOT NULL,
    has_imgs smallint DEFAULT 0 NOT NULL
);


ALTER TABLE paper."14a266a743ac47e4bb3ea4e01323a98e" OWNER TO postgres;

--
-- Name: 88ea4153429c474c8348649aad517c7c; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper."88ea4153429c474c8348649aad517c7c" (
    id smallint NOT NULL,
    type smallint NOT NULL,
    subject character varying NOT NULL,
    option_a character varying NOT NULL,
    option_b character varying NOT NULL,
    option_c character varying NOT NULL,
    option_d character varying NOT NULL,
    answer character varying NOT NULL,
    has_imgs smallint DEFAULT 0 NOT NULL
);


ALTER TABLE paper."88ea4153429c474c8348649aad517c7c" OWNER TO postgres;

--
-- Name: paper_collect; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_collect (
    uuid character varying NOT NULL,
    subject_id smallint NOT NULL,
    user_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    id character varying NOT NULL
);


ALTER TABLE paper.paper_collect OWNER TO postgres;

--
-- Name: COLUMN paper_collect.uuid; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.uuid IS '试卷uuid';


--
-- Name: COLUMN paper_collect.subject_id; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.subject_id IS '题目序号';


--
-- Name: COLUMN paper_collect.user_id; Type: COMMENT; Schema: paper; Owner: postgres
--

COMMENT ON COLUMN paper.paper_collect.user_id IS '用户id';


--
-- Name: paper_correct; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_correct (
    uuid character varying NOT NULL,
    subject_id smallint NOT NULL,
    user_id bigint NOT NULL,
    correct_text text NOT NULL,
    create_time timestamp without time zone NOT NULL,
    id character varying NOT NULL
);


ALTER TABLE paper.paper_correct OWNER TO postgres;

--
-- Name: paper_imgs_id_seq; Type: SEQUENCE; Schema: paper; Owner: postgres
--

CREATE SEQUENCE paper.paper_imgs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE paper.paper_imgs_id_seq OWNER TO postgres;

--
-- Name: paper_info; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_info (
    id character varying NOT NULL,
    name character varying NOT NULL,
    remark character varying NOT NULL,
    create_by bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    type smallint NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL
);


ALTER TABLE paper.paper_info OWNER TO postgres;

--
-- Name: paper_record; Type: TABLE; Schema: paper; Owner: postgres
--

CREATE TABLE paper.paper_record (
    id character varying NOT NULL,
    user_id bigint NOT NULL,
    score smallint,
    create_time timestamp without time zone NOT NULL,
    answer character varying
);


ALTER TABLE paper.paper_record OWNER TO postgres;

--
-- Name: paper_collect_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paper_collect_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.paper_collect_id_seq OWNER TO postgres;

--
-- Name: paper_imgs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paper_imgs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.paper_imgs_id_seq OWNER TO postgres;

--
-- Name: user_info_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_info_id_seq OWNER TO postgres;

--
-- Name: user_info; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_info (
    id bigint DEFAULT nextval('public.user_info_id_seq'::regclass) NOT NULL,
    account character varying NOT NULL,
    password character varying NOT NULL,
    authority smallint DEFAULT 2 NOT NULL,
    is_deleted smallint DEFAULT 0 NOT NULL,
    create_time timestamp without time zone NOT NULL,
    create_by bigint,
    is_locked smallint DEFAULT 0 NOT NULL,
    name character varying,
    phone_number character varying,
    college_id smallint,
    email character varying,
    gender smallint,
    birthday date
);


ALTER TABLE "user".user_info OWNER TO postgres;

--
-- Name: user_login_and_logout_record; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_login_and_logout_record (
    id bigint NOT NULL,
    login_or_logout smallint NOT NULL,
    "time" timestamp without time zone NOT NULL
);


ALTER TABLE "user".user_login_and_logout_record OWNER TO postgres;

--
-- Name: user_update_record; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "user".user_update_record (
    id bigint NOT NULL,
    table_name character varying NOT NULL,
    column_name character varying NOT NULL,
    before_update character varying,
    after_update character varying,
    update_time timestamp without time zone NOT NULL,
    update_by bigint NOT NULL,
    remark character varying
);


ALTER TABLE "user".user_update_record OWNER TO postgres;

--
-- Name: college_info college_info_pk; Type: CONSTRAINT; Schema: college; Owner: postgres
--

ALTER TABLE ONLY college.college_info
    ADD CONSTRAINT college_info_pk PRIMARY KEY (id);


--
-- Name: 0877d41ba06447e38667756a45c2e5ea 0877d41ba06447e38667756a45c2e5ea_pkey; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper."0877d41ba06447e38667756a45c2e5ea"
    ADD CONSTRAINT "0877d41ba06447e38667756a45c2e5ea_pkey" PRIMARY KEY (id);


--
-- Name: 14a266a743ac47e4bb3ea4e01323a98e 14a266a743ac47e4bb3ea4e01323a98e_pkey; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper."14a266a743ac47e4bb3ea4e01323a98e"
    ADD CONSTRAINT "14a266a743ac47e4bb3ea4e01323a98e_pkey" PRIMARY KEY (id);


--
-- Name: 88ea4153429c474c8348649aad517c7c 88ea4153429c474c8348649aad517c7c_pkey; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper."88ea4153429c474c8348649aad517c7c"
    ADD CONSTRAINT "88ea4153429c474c8348649aad517c7c_pkey" PRIMARY KEY (id);


--
-- Name: paper_collect paper_collect_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_collect
    ADD CONSTRAINT paper_collect_pk PRIMARY KEY (id);


--
-- Name: paper_correct paper_correct_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_correct
    ADD CONSTRAINT paper_correct_pk PRIMARY KEY (id);


--
-- Name: paper_info paper_info_pk; Type: CONSTRAINT; Schema: paper; Owner: postgres
--

ALTER TABLE ONLY paper.paper_info
    ADD CONSTRAINT paper_info_pk PRIMARY KEY (id);


--
-- Name: user_info user_info_pk; Type: CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "user".user_info
    ADD CONSTRAINT user_info_pk PRIMARY KEY (id);


--
-- Name: user_login_and_logout_record_id_index; Type: INDEX; Schema: user; Owner: postgres
--

CREATE INDEX user_login_and_logout_record_id_index ON "user".user_login_and_logout_record USING btree (id);


--
-- Name: user_update_record_id_index; Type: INDEX; Schema: user; Owner: postgres
--

CREATE INDEX user_update_record_id_index ON "user".user_update_record USING btree (id);


--
-- PostgreSQL database dump complete
--

